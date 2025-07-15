package com.prm.manzone.payload.order;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderStatisticsResponse {
    long pending;
    long confirmed;
    long shipped;
    long delivered;
    long cancelled;
    long total;
}
