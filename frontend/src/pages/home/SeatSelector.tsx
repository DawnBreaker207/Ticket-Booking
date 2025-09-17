// src/components/SeatSelector.tsx
import { useEffect, useMemo, useState } from "react";
import {
  Card,
  Button,
  Image,
  Typography,
  Space,
  message,
  Layout,
  Spin,
} from "antd";
import type { CSSProperties } from "react";
import screenImg from "../../assets/ic-screen.png";
import Navbar from "../../components/Navbar";
import { Content } from "antd/es/layout/layout";
import { useParams } from "react-router-dom";
import { cinemaHallService } from "../../services/cinemaHallService";

const { Title, Text } = Typography;

export interface MovieInfo {
  posterUrl?: string;
  title?: string;
  genres?: string[];
  duration?: any; // giữ raw (number hoặc string) theo yêu cầu
  showtime?: string; // e.g. "2025-09-08 19:30" or ISO string
}

export interface SeatSelectorProps {
  rows?: string[]; // e.g. ['A','B','C','D','E']
  cols?: number; // e.g. 10
  reserved?: string[]; // list of seat ids like ['A3','B5']
  initialSelected?: string[];
  maxSelectable?: number; // optional max number of selectable seats
  onConfirm?: (selected: string[]) => void;
  movie?: MovieInfo; // optional; if not provided will fetch from API using id in URL
  reserveSeconds?: number; // how long the hold lasts (countdown), default 900s
}

const defaultRows = ["A", "B", "C", "D", "E"];
const PRICE_PER_SEAT = 50000; // VND

const seatStyle: CSSProperties = {
  width: 40,
  height: 40,
  padding: 0,
  display: "flex",
  alignItems: "center",
  justifyContent: "center",
  borderRadius: 6,
};

function formatVND(value: number) {
  return new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
  }).format(value);
}

function formatTime(seconds: number) {
  const mm = Math.floor(seconds / 60)
    .toString()
    .padStart(2, "0");
  const ss = (seconds % 60).toString().padStart(2, "0");
  return `${mm}:${ss}`;
}

// Parse showtime string like "YYYY-MM-DD HH:mm" into {date: 'DD/MM/YYYY', time: 'HH:mm'}
// Also supports ISO string fallback.
function parseShowtime(showtime?: string) {
  if (!showtime) return { date: "-", time: "-" };
  const parts = showtime.trim().split(" ");
  if (parts.length >= 2) {
    const [datePart, timePart] = parts;
    const dateParts = datePart.split("-");
    if (dateParts.length === 3) {
      const [y, m, d] = dateParts;
      return { date: `${d}/${m}/${y}`, time: timePart };
    }
    return { date: datePart, time: timePart };
  }
  // fallback: try ISO parsing
  try {
    const d = new Date(showtime);
    if (!isNaN(d.getTime())) {
      const date = d.toLocaleDateString("vi-VN");
      const time = d.toLocaleTimeString("en-GB", {
        hour: "2-digit",
        minute: "2-digit",
      });
      return { date, time };
    }
  } catch (e) {
    /* ignore */
  }
  return { date: showtime, time: "-" };
}

const sampleMovie: MovieInfo = {
  posterUrl:
    "https://cinema.momocdn.net/img/90126602072536934-kimesstu.png?size=M",
  title: "Thanh gươm diệt quỷ",
  genres: ["Hành động", "Viễn tưởng"],
  duration: 120,
  showtime: "2025-09-12 19:30",
};

