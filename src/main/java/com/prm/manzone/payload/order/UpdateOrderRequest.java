package com.prm.manzone.payload.order;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateOrderRequest {
    @Pattern(regexp = "^(0)[3|5|7|8|9][0-9]{8}$", message = "Số điện thoại không hợp lệ")
    String phoneNumber;

    @Size(max = 500, message = "Địa chỉ giao hàng không được vượt quá 500 ký tự")
    String shippingAddress;

    @Size(max = 1000, message = "Ghi chú không được vượt quá 1000 ký tự")
    String note;

    @Size(max = 100, message = "Tên khách hàng không được vượt quá 100 ký tự")
    String customerName;
}
