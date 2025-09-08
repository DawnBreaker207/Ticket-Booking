import React, { useEffect, useMemo } from "react";
import { Row, Card, Button, Typography, Image, Layout, message } from "antd";
import Navbar from "../../components/Navbar";
import { useAppDispatch } from "../../hooks/useAppDispatch";
import { useAppSelector } from "../../hooks/useAppSelector";
import { setCinemaHalls } from "../../features/cinemaHalls/cinemaHallsSlice";
import { cinemaHallService } from "../../services/cinemaHallService";
const { Title, Text } = Typography;
import type { Movie as MovieType } from "../../types/Movie";
import type { CinemaHall } from "../../types/CinemaHall";
const { Content } = Layout;

const CARD_WIDTH = 227;
const CARD_HEIGHT = 570;
const IMAGE_HEIGHT = 360;

const HomePage: React.FC = () => {
  const dispatch = useAppDispatch();
  const cinemaHalls = useAppSelector(
    (s) => s.cinemaHalls.items
  ) as CinemaHall[];

  useEffect(() => {
    let mounted = true;
    const load = async () => {
      try {
        const res = await cinemaHallService.getAll();
        if (!mounted) return;
        dispatch(setCinemaHalls(res));
      } catch (err: any) {
        console.error("Load cinema halls failed", err);
        message.error(
          err?.response?.data?.message ?? "Không thể tải suất chiếu"
        );
      }
    };
    load();
    return () => {
      mounted = false;
    };
  }, [dispatch]);

  const moviesFromHalls: MovieType[] = useMemo(() => {
    const map = new Map<number | string, MovieType>();
    if (!Array.isArray(cinemaHalls)) return [];

    for (const h of cinemaHalls) {
      const m = (h as any)?.movie as MovieType | undefined;
      if (!m) continue;
      const key = (m.id ?? m.title) as number | string;

      if (!map.has(key)) {
        const normalized: MovieType = {
          ...(m as any),
          id: Number(m.id ?? key),
          poster: m.poster,
          title: m.title,
          genres: (m as any).genres,
          duration: (m as any).duration,
        };
        map.set(key, normalized);
      }
    }

    return Array.from(map.values());
  }, [cinemaHalls]);

  const movies = moviesFromHalls;
  return (
    <Layout style={{ minHeight: "100vh" }}>
      <Navbar />
      <Content
        style={{
          maxWidth: "1200px",
          marginRight: "auto",
          marginLeft: "auto",
          paddingTop: 30,
        }}
      >
        <Title level={2} style={{ textAlign: "center", margin: "24px 0" }}>
          PHIM ĐANG CHIẾU
        </Title>
        <Row
          gutter={[16, 16]}
          justify="center"
          style={{
            paddingBottom: 30,
            display: "flex",
            flexWrap: "wrap",
            gap: `60px`,
          }}
        >
          {movies.map((movie, idx) => (
            <div
              key={movie.id ?? `movie-${idx}`}
              style={{
                flex: `0 0 ${CARD_WIDTH}px`,
                maxWidth: CARD_WIDTH,
                display: "flex",
                justifyContent: "center",
              }}
            >
              <Card
                cover={
                  <Image
                    alt={movie.title}
                    src={movie.poster}
                    preview={false}
                    style={{
                      width: "100%",
                      height: IMAGE_HEIGHT,
                      objectFit: "cover",
                      display: "block",
                      borderRadius: 8,
                    }}
                  />
                }
                style={{
                  width: CARD_WIDTH,
                  height: CARD_HEIGHT,
                  display: "flex",
                  flexDirection: "column",
                  overflow: "hidden",
                  border: "none",
                  boxShadow: "none",
                  background: "transparent",
                }}
                styles={{
                  body: {
                    padding: 12,
                    display: "flex",
                    flexDirection: "column",
                    flex: 1,
                  },
                }}
              >
                <div>
                  <Title
                    level={4}
                    style={{ margin: 0, padding: 0, lineHeight: 1.2 }}
                  >
                    {movie.title}
                  </Title>
                  <Text style={{ display: "block", marginTop: 6 }}>
                    <b>Thể loại:</b> {movie.genres.join(", ")}
                  </Text>
                  <Text style={{ display: "block", marginTop: 4 }}>
                    <b>Thời lượng:</b> {movie.duration} phút
                  </Text>
                </div>

                <div style={{ marginTop: "auto" }}>
                  <Button
                    type="primary"
                    style={{
                      width: "100%",
                      padding: "6px 12px",
                      marginTop: 8,
                      fontWeight: "bold",
                      background: "#1D81C3",
                    }}
                  >
                    MUA VÉ
                  </Button>
                </div>
              </Card>
            </div>
          ))}
        </Row>
      </Content>
    </Layout>
  );
};

export default HomePage;
