package com.prm.manzone.controller;

import com.prm.manzone.payload.ApiResponse;
import com.prm.manzone.enums.Role;
import com.prm.manzone.enums.UserSortField;
import com.prm.manzone.payload.user.request.AdminCreateUserRequest;
import com.prm.manzone.payload.user.request.AdminUpdateUserRequest;
import com.prm.manzone.service.IAdminUserService;
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

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Tag(name = "Admin User", description = "API for admin user management")
@CrossOrigin(origins = {
                "https://manzone.wizlab.io.vn",
                "http://localhost:5173",
                "http://localhost:3001",
                "http://localhost:3000",
                "https://manzone-admin.vercel.app"
}, allowCredentials = "true", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
                RequestMethod.DELETE, RequestMethod.OPTIONS })
public class AdminUserController {

        private final IUserService userService;
        private final IAdminUserService adminUserService;

        @Operation(summary = "Get all users", description = "Get paginated list of users with optional filtering and sorting")
        @GetMapping
        @SecurityRequirement(name = "Bearer Authentication")
        @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
        public ResponseEntity<ApiResponse<Object>> getAllUsers(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "ASC") Sort.Direction sortDir,
                        @RequestParam(defaultValue = "CREATED_AT") UserSortField sortBy,
                        @RequestParam(required = false) String searchString,
                        @RequestParam(required = false) Role role,
                        @RequestParam(required = false, defaultValue = "false") Boolean isDeleted) {

                return ResponseEntity.ok(
                                ApiResponse.builder()
                                                .success(true)
                                                .message("Lấy danh sách người dùng thành công")
                                                .data(userService.getAllUsers(page, size, sortDir, sortBy, searchString,
                                                                role, isDeleted))
                                                .build());
        }

        @Operation(summary = "Get user by ID", description = "Get detailed information of a specific user")
        @GetMapping("/{id}")
        @SecurityRequirement(name = "Bearer Authentication")
        @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
        public ResponseEntity<ApiResponse<Object>> getUserById(@PathVariable int id) {
                return ResponseEntity.ok(
                                ApiResponse.builder()
                                                .success(true)
                                                .message("Lấy thông tin người dùng thành công")
                                                .data(userService.getUserById(id))
                                                .build());
        }

        @Operation(summary = "Create new user", description = "Create a new user with admin privileges")
        @PostMapping
        @SecurityRequirement(name = "Bearer Authentication")
        @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
        public ResponseEntity<ApiResponse<Object>> createUser(@Valid @RequestBody AdminCreateUserRequest request) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(ApiResponse.builder()
                                                .success(true)
                                                .message("Tạo người dùng thành công")
                                                .data(adminUserService.createUser(request))
                                                .build());
        }

        @Operation(summary = "Update user", description = "Update user information")
        @PutMapping("/{id}")
        @SecurityRequirement(name = "Bearer Authentication")
        @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
        public ResponseEntity<ApiResponse<Object>> updateUser(
                        @PathVariable int id,
                        @Valid @RequestBody AdminUpdateUserRequest request) {

                return ResponseEntity.ok(
                                ApiResponse.builder()
                                                .success(true)
                                                .message("Cập nhật người dùng thành công")
                                                .data(adminUserService.updateUser(id, request))
                                                .build());
        }

        @Operation(summary = "Delete user", description = "Soft delete a user (cannot delete admin users)")
        @DeleteMapping("/{id}")
        @SecurityRequirement(name = "Bearer Authentication")
        @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
        public ResponseEntity<ApiResponse<Object>> deleteUser(@PathVariable int id) {
                adminUserService.deleteUser(id);
                return ResponseEntity.ok(
                                ApiResponse.builder()
                                                .success(true)
                                                .message("Xóa người dùng thành công")
                                                .build());
        }

        @Operation(summary = "Restore user", description = "Restore a deleted user")
        @PostMapping("/{id}/restore")
        @SecurityRequirement(name = "Bearer Authentication")
        @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
        public ResponseEntity<ApiResponse<Object>> restoreUser(@PathVariable int id) {
                return ResponseEntity.ok(
                                ApiResponse.builder()
                                                .success(true)
                                                .message("Khôi phục người dùng thành công")
                                                .data(adminUserService.restoreUser(id))
                                                .build());
        }

        @Operation(summary = "Activate user", description = "Activate a deactivated user")
        @PostMapping("/{id}/activate")
        @SecurityRequirement(name = "Bearer Authentication")
        @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
        public ResponseEntity<ApiResponse<Object>> activateUser(@PathVariable int id) {
                return ResponseEntity.ok(
                                ApiResponse.builder()
                                                .success(true)
                                                .message("Kích hoạt người dùng thành công")
                                                .data(adminUserService.activateUser(id))
                                                .build());
        }

        @Operation(summary = "Deactivate user", description = "Deactivate a user (cannot deactivate admin users)")
        @PostMapping("/{id}/deactivate")
        @SecurityRequirement(name = "Bearer Authentication")
        @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
        public ResponseEntity<ApiResponse<Object>> deactivateUser(@PathVariable int id) {
                return ResponseEntity.ok(
                                ApiResponse.builder()
                                                .success(true)
                                                .message("Vô hiệu hóa người dùng thành công")
                                                .data(adminUserService.deactivateUser(id))
                                                .build());
        }

        @Operation(summary = "Reset user password", description = "Reset user password to default or specified value")
        @PostMapping("/{id}/reset-password")
        @SecurityRequirement(name = "Bearer Authentication")
        @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
        public ResponseEntity<ApiResponse<Object>> resetUserPassword(
                        @PathVariable int id,
                        @RequestParam(required = false) String newPassword) {

                adminUserService.resetUserPassword(id, newPassword);
                return ResponseEntity.ok(
                                ApiResponse.builder()
                                                .success(true)
                                                .message("Đặt lại mật khẩu thành công")
                                                .build());
        }

        @Operation(summary = "Get user statistics", description = "Get comprehensive user statistics for admin dashboard")
        @GetMapping("/statistics")
        @SecurityRequirement(name = "Bearer Authentication")
        @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
        public ResponseEntity<ApiResponse<Object>> getUserStatistics() {
                return ResponseEntity.ok(
                                ApiResponse.builder()
                                                .success(true)
                                                .message("Lấy thống kê người dùng thành công")
                                                .data(adminUserService.getUserStatistics())
                                                .build());
        }

        @Operation(summary = "CORS test endpoint", description = "Simple endpoint to test CORS configuration")
        @GetMapping("/test")
        @SecurityRequirement(name = "Bearer Authentication")
        @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
        public ResponseEntity<ApiResponse<Object>> testCors() {
                return ResponseEntity.ok(
                                ApiResponse.builder()
                                                .success(true)
                                                .message("CORS test successful - Admin API is accessible")
                                                .data("Admin User API is working correctly")
                                                .build());
        }
}
