package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Booking;
import seedu.address.model.person.BookingList;
import seedu.address.model.person.Client;
import seedu.address.model.person.Email;
import seedu.address.model.person.Housekeeper;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Type;
import seedu.address.model.tag.Tag;

public class JsonAdaptedHousekeeper extends JsonAdaptedPerson {
    protected final ArrayList<JsonAdaptedBooking> bookingList;

    /**
     * Constructs a {@code JsonAdaptedHousekeeper} with the given housekeeper details.
     */
    @JsonCreator
    public JsonAdaptedHousekeeper(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
                             @JsonProperty("email") String email, @JsonProperty("address") String address,
                             @JsonProperty("tags") List<JsonAdaptedTag> tags, @JsonProperty("type") String type,
                             @JsonProperty("booking list") ArrayList<JsonAdaptedBooking> bookingList) {
        super(name, phone, email, address, tags, type);
        this.bookingList = bookingList;
    }

    /**
     * Converts a given {@code Housekeeper} into this class for Jackson use.
     */
    public JsonAdaptedHousekeeper(Housekeeper source) {
        super(source);
        ArrayList<JsonAdaptedBooking> bookingList = new ArrayList<>();

        for (Booking booking : source.getBookingList().getBookings()) {
            JsonAdaptedBooking jsonAdaptedBooking = new JsonAdaptedBooking(booking);
            bookingList.add(jsonAdaptedBooking);
        }

        this.bookingList = bookingList;
    }

    @Override
    public Housekeeper toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        final Set<Tag> modelTags = new HashSet<>(personTags);

        if (type == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Type.class.getSimpleName()));
        }
        if (!Type.isValidType(type)) {
            throw new IllegalValueException(Type.MESSAGE_CONSTRAINTS);
        }
        final Type modelType = new Type(type);

        final ArrayList<Booking> personBookings = new ArrayList<>();
        for (JsonAdaptedBooking booking : bookingList) {
            personBookings.add(booking.toModelType());
        }

        final BookingList modelBookingList = new BookingList(personBookings);

        return new Housekeeper(modelName, modelPhone, modelEmail, modelAddress, modelTags, modelType, modelBookingList);
    }
}
