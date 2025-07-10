package com.prm.manzone.payload.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserRequest {
    @NotBlank(message = "Tên là bắt buộc")
    String firstName;
    
    @NotBlank(message = "Họ là bắt buộc")
    String lastName;
    
    @NotBlank(message = "Mật khẩu là bắt buộc")
    String password;
    
    @NotBlank(message = "Số điện thoại là bắt buộc")
    String phoneNumber;
    
    @NotBlank(message = "Email là bắt buộc")
    @Email(message = "Email không hợp lệ")
    String email;
    
    @NotBlank(message = "Địa chỉ là bắt buộc")
    String address;
}
