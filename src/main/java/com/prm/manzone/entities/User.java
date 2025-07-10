package com.prm.manzone.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prm.manzone.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

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

    @Email
    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    @NotEmpty(message = "Email là bắt buộc")
    String email;

    String address;

    @Column(nullable = false, updatable = false)
    Instant createdAt;

    @Column(nullable = false)
    Instant updatedAt;

    @Column(nullable = false)
    @Builder.Default
    Boolean deleted = false;

    @Column(nullable = false)
    @Builder.Default
    Boolean active = false;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}