// frontend/src/components/Countdown.tsx
import React, { useEffect, useRef, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { message } from "antd";
import socketService from "../services/socketService";
import {
  updateCountdownTTL,
  selectRemainingTime,
} from "../features/countdown/countdownSlice";

const formatTTL = (seconds: number) => {
  const s = Math.max(0, Math.floor(Number(seconds) || 0));
  const m = Math.floor(s / 60)
    .toString()
    .padStart(2, "0");
  const ss = (s % 60).toString().padStart(2, "0");
  return `${m}:${ss}`;
};

const Countdown: React.FC = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const ttl = useSelector(selectRemainingTime) as number | undefined;

  const ttlRef = useRef<number>(typeof ttl === "number" ? ttl : 0);
  const prevTtlRef = useRef<number | null>(null);
  const redirectedRef = useRef(false);

  useEffect(() => {
    const newVal = typeof ttl === "number" ? ttl : 0;
    ttlRef.current = newVal;
    if (newVal > 0) redirectedRef.current = false;
    prevTtlRef.current = prevTtlRef.current ?? newVal;
  }, [ttl]);

  const [orderId, setOrderId] = useState<string | null>(() =>
    sessionStorage.getItem("orderId")
  );

  useEffect(() => {
    const read = () => sessionStorage.getItem("orderId");

    const onStorage = (e: StorageEvent) => {
      if (e.key === "orderId") {
        setOrderId(e.newValue);
      }
    };
    const onCustom = (ev: Event) => {
      const val = (ev as CustomEvent).detail as string | null;
      setOrderId(val ?? read());
    };

    window.addEventListener("storage", onStorage);
    window.addEventListener("orderId:changed", onCustom as EventListener);

    setOrderId(read());

    return () => {
      window.removeEventListener("storage", onStorage);
      window.removeEventListener("orderId:changed", onCustom as EventListener);
    };
  }, []);

  useEffect(() => {
    if (!orderId) {
      // console.log('[Countdown] no orderId -> not subscribing');
      return;
    }

    const topic = `/topic/order/${orderId}`;
    const sub = socketService.watchOrder(topic).subscribe({
      next: (msg: any) => {
        let body: any;
        try {
          body = typeof msg.body === "string" ? JSON.parse(msg.body) : msg.body;
        } catch {
          return;
        }
        if (body?.event === "TTL_SYNC" && body.ttl !== undefined) {
          const ttlNum = Number(body.ttl);
          if (!Number.isFinite(ttlNum)) return;
          const n = Math.max(0, Math.floor(ttlNum));
          ttlRef.current = n;
          dispatch(updateCountdownTTL({ ttl: n }));
        }
      },
      error: (err: any) => {
        console.error("[Countdown] socket subscription error", err);
      },
    });

    return () => {
      try {
        sub.unsubscribe();
      } catch {}
    };
  }, [orderId, dispatch]);

  useEffect(() => {
    const id = window.setInterval(() => {
      const cur = ttlRef.current ?? 0;
      if (cur > 0) {
        const next = cur - 1;
        ttlRef.current = next;
        dispatch(updateCountdownTTL({ ttl: next }));
      }
    }, 1000);
    return () => clearInterval(id);
  }, [dispatch]);

  useEffect(() => {
    const current = typeof ttl === "number" ? ttl : 0;
    const prev = prevTtlRef.current ?? current;

    if (prev > 0 && current === 0 && !redirectedRef.current) {
      redirectedRef.current = true;
      message.info("Thời gian giữ ghế đã hết — chuyển về trang chủ");
      setTimeout(() => navigate("/", { replace: true }), 400);
    }
    prevTtlRef.current = current;
  }, [ttl, navigate]);

  return (
    <div style={{ textAlign: "right" }}>
      <div style={{ fontSize: 18, fontWeight: 600 }}>{formatTTL(ttl ?? 0)}</div>
    </div>
  );
};

export default Countdown;
