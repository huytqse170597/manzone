package com.prm.manzone.mapper;

import com.prm.manzone.entities.User;
import com.prm.manzone.payload.user.request.AdminCreateUserRequest;
import com.prm.manzone.payload.user.request.AdminUpdateUserRequest;
import com.prm.manzone.payload.user.request.UpdateUserRequest;
import com.prm.manzone.payload.user.response.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toUserDTO(User user) {
        if (user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setAvatarUrl(user.getAvatarUrl());
        userDTO.setRole(user.getRole());
        userDTO.setAddress(user.getAddress());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setActive(user.getActive());
        userDTO.setDeleted(user.getDeleted());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        return userDTO;
    }

    public User updateRequestToUser(UpdateUserRequest userDTO, User user) {
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setEmail(userDTO.getEmail());
        user.setAddress(userDTO.getAddress());
        return user;
    }

    public User adminCreateRequestToUser(AdminCreateUserRequest request) {
        if (request == null) {
            return null;
        }

        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .role(request.getRole())
                .avatarUrl(request.getAvatarUrl())
                .active(request.getActive())
                .build();
    }

    public User adminUpdateRequestToUser(AdminUpdateUserRequest request, User user) {
        if (request == null || user == null) {
            return user;
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setEmail(request.getEmail());
        user.setAddress(request.getAddress());

        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }
        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
        }
        if (request.getActive() != null) {
            user.setActive(request.getActive());
        }

        return user;
    }
}
