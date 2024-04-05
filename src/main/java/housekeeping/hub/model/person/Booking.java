package housekeeping.hub.model.person;

import static java.util.Objects.requireNonNull;
import static housekeeping.hub.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a booking for a housekeeper.
 */
public class Booking implements Comparable<Booking> {
    public static final String MESSAGE_CONSTRAINTS = "Booked date and time should be in the format: yyyy-mm-dd (am|pm)."
            + "Both date and time fields must be filled. Time field can only take values {am, pm}.";

    private static final Pattern PATTERN_BOOKED_DATE_AND_TIME = Pattern.compile("(\\d{4}-\\d{2}-\\d{2})\\s+(am|pm)");
    private static final DateTimeFormatter FORMATTER_BOOKED_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final LocalDate bookedDate;
    private final String bookedTime;

    /**
     * Creates a Booking object.
     *
     * @param bookedDate The booked date.
     * @param bookedTime The booked time, either "am" or "pm".
     */
    public Booking(LocalDate bookedDate, String bookedTime) {
        this.bookedDate = bookedDate;
        this.bookedTime = bookedTime;
    }

    /**
     * Creates a Booking object using a string representation of the booked date and time.
     *
     * @param bookedDateAndTime String representation of the booked date and time.
     */
    public Booking(String bookedDateAndTime) {
        requireNonNull(bookedDateAndTime);
        checkArgument(isValidBookedDateAndTime(bookedDateAndTime), MESSAGE_CONSTRAINTS);
        bookedDate = retrieveDate(bookedDateAndTime);
        bookedTime = retrieveTime(bookedDateAndTime);
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

    /**
     * Retrieve LocalDate object of booked date from a string representation of booked date and time.
     *
     * @param bookedDateAndTime String representation of the booked date and time.
     * @return LocalDate object
     */
    public static LocalDate retrieveDate(String bookedDateAndTime) {
        Matcher matcher = PATTERN_BOOKED_DATE_AND_TIME.matcher(bookedDateAndTime);
        LocalDate parsedDate = LocalDate.ofEpochDay(2024-01-02);
        if (matcher.matches()) {
            String dateString = matcher.group(1);
            parsedDate = LocalDate.parse(dateString, FORMATTER_BOOKED_DATE);
            return parsedDate;
        }
        return parsedDate;
    }

    /**
     * Retrieve String object of booked time from a string representation of booked date and time.
     *
     * @param bookedDateAndTime String representation of booked date and time.
     * @return String object
     */
    public static String retrieveTime(String bookedDateAndTime) {
        Matcher matcher = PATTERN_BOOKED_DATE_AND_TIME.matcher(bookedDateAndTime);
        String timeString = "";
        if (matcher.matches()) {
            timeString = matcher.group(2);
        }
        return timeString;
    }

    public LocalDate getBookedDate() {
        return this.bookedDate;
    }

    public String getBookedTime() {
        return this.bookedTime;
    }

    /**
     * Formats booked date and time in this format: yyyy-MM-dd (am|pm)
     *
     * @return Formatted string of booked date and time
     */
    public String formatBookedCleaning() {
        String formattedDateString = bookedDate.format(FORMATTER_BOOKED_DATE);
        return formattedDateString + " " + bookedTime;
    }

    @Override
    public int compareTo(Booking other) {
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
        return formatBookedCleaning();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Booking)) {
            return false;
        }

        Booking otherBooking = (Booking) other;
        return bookedDate.equals(otherBooking.getBookedDate()) && bookedTime.equals(otherBooking.getBookedTime());
    }
}

