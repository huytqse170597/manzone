# Order Management System Documentation

## Overview

Hệ thống quản lý đơn hàng (Order Management System) được thiết kế theo chuẩn Spring Boot với các tính năng đầy đủ cho cả khách hàng (Customer) và quản trị viên (Admin).

## Entities

### Order Entity

- **Thuộc tính chính:**
  - `id`: ID đơn hàng (auto-generated)
  - `user`: Khách hàng đặt hàng
  - `orderDetails`: Danh sách chi tiết đơn hàng
  - `totalAmount`: Tổng tiền (BigDecimal)
  - `status`: Trạng thái đơn hàng (OrderStatus enum)
  - `phoneNumber`: Số điện thoại giao hàng
  - `shippingAddress`: Địa chỉ giao hàng
  - `customerName`: Tên khách hàng
  - `note`: Ghi chú đơn hàng

### OrderDetail Entity

- **Thuộc tính chính:**
  - `id`: ID chi tiết đơn hàng
  - `order`: Đơn hàng
  - `product`: Sản phẩm
  - `quantity`: Số lượng
  - `price`: Giá tại thời điểm đặt hàng (BigDecimal)
  - `productName`: Tên sản phẩm (snapshot)
  - `productImageUrl`: Ảnh sản phẩm (snapshot)

### OrderStatus Enum

- `PENDING`: Chờ xác nhận
- `CONFIRMED`: Đã xác nhận
- `SHIPPED`: Đang giao hàng
- `DELIVERED`: Đã giao hàng
- `CANCELLED`: Đã hủy

## API Endpoints

### Customer APIs (Role: CUSTOMER)

#### POST /orders

**Tạo đơn hàng mới**

- Request Body: `CreateOrderRequest`
- Response: `OrderResponse`
- Validation: Kiểm tra sản phẩm tồn tại, số lượng > 0, thông tin giao hàng hợp lệ

#### GET /orders/my-orders

**Lấy danh sách đơn hàng của tôi**

- Query Parameters: `page`, `size`
- Response: `Page<OrderResponse>`
- Sắp xếp theo thời gian tạo giảm dần

#### GET /orders/my-orders/{id}

**Lấy chi tiết đơn hàng của tôi**

- Path Variable: `id`
- Response: `OrderResponse`
- Kiểm tra quyền sở hữu đơn hàng

#### PUT /orders/my-orders/{id}/cancel

**Hủy đơn hàng**

- Path Variable: `id`
- Chỉ cho phép hủy đơn hàng có status = PENDING

#### GET /orders/my-orders/status/{status}

**Lấy đơn hàng theo trạng thái**

- Path Variable: `status`
- Response: `List<OrderResponse>`

### Admin APIs (Role: ADMIN)

#### GET /orders/admin

**Lấy tất cả đơn hàng**

- Query Parameters: `page`, `size`, `status`
- Response: `Page<OrderResponse>`

#### GET /orders/admin/{id}

**Lấy chi tiết đơn hàng**

- Path Variable: `id`
- Response: `OrderResponse`

#### PUT /orders/admin/{id}/status

**Cập nhật trạng thái đơn hàng**

- Path Variable: `id`
- Request Body: `UpdateOrderStatusRequest`
- Validation: Kiểm tra transition hợp lệ

#### PUT /orders/admin/{id}

**Cập nhật thông tin đơn hàng**

- Path Variable: `id`
- Request Body: `UpdateOrderRequest`
- Chỉ cho phép update khi status = PENDING

#### DELETE /orders/admin/{id}

**Xóa đơn hàng (soft delete)**

- Path Variable: `id`

#### GET /orders/admin/statistics

**Thống kê đơn hàng**

- Response: `OrderStatisticsResponse`
- Bao gồm số lượng đơn hàng theo từng trạng thái

## Business Logic & Validations

### Status Transition Rules

```
PENDING → CONFIRMED | CANCELLED
CONFIRMED → SHIPPED | CANCELLED
SHIPPED → DELIVERED
DELIVERED → (final state)
CANCELLED → (final state)
```

### Validations

1. **Phone Number**: Pattern `^(0)[3|5|7|8|9][0-9]{8}$`
2. **Product Existence**: Kiểm tra sản phẩm có tồn tại và chưa bị xóa
3. **Quantity**: Phải > 0
4. **Price**: Sử dụng BigDecimal để tính toán chính xác
5. **Order Ownership**: Khách hàng chỉ có thể truy cập đơn hàng của mình
6. **Status Update**: Chỉ Admin mới có thể update status, theo đúng flow

### Security

- **Authentication**: JWT token required
- **Authorization**:
  - Customer: Chỉ truy cập đơn hàng của mình
  - Admin: Truy cập tất cả đơn hàng và quản lý
- **Scope-based**: Sử dụng `@PreAuthorize` với SCOPE_CUSTOMER và SCOPE_ADMIN

## Data Consistency

1. **Soft Delete**: Sử dụng `deleted` flag thay vì xóa thật
2. **Product Snapshot**: Lưu tên và ảnh sản phẩm tại thời điểm đặt hàng
3. **Price Calculation**: Tự động tính total amount từ order details
4. **Audit Trail**: Timestamp cho created/updated

## Error Handling

- **AppException**: Custom exception với ErrorCode
- **Global Exception Handler**: Xử lý tất cả exception thống nhất
- **Validation**: Bean Validation với custom messages tiếng Việt
- **HTTP Status Codes**: Trả về đúng status code cho từng trường hợp

## Technical Features

1. **Pagination**: Hỗ trợ phân trang cho danh sách đơn hàng
2. **Sorting**: Sắp xếp theo thời gian tạo
3. **Filtering**: Lọc theo trạng thái đơn hàng
4. **Mapping**: Sử dụng mapper pattern để convert Entity ↔ DTO
5. **Transaction**: Đảm bảo data consistency với @Transactional
6. **Repository Pattern**: JPA Repository với custom queries
7. **Service Layer**: Business logic tách biệt khỏi controller

## Testing

- Project build thành công với Maven
- Spring Boot test context loads correctly
- Tất cả dependencies được resolve đúng

Hệ thống đã được thiết kế hoàn chỉnh, tuân thủ best practices và ready for production!
