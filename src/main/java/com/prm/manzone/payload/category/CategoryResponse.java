package com.prm.manzone.payload.category;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryResponse {

    int id;
    String name;
    String description;
    Instant createdAt;
    Instant updatedAt;
}
