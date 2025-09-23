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
  Breadcrumb,
} from "antd";
import type { CSSProperties } from "react";
import screenImg from "../../assets/ic-screen.png";
import Navbar from "../../components/Navbar";
import { Content } from "antd/es/layout/layout";
import { useParams, useNavigate, Link } from "react-router-dom";
import { cinemaHallService } from "../../services/cinemaHallService";
import MovieInfoCard from "../../components/MovieInfoCard";
import PaymentPanel from "./PaymentPanel";
import { orderService } from "../../services/orderService";

const { Title, Text } = Typography;

export interface MovieInfo {
  posterUrl?: string;
  title?: string;
  genres?: string[];
  duration?: any;
  showtime?: string;
  cinemaHallId?: number | string;
}

export interface SeatSelectorProps {
  rows?: string[];
  cols?: number;
  reserved?: string[]; // prop-based reserved (optional)
  initialSelected?: string[];
  maxSelectable?: number;
  onConfirm?: (selected: string[]) => void;
  movie?: MovieInfo;
  reserveSeconds?: number;
}

const defaultRows = ["A", "B", "C", "D", "E"];

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
  } catch {}
  return { date: showtime, time: "-" };
}

type SeatInfo = {
  id: number;
  seatNumber: string;
  price: number;
  status: string;
};

