package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.DateFormatter;

/**
 * Represents a booked cleaning for a housekeeper.
 */
public class BookedCleaning implements Comparable<BookedCleaning> {
    public static final String MESSAGE_CONSTRAINTS = "Booked date and time should be in the format: yyyy-mm-dd (am|pm)."
            + "Both date and time fields must be filled. Time field can only take values [am, pm].";

    private static final String REGEX_BOOKED_DATE_AND_TIME = "(\\d{4}-\\d{2}-\\d{2})\\s+(am|pm)";
    private static final Pattern PATTERN_BOOKED_DATE_AND_TIME = Pattern.compile(REGEX_BOOKED_DATE_AND_TIME,
            Pattern.CASE_INSENSITIVE);
    private static final DateTimeFormatter FORMATTER_BOOKED_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private LocalDate bookedDate;
    private String bookedTime;

    /**
     * Creates a BookedCleaning object.
     *
     * @param bookedDate The booked date.
     * @param bookedTime The booked time, either "am" or "pm".
     */
    public BookedCleaning(LocalDate bookedDate, String bookedTime) {
        this.bookedDate = bookedDate;
        this.bookedTime = bookedTime;
    }

    /**
     * Creates a BookedCleaning object using a string representation of the booked date and time.
     *
     * @param bookedDateAndTime String representation of the booked date and time.
     */
    public BookedCleaning(String bookedDateAndTime) {
        requireNonNull(bookedDateAndTime);
        checkArgument(isValidBookedDateAndTime(bookedDateAndTime), MESSAGE_CONSTRAINTS);
        this.bookedDate = retrieveDate(bookedDateAndTime);
        this.bookedTime = retrieveTime(bookedDateAndTime);
    }

    /**
     * Checks if specified string representation of booked date and time is in a valid format.
     *
     * @param bookedDateAndTime String representation of the booked date and time.
     * @return True if valid, false otherwise.
     */
    public static boolean isValidBookedDateAndTime(String bookedDateAndTime) {
        Matcher matcher = PATTERN_BOOKED_DATE_AND_TIME.matcher(bookedDateAndTime);
        return matcher.matches();
    }

    public static LocalDate retrieveDate(String bookedDateAndTime) {
        Matcher matcher = PATTERN_BOOKED_DATE_AND_TIME.matcher(bookedDateAndTime);
        String dateString = matcher.group(1);
        LocalDate parsedDate = LocalDate.parse(dateString);
        return parsedDate;
    }

    public static String retrieveTime(String bookedDateAndTime) {
        assert bookedDateAndTime == null : "Booked date and time should not be null";
        Matcher matcher = PATTERN_BOOKED_DATE_AND_TIME.matcher(bookedDateAndTime);
        String dateString = matcher.group(2);
        return dateString;
    }

    public LocalDate getBookedDate() {
        return this.bookedDate;
    }

    public String getBookedTime() {
        return this.bookedTime;
    }

    public String formatBookedCleaning() {
        String formattedDateString = bookedDate.format(FORMATTER_BOOKED_DATE);
        return formattedDateString + " " + bookedTime;
    }

    @Override
    public int compareTo(BookedCleaning other) {
        LocalDate thisDate = this.getBookedDate();
        LocalDate otherDate = other.getBookedDate();
        String thisTime = this.getBookedTime();
        String otherTime = other.getBookedTime();

        int dateComparison = thisDate.compareTo(otherDate);
        if (dateComparison != 0) {
            return dateComparison;
        }

        return thisTime.compareTo(otherTime);
    }

    @Override
    public String toString() {
        return this.formatBookedCleaning();
    }
}
