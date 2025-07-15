package com.prm.manzone.controller;

import com.prm.manzone.payload.ApiResponse;
import com.prm.manzone.payload.user.request.CreateUserRequest;
import com.prm.manzone.enums.Role;
import com.prm.manzone.enums.UserSortField;
import com.prm.manzone.payload.user.request.UpdateUserRequest;
import com.prm.manzone.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "API for user management")
public class UserController {

        private final IUserService userService;

        @GetMapping("/profile")
        @Operation(summary = "Get authenticated user info")
        @SecurityRequirement(name = "Bearer Authentication")
        public ResponseEntity<ApiResponse<Object>> getAuthenticatedUser() {
                return ResponseEntity.ok(
                                ApiResponse.builder()
                                                .success(true)
                                                .errors(null)
                                                .message("Lấy thông tin người dùng thành công")
                                                .data(userService.getAuthenticatedUser())
                                                .build());
        }

        @Operation(summary = "Get user info by UserId")
        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<Object>> getUserById(@PathVariable int id) {
                return ResponseEntity.ok(
                                ApiResponse.builder()
                                                .success(true)
                                                .errors(null)
                                                .message("Lấy thông tin người dùng theo ID thành công")
                                                .data(userService.getUserById(id))
                                                .build());
        }

        @Operation(summary = "Register a new user")
        @PostMapping
        public ResponseEntity<ApiResponse<Object>> register(@RequestBody @Valid CreateUserRequest request) {
                return ResponseEntity.status(HttpStatus.CREATED).body(
                                ApiResponse.builder()
                                                .success(true)
                                                .errors(null)
                                                .message("Đăng ký tài khoản thành công")
                                                .data(userService.register(request))
                                                .build());
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @Operation(summary = "Update authenticated user info")
        @PutMapping
        public ResponseEntity<ApiResponse<Object>> updateUser(@RequestBody UpdateUserRequest request) {
                return ResponseEntity.ok(
                                ApiResponse.builder()
                                                .success(true)
                                                .errors(null)
                                                .message("Cập nhật thông tin người dùng thành công")
                                                .data(userService.updateUser(request))
                                                .build());
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @Operation(summary = "Update authenticated user's password")
        @PutMapping("/change-password")
        public ResponseEntity<ApiResponse<Object>> changePassword(@RequestParam String oldPassword,
                        @RequestParam String newPassword) {
                userService.changePassword(oldPassword, newPassword);
                return ResponseEntity.ok(
                                ApiResponse.builder()
                                                .success(true)
                                                .errors(null)
                                                .message("Mật khẩu đã được cập nhật thành công")
                                                .build());
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @Operation(summary = "Get all users", description = "Get paginated list of users with optional filtering. " +
                        "Parameters: " +
                        "• page (default: 0) - Page number starting from 0 " +
                        "• size (default: 10) - Number of items per page " +
                        "• sortDir (default: ASC) - Sort direction: ASC or DESC " +
                        "• sortBy (default: ID) - Field to sort by " +
                        "• searchString (optional) - Search in email, firstName, lastName, phoneNumber " +
                        "• role (optional) - Filter by role: CUSTOMER, ADMIN, ROLE_CUSTOMER, ROLE_ADMIN. If not provided, shows all roles "
                        +
                        "• isDeleted (optional) - Filter by deletion status: true, false. If not provided, shows all users regardless of deletion status")
        @GetMapping
        @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
        public ResponseEntity<ApiResponse<Object>> getAllUsers(
                        @RequestParam(defaultValue = "0", name = "page") int page,
                        @RequestParam(defaultValue = "10", name = "size") int size,
                        @RequestParam(defaultValue = "ASC", name = "sortDir") Sort.Direction sortDir,
                        @RequestParam(defaultValue = "CREATED_AT", name = "sortBy") UserSortField sortBy,
                        @RequestParam(required = false, name = "searchString") String searchString,
                        @RequestParam(required = false, name = "role") Role role,
                        @RequestParam(required = false,defaultValue = "false", name = "isDeleted") Boolean isDeleted) {
                return ResponseEntity.ok(
                                ApiResponse.builder()
                                                .success(true)
                                                .errors(null)
                                                .message("Lấy danh sách người dùng thành công")
                                                .data(userService.getAllUsers(page, size, sortDir, sortBy, searchString,
                                                                role, isDeleted))
                                                .build());
        }

}
