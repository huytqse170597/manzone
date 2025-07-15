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
        try {
            SecurityContext context = SecurityContextHolder.getContext();

            if (context.getAuthentication() == null) {
                throw new RuntimeException("No authentication found in security context");
            }

            if (context.getAuthentication().getName() == null) {
                throw new RuntimeException("Authentication principal is null");
            }

            String principal = context.getAuthentication().getName();

            if ("anonymousUser".equals(principal)) {
                throw new RuntimeException("User is not authenticated - found anonymous user");
            }

            int id = Integer.parseInt(principal);
            return userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng " + id));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Failed to parse user ID from security context: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract user information from security context: " + e.getMessage(),
                    e);
        }
    }

    @Override
    public UserDTO register(CreateUserRequest userDto) {
        String PHONE_REGEX = "^[0-9]{10}$";
        User newUser = new User();
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã được sử dụng");
        }
        if (!userDto.getPhoneNumber().matches(PHONE_REGEX)) {
            throw new RuntimeException("Số điện thoại không hợp lệ.");
        }
        newUser.setFirstName(userDto.getFirstName());
        newUser.setLastName(userDto.getLastName());
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        newUser.setPhoneNumber(userDto.getPhoneNumber());
        newUser.setEmail(userDto.getEmail());
        newUser.setAddress(userDto.getAddress());
        newUser.setRole(Role.CUSTOMER);
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

    public Page<UserDTO> getAllUsers(int page, int size, Sort.Direction sortDir, UserSortField sortBy,
            String searchString, Role role, Boolean isDeleted) {

        // Validate pagination parameters
        if (page < 0) {
            page = 0;
        }
        if (size <= 0 || size > 100) { // Limit max size to prevent performance issues
            size = 10;
        }

        Specification<User> spec = UserSpecification.hasSearchString(searchString)
                .and(UserSpecification.hasRole(role))
                .and(UserSpecification.hasDeleted(isDeleted));
        Pageable pageable = PageRequest.of(page, size, sortDir, sortBy.getFieldName());
        Page<User> users = userRepository.findAll(spec, pageable);
        return users.map(userMapper::toUserDTO);
    }
}
