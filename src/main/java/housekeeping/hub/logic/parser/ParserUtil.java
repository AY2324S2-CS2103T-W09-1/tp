package housekeeping.hub.logic.parser;

import static housekeeping.hub.logic.parser.CliSyntax.ALLOWED_PREAMBLES_TYPE;
import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import housekeeping.hub.commons.core.index.Index;
import housekeeping.hub.commons.util.StringUtil;
import housekeeping.hub.logic.commands.BookingCommand;
import housekeeping.hub.logic.parser.exceptions.ParseException;
import housekeeping.hub.model.person.Address;
import housekeeping.hub.model.person.Area;
import housekeeping.hub.model.person.Booking;
import housekeeping.hub.model.person.Email;
import housekeeping.hub.model.person.HousekeepingDetails;
import housekeeping.hub.model.person.Name;
import housekeeping.hub.model.person.Phone;
import housekeeping.hub.model.person.Type;
import housekeeping.hub.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {
    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";
    public static final String TYPE_VALIDATION_REGEX = "[^\\s].*";

    private static final Pattern PATTERN_BOOKING = Pattern.compile(
            "(\\d{4}-\\d{2}-\\d{2}\\s+(am|pm))");

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String hub} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code hub} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses a {@code String type} into a {@code type}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code type} is invalid.
     */
    public static String parseType(String type) throws ParseException {
        requireNonNull(type);
        String trimmedType = type.trim();
        if (!(trimmedType.matches(TYPE_VALIDATION_REGEX) && preambleIsAllowed(trimmedType))) {
            throw new ParseException(Type.MESSAGE_CONSTRAINTS);
        }
        return trimmedType;
    }

    /**
     * Checks if a given command uses a preamble that is allowed. (we define preamble as TYPE)
     * @param preamble
     * @return true if the preamble is allowed and no if it is not.
     */
    public static boolean preambleIsAllowed(String preamble) {
        for (String s : ALLOWED_PREAMBLES_TYPE) {
            if (s.equals(preamble)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Parses a {@code String area} into an {@code Area}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code area} is invalid.
     */
    public static Area parseArea(String area) throws ParseException {
        requireNonNull(area);
        String trimmedArea = area.trim();
        if (!Area.isValidArea(trimmedArea)) {
            throw new ParseException(Area.MESSAGE_CONSTRAINTS);
        }
        return new Area(trimmedArea);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses a {@code String lHD} into a {@code LocalDate}.
     */
    public static LocalDate parseLastHousekeepingDate(String lHD) throws ParseException {
        requireNonNull(lHD);
        try {
            LocalDate parsedLhd = LocalDate.parse(lHD);
            return parsedLhd;
        } catch (Exception e) {
            throw new ParseException(BookingCommand.MESSAGE_USAGE);
        }
    }

    /**
     * Parses a {@code String booking} into a {@code Booking}.
     */
    public static Booking parseBooking(String booking) throws ParseException {
        requireNonNull(booking);
        if (PATTERN_BOOKING.matcher(booking.trim()).matches()) {
            Booking parsedBooking = new Booking(booking);
            return parsedBooking;
        } else {
            throw new ParseException(Booking.MESSAGE_CONSTRAINTS);
        }
    }

    /**
     * Parses a {@code String pI} into a {@code Period}.
     */
    public static Period parsePreferredInterval(String pI) throws ParseException {
        requireNonNull(pI);
        String trimmedPI = pI.trim();
        String[] splitPI = trimmedPI.split("\\s+");
        Period period;
        int quantity = Integer.parseInt(splitPI[0]);
        if (quantity <= 0) {
            throw new ParseException(HousekeepingDetails.MESSAGE_CONSTRAINTS);
        }
        switch (splitPI[1]) {
        case "days":
            period = Period.ofDays(quantity);
            break;
        case "weeks":
            period = Period.ofWeeks(quantity);
            break;
        case "months":
            period = Period.ofMonths(quantity);
            break;
        case "years":
            period = Period.ofYears(quantity);
            break;
        default:
            throw new ParseException(BookingCommand.MESSAGE_USAGE);
        }
        return period;
    }

    /**
     * Parses a {@code String details} into a {@code HousekeepingDetails}.
     */
    public static HousekeepingDetails parseHousekeepingDetails(Optional<String> details) throws ParseException {
        if (details.isEmpty()) {
            return HousekeepingDetails.EMPTY;
        }
        String trimmedDetails = details.get().trim();
        if (!HousekeepingDetails.isValidHousekeepingDetailsUser(trimmedDetails)) {
            throw new ParseException(HousekeepingDetails.MESSAGE_CONSTRAINTS);
        }

        String[] s;
        LocalDate date;
        Period period;
        int quantity;

        try {
            s = trimmedDetails.split(" ");
            date = LocalDate.parse(s[0]);
            quantity = Integer.parseInt(s[1]);
            if (quantity <= 0) {
                throw new ParseException(HousekeepingDetails.MESSAGE_CONSTRAINTS);
            }
        } catch (DateTimeParseException e) {
            throw new ParseException(e.getMessage());
        }

        switch (s[2]) {
        case "days":
            period = Period.ofDays(quantity);
            break;
        case "weeks":
            period = Period.ofWeeks(quantity);
            break;
        case "months":
            period = Period.ofMonths(quantity);
            break;
        case "years":
            period = Period.ofYears(quantity);
            break;
        default:
            throw new ParseException(HousekeepingDetails.MESSAGE_CONSTRAINTS);
        }

        return new HousekeepingDetails(date, period);
    }
}
