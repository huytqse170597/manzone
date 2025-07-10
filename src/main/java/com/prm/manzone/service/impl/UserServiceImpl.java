package com.prm.manzone.service.impl;

import com.prm.manzone.entities.User;
import com.prm.manzone.enums.Role;
import com.prm.manzone.enums.UserSortField;
import com.prm.manzone.mapper.UserMapper;
import com.prm.manzone.payload.user.request.CreateUserRequest;
import com.prm.manzone.payload.user.request.UpdateUserRequest;
import com.prm.manzone.payload.user.response.UserDTO;
import com.prm.manzone.repository.UserRepository;
import com.prm.manzone.service.IUserService;
import com.prm.manzone.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    @NonFinal
    @Value("${app.default.avatar}")
    protected String defaultAvatar;

    @Override
    public User getAuthenticatedUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        //get subject from token
        String userId = context.getAuthentication().getName();
        int id = Integer.parseInt(userId);
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng " + id));
    }

    @Override
    public UserDTO register(CreateUserRequest userDto) {
        String PHONE_REGEX = "^[0-9]{10}$";
        User newUser = new User();
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()){
            throw new RuntimeException("Email đã được sử dụng");
        }
        if(!userDto.getPhoneNumber().matches(PHONE_REGEX)){
            throw new RuntimeException("Số điện thoại không hợp lệ.");
        }
        newUser.setFirstName(userDto.getFirstName());
        newUser.setLastName(userDto.getLastName());
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        newUser.setPhoneNumber(userDto.getPhoneNumber());
        newUser.setEmail(userDto.getEmail());
        newUser.setAddress(userDto.getAddress());
        newUser.setRole(Role.USER);
        newUser.setAvatarUrl(defaultAvatar);
        userRepository.save(newUser);
        return userMapper.toUserDTO(newUser);
    }

    @Override
    public UserDTO getUserById(int id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng: " + id));

        return userMapper.toUserDTO(user);
    }

    @Transactional
    @Override
    public UserDTO updateUser(UpdateUserRequest updateRequest) {
        try {
            User authenticatedUser = getAuthenticatedUser();
            authenticatedUser = userMapper.updateRequestToUser(updateRequest, authenticatedUser);
            userRepository.save(authenticatedUser);
            return userMapper.toUserDTO(authenticatedUser);
        } catch (Exception e) {
            throw new RuntimeException("Cập nhật thông tin người dùng thất bại: " + e.getMessage());
        }
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        User authenticatedUser = getAuthenticatedUser();
        if (!passwordEncoder.matches(oldPassword, authenticatedUser.getPassword())) {
            throw new RuntimeException("Mật khẩu cũ không đúng");
        }
        if (passwordEncoder.matches(newPassword, authenticatedUser.getPassword())) {
            throw new RuntimeException("Mật khẩu mới không được trùng với mật khẩu cũ");
        }
        authenticatedUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(authenticatedUser);
    }
    public Page<UserDTO> getAllUsers(int page, int size, Sort.Direction sortDir, UserSortField sortBy, String searchString, Role role, boolean isDeleted) {
        Specification<User> spec = UserSpecification.hasSearchString(searchString)
                .and(UserSpecification.hasRole(role.name()))
                .and(UserSpecification.hasDeleted(isDeleted));
        Pageable pageable = PageRequest.of(page, size, sortDir, sortBy.getFieldName());
        Page<User> users = userRepository.findAll(spec, pageable);
        return users.map(userMapper::toUserDTO);
    }
}
