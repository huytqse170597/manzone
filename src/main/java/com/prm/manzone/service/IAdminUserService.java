package com.prm.manzone.service;

import com.prm.manzone.payload.user.request.AdminCreateUserRequest;
import com.prm.manzone.payload.user.request.AdminUpdateUserRequest;
import com.prm.manzone.payload.user.response.UserDTO;
import com.prm.manzone.payload.user.response.UserStatisticsResponse;

public interface IAdminUserService {

    /**
     * Create a new user (Admin only)
     */
    UserDTO createUser(AdminCreateUserRequest request);

    /**
     * Update user by ID (Admin only)
     */
    UserDTO updateUser(int userId, AdminUpdateUserRequest request);

    /**
     * Soft delete user by ID (Admin only)
     */
    void deleteUser(int userId);

    /**
     * Restore deleted user by ID (Admin only)
     */
    UserDTO restoreUser(int userId);

    /**
     * Activate user by ID (Admin only)
     */
    UserDTO activateUser(int userId);

    /**
     * Deactivate user by ID (Admin only)
     */
    UserDTO deactivateUser(int userId);

    /**
     * Reset user password (Admin only)
     */
    void resetUserPassword(int userId, String newPassword);

    /**
     * Get comprehensive user statistics (Admin only)
     */
    UserStatisticsResponse getUserStatistics();
}
