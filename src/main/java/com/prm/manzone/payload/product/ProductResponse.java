package com.prm.manzone.payload.product;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    int id;
    String name;
    String description;
    BigDecimal price;
    List<String> imageUrls;
    Instant createdAt;
    Instant updatedAt;
}
