// src/pages/CinemaHallPage.tsx
import React, { useEffect, useRef, useState } from "react";
import {
  Table,
  Button,
  Input,
  Space,
  Modal,
  Form,
  DatePicker,
  Row,
  Col,
  Typography,
  Popconfirm,
  message,
  Spin,
  Image,
  List,
  Avatar,
} from "antd";
import {
  PlusOutlined,
  EditOutlined,
  EyeOutlined,
  DeleteOutlined,
  SearchOutlined,
} from "@ant-design/icons";
import type { ColumnsType } from "antd/es/table";
import moment from "moment";
import type { Moment } from "moment";
import type { Movie } from "../../types/Movie";
import movieService from "../../services/Movie";
import { useAppDispatch } from "../../hooks/useAppDispatch";
import { useAppSelector } from "../../hooks/useAppSelector";
import {
  setCinemaHalls,
  addCinemaHall,
  updateCinemaHall,
  removeCinemaHall,
} from "../../features/cinemaHalls/cinemaHallsSlice";
import type { CinemaHall } from "../../types/CinemaHall";
import { cinemaHallService } from "../../services/cinemaHallService";

const { Title } = Typography;

const parseSession = (s?: string) => {
  if (!s) return { date: "-", time: "-", iso: "" };
  let m = moment(s);
  if (!m.isValid()) {
    m = moment(s, "DD/MM/YYYY HH:mm");
  }
  if (!m.isValid()) {
    return { date: s, time: "-", iso: "" };
  }
  return {
    date: m.format("DD/MM/YYYY"),
    time: m.format("HH:mm"),
    iso: m.toISOString(),
  };
};

const parseReleaseDate = (rd?: string | null) => {
  if (!rd) return { display: "-", iso: undefined };
  const ddmmyyyy = /^\d{2}\/\d{2}\/\d{4}$/;
  if (ddmmyyyy.test(rd)) {
    const m = moment(rd, "DD/MM/YYYY");
    if (m.isValid())
      return {
        display: m.format("DD/MM/YYYY"),
        iso: m.startOf("day").toISOString(),
      };
    return { display: rd, iso: undefined };
  }

  const m = moment(rd);
  if (m.isValid())
    return { display: m.format("DD/MM/YYYY"), iso: m.toISOString() };
  return { display: rd, iso: undefined };
};

