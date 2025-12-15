// src/components/MovieInfoCard.tsx
import { Card, Image, Typography, Button } from "antd";

const { Title, Text } = Typography;

export interface MovieInfo {
  posterUrl?: string;
  title?: string;
  genres?: string[];
  duration?: any;
  showtime?: string;
  cinemaHallId?: number | string;
}

interface MovieInfoCardProps {
  movie?: MovieInfo | null;
  genresText?: string;
  showDate?: string;
  showTime?: string;
  selectedSeats?: string[];
  totalFormatted?: string;
  onConfirm?: () => void;
  showCheckoutActions?: boolean;
  onBack?: () => void;
  onPay?: () => void;
  confirmLoading?: boolean;
  payLoading?: boolean;
}

export default function MovieInfoCard({
  movie,
  genresText = "",
  showDate = "",
  showTime = "",
  selectedSeats = [],
  onConfirm,
  showCheckoutActions = false,
  onBack,
  onPay,
  confirmLoading = false,
  payLoading = false,
}: MovieInfoCardProps) {
  const hasSeats = Array.isArray(selectedSeats) && selectedSeats.length > 0;

  return (
    <Card size="small" style={{ position: "sticky", top: 16 }}>
      <div style={{ marginBottom: 12 }}>
        {movie?.posterUrl ? (
          <Image
            src={movie.posterUrl}
            alt={movie.title || "Poster"}
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
        {movie?.title}
      </Title>

      <div style={{ marginBottom: 8 }}>
        <Text strong>Thể loại: </Text>
        <Text>{genresText}</Text>
      </div>

      <div style={{ marginBottom: 8 }}>
        <Text strong>Thời lượng:</Text> <Text>{movie?.duration} phút</Text>
      </div>
      {movie?.cinemaHallId !== undefined && (
        <div style={{ marginBottom: 8 }}>
          <Text strong>Rạp chiếu:</Text> <Text>{movie.cinemaHallId}</Text>
        </div>
      )}
      <div style={{ marginBottom: 8 }}>
        <Text strong>Ngày chiếu:</Text> <Text>{showDate}</Text>
      </div>

      <div>
        <Text strong>Giờ chiếu:</Text> <Text>{showTime}</Text>
      </div>

      <div style={{ marginTop: 12 }}>
        <Text strong>Ghế đã chọn:</Text>
        <div style={{ marginTop: 6, marginBottom: 12 }}>
          <Text>{selectedSeats.join(", ")}</Text>
        </div>
        {showCheckoutActions ? (
          <div style={{ display: "flex", gap: 8 }}>
            <Button block onClick={onBack}>
              Quay lại
            </Button>
            <Button
              block
              type="primary"
              onClick={onPay}
              disabled={!hasSeats}
              loading={payLoading}
            >
              Thanh toán
            </Button>
          </div>
        ) : (
          <Button
            block
            type="primary"
            onClick={onConfirm}
            style={{ marginTop: 12 }}
            loading={confirmLoading}
            disabled={confirmLoading}
          >
            Tiếp tục
          </Button>
        )}
      </div>
    </Card>
  );
}
