package com.prm.manzone.service.impl;

import com.prm.manzone.entities.User;
import com.prm.manzone.enums.Role;
import com.prm.manzone.exception.AppException;
import com.prm.manzone.exception.ErrorCode;
import com.prm.manzone.mapper.UserMapper;
import com.prm.manzone.payload.user.request.AdminCreateUserRequest;
import com.prm.manzone.payload.user.request.AdminUpdateUserRequest;
import com.prm.manzone.payload.user.response.UserDTO;
import com.prm.manzone.payload.user.response.UserStatisticsResponse;
import com.prm.manzone.repository.UserRepository;
import com.prm.manzone.service.IAdminUserService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements IAdminUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${app.default.avatar}")
    protected String defaultAvatar;

    @NonFinal
    @Value("${app.default.password}")
    protected String defaultPassword;

    @Override
    @Transactional
    public UserDTO createUser(AdminCreateUserRequest request) {
        log.info("Admin creating user with email: {}", request.getEmail());

        // Validate email uniqueness
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // Validate phone number format
        if (!isValidPhoneNumber(request.getPhoneNumber())) {
            throw new AppException(ErrorCode.INVALID_PHONE_NUMBER);
        }

        try {
            User user = userMapper.adminCreateRequestToUser(request);

            // Set default values if not provided
            if (user.getAvatarUrl() == null || user.getAvatarUrl().trim().isEmpty()) {
                user.setAvatarUrl(defaultAvatar);
            }

            // Encode password
            user.setPassword(passwordEncoder.encode(request.getPassword()));

            User savedUser = userRepository.save(user);
            log.info("Successfully created user with ID: {}", savedUser.getId());

            return userMapper.toUserDTO(savedUser);
        } catch (Exception e) {
            log.error("Error creating user: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR, "Lỗi tạo người dùng: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public UserDTO updateUser(int userId, AdminUpdateUserRequest request) {
        log.info("Admin updating user with ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Check if email is being changed and if it's already taken
        if (!user.getEmail().equals(request.getEmail())) {
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
            }
        }

        // Validate phone number format
        if (!isValidPhoneNumber(request.getPhoneNumber())) {
            throw new AppException(ErrorCode.INVALID_PHONE_NUMBER);
        }

        try {
            user = userMapper.adminUpdateRequestToUser(request, user);
            User savedUser = userRepository.save(user);

            log.info("Successfully updated user with ID: {}", userId);
            return userMapper.toUserDTO(savedUser);
        } catch (Exception e) {
            log.error("Error updating user {}: {}", userId, e.getMessage(), e);
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR, "Lỗi cập nhật người dùng: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteUser(int userId) {
        log.info("Admin deleting user with ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Prevent deletion of admin users
        if (user.getRole() == Role.ADMIN) {
            throw new AppException(ErrorCode.CANNOT_DELETE_ADMIN);
        }

        if (user.getDeleted()) {
            throw new AppException(ErrorCode.USER_ALREADY_DELETED);
        }

        try {
            user.setDeleted(true);
            user.setActive(false); // Also deactivate when deleting
            userRepository.save(user);

            log.info("Successfully deleted user with ID: {}", userId);
        } catch (Exception e) {
            log.error("Error deleting user {}: {}", userId, e.getMessage(), e);
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR, "Lỗi xóa người dùng: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public UserDTO restoreUser(int userId) {
        log.info("Admin restoring user with ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!user.getDeleted()) {
            throw new AppException(ErrorCode.INVALID_REQUEST, "Người dùng chưa bị xóa");
        }

        try {
            user.setDeleted(false);
            user.setActive(true); // Activate when restoring
            User savedUser = userRepository.save(user);

            log.info("Successfully restored user with ID: {}", userId);
            return userMapper.toUserDTO(savedUser);
        } catch (Exception e) {
            log.error("Error restoring user {}: {}", userId, e.getMessage(), e);
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR, "Lỗi khôi phục người dùng: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public UserDTO activateUser(int userId) {
        log.info("Admin activating user with ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (user.getActive()) {
            throw new AppException(ErrorCode.USER_ALREADY_ACTIVATED);
        }

        try {
            user.setActive(true);
            User savedUser = userRepository.save(user);

            log.info("Successfully activated user with ID: {}", userId);
            return userMapper.toUserDTO(savedUser);
        } catch (Exception e) {
            log.error("Error activating user {}: {}", userId, e.getMessage(), e);
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR, "Lỗi kích hoạt người dùng: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public UserDTO deactivateUser(int userId) {
        log.info("Admin deactivating user with ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Prevent deactivation of admin users
        if (user.getRole() == Role.ADMIN) {
            throw new AppException(ErrorCode.CANNOT_DEACTIVATE_ADMIN);
        }

        if (!user.getActive()) {
            throw new AppException(ErrorCode.USER_ALREADY_DEACTIVATED);
        }

        try {
            user.setActive(false);
            User savedUser = userRepository.save(user);

            log.info("Successfully deactivated user with ID: {}", userId);
            return userMapper.toUserDTO(savedUser);
        } catch (Exception e) {
            log.error("Error deactivating user {}: {}", userId, e.getMessage(), e);
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR, "Lỗi vô hiệu hóa người dùng: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void resetUserPassword(int userId, String newPassword) {
        log.info("Admin resetting password for user with ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        try {
            String passwordToUse = (newPassword != null && !newPassword.trim().isEmpty())
                    ? newPassword
                    : defaultPassword;

            user.setPassword(passwordEncoder.encode(passwordToUse));
            userRepository.save(user);

            log.info("Successfully reset password for user with ID: {}", userId);
        } catch (Exception e) {
            log.error("Error resetting password for user {}: {}", userId, e.getMessage(), e);
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR, "Lỗi đặt lại mật khẩu: " + e.getMessage());
        }
    }

    @Override
    public UserStatisticsResponse getUserStatistics() {
        log.info("Admin requesting user statistics");

        try {
            // Current date calculations
            LocalDate today = LocalDate.now();
            LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
            LocalDate startOfMonth = today.withDayOfMonth(1);
            LocalDate startOfYear = today.withDayOfYear(1);
            LocalDate thirtyDaysAgo = today.minusDays(30);
            LocalDate previousMonthStart = startOfMonth.minusMonths(1);
            LocalDate previousMonthEnd = startOfMonth.minusDays(1);

            // Convert to Instant
            Instant todayInstant = today.atStartOfDay(ZoneId.systemDefault()).toInstant();
            Instant startOfWeekInstant = startOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant();
            Instant startOfMonthInstant = startOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant();
            Instant startOfYearInstant = startOfYear.atStartOfDay(ZoneId.systemDefault()).toInstant();
            Instant thirtyDaysAgoInstant = thirtyDaysAgo.atStartOfDay(ZoneId.systemDefault()).toInstant();
            Instant previousMonthStartInstant = previousMonthStart.atStartOfDay(ZoneId.systemDefault()).toInstant();
            Instant previousMonthEndInstant = previousMonthEnd.atStartOfDay(ZoneId.systemDefault()).toInstant().plus(1,
                    ChronoUnit.DAYS);

            // Basic counts
            Long totalUsers = userRepository.count();
            Long deletedUsers = userRepository.countDeletedUsers();
            Long activatedUsers = userRepository.countActivatedUsers();
            Long deactivatedUsers = userRepository.countDeactivatedUsers();
            Long customersCount = userRepository.countByRole(Role.CUSTOMER);
            Long adminsCount = userRepository.countByRole(Role.ADMIN);

            // Registration statistics
            Long todayRegistrations = userRepository.countRegistrationsBetween(todayInstant, Instant.now());
            Long thisWeekRegistrations = userRepository.countRegistrationsBetween(startOfWeekInstant, Instant.now());
            Long thisMonthRegistrations = userRepository.countRegistrationsBetween(startOfMonthInstant, Instant.now());
            Long thisYearRegistrations = userRepository.countRegistrationsBetween(startOfYearInstant, Instant.now());
            Long previousMonthRegistrations = userRepository.countRegistrationsBetween(previousMonthStartInstant,
                    previousMonthEndInstant);

            // Daily registrations for last 30 days
            Map<LocalDate, Long> dailyRegistrations = getDailyRegistrations(thirtyDaysAgoInstant, Instant.now());

            // Calculate percentages
            Double customerPercentage = totalUsers > 0 ? (customersCount.doubleValue() / totalUsers) * 100 : 0.0;
            Double adminPercentage = totalUsers > 0 ? (adminsCount.doubleValue() / totalUsers) * 100 : 0.0;
            Double activeUserPercentage = totalUsers > 0 ? (activatedUsers.doubleValue() / totalUsers) * 100 : 0.0;
            Double deletedUserPercentage = totalUsers > 0 ? (deletedUsers.doubleValue() / totalUsers) * 100 : 0.0;

            // Calculate growth rate
            Double registrationGrowthRate = calculateGrowthRate(thisMonthRegistrations, previousMonthRegistrations);

            return UserStatisticsResponse.builder()
                    .totalUsers(totalUsers)
                    .activeUsers(activatedUsers)
                    .inactiveUsers(deactivatedUsers)
                    .deletedUsers(deletedUsers)
                    .customersCount(customersCount)
                    .adminsCount(adminsCount)
                    .todayRegistrations(todayRegistrations)
                    .thisWeekRegistrations(thisWeekRegistrations)
                    .thisMonthRegistrations(thisMonthRegistrations)
                    .thisYearRegistrations(thisYearRegistrations)
                    .dailyRegistrations(dailyRegistrations)
                    .customerPercentage(customerPercentage)
                    .adminPercentage(adminPercentage)
                    .activeUserPercentage(activeUserPercentage)
                    .deletedUserPercentage(deletedUserPercentage)
                    .previousMonthRegistrations(previousMonthRegistrations)
                    .registrationGrowthRate(registrationGrowthRate)
                    .build();

        } catch (Exception e) {
            log.error("Error getting user statistics: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR, "Lỗi lấy thống kê người dùng: " + e.getMessage());
        }
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        // Vietnamese phone number validation
        String regex = "^(0|\\+84|84)([35789])[0-9]{8}$";
        return phoneNumber.matches(regex);
    }

    private Map<LocalDate, Long> getDailyRegistrations(Instant startDate, Instant endDate) {
        List<Object[]> results = userRepository.findDailyRegistrations(startDate, endDate);
        Map<LocalDate, Long> dailyMap = new HashMap<>();

        for (Object[] result : results) {
            Date sqlDate = (Date) result[0];
            Long count = (Long) result[1];
            LocalDate date = sqlDate.toLocalDate();
            dailyMap.put(date, count);
        }

        return dailyMap;
    }

    private Double calculateGrowthRate(Long currentValue, Long previousValue) {
        if (previousValue == null || previousValue == 0) {
            return currentValue > 0 ? 100.0 : 0.0;
        }
        return ((currentValue.doubleValue() - previousValue.doubleValue()) / previousValue.doubleValue()) * 100;
    }
}
