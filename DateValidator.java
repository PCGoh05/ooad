import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class DateValidator {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);
    
    /**
     * Validates if the given date string is in the correct format and not in the past
     * @param dateStr Date string in DD/MM/YYYY format
     * @return true if date is valid and not in the past, false otherwise
     */
    public static boolean isValidFutureDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return false;
        }
        
        try {
            LocalDate eventDate = LocalDate.parse(dateStr.trim(), DATE_FORMATTER);
            LocalDate today = LocalDate.now();
            
            // Check if the event date is today or in the future
            return !eventDate.isBefore(today);
        } catch (DateTimeParseException e) {
            // This will catch invalid dates like June 31st, February 30th, etc.
            return false;
        }
    }
    
    /**
     * Gets a formatted error message for invalid dates
     * @param dateStr The invalid date string
     * @return Error message
     */
    public static String getDateErrorMessage(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return "Date cannot be empty.";
        }
        
        try {
            LocalDate eventDate = LocalDate.parse(dateStr.trim(), DATE_FORMATTER);
            LocalDate today = LocalDate.now();
            
            if (eventDate.isBefore(today)) {
                return "Event date cannot be in the past. Please select today's date or a future date.";
            }
            return "Date is valid.";
        } catch (DateTimeParseException e) {
            // Check if it's a format issue or invalid date
            String[] parts = dateStr.trim().split("/");
            if (parts.length != 3) {
                return "Invalid date format. Please use DD/MM/YYYY format (e.g., 30/06/2025).";
            }
            
            try {
                int day = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int year = Integer.parseInt(parts[2]);
                
                if (day < 1 || day > 31) {
                    return "Invalid day. Day must be between 1 and 31.";
                }
                if (month < 1 || month > 12) {
                    return "Invalid month. Month must be between 1 and 12.";
                }
                if (year < 1900 || year > 3000) {
                    return "Invalid year. Please enter a reasonable year.";
                }
                
                // If we get here, it's likely an invalid date like June 31st
                return String.format("Invalid date: %s. Please check if the day exists in the specified month.", dateStr);
                
            } catch (NumberFormatException nfe) {
                return "Invalid date format. Please use DD/MM/YYYY format with numbers only (e.g., 30/06/2025).";
            }
        }
    }
    
    /**
     * Gets today's date in DD/MM/YYYY format
     * @return Today's date as string
     */
    public static String getTodayDate() {
        return LocalDate.now().format(DATE_FORMATTER);
    }
    
    /**
     * Checks if a date string is in the correct format
     * @param dateStr Date string to check
     * @return true if format is correct, false otherwise
     */
    public static boolean isValidDateFormat(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return false;
        }
        
        try {
            LocalDate.parse(dateStr.trim(), DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
    /**
     * Checks if early bird discount is available (at least 14 days before event)
     * @param eventDateStr Event date string in DD/MM/YYYY format
     * @return true if current date is at least 14 days before event date
     */
    public static boolean isEarlyBirdEligible(String eventDateStr) {
        if (eventDateStr == null || eventDateStr.trim().isEmpty()) {
            return false;
        }
        
        try {
            LocalDate eventDate = LocalDate.parse(eventDateStr.trim(), DATE_FORMATTER);
            LocalDate today = LocalDate.now();
            LocalDate earlyBirdCutoff = eventDate.minusDays(14);
            
            // Early bird is available if today is at least 14 days before the event
            return !today.isAfter(earlyBirdCutoff);
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
    /**
     * Gets the number of days until the event
     * @param eventDateStr Event date string in DD/MM/YYYY format
     * @return number of days until event, or -1 if invalid date
     */
    public static long getDaysUntilEvent(String eventDateStr) {
        if (eventDateStr == null || eventDateStr.trim().isEmpty()) {
            return -1;
        }
        
        try {
            LocalDate eventDate = LocalDate.parse(eventDateStr.trim(), DATE_FORMATTER);
            LocalDate today = LocalDate.now();
            
            return java.time.temporal.ChronoUnit.DAYS.between(today, eventDate);
        } catch (DateTimeParseException e) {
            return -1;
        }
    }
}
