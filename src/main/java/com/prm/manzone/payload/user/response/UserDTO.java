package com.prm.manzone.payload.user.response;

import com.prm.manzone.enums.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {

    int id;
    String firstName;
    String lastName;
    Role role;
    String phoneNumber;
    String avatarUrl;
    String email;
    String address;
    boolean isActive;
    boolean isDeleted;
    Instant createdAt;
    Instant updatedAt;
}
