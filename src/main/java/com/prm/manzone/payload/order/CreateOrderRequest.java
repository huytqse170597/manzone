package com.prm.manzone.payload.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateOrderRequest {
    @NotEmpty(message = "Danh sách sản phẩm là bắt buộc")
    @Size(min = 1, message = "Phải có ít nhất 1 sản phẩm")
    @Valid
    List<OrderDetailRequest> orderDetails;

    @NotEmpty(message = "Số điện thoại là bắt buộc")
    @Pattern(regexp = "^(0)[3|5|7|8|9][0-9]{8}$", message = "Số điện thoại không hợp lệ")
    String phoneNumber;

    @NotEmpty(message = "Địa chỉ giao hàng là bắt buộc")
    @Size(max = 500, message = "Địa chỉ giao hàng không được vượt quá 500 ký tự")
    String shippingAddress;

    @Size(max = 1000, message = "Ghi chú không được vượt quá 1000 ký tự")
    String note;

    @NotEmpty(message = "Tên khách hàng là bắt buộc")
    @Size(max = 100, message = "Tên khách hàng không được vượt quá 100 ký tự")
    String customerName;
}
