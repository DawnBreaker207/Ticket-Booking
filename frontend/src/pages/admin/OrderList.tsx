// src/pages/admin/OrderList.tsx
import React, { useEffect, useState, useMemo, useRef } from "react";
import {
  Table,
  Modal,
  Button,
  message,
  Typography,
  Descriptions,
  Input,
  Row,
  Col,
  DatePicker,
  Select,
  InputNumber,
} from "antd";
import { EyeOutlined } from "@ant-design/icons";
import type { ColumnsType, TablePaginationConfig } from "antd/es/table";
import { orderService } from "../../services/orderService";

interface OrderRaw {
  createdAt: string;
  updatedAt?: string;
  orderId: string;
  userId?: number;
  cinemaHallId?: number;
  orderStatus?: string;
  paymentMethod?: string;
  paymentStatus?: string;
  totalAmount?: number;
  seats?: any[];
  [k: string]: any;
}

interface OrderItem {
  key: string;
  id: string;
  userId?: number;
  orderStatus?: string;
  paymentMethod?: string;
  paymentStatus?: string;
  totalAmount?: number | string;
  createdAt?: string;
  seats?: any[];
  raw?: OrderRaw;
}

const { Search } = Input;
const { RangePicker } = DatePicker;

const currency = new Intl.NumberFormat("vi-VN", {
  style: "currency",
  currency: "VND",
});

/**
 * Client-side search only (filter within the currently loaded rows).
 * Now with date range filter, sort select and exact total filter input.
 */
