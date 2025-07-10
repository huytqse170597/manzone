package com.prm.manzone.payload.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerifyTokenRequest {
    @NotBlank(message = "Token là bắt buộc")
    String token;
}

