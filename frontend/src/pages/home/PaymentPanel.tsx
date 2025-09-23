// src/components/PaymentPanel.tsx
import { Card, Image, Typography, Radio, Space } from "antd";
import type { FC } from "react";
import infoPaymentImg from "../../assets/ic-inforpayment.png";
import paymentIcon from "../../assets/ic-payment.png";
import cardImg from "../../assets/ic-card-vn.png";
import ewalletImg from "../../assets/0oxhzjmxbksr1686814746087.png";
const { Title, Text } = Typography;

export interface PaymentPanelProps {
  selected: string[];
  remaining: number;
  paymentMethod: string;
  setPaymentMethod: (v: string) => void;
  paymentIcon?: string;
  // pass seatInfos from parent so we can read per-seat price
  seatInfos?: { seatNumber: string; price: number; id?: number }[];
}

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

const PaymentPanel: FC<PaymentPanelProps> = ({
  selected,
  remaining,
  paymentMethod,
  setPaymentMethod,
  seatInfos = [],
}) => {
  const userRaw = sessionStorage.getItem("user");
  let username = "-";
  let email = sessionStorage.getItem("email") || "-";
  if (userRaw) {
    try {
      const u = JSON.parse(userRaw as string);
      username = u?.username ?? u?.name ?? username;
      email = u?.email ?? email;
    } catch {}
  }
  username =
    username === "-"
      ? sessionStorage.getItem("username") || username
      : username;

  // build selected seat details with price from seatInfos (fallback price = 0)
  const seatDetails = selected.map((seatNumber) => {
    const info = seatInfos.find((s) => s.seatNumber === seatNumber);
    return {
      seatNumber,
      price: typeof info?.price === "number" ? info.price : 0,
    };
  });

  const total = seatDetails.reduce((acc, s) => acc + (s.price ?? 0), 0);

  // determine unitPrice if all selected seats have same price
  const unitPrice =
    seatDetails.length > 0 &&
    seatDetails.every((s) => s.price === seatDetails[0].price)
      ? seatDetails[0].price
      : undefined;

  const count = selected.length || 0;
  const unitPriceDisplay =
    unitPrice !== undefined ? formatVND(unitPrice) : "Nhiều mức giá";
  const lineTotal = total;

  return (
    <>
      <Card size="small" style={{ position: "sticky", top: 16 }}>
        <div
          style={{
            display: "flex",
            gap: 10,
            alignItems: "center",
            marginBottom: 12,
          }}
        >
          <Image src={infoPaymentImg} preview={false} alt="icon" width={35} />
          <Title level={4}>Thông tin thanh toán</Title>
        </div>

        <div style={{ padding: 12, display: "flex" }}>
          <div style={{ marginBottom: 12 }}>
            <Text
              strong
              style={{ display: "block", marginBottom: 6, marginRight: 250 }}
            >
              Tên tài khoản:
            </Text>
            <div>{username}</div>
          </div>

          <div>
            <Text strong style={{ display: "block", marginBottom: 6 }}>
              Email:
            </Text>
            <div>{email}</div>
          </div>
        </div>

        {/* --- Thay phần chi tiết ghế bằng giao diện bạn yêu cầu --- */}
        <div
          style={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
          }}
        >
          <div>
            <Text strong>GHẾ THƯỜNG</Text>
          </div>

          <div style={{ textAlign: "right" }}>
            <div style={{ marginBottom: 6 }}>
              {count} x {unitPriceDisplay}
            </div>
            <div style={{ fontWeight: 600 }}>{formatVND(lineTotal)}</div>
          </div>
        </div>

        <hr
          style={{
            marginTop: 12,
            marginBottom: 0,
            border: "none",
            borderTop: "2px solid #cdcdcdff",
          }}
        />

        <div
          style={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            marginTop: 8,
          }}
        >
          <div>
            <Text strong>Tổng cộng</Text>
          </div>
          <div style={{ fontWeight: 600, color: "red" }}>
            {formatVND(lineTotal)}
          </div>
        </div>
      </Card>

      <Card size="small" style={{ marginTop: 12 }}>
        <div style={{ display: "flex", gap: 10, alignItems: "center" }}>
          {paymentIcon && (
            <Image
              src={paymentIcon}
              preview={false}
              alt="payment icon"
              width={35}
              style={{ flex: "0 0 auto" }}
            />
          )}
          <Text strong style={{ margin: 0, fontSize: 16 }}>
            Phương thức thanh toán
          </Text>
        </div>

        <div style={{ marginTop: 8 }}>
          <Radio.Group
            onChange={(e) => setPaymentMethod(e.target.value)}
            value={paymentMethod}
          >
            <Space direction="horizontal" size="large">
              <Radio value="card">
                <Image src={cardImg} preview={false} alt="Thẻ" width={64} />
              </Radio>
              <Radio value="e_wallet">
                <Image
                  src={ewalletImg}
                  preview={false}
                  alt="Ví điện tử"
                  width={64}
                />
              </Radio>
            </Space>
          </Radio.Group>
        </div>
      </Card>

      <Card
        size="small"
        style={{ marginTop: 12, display: "flex", justifyContent: "end" }}
      >
        <Text strong>Thời gian còn lại</Text>
        <div style={{ marginTop: 8 }}>
          <Title level={4} style={{ margin: 0 }}>
            {formatTime(remaining)}
          </Title>
        </div>
      </Card>
    </>
  );
};

export default PaymentPanel;
