package com.prm.manzone.controller;

import com.prm.manzone.payload.ApiResponse;
import com.prm.manzone.enums.Role;
import com.prm.manzone.enums.UserSortField;
import com.prm.manzone.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Tag(name = "Admin User", description = "API for admin user management")
public class AdminUserController {
    private final IUserService userService;

    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get all users", description = "Get paginated list of users with optional filtering. " +
            "Parameters: " +
            "• page (default: 0) - Page number starting from 0 " +
            "• size (default: 10) - Number of items per page " +
            "• sortDir (default: ASC) - Sort direction: ASC or DESC " +
            "• sortBy (default: ID) - Field to sort by " +
            "• searchString (optional) - Search in email, firstName, lastName, phoneNumber " +
            "• role (optional) - Filter by role: CUSTOMER, ADMIN, ROLE_CUSTOMER, ROLE_ADMIN. If not provided, shows all roles " +
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
                        .data(userService.getAllUsers(page, size, sortDir, sortBy, searchString, role, isDeleted))
                        .build());
    }
}

