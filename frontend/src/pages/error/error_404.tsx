type Props = {
  homeUrl?: string;
  label?: string;
};

export default function NotFoundPage({
  homeUrl = "/",
  label = "Về trang chủ",
}: Props) {
  const goHome = () => {
    window.location.href = homeUrl;
  };

  return (
    <div
      style={{
        minHeight: "100vh",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        background: "#f8fafc",
      }}
    >
      <div style={{ textAlign: "center", padding: 24 }}>
        <h1 style={{ fontSize: 170, margin: 0, color: "#1e293b" }}>404</h1>
        <p style={{ marginTop: 8, marginBottom: 16, fontSize: 18 }}>
          Trang không tìm thấy
        </p>
        <button
          onClick={goHome}
          style={{
            padding: "10px 20px",
            fontSize: 16,
            cursor: "pointer",
            backgroundColor: "#2563eb",
            color: "#fff",
            border: "none",
            borderRadius: 6,
          }}
        >
          {label}
        </button>
      </div>
    </div>
  );
}
