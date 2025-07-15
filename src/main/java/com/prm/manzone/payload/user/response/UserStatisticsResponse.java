package com.prm.manzone.payload.user.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserStatisticsResponse {

    // Overall user statistics
    Long totalUsers;
    Long activeUsers;
    Long inactiveUsers;
    Long deletedUsers;
    Long customersCount;
    Long adminsCount;

    // Registration statistics
    Long todayRegistrations;
    Long thisWeekRegistrations;
    Long thisMonthRegistrations;
    Long thisYearRegistrations;

    // Registration trends (last 30 days)
    Map<LocalDate, Long> dailyRegistrations;

    // Role distribution percentage
    Double customerPercentage;
    Double adminPercentage;

    // Activity statistics
    Double activeUserPercentage;
    Double deletedUserPercentage;

    // Growth statistics
    Long previousMonthRegistrations;
    Double registrationGrowthRate; // Percentage growth compared to previous month
}