const CinemaHallPage: React.FC = () => {
  const dispatch = useAppDispatch();
  const items = useAppSelector((s) => s.cinemaHalls.items);
  const [movieOptions, setMovieOptions] = useState<Movie[]>([]);
  const [loadingMovies, setLoadingMovies] = useState(false);

  const [search, setSearch] = useState("");
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);

  const [isEditModalOpen, setEditModalOpen] = useState(false);
  const [isViewModalOpen, setViewModalOpen] = useState(false);
  const [isSearchMovieModalOpen, setSearchMovieModalOpen] = useState(false);

  const [editingId, setEditingId] = useState<number | null>(null);
  const [viewing, setViewing] = useState<CinemaHall | null>(null);

  const [form] = Form.useForm<{
    movieId?: number;
    movieTitle?: string;
    movieSession?: Moment;
  }>();
  const searchTimer = useRef<number | null>(null);

  useEffect(() => {
    let mounted = true;
    const load = async () => {
      try {
        setLoadingMovies(true);
        const res = await cinemaHallService.getAll();
        if (!mounted) return;
        dispatch(setCinemaHalls(res));
      } catch (err: any) {
        console.error("Load cinema halls failed", err);
        message.error(
          err?.response?.data?.message ?? "Không thể tải suất chiếu"
        );
      } finally {
        if (mounted) setLoadingMovies(false);
      }
    };
    load();
    return () => {
      mounted = false;
    };
  }, [dispatch]);

  const filtered = items.filter((item) => {
    const session = parseSession(item.movieSession);
    const sessionText = `${session.date} ${session.time}`.toLowerCase();
    const title = (item.movie?.title ?? "").toLowerCase();
    const q = search.toLowerCase();
    return sessionText.includes(q) || title.includes(q);
  });

  const openAdd = () => {
    form.resetFields();
    setEditingId(null);
    setEditModalOpen(true);
  };

  const openEdit = (record: CinemaHall) => {
    form.setFieldsValue({
      movieId: record.movie?.id,
      movieTitle: record.movie?.title,
      movieSession: moment(record.movieSession),
    });

    if (record.movie) {
      setMovieOptions((prev) => {
        const exists = prev.find((m) => m.id === record.movie!.id);
        if (exists) return prev;
        return [record.movie!, ...prev];
      });
    }

    setEditingId(record.id);
    setEditModalOpen(true);
  };

  const openView = (record: CinemaHall) => {
    setViewing(record);
    setViewModalOpen(true);
  };

  const closeEdit = () => {
    form.resetFields();
    setEditingId(null);
    setEditModalOpen(false);
  };

  const closeView = () => {
    setViewing(null);
    setViewModalOpen(false);
  };

  const openSearchMovieModal = () => {
    setSearchMovieModalOpen(true);
  };

  const closeSearchMovieModal = () => {
    setSearchMovieModalOpen(false);
  };

  const handleMovieSearch = (q: string) => {
    if (searchTimer.current) {
      window.clearTimeout(searchTimer.current);
      searchTimer.current = null;
    }
    if (!q || q.trim().length < 1) {
      setMovieOptions([]);
      return;
    }
    setLoadingMovies(true);
    searchTimer.current = window.setTimeout(async () => {
      try {
        const results = await movieService.getMovies(q.trim());
        setMovieOptions(Array.isArray(results) ? results : []);
      } catch (err) {
        console.warn("Search movies failed", err);
        setMovieOptions([]);
      } finally {
        setLoadingMovies(false);
        searchTimer.current = null;
      }
    }, 400);
  };

  const handleSelectMovieFromSearch = (m: Movie) => {
    form.setFieldsValue({ movieId: m.id, movieTitle: m.title });
    closeSearchMovieModal();
  };

  const handleSave = async () => {
    try {
      const values = await form.validateFields();
      const mSession: Moment = values.movieSession!;
      const selectedMovieId = values.movieId;
      const movieObj = movieOptions.find(
        (m) => m.id === Number(selectedMovieId)
      );

      if (!movieObj) {
        message.error(
          'Phim không hợp lệ - vui lòng chọn phim bằng nút "Tìm phim"'
        );
        return;
      }

      const movieSessionIso = mSession.toDate().toISOString();

      const normalizeReleaseDate = (
        rd?: string | undefined
      ): string | undefined => {
        if (!rd) return undefined;
        const ddmmyyyy = /^\d{2}\/\d{2}\/\d{4}$/;
        if (ddmmyyyy.test(rd)) {
          return moment(rd, "DD/MM/YYYY").startOf("day").toISOString();
        }
        const parsed = new Date(rd);
        if (!isNaN(parsed.getTime())) {
          return parsed.toISOString();
        }
        return rd;
      };

      const movieNormalized: Movie = {
        ...movieObj,
        releaseDate: normalizeReleaseDate(movieObj.releaseDate) as any,
      };

      const payload: Omit<CinemaHall, "id"> = {
        movieSession: movieSessionIso,
        movie: movieNormalized,
      };

      console.log("[CinemaHallPage] payload to create/update:", payload);

      if (editingId) {
        const updated = await cinemaHallService.update(editingId, payload);
        dispatch(updateCinemaHall(updated));
        message.success("Cập nhật suất chiếu thành công");
      } else {
        const created = await cinemaHallService.create(payload);
        dispatch(addCinemaHall(created));
        message.success("Thêm suất chiếu thành công");
      }

      closeEdit();
    } catch (err: any) {
      if (err?.errorFields) return;
      console.error("[CinemaHallPage] create/update error:", err);
      if (err?.response?.status === 401) message.error("Chưa xác thực (401)");
      else
        message.error(
          err?.response?.data?.message ??
            err?.message ??
            "Lỗi khi lưu suất chiếu"
        );
    }
  };

  const handleDelete = async (id: number) => {
    try {
      await cinemaHallService.remove(id);
      dispatch(removeCinemaHall(id));
      message.success("Đã xóa suất chiếu");
    } catch (err: any) {
      console.error("Delete error", err);
      if (err?.response?.status === 401) message.error("Không có quyền (401)");
      else message.error("Xóa thất bại");
    }
  };

  const columns: ColumnsType<CinemaHall> = [
    {
      title: "STT",
      key: "index",
      width: 80,
      render: (_v, _r, index) => (page - 1) * pageSize + (index + 1),
    },
    {
      title: "Poster",
      dataIndex: ["movie", "poster"],
      key: "poster",
      width: 120,
      render: (_val, record) =>
        record.movie?.poster ? (
          <Image
            src={record.movie.poster}
            width={72}
            height={108}
            alt={record.movie.title}
            preview={false}
          />
        ) : (
          <span>-</span>
        ),
    },
    {
      title: "Phim",
      dataIndex: ["movie", "title"],
      key: "movieTitle",
      render: (_val, record) => (
        <span style={{ fontWeight: 600 }}>{record.movie?.title ?? "-"}</span>
      ),
    },
    {
      title: "Thời gian chiếu",
      dataIndex: "movieSession",
      key: "movieSession",
      render: (val: string) => {
        const { date, time } = parseSession(val);
        return (
          <div>
            <div style={{ fontWeight: 600 }}>{date}</div>
            <div style={{ color: "#666" }}>{time}</div>
          </div>
        );
      },
      sorter: (a, b) =>
        moment(a.movieSession).valueOf() - moment(b.movieSession).valueOf(),
    },
    {
      title: "Hành động",
      key: "action",
      width: 160,
      render: (_t, record) => (
        <Space>
          <Button
            type="text"
            icon={<EyeOutlined style={{ color: "#1890ff", fontSize: 20 }} />}
            onClick={() => openView(record)}
          />
          <Button
            type="text"
            icon={<EditOutlined style={{ color: "#52c41a", fontSize: 20 }} />}
            onClick={() => openEdit(record)}
          />
          <Popconfirm
            title={`Xóa suất chiếu #${record.id}?`}
            onConfirm={() => handleDelete(record.id)}
            okText="Xóa"
            cancelText="Hủy"
          >
            <Button
              type="text"
              danger
              icon={
                <DeleteOutlined style={{ color: "#ff4d4f", fontSize: 20 }} />
              }
            />
          </Popconfirm>
        </Space>
      ),
    },
  ];

  return (
    <>
      <Space direction="vertical" style={{ width: "100%" }} size="middle">
        <Title level={3}>Quản lý xuất chiếu</Title>

        <Row gutter={12} style={{ width: "100%" }} align="middle">
          <Col>
            <Input.Search
              placeholder="Tìm kiếm theo phim / thời gian"
              allowClear
              enterButton
              onSearch={(v) => setSearch(v)}
              style={{ width: 360 }}
            />
          </Col>
          <Col flex="auto" />
          <Col>
            <Button type="primary" icon={<PlusOutlined />} onClick={openAdd}>
              Thêm suất chiếu
            </Button>
          </Col>
        </Row>

        <Spin spinning={loadingMovies}>
          <Table
            columns={columns}
            dataSource={filtered}
            rowKey="id"
            pagination={{
              current: page,
              pageSize,
              total: filtered.length,
              showSizeChanger: true,
              onChange: (p, ps) => {
                setPage(p);
                setPageSize(ps);
              },
            }}
          />
        </Spin>
      </Space>
      <Modal
        title={
          editingId ? `Sửa suất chiếu #${editingId}` : "Thêm suất chiếu mới"
        }
        open={isEditModalOpen}
        onCancel={closeEdit}
        onOk={handleSave}
        width={720}
        okText={editingId ? "Lưu" : "Thêm"}
      >
        <Form
          form={form}
          layout="vertical"
          initialValues={{ movieId: undefined }}
        >
          <Row gutter={16}>
            <Col span={10}>
              <div
                style={{
                  display: "flex",
                  flexDirection: "column",
                  alignItems: "center",
                  gap: 12,
                }}
              >
                <Form.Item shouldUpdate noStyle>
                  {() => {
                    const selId = form.getFieldValue("movieId");
                    const sel = movieOptions.find(
                      (m) => m.id === Number(selId)
                    );
                    if (sel && sel.poster) {
                      return (
                        <Image
                          src={sel.poster}
                          width={180}
                          height={270}
                          alt={sel.title}
                        />
                      );
                    }
                    return (
                      <div
                        style={{
                          width: 180,
                          height: 270,
                          background: "#f5f5f5",
                          display: "flex",
                          alignItems: "center",
                          justifyContent: "center",
                          color: "#999",
                        }}
                      >
                        No image
                      </div>
                    );
                  }}
                </Form.Item>

                <Form.Item
                  name="movieTitle"
                  noStyle
                  rules={[{ required: true, message: "Vui lòng chọn phim" }]}
                >
                  <Input
                    readOnly
                    placeholder="Chưa có phim"
                    style={{ textAlign: "center", fontWeight: 600 }}
                  />
                </Form.Item>
                <Form.Item name="movieId" noStyle>
                  <input type="hidden" />
                </Form.Item>
              </div>
            </Col>

            <Col span={14}>
              <div
                style={{
                  display: "flex",
                  flexDirection: "column",
                  height: "100%",
                  gap: 12,
                }}
              >
                <div style={{ display: "flex", justifyContent: "flex-end" }}>
                  <Button
                    type="default"
                    icon={<SearchOutlined />}
                    onClick={openSearchMovieModal}
                    style={{ width: "100%" }}
                  >
                    Tìm phim
                  </Button>
                </div>

                <div
                  style={{ flex: 1, display: "flex", alignItems: "flex-start" }}
                >
                  <Form.Item
                    name="movieSession"
                    label="Thời gian chiếu"
                    rules={[
                      {
                        required: true,
                        message: "Vui lòng chọn thời gian chiếu",
                      },
                    ]}
                    style={{ width: "100%" }}
                  >
                    <DatePicker
                      showTime={{ format: "HH:mm" }}
                      format="DD/MM/YYYY HH:mm"
                      style={{ width: "100%" }}
                    />
                  </Form.Item>
                </div>
              </div>
            </Col>
          </Row>
        </Form>
      </Modal>

      {/* Movie search modal */}
      <Modal
        title="Tìm phim"
        open={isSearchMovieModalOpen}
        onCancel={closeSearchMovieModal}
        footer={null}
        width={720}
      >
        <Space direction="vertical" style={{ width: "100%" }}>
          <Input.Search
            placeholder="Gõ tên phim rồi Enter để tìm"
            allowClear
            enterButton="Tìm"
            onSearch={handleMovieSearch}
          />
          <Spin spinning={loadingMovies}>
            <List
              itemLayout="horizontal"
              dataSource={movieOptions}
              renderItem={(m) => (
                <List.Item
                  onClick={() => handleSelectMovieFromSearch(m)}
                  style={{ cursor: "pointer" }}
                >
                  <List.Item.Meta
                    avatar={
                      m.poster ? (
                        <Avatar shape="square" size={100} src={m.poster} />
                      ) : (
                        <Avatar shape="square" size={100}>
                          {m.title?.[0]}
                        </Avatar>
                      )
                    }
                    title={<div style={{ fontWeight: 600 }}>{m.title}</div>}
                    description={
                      <div style={{ fontSize: 12 }}>
                        {parseReleaseDate(m.releaseDate).display}
                      </div>
                    }
                  />
                </List.Item>
              )}
              style={{ maxHeight: 420, overflowY: "auto" }}
            />
          </Spin>
        </Space>
      </Modal>

      {/* View Modal */}
      <Modal
        title={`Chi tiết suất chiếu`}
        open={isViewModalOpen}
        onCancel={closeView}
        footer={[
          <Button key="close" onClick={closeView}>
            Đóng
          </Button>,
        ]}
        width={1100}
        centered
        styles={{ body: { padding: 24 } }}
      >
        {viewing ? (
          <Row gutter={16}>
            <Col xs={24} sm={8}>
              {viewing.movie.poster ? (
                <Image
                  src={viewing.movie.poster}
                  width="100%"
                  height={"100%"}
                />
              ) : (
                <div
                  style={{
                    width: "100%",
                    height: 300,
                    background: "#f5f5f5",
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    color: "#999",
                  }}
                >
                  No image
                </div>
              )}
            </Col>
            <Col xs={24} sm={16}>
              <h2
                style={{
                  marginTop: 0,
                  marginBottom: 8,
                  fontSize: 28,
                  fontWeight: 700,
                }}
              >
                {viewing.movie.title}
              </h2>
              {(() => {
                const s = parseSession(viewing.movieSession);
                return (
                  <div
                    style={{ marginBottom: 12, fontSize: 16, color: "#222" }}
                  >
                    <div>
                      <strong>Ngày: </strong>
                      <span>{s.date}</span>
                    </div>
                    <div>
                      <strong>Giờ: </strong>
                      <span>{s.time}</span>
                    </div>
                  </div>
                );
              })()}

              <div style={{ fontSize: 16, marginBottom: 12 }}>
                {viewing.movie.releaseDate ? (
                  <div>
                    Ngày phát hành:{" "}
                    {parseReleaseDate(viewing.movie.releaseDate).display}
                  </div>
                ) : null}
                {viewing.movie.duration ? (
                  <div>Thời lượng: {viewing.movie.duration} phút</div>
                ) : null}
              </div>
              {viewing.movie.overview ? (
                <p
                  style={{
                    marginTop: 10,
                    fontSize: 16,
                    lineHeight: 1.6,
                    color: "#333",
                  }}
                >
                  {viewing.movie.overview}
                </p>
              ) : null}
            </Col>
          </Row>
        ) : null}
      </Modal>
    </>
  );
};

export default CinemaHallPage;