export default function SeatSelector({
  rows = defaultRows,
  cols = 10,
  reserved = [],
  initialSelected = [],
  maxSelectable = 8,
  onConfirm,
  movie = sampleMovie,
  reserveSeconds = 900,
}: SeatSelectorProps) {
  const { id } = useParams<{ id: string }>();

  const [selected, setSelected] = useState<string[]>(initialSelected);
  const [remaining, setRemaining] = useState<number>(reserveSeconds);

  // remote state (fetched from API if id present)
  const [remoteMovie, setRemoteMovie] = useState<MovieInfo | null>(null);
  const [loading, setLoading] = useState<boolean>(false);

  const seats = useMemo(() => {
    const out: string[] = [];
    for (const r of rows) {
      for (let c = 1; c <= cols; c++) out.push(`${r}${c}`);
    }
    return out;
  }, [rows, cols]);

  // when reserveSeconds changes reset timer
  useEffect(() => {
    setRemaining(reserveSeconds);
  }, [reserveSeconds]);

  // countdown interval
  useEffect(() => {
    if (remaining <= 0) return;
    const idInterval = setInterval(() => {
      setRemaining((s) => Math.max(0, s - 1));
    }, 1000);
    return () => clearInterval(idInterval);
  }, [remaining]);

  // fetch cinemaHall by id from URL and set remoteMovie (note: we DO NOT set reserved seats from API)
  useEffect(() => {
    let mounted = true;
    if (!id) return;

    (async () => {
      try {
        setLoading(true);
        const hall = await cinemaHallService.getById(Number(id));
        if (!mounted) return;

        // movie info from hall.movie and showtime from hall.movieSession
        const m = hall?.movie ?? null;
        const movieInfo: MovieInfo = {
          posterUrl: m?.poster ?? undefined,
          title: m?.title ?? "-",
          genres: Array.isArray(m?.genres) ? m.genres : [],
          // giữ raw duration (không ép String)
          duration: m?.duration,
          showtime: hall?.movieSession ?? undefined,
        };
        setRemoteMovie(movieInfo);
      } catch (err: any) {
        console.error("Lấy suất chiếu thất bại", err);
        message.error(
          err?.response?.data?.message ?? "Không lấy được thông tin suất chiếu"
        );
      } finally {
        if (mounted) setLoading(false);
      }
    })();

    return () => {
      mounted = false;
    };
  }, [id]);

  function toggleSeat(id: string) {
    // dùng CHỈ prop 'reserved' để kiểm tra ghế đã đặt
    if (reserved.includes(id)) return;
    if (selected.includes(id)) {
      setSelected((s) => s.filter((x) => x !== id));
      return;
    }
    if (typeof maxSelectable === "number" && selected.length >= maxSelectable) {
      message.warning(`Bạn chỉ được chọn tối đa ${maxSelectable} ghế`);
      return;
    }
    setSelected((s) => [...s, id]);
  }

  function handleConfirm() {
    onConfirm?.(selected);
  }

  const total = selected.length * PRICE_PER_SEAT;

  // choose which movie to show: remote (if fetched) else props
  const movieToShow = remoteMovie ?? movie;

  // parse showtime into date/time
  const { date: showDate, time: showTime } = parseShowtime(
    movieToShow?.showtime
  );

  // genres as text
  const genresText =
    movieToShow?.genres && movieToShow.genres.length > 0
      ? movieToShow.genres.join(", ")
      : "-";

  // loading spinner while fetching
  if (loading) {
    return (
      <Layout>
        <Navbar />
        <Content
          style={{ minHeight: "100vh", padding: 80, textAlign: "center" }}
        >
          <Spin size="large" />
        </Content>
      </Layout>
    );
  }

  return (
    <Layout>
      <Navbar />
      <Content style={{ minHeight: "100vh", padding: 50 }}>
        <div style={{ maxWidth: 1100, margin: "0 auto", padding: 16 }}>
          <div style={{ display: "flex", flexDirection: "column", gap: 24 }}>
            <div
              style={{
                display: "flex",
                gap: 24,
                flexDirection: "row",
                alignItems: "flex-start",
              }}
            >
              {/* Left: seats */}
              <div style={{ flex: 1 }}>
                {/* Legend above screen */}
                <Card size="small" style={{ marginBottom: 12 }}>
                  <Space>
                    <Space>
                      <div
                        style={{
                          width: 18,
                          height: 18,
                          border: "1px solid #d9d9d9",
                          background: "#fff",
                        }}
                      />
                      <Text style={{ fontSize: 14 }}>Trống</Text>
                    </Space>
                    <Space>
                      <div
                        style={{
                          width: 18,
                          height: 18,
                          background: "#2f54eb",
                          borderRadius: 3,
                        }}
                      />
                      <Text style={{ fontSize: 14 }}>Đang chọn</Text>
                    </Space>
                    <Space>
                      <div
                        style={{
                          width: 18,
                          height: 18,
                          background: "#ff4d4f",
                          borderRadius: 3,
                        }}
                      />
                      <Text style={{ fontSize: 14 }}>Đã đặt</Text>
                    </Space>
                  </Space>
                </Card>

                {/* Screen image - larger */}
                <div style={{ textAlign: "center", marginBottom: 12 }}>
                  <div
                    style={{
                      width: "100%",
                      display: "flex",
                      justifyContent: "center",
                    }}
                  >
                    <Image
                      src={screenImg}
                      alt="Screen"
                      preview={false}
                      style={{
                        maxWidth: 1100,
                        width: "100%",
                        height: 100,
                        objectFit: "contain",
                      }}
                    />
                  </div>
                </div>

                {/* Seats grid */}
                <Card style={{ boxShadow: "none", background: "transparent" }}>
                  <div
                    style={{
                      display: "grid",
                      gap: 12,
                      border: "0",
                      gridTemplateColumns: `repeat(${cols}, 1fr)`,
                    }}
                  >
                    {seats.map((seatId) => {
                      const isReserved = reserved.includes(seatId);
                      const isSelected = selected.includes(seatId);
                      return (
                        <Button
                          key={seatId}
                          onClick={() => toggleSeat(seatId)}
                          aria-pressed={isSelected}
                          aria-label={`Ghế ${seatId} ${
                            isReserved
                              ? "đã đặt"
                              : isSelected
                              ? "đang chọn"
                              : "trống"
                          }`}
                          disabled={isReserved}
                          style={{
                            ...seatStyle,
                            background: isReserved
                              ? "#ff4d4f"
                              : isSelected
                              ? undefined
                              : "#fff",
                            color: isReserved ? "#fff" : undefined,
                            borderColor: isReserved ? "#ff4d4f" : undefined,
                          }}
                          type={isSelected ? "primary" : "default"}
                          size="small"
                        >
                          {seatId}
                        </Button>
                      );
                    })}
                  </div>
                </Card>
                <Card size="small">
                  <div
                    style={{
                      display: "flex",
                      justifyContent: "space-between",
                      alignItems: "center",
                    }}
                  >
                    <div>
                      <Text strong style={{ fontSize: 16 }}>
                        Tổng tiền:
                      </Text>
                      <div>
                        <Title level={4} style={{ margin: 0, fontSize: 20 }}>
                          {formatVND(total)}
                        </Title>
                      </div>
                    </div>

                    <div style={{ textAlign: "right" }}>
                      <Text style={{ display: "block", marginBottom: 4 }}>
                        Thời gian còn lại
                      </Text>
                      <div>
                        <Title level={4} style={{ margin: 0, fontSize: 20 }}>
                          {formatTime(remaining)}
                        </Title>
                      </div>
                    </div>
                  </div>
                </Card>
              </div>

              {/* Right: movie info */}
              <div style={{ width: 360, flexShrink: 0 }}>
                <Card size="small" style={{ position: "sticky", top: 16 }}>
                  <div style={{ marginBottom: 12 }}>
                    {movieToShow?.posterUrl ? (
                      <Image
                        src={movieToShow.posterUrl}
                        alt={movieToShow.title || "Poster"}
                        preview={false}
                        style={{
                          width: "100%",
                          height: 300,
                          objectFit: "cover",
                          borderRadius: 6,
                        }}
                      />
                    ) : (
                      <div
                        style={{
                          width: "100%",
                          height: 300,
                          background: "#fafafa",
                          display: "flex",
                          alignItems: "center",
                          justifyContent: "center",
                          color: "#bfbfbf",
                          borderRadius: 6,
                        }}
                      >
                        No poster
                      </div>
                    )}
                  </div>

                  <Title level={3} style={{ marginBottom: 6 }}>
                    {movieToShow?.title}
                  </Title>

                  <div style={{ marginBottom: 8 }}>
                    <Text strong>Thể loại: </Text>
                    <Text>{genresText}</Text>
                  </div>

                  <div style={{ marginBottom: 8 }}>
                    <Text strong>Thời lượng:</Text>{" "}
                    <Text>{movieToShow?.duration} phút</Text>
                  </div>

                  <div style={{ marginBottom: 8 }}>
                    <Text strong>Ngày chiếu:</Text> <Text>{showDate}</Text>
                  </div>

                  <div>
                    <Text strong>Giờ chiếu:</Text> <Text>{showTime}</Text>
                  </div>

                  {/* Selected seats + action */}
                  <div style={{ marginTop: 12 }}>
                    <Text strong>Ghế đã chọn:</Text>
                    <div style={{ marginTop: 6, marginBottom: 12 }}>
                      <Text>{selected.join(", ")}</Text>
                    </div>

                    <Button block type="primary" onClick={handleConfirm}>
                      Tiếp tục
                    </Button>
                  </div>
                </Card>
              </div>
            </div>
          </div>
        </div>
      </Content>
    </Layout>
  );
}