const OrderList: React.FC = () => {
  const [rawData, setRawData] = useState<OrderItem[]>([]); // data from server for current page
  const [data, setData] = useState<OrderItem[]>([]); // displayed (possibly filtered) data
  const [loading, setLoading] = useState(false);
  const [total, setTotal] = useState(0); // total after local filter (for current page)
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [search, setSearch] = useState("");

  // dateRange: [startMoment, endMoment] or null
  const [dateRange, setDateRange] = useState<any | null>(null);

  // sortOrder: 'newest' | 'oldest'
  const [sortOrder, setSortOrder] = useState<"newest" | "oldest">("newest");

  // exact total filter (VND). null = no filter
  const [totalExact, setTotalExact] = useState<number | null>(null);

  const [visible, setVisible] = useState(false);
  const [selected, setSelected] = useState<OrderItem | null>(null);

  const debounceRef = useRef<ReturnType<typeof setTimeout> | null>(null);

  const parseDate = (v?: string | null) => {
    if (!v) return null;
    const d = new Date(v);
    return isNaN(d.getTime()) ? null : d;
  };

  const inRange = (createdAt?: string, range?: any | null) => {
    if (!range || !Array.isArray(range) || range.length !== 2) return true; // no range -> pass
    const start = range[0];
    const end = range[1];
    if (!start || !end) return true;
    // range items are moment/dayjs objects from antd; they have toDate() or toISOString. We try both.
    const startDate =
      start && typeof start.toDate === "function"
        ? start.toDate()
        : new Date(start);
    const endDate =
      end && typeof end.toDate === "function" ? end.toDate() : new Date(end);
    const c = parseDate(createdAt);
    if (!c) return false;
    // compare dates ignoring time zone subtlety; include endpoints
    return c >= startDate && c <= endDate;
  };

  const applyLocalFilter = (
    rows: OrderItem[],
    q?: string,
    range?: any | null,
    sort?: "newest" | "oldest",
    totalAmt?: number | null
  ) => {
    let out = Array.isArray(rows) ? [...rows] : [];

    // sort first
    if (sort === "newest") {
      out.sort((a, b) => {
        const da = parseDate(a.createdAt)?.getTime() ?? 0;
        const db = parseDate(b.createdAt)?.getTime() ?? 0;
        return db - da;
      });
    } else if (sort === "oldest") {
      out.sort((a, b) => {
        const da = parseDate(a.createdAt)?.getTime() ?? 0;
        const db = parseDate(b.createdAt)?.getTime() ?? 0;
        return da - db;
      });
    }

    if (q && q.trim() !== "") {
      const qLower = q.trim().toLowerCase();
      out = out.filter((r) => (r.id ?? "").toLowerCase().includes(qLower));
    }
    if (range) {
      out = out.filter((r) => inRange(r.createdAt, range));
    }

    // exact total amount filter (if provided)
    if (typeof totalAmt === "number") {
      out = out.filter((r) => {
        // ensure numeric comparison; handle strings/numbers
        const amt = Number(r.totalAmount ?? 0);
        return Number.isFinite(amt) && amt === totalAmt;
      });
    }

    setData(out);
    setTotal(out.length);
  };

  const fetchOrders = async (p = page, ps = pageSize) => {
    setLoading(true);
    try {
      // only pagination params — no server-side search
      const params: Record<string, any> = { page: p, limit: ps };
      const res = await orderService.getOrders(params);
      const list: OrderRaw[] = Array.isArray(res?.data) ? res.data : [];

      const normalized = list.map((o) => ({
        key: o.orderId,
        id: o.orderId,
        userId: o.userId,
        orderStatus: o.orderStatus,
        paymentMethod: o.paymentMethod,
        paymentStatus: o.paymentStatus,
        totalAmount: o.totalAmount,
        createdAt: o.createdAt,
        seats: o.seats,
        raw: o,
      }));

      setRawData(normalized);
      // apply any active local search, date range, sort and exact total on the freshly fetched page
      applyLocalFilter(normalized, search, dateRange, sortOrder, totalExact);
    } catch (err: any) {
      console.error(err);
      message.error(err?.message ?? "Lỗi khi tải danh sách đơn hàng");
      setRawData([]);
      setData([]);
      setTotal(0);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    // initial load
    fetchOrders(1, pageSize);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // immediate search (Enter / click) -> filter client-side only (uses dateRange too)
  const onSearch = (value: string) => {
    if (debounceRef.current) {
      clearTimeout(debounceRef.current);
      debounceRef.current = null;
    }
    const q = value ?? "";
    setSearch(q);
    // filter the currently loaded rows (rawData) using q + dateRange + sort + totalExact
    applyLocalFilter(rawData, q, dateRange, sortOrder, totalExact);
    setPage(1); // UX: reset to page 1 of current loaded page
  };

  // typed change handler with debounce -> local filter
  const onSearchChange = (value: string) => {
    setSearch(value);

    if (debounceRef.current) {
      clearTimeout(debounceRef.current);
    }

    debounceRef.current = setTimeout(() => {
      applyLocalFilter(rawData, value, dateRange, sortOrder, totalExact);
      debounceRef.current = null;
      setPage(1);
    }, 300);
  };

  const onRangeChange = (range: any) => {
    setDateRange(range);
    // apply filter immediately when range changes
    applyLocalFilter(rawData, search, range, sortOrder, totalExact);
    setPage(1);
  };

  const onSortChange = (val: "newest" | "oldest") => {
    setSortOrder(val);
    applyLocalFilter(rawData, search, dateRange, val, totalExact);
    setPage(1);
  };

  const onTotalExactChange = (val: number | null) => {
    // val is a number in VND (no decimals)
    setTotalExact(val);
    applyLocalFilter(rawData, search, dateRange, sortOrder, val);
    setPage(1);
  };

  const handleView = (record: OrderItem) => {
    setSelected(record);
    setVisible(true);
  };

  const handleClose = () => {
    setVisible(false);
    setSelected(null);
  };

  const columns: ColumnsType<OrderItem> = useMemo(
    () => [
      {
        title: "Mã đơn",
        dataIndex: "id",
        key: "id",
      },
      {
        title: "Trạng thái đơn",
        dataIndex: "orderStatus",
        key: "orderStatus",
      },
      {
        title: "Phương thức TT",
        dataIndex: "paymentMethod",
        key: "paymentMethod",
      },
      {
        title: "Trạng thái TT",
        dataIndex: "paymentStatus",
        key: "paymentStatus",
      },
      {
        title: "Tổng tiền",
        dataIndex: "totalAmount",
        key: "totalAmount",
        render: (v) => (v != null ? currency.format(Number(v)) : "-"),
      },
      {
        title: "Ngày tạo",
        dataIndex: "createdAt",
        key: "createdAt",
        render: (v) => (v ? new Date(v).toLocaleString() : "-"),
      },
      {
        title: "Hành động",
        key: "action",
        render: (_, record) => (
          <Button type="link" onClick={() => handleView(record)}>
            <EyeOutlined />
          </Button>
        ),
      },
    ],
    []
  );

  const handleTableChange = (pagination: TablePaginationConfig) => {
    const cur = pagination.current ?? 1;
    const ps = pagination.pageSize ?? pageSize;
    setPage(cur);
    setPageSize(ps);
    // fetch the new page from server, then apply local filter (if any)
    fetchOrders(cur, ps);
  };

  return (
    <div>
      <Typography.Title level={3} style={{ marginBottom: 8 }}>
        Quản lý đơn đặt
      </Typography.Title>

      {/* Filters area placed under the title */}
      <Row
        gutter={[12, 12]}
        style={{ marginBottom: 12 }}
        justify="space-between"
        align="middle"
      >
        {/* Search - stretches if cần, but won't force wrap */}
        <Col
          xs={24}
          sm={16}
          md={12}
          lg={10}
          style={{ display: "flex", alignItems: "center" }}
        >
          <Search
            placeholder="Tìm kiếm"
            enterButton
            onSearch={onSearch}
            onChange={(e) => onSearchChange(e.target.value)}
            value={search}
            allowClear
            style={{ width: "100%", minWidth: 0 }}
          />
        </Col>

        <Col
          xs={24}
          sm={8}
          md={6}
          lg={10}
          style={{
            display: "flex",
            justifyContent: "flex-end",
            alignItems: "center",
            gap: 8,
          }}
        >
          <RangePicker
            onChange={onRangeChange}
            value={dateRange}
            style={{ minWidth: 200 }}
          />
          <InputNumber
            value={totalExact ?? undefined}
            onChange={onTotalExactChange}
            placeholder="Tổng tiền (VND)"
            style={{ width: 160 }}
            min={0}
            step={1000}
            formatter={(value) =>
              value !== undefined && value !== null
                ? String(value).replace(/\B(?=(\d{3})+(?!\d))/g, ",")
                : ""
            }
          />

          <Select
            value={sortOrder}
            onChange={onSortChange}
            style={{ width: 140 }}
            options={[
              { label: "Mới nhất", value: "newest" },
              { label: "Cũ nhất", value: "oldest" },
            ]}
          />
        </Col>
      </Row>

      <Table<OrderItem>
        columns={columns}
        dataSource={data}
        loading={loading}
        pagination={{ current: page, pageSize, total, showSizeChanger: true }}
        onChange={handleTableChange}
        rowKey="id"
      />

      <Modal
        title={`Chi tiết đơn hàng ${selected?.id ?? ""}`}
        visible={visible}
        onCancel={handleClose}
        footer={null}
      >
        {selected ? (
          <div>
            <Descriptions column={1} bordered size="small">
              <Descriptions.Item label="Mã đơn">
                {selected.id}
              </Descriptions.Item>
              <Descriptions.Item label="User ID">
                {selected.userId ?? "-"}
              </Descriptions.Item>
              <Descriptions.Item label="Trạng thái">
                {selected.orderStatus ?? "-"}
              </Descriptions.Item>
              <Descriptions.Item label="Phương thức thanh toán">
                {selected.paymentMethod ?? "-"}
              </Descriptions.Item>
              <Descriptions.Item label="Trạng thái thanh toán">
                {selected.paymentStatus ?? "-"}
              </Descriptions.Item>
              <Descriptions.Item label="Tổng tiền">
                {selected.totalAmount != null
                  ? currency.format(Number(selected.totalAmount))
                  : "-"}
              </Descriptions.Item>
              <Descriptions.Item label="Ngày tạo">
                {selected.createdAt
                  ? new Date(selected.createdAt).toLocaleString()
                  : "-"}
              </Descriptions.Item>
              <Descriptions.Item label="Cinema Hall ID">
                {selected?.raw?.cinemaHallId ??
                  selected?.seats?.[0]?.seat?.cinemaHallId ??
                  selected?.seats?.[0]?.cinemaHallId ??
                  "-"}
              </Descriptions.Item>
              <Descriptions.Item label="Ghế đặt">
                {Array.isArray(selected.seats) && selected.seats.length > 0
                  ? selected.seats
                      .map(
                        (s: any) =>
                          s?.seat?.seatNumber ?? s?.seatNumber ?? s?.id
                      )
                      .filter(Boolean)
                      .join(", ")
                  : "-"}
              </Descriptions.Item>
            </Descriptions>
          </div>
        ) : null}
      </Modal>
    </div>
  );
};

export default OrderList;
