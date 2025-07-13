package com.prm.manzone.payload.product;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
    Double price;
    List<String> imageUrls;
    Instant createdAt;
    Instant updatedAt;
}
