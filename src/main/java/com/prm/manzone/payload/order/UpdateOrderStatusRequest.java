package com.prm.manzone.payload.order;

import com.prm.manzone.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateOrderStatusRequest {
    @NotNull(message = "Trạng thái đơn hàng là bắt buộc")
    OrderStatus status;
}
