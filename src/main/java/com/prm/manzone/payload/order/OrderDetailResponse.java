package com.prm.manzone.payload.order;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailResponse {
    Integer id;
    Integer productId;
    String productName;
    String productImageUrl;
    Integer quantity;
    BigDecimal price;
    BigDecimal subtotal;
}
