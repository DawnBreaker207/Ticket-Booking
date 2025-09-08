import { useEffect, useMemo, useState } from "react";
import { Card, Button, Image, Typography, Tag, Space, message } from "antd";
import type { CSSProperties } from "react";

const { Title, Text } = Typography;

export interface MovieInfo {
  posterUrl?: string;
  title?: string;
  genres?: string[];
  duration?: string; // e.g. "2h 10m"
  showtime?: string; // e.g. "2025-09-08 19:30"
}

export interface SeatSelectorProps {
  rows?: string[]; // e.g. ['A','B','C','D','E']
  cols?: number; // e.g. 10
  reserved?: string[]; // list of seat ids like ['A3','B5']
  initialSelected?: string[];
  maxSelectable?: number; // optional max number of selectable seats
  onConfirm?: (selected: string[]) => void;
  movie?: MovieInfo; // movie info to show on the right
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

export default function SeatSelector({
  rows = defaultRows,
  cols = 10,
  reserved = [],
  initialSelected = [],
  maxSelectable,
  onConfirm,
  movie,
  reserveSeconds = 900,
}: SeatSelectorProps) {
  const [selected, setSelected] = useState<string[]>(initialSelected);
  const [remaining, setRemaining] = useState<number>(reserveSeconds);

  const seats = useMemo(() => {
    const out: string[] = [];
    for (const r of rows) {
      for (let c = 1; c <= cols; c++) out.push(`${r}${c}`);
    }
    return out;
  }, [rows, cols]);

  useEffect(() => {
    setRemaining(reserveSeconds);
  }, [reserveSeconds]);

  useEffect(() => {
    if (remaining <= 0) return;
    const id = setInterval(() => {
      setRemaining((s) => Math.max(0, s - 1));
    }, 1000);
    return () => clearInterval(id);
  }, [remaining]);

  function toggleSeat(id: string) {
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

  return (
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
            <Card
              size="small"
              variant="borderless"
              style={{ marginBottom: 12 }}
            >
              <Space>
                <Space>
                  <div
                    style={{
                      width: 14,
                      height: 14,
                      border: "1px solid #d9d9d9",
                      background: "#fff",
                    }}
                  />
                  <Text type="secondary" style={{ fontSize: 12 }}>
                    Trống
                  </Text>
                </Space>
                <Space>
                  <div
                    style={{
                      width: 14,
                      height: 14,
                      background: "#2f54eb",
                      borderRadius: 3,
                    }}
                  />
                  <Text type="secondary" style={{ fontSize: 12 }}>
                    Đang chọn
                  </Text>
                </Space>
                <Space>
                  <div
                    style={{
                      width: 14,
                      height: 14,
                      background: "#f5f5f5",
                      border: "1px solid #d9d9d9",
                    }}
                  />
                  <Text type="secondary" style={{ fontSize: 12 }}>
                    Đã đặt
                  </Text>
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
                  src={
                    "https://www.betacinemas.vn/Assets/global/img/booking/ic-screen.png"
                  }
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
            <Card variant="borderless" style={{ boxShadow: "none" }}>
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
                          ? "#f5f5f5"
                          : isSelected
                          ? undefined
                          : "#fff",
                        borderColor: isReserved ? "#e6e6e6" : undefined,
                      }}
                      type={isSelected ? "primary" : "default"}
                      size="small"
                    >
                      {seatId}
                    </Button>
                  );
                })}
              </div>

              {/* Footer inside seats card: total on left, remaining time on right */}
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
                  <Text strong>Tổng tiền:</Text>
                  <div>
                    <Title level={5} style={{ margin: 0 }}>
                      {formatVND(total)}
                    </Title>
                  </div>
                </div>

                <div style={{ textAlign: "right" }}>
                  <Text type="secondary">Thời gian còn lại</Text>
                  <div>
                    <Text strong>10:00</Text>
                  </div>
                </div>
              </div>
            </Card>
          </div>

          {/* Right: movie info */}
          <div style={{ width: 320, flexShrink: 0 }}>
            <Card size="small" style={{ position: "sticky", top: 16 }}>
              <div style={{ marginBottom: 12 }}>
                {movie?.posterUrl ? (
                  <Image
                    src={movie.posterUrl}
                    alt={movie.title || "Poster"}
                    style={{
                      width: "100%",
                      height: 200,
                      objectFit: "cover",
                      borderRadius: 6,
                    }}
                  />
                ) : (
                  <div
                    style={{
                      width: "100%",
                      height: 200,
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

              <Title level={5} style={{ marginBottom: 6 }}>
                {movie?.title || "-"}
              </Title>

              <div style={{ marginBottom: 8 }}>
                {(movie?.genres ?? []).map((g) => (
                  <Tag key={g} style={{ marginBottom: 6 }}>
                    {g}
                  </Tag>
                ))}
              </div>

              <div style={{ marginBottom: 8 }}>
                <Text strong>Thời lượng:</Text>{" "}
                <Text>{movie?.duration || "-"}</Text>
              </div>

              <div>
                <Text strong>Thời gian chiếu:</Text>{" "}
                <Text>{movie?.showtime || "-"}</Text>
              </div>

              {/* Selected seats + action */}
              <div style={{ marginTop: 12 }}>
                <Text type="secondary">Ghế đã chọn:</Text>
                <div style={{ marginTop: 6, marginBottom: 12 }}>
                  <Text>
                    {selected.length > 0 ? selected.join(", ") : "(chưa chọn)"}
                  </Text>
                </div>

                <Button
                  block
                  type="primary"
                  disabled={selected.length === 0}
                  onClick={handleConfirm}
                >
                  Tiếp tục
                </Button>
              </div>
            </Card>
          </div>
        </div>
      </div>
    </div>
  );
}
