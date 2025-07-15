package com.prm.manzone.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prm.manzone.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
public class User extends  BaseEntity {
    @NotEmpty(message = "Mật khẩu là bắt buộc")
    @JsonIgnore
    String password;

    @NotEmpty(message = "Tên là bắt buộc")
    String firstName;

    @NotEmpty(message = "Họ là bắt buộc")
    String lastName;

    @Enumerated(EnumType.STRING)
    Role role;

    @NotEmpty(message = "Số điện thoại là bắt buộc")
    String phoneNumber;

    String avatarUrl;

    @Column(nullable = false)
    @Builder.Default
    Boolean active = true;

    @Email
    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    @NotEmpty(message = "Email là bắt buộc")
    String email;

    String address;

}