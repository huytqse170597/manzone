package com.prm.manzone.service;

import com.prm.manzone.entities.User;
import com.prm.manzone.enums.Role;
import com.prm.manzone.enums.UserSortField;
import com.prm.manzone.payload.user.request.CreateUserRequest;
import com.prm.manzone.payload.user.request.UpdateUserRequest;
import com.prm.manzone.payload.user.response.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

public interface IUserService {

    User getAuthenticatedUser();
    UserDTO register(CreateUserRequest request);
    UserDTO getUserById(int id);
    UserDTO updateUser(UpdateUserRequest updateRequest);
    void changePassword(String oldPassword, String newPassword);
    Page<UserDTO> getAllUsers(int page,
                              int size,
                              Sort.Direction sortDir,
                              UserSortField sortBy,
                              String searchString,
                              Role role,
                              boolean isDeleted);

}
