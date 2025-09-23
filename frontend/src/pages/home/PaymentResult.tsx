// src/pages/PaymentResult.tsx
import { useMemo, useEffect, useState, useRef, type JSX } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { Result, Button, message, Typography } from "antd";
import { orderService } from "../../services/orderService";

const { Text } = Typography;

export default function PaymentResult(): JSX.Element {
  const location = useLocation();
  const navigate = useNavigate();
  const didConfirmRef = useRef(false);
  const [secondsLeft, setSecondsLeft] = useState<number>(60);

  const qp = useMemo(() => {
    const p = new URLSearchParams(location.search);
    const get = (k: string) => p.get(k) ?? undefined;

    const txnRef = get("vnp_TxnRef") ?? get("vnp_TrxRef") ?? "-";
    const responseCode = (get("vnp_ResponseCode") ?? "-").toString();
    const amountRaw = get("vnp_Amount") ?? "0";
    const amountNum = (() => {
      const n = Number(amountRaw);
      return Number.isFinite(n) ? n : 0;
    })();
    const orderInfo = get("vnp_OrderInfo")
      ? decodeURIComponent(get("vnp_OrderInfo") as string)
      : "-";

    const bankCode = get("vnp_BankCode") ?? "-";
    const payDate = get("vnp_PayDate") ?? "-";
    const txnNo = get("vnp_TransactionNo") ?? "-";
    const txnStatus = get("vnp_TransactionStatus") ?? "-";
    const secureHash = get("vnp_SecureHash") ?? "-";

    return {
      txnRef,
      responseCode,
      amountNum,
      orderInfo,
      bankCode,
      payDate,
      txnNo,
      txnStatus,
      secureHash,
      rawSearch: location.search,
    };
  }, [location.search]);

  const success = qp.responseCode === "00";

  useEffect(() => {
    if (!success) return;

    if (didConfirmRef.current) {
      console.debug("confirmOrder skipped: already attempted (didConfirmRef)");
      return;
    }

    didConfirmRef.current = true;

    let mounted = true;
    const key = "confirmOrder";

    const doConfirm = async () => {
      let userId: number | undefined;
      try {
        const userRaw = sessionStorage.getItem("user");
        if (userRaw) {
          const u = JSON.parse(userRaw);
          userId = u?.id ?? u?.userId ?? undefined;
        }
        if (!userId) {
          const uid = sessionStorage.getItem("userId");
          if (uid) userId = Number(uid);
        }
      } catch (e) {
        console.warn("Không parse user từ sessionStorage", e);
      }

      let cinemaHallId: number | undefined;
      try {
        const ch = sessionStorage.getItem("cinemaHallId");
        if (ch) {
          const n = Number(ch);
          if (Number.isFinite(n)) cinemaHallId = n;
        }
      } catch (e) {
        console.warn("Không parse cinemaHallId từ sessionStorage", e);
      }

      const payload = {
        orderId: qp.txnRef,
        userId: userId,
        cinemaHallId: cinemaHallId,
        orderStatus: "CONFIRMED",
        paymentMethod: "CASH",
        paymentStatus: "PENDING",
        totalAmount: String(qp.amountNum),
      };

      try {
        console.debug("Calling confirmOrder with payload:", payload);
        const resp = await orderService.confirmOrder(payload);
        if (!mounted) return;

        const ok =
          !!resp &&
          ((typeof (resp as any).code === "number" &&
            (resp as any).code === 200) ||
            (resp as any).success === true ||
            !("code" in (resp as any)));

        if (ok) {
          message.success({
            content: "Xác nhận đơn thành công",
            key,
            duration: 3,
          });
          try {
            sessionStorage.removeItem("cinemaHallId");
            sessionStorage.removeItem("orderId");
          } catch (e) {
            /* ignore */
          }
        } else {
          message.error({ content: "Xác nhận đơn thất bại", key, duration: 4 });
          console.error("confirmOrder failed:", resp);
        }
      } catch (err: any) {
        if (!mounted) return;
        console.error("Error calling confirmOrder:", err);
        console.error("error.response:", err?.response ?? err);
        message.error({
          content: err?.message ?? "Lỗi khi xác nhận đơn",
          key,
          duration: 4,
        });
      }
    };

    doConfirm();

    return () => {
      mounted = false;
    };
  }, [success, qp.txnRef, qp.amountNum]);

  useEffect(() => {
    if (secondsLeft <= 0) {
      navigate("/");
      return;
    }
    const t = setInterval(() => {
      setSecondsLeft((s) => s - 1);
    }, 1000);

    return () => clearInterval(t);
  }, [secondsLeft]);

  return (
    <div
      style={{
        minHeight: "100vh",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        padding: 24,
        background: "#f5f7fb",
      }}
    >
      <div style={{ textAlign: "center" }}>
        <Result
          status={success ? "success" : "error"}
          title={success ? "Thanh toán thành công" : "Thanh toán thất bại"}
          subTitle={
            success
              ? `Giao dịch ${qp.txnRef} đã được xử lý thành công.`
              : `Giao dịch ${qp.txnRef} không thành công (mã: ${qp.responseCode}).`
          }
          extra={[
            <Button key="home" type="primary" onClick={() => navigate("/")}>
              Về trang chủ
            </Button>,
          ]}
        />

        <div style={{ marginTop: 12 }}>
          <Text type="secondary">
            Trang sẽ tự động chuyển về trang chủ sau{" "}
            <Text strong>{secondsLeft}</Text> giây.
          </Text>
        </div>
      </div>
    </div>
  );
}