export default function SeatSelector({
  rows = defaultRows,
  cols = 10,
  reserved = [],
  initialSelected = [],
  maxSelectable = 8,
  movie,
  reserveSeconds = 900,
}: SeatSelectorProps) {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [selected, setSelected] = useState<string[]>(initialSelected);
  const [remaining, setRemaining] = useState<number>(reserveSeconds);

  const [remoteMovie, setRemoteMovie] = useState<MovieInfo | null>(null);
  const [loading, setLoading] = useState<boolean>(false);
  const [authenticated, setAuthenticated] = useState<boolean | null>(null);

  // NEW: whether we are in checkout (payment) state
  const [checkoutMode, setCheckoutMode] = useState<boolean>(false);
  const [paymentMethod, setPaymentMethod] = useState<string>("card");

  // NEW: detailed seat info from API
  const [seatInfos, setSeatInfos] = useState<SeatInfo[]>([]);
  const [holding, setHolding] = useState<boolean>(false);
  // seats list used for rendering: prefer seatInfos if available, fallback to generated list
  const seatInfoMap = useMemo(() => {
    const m = new Map<string, SeatInfo>();
    for (const s of seatInfos) {
      if (s && s.seatNumber) m.set(String(s.seatNumber), s);
    }
    return m;
  }, [seatInfos]);

  // Tự động suy số cột tối đa từ API (nếu API trả A1..A12), fallback về prop cols
  const maxCol = useMemo(() => {
    if (!seatInfos || seatInfos.length === 0) return cols;
    let max = cols;
    for (const s of seatInfos) {
      const sn = String(s.seatNumber);
      const m = sn.match(/([A-Za-z]+)(\d+)/);
      if (m) {
        const num = parseInt(m[2], 10);
        if (!isNaN(num) && num > max) max = num;
      }
    }
    return max;
  }, [seatInfos, cols]);

  // Tạo danh sách ghế theo ROW-MAJOR: hàng letters (A,B,...) -> cột numbers (1..maxCol)
  // => A1, A2, A3, ... , B1, B2, B3, ...
  const seats = useMemo(() => {
    const out: string[] = [];
    for (const letter of rows) {
      for (let num = 1; num <= maxCol; num++) {
        out.push(`${letter}${num}`);
      }
    }

    // Nếu bạn chỉ muốn hiển thị những ghế API trả (API có thể trả subset),
    // bỏ comment dòng dưới:
    // return out.filter((id) => seatInfoMap.has(id));

    return out;
  }, [rows, maxCol, seatInfoMap]);

  // reserved set derived from seatInfos where status is not AVAILABLE
  const seatStatusMap = useMemo(() => {
    const m = new Map<string, string>();
    for (const s of seatInfos) {
      if (s && s.seatNumber)
        m.set(String(s.seatNumber), (s.status ?? "UNKNOWN").toUpperCase());
    }
    return m;
  }, [seatInfos]);

  // reserved set derived from seatInfos where status is not AVAILABLE
  const reservedFromApi = useMemo(() => {
    const set = new Set<string>();
    for (const [seat, status] of seatStatusMap.entries()) {
      if (status !== "AVAILABLE") set.add(seat);
    }
    return set;
  }, [seatStatusMap]);

  function isLoggedInLocal() {
    try {
      const token = sessionStorage.getItem("token");
      if (token) return true;

      const userStr = sessionStorage.getItem("user");
      if (userStr) {
        try {
          const u = JSON.parse(userStr);
          if (u && (u.userId || u.token || u.email || u.username)) return true;
        } catch {
          // ignore
        }
      }
      if (sessionStorage.getItem("userId")) return true;
      if (sessionStorage.getItem("email")) return true;
      return false;
    } catch (e) {
      console.warn("Error checking sessionStorage for login", e);
      return false;
    }
  }

  useEffect(() => {
    const ok = isLoggedInLocal();
    setAuthenticated(ok);
    if (!ok) {
      message.info("Bạn cần đăng nhập để chọn ghế");
      const next = `/seat/${id ?? ""}`;
      navigate(`/login?next=${encodeURIComponent(next)}`);
    }
  }, [id]);

  useEffect(() => {
    setRemaining(reserveSeconds);
  }, [reserveSeconds]);

  useEffect(() => {
    const onAuthLogout = () => {
      message.info("Bạn đã đăng xuất");
      const next = `/seat/${id ?? ""}`;
      navigate(`/login?next=${encodeURIComponent(next)}`);
    };
    window.addEventListener("auth:logout", onAuthLogout);
    return () => window.removeEventListener("auth:logout", onAuthLogout);
  }, [id, navigate]);

  useEffect(() => {
    if (remaining <= 0) return;
    const idInterval = setInterval(() => {
      setRemaining((s) => Math.max(0, s - 1));
    }, 1000);
    return () => clearInterval(idInterval);
  }, [remaining]);

  useEffect(() => {
    let mounted = true;
    if (!id) return;
    if (authenticated === false) return;
    (async () => {
      try {
        setLoading(true);
        const hall = await cinemaHallService.getById(Number(id));
        if (!mounted) return;
        const m = hall?.movie ?? null;
        const movieInfo: MovieInfo = {
          posterUrl: m?.poster ?? undefined,
          title: m?.title ?? "-",
          genres: Array.isArray(m?.genres) ? m.genres : [],
          duration: m?.duration,
          showtime: hall?.movieSession ?? undefined,
          cinemaHallId: hall?.id ?? Number(id),
        };
        setRemoteMovie(movieInfo);

        // NEW: map seats from API into seatInfos state
        const apiSeats = Array.isArray(hall?.seats) ? hall.seats : [];
        const mapped: SeatInfo[] = apiSeats.map((s: any) => ({
          id: s.id,
          seatNumber: String(s.seatNumber),
          price: typeof s.price === "number" ? s.price : Number(s.price),
          status: s.status ?? "UNKNOWN",
        }));
        setSeatInfos(mapped);
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
  }, [id, authenticated]);

  function toggleSeat(seatId: string) {
    // if seat exists in seatInfos and is not available, block
    const info = seatInfos.find((s) => s.seatNumber === seatId);
    console.log("Seat clicked:", {
      id: info?.id ?? null,
      seatNumber: seatId,
      price: info?.price ?? 0,
      status: info?.status ?? "UNKNOWN",
    });
    if (info && info.status && info.status.toUpperCase() !== "AVAILABLE")
      return;
    // also block if reserved prop includes it
    if (
      reserved.includes(seatId) ||
      reservedFromApi.has(seatId) ||
      checkoutMode
    )
      return;
    if (selected.includes(seatId)) {
      setSelected((s) => s.filter((x) => x !== seatId));
      return;
    }
    if (typeof maxSelectable === "number" && selected.length >= maxSelectable) {
      message.warning(`Bạn chỉ được chọn tối đa ${maxSelectable} ghế`);
      return;
    }
    setSelected((s) => [...s, seatId]);
  }

  async function handlePay(): Promise<void> {
    if (!selected || selected.length === 0) {
      message.warning("Vui lòng chọn ghế trước khi thanh toán.");
      return;
    }

    // Lấy userId
    let userId: number | undefined;
    try {
      const userStr = sessionStorage.getItem("user");
      if (userStr) {
        const u = JSON.parse(userStr);
        userId = u?.id ?? u?.userId ?? undefined;
      }
      if (!userId) {
        const uid = sessionStorage.getItem("userId");
        if (uid) userId = Number(uid);
      }
    } catch (e) {
      console.warn("Không parse user từ sessionStorage", e);
    }

    // cinemaHallId
    const cinemaHallIdRaw =
      (movieToShow && movieToShow.cinemaHallId) ?? (id ? id : undefined);
    const cinemaHallIdNum: number | undefined = (() => {
      if (cinemaHallIdRaw === undefined || cinemaHallIdRaw === null)
        return undefined;
      if (typeof cinemaHallIdRaw === "number") return cinemaHallIdRaw;
      const n = Number(cinemaHallIdRaw);
      return Number.isFinite(n) ? n : undefined;
    })();
    if (!cinemaHallIdNum) {
      message.error("Không xác định được cinemaHallId. Vui lòng thử lại.");
      return;
    }

    // build seats payload (dùng id từ seatInfos)
    const seatsPayload = selected.map((sid) => {
      const si = seatInfos.find((s) => s.seatNumber === sid);
      return { seat: { id: si?.id }, price: String(si?.price ?? 0) };
    });
    const missing = seatsPayload.findIndex((p) => p.seat.id === undefined);
    if (missing !== -1) {
      message.error(
        `Không tìm thấy id cho ghế ${selected[missing]}. Không thể thanh toán.`
      );
      return;
    }

    setHolding(true);
    try {
      // 1) initOrder
      const initResp = await orderService.initOrder({
        userId: userId ?? 0,
        cinemaHallId: cinemaHallIdNum,
      });

      // extract orderId (thử nhiều dạng)
      let orderId: string | undefined;
      if (initResp) {
        if (typeof (initResp as any).data === "string")
          orderId = (initResp as any).data;
        else if (typeof (initResp as any).data === "object")
          orderId =
            (initResp as any).data.orderId ??
            (initResp as any).data.data ??
            undefined;
        else orderId = (initResp as any).orderId ?? undefined;
      }
      if (!orderId) throw new Error("Không tạo được orderId từ initOrder");

      // 2) seatHold
      const holdResp = await orderService.seatHold({
        orderId,
        userId,
        cinemaHallId: cinemaHallIdNum,
        seats: seatsPayload,
      });

      const holdOk =
        holdResp &&
        ((typeof (holdResp as any).code === "number" &&
          (holdResp as any).code === 200) ||
          (holdResp as any).success === true ||
          !("code" in (holdResp as any)));

      if (!holdOk) {
        const err =
          (holdResp as any)?.message ??
          (holdResp as any)?.error ??
          "Giữ ghế thất bại";
        message.error(err);
        console.error("seatHold failed:", holdResp);
        return;
      }

      message.success("Giữ ghế thành công — chuyển tới cổng thanh toán...");

      // 3) gọi vnpay để lấy link -> redirect
      // CHÚ Ý: kiểm tra backend cần amount units nào. Nếu cần nhân 100 thì sửa bên dưới.
      const amountToSend = total; // hoặc total * 100 nếu backend yêu cầu
      // mapping paymentMethod -> bankCode nếu có
      const bankCode = undefined;
      const { raw, paymentUrl } = await orderService.vnpay(
        amountToSend,
        bankCode,
        orderId // <-- truyền orderId vào vnp_TxnRef
      );

      if (paymentUrl) {
        // redirect người dùng sang đường dẫn paymentUrl
        window.location.href = paymentUrl;
        return; // dừng flow — user sẽ bị điều hướng
      }

      // fallback nếu không có paymentUrl
      console.warn("VNPAY no paymentUrl:", raw);
      message.warning("Không lấy được link thanh toán. Vui lòng thử lại.");

      // (tuỳ ý) chuyển tới confirmation / trang khác nếu cần
      const selectedSeatDetails = selected.map((sid) => {
        const si = seatInfos.find((s) => s.seatNumber === sid);
        return (
          si ?? { id: undefined, seatNumber: sid, price: 0, status: "UNKNOWN" }
        );
      });
      navigate("/confirmation", {
        replace: true,
        state: {
          movie: movieToShow,
          selectedSeats: selectedSeatDetails,
          total,
        },
      });
    } catch (err: any) {
      console.error("Payment flow error:", err);
      message.error(err?.message ?? "Lỗi khi xử lý thanh toán");
    } finally {
      setHolding(false);
    }
  }

  const total = selected.reduce((acc, sId) => {
    const si = seatInfos.find((s) => s.seatNumber === sId);
    return acc + (si?.price ?? 0);
  }, 0);
  const movieToShow = remoteMovie ?? movie;
  const { date: showDate, time: showTime } = parseShowtime(
    movieToShow?.showtime
  );
  const genresText =
    movieToShow?.genres && movieToShow?.genres.length
      ? movieToShow.genres.join(", ")
      : "-";

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
              {/* LEFT: either seat grid (default) or payment panel (checkoutMode) */}
              <div style={{ flex: 1 }}>
                <div style={{ marginBottom: 12 }}>
                  <Breadcrumb>
                    <Breadcrumb.Item>
                      <Link to="/">Trang chủ</Link>
                    </Breadcrumb.Item>
                    <Breadcrumb.Item>Đặt vé</Breadcrumb.Item>
                  </Breadcrumb>
                </div>

                {!checkoutMode ? (
                  <>
                    {/* Legend */}
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

                    {/* Screen */}
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
                    <Card
                      style={{ boxShadow: "none", background: "transparent" }}
                    >
                      <div
                        style={{
                          display: "grid",
                          gap: 12,
                          border: "0",
                          gridTemplateColumns: `repeat(${maxCol}, 1fr)`,
                        }}
                      >
                        {seats.map((seatId) => {
                          const status = seatStatusMap.get(seatId) ?? "UNKNOWN";
                          const isBooked = status === "BOOKED"; // theo yêu cầu: BOOKED = đỏ
                          const isSelected = selected.includes(seatId);
                          const isReservedProp = reserved.includes(seatId);
                          const disabled =
                            isBooked || isReservedProp || checkoutMode;

                          // màu: BOOKED -> đỏ (#ff4d4f), SELECTED -> #2f54eb, ngược lại trắng
                          const background = isBooked
                            ? "#ff4d4f"
                            : isSelected
                            ? "#2f54eb"
                            : "#fff";
                          const color =
                            isBooked || isSelected ? "#fff" : undefined;
                          const borderColor = isBooked ? "#ff4d4f" : undefined;

                          return (
                            <Button
                              key={seatId}
                              onClick={() => toggleSeat(seatId)}
                              aria-pressed={isSelected}
                              aria-label={`Ghế ${seatId} ${
                                isBooked
                                  ? "đã đặt"
                                  : isSelected
                                  ? "đang chọn"
                                  : "trống"
                              }`}
                              disabled={disabled}
                              style={{
                                ...seatStyle,
                                background,
                                color,
                                borderColor,
                              }}
                              // dùng 'default' để không ghi đè style nền custom; màu chọn đã set bằng inline style
                              type="default"
                              size="small"
                            >
                              {seatId}
                            </Button>
                          );
                        })}
                      </div>
                    </Card>

                    {/* Bottom summary */}
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
                            <Title
                              level={4}
                              style={{ margin: 0, fontSize: 20 }}
                            >
                              {formatVND(total)}
                            </Title>
                          </div>
                        </div>
                        <div style={{ textAlign: "right" }}>
                          <Text style={{ display: "block", marginBottom: 4 }}>
                            Thời gian còn lại
                          </Text>
                          <div>
                            <Title
                              level={4}
                              style={{ margin: 0, fontSize: 20 }}
                            >
                              {formatTime(remaining)}
                            </Title>
                          </div>
                        </div>
                      </div>
                    </Card>
                  </>
                ) : (
                  // show payment panel instead of seats
                  <PaymentPanel
                    selected={selected}
                    remaining={remaining}
                    paymentMethod={paymentMethod}
                    setPaymentMethod={setPaymentMethod}
                    seatInfos={seatInfos}
                  />
                )}
              </div>

              {/* RIGHT: always MovieInfoCard */}
              <div style={{ width: 360, flexShrink: 0 }}>
                <MovieInfoCard
                  movie={movieToShow}
                  genresText={genresText}
                  showDate={showDate}
                  showTime={showTime}
                  selectedSeats={selected}
                  totalFormatted={formatVND(total)}
                  remainingFormatted={formatTime(remaining)}
                  onConfirm={() => {
                    if (selected.length === 0) {
                      message.warning("Vui lòng chọn ghế trước khi tiếp tục.");
                      return;
                    }
                    setCheckoutMode(true);
                  }}
                  showCheckoutActions={checkoutMode}
                  onBack={() => setCheckoutMode(false)}
                  onPay={handlePay}
                  confirmLoading={holding}
                  payLoading={holding}
                />
              </div>
            </div>
          </div>
        </div>
      </Content>
    </Layout>
  );
}
