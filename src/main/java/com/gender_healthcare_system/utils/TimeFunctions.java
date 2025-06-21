package com.gender_healthcare_system.utils;

import com.gender_healthcare_system.exceptions.AppException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class TimeFunctions {

    public static LocalDateTime getCurrentDateTimeWithTimeZone(){
        ZoneId zone = ZoneId.of("Asia/Bangkok");
        return LocalDateTime.now(zone);
    }

    public static void validateIssueDateAndExpiryDate(LocalDate issueDate,
                                                      LocalDate expiryDate){
        ZoneId zone = ZoneId.of("Asia/Bangkok");
        long daysCheckPoint = 365 + 182;
        long yearsCheckPoint = 2;

        boolean validateIssueDate = issueDate.isEqual(LocalDate.now(zone))
                || issueDate.isAfter(LocalDate.now(zone));

        if(validateIssueDate){

            throw new AppException(400, "Issue Date cannot be equal to or after current Date");
        }

        boolean validateExpiryDate = expiryDate.isEqual(LocalDate.now(zone))
                || expiryDate.isBefore(LocalDate.now(zone));

        if(validateExpiryDate){

            throw new AppException(400, "Expiry Date cannot be equal to or before current Date");
        }

        long differenceInDays = ChronoUnit.YEARS.between(LocalDate.now(zone), expiryDate);

        if(differenceInDays < daysCheckPoint){

            throw new AppException(400, "Expiry Date has to be after current Date " +
                    "by 1 and a half years or more");
        }

        long differenceInYears = ChronoUnit.YEARS.between(issueDate, expiryDate);

        if(differenceInYears < yearsCheckPoint){

            throw new AppException(400, "Expiry Date has to be after Issue Date " +
                    "by 2 years or more");
        }

    }

    public static void validateRealStartAndEndTime
            (LocalDateTime expectedStartTime, LocalDateTime expectedEndTime,
             LocalDateTime realStartTime, LocalDateTime realEndTime){

        long minutesCheckPoint1 = 90 ;
        long minutesCheckPoint2 = 60 ;

        long startTimeDifference = ChronoUnit.MINUTES.between(expectedEndTime, realStartTime);

        if(startTimeDifference < 0 || startTimeDifference > minutesCheckPoint1){

            throw new AppException(400,
                    "Real Start time cannot be before Expected Start time" +
                            "and can only be 90 minutes later at most " +
                            "compared to Expected Start time");
        }

        if(realEndTime.isEqual(expectedStartTime)
                || realEndTime.isBefore(expectedStartTime)){

            throw new AppException(400,
                    "Real End time cannot be equal to or before Expected Start time");
        }

        long startAndEndTimeDifference =
                ChronoUnit.MINUTES.between(realStartTime, realEndTime);

        if(startAndEndTimeDifference < 20
                || startAndEndTimeDifference > minutesCheckPoint2){

            throw new AppException(400,
                    "Real End time must be at least 20 minute or at most 60 minute later " +
                            "compared to Real Start time");
        }

    }
}
