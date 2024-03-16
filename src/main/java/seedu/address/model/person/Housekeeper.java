package seedu.address.model.person;

import java.util.Set;

import seedu.address.model.tag.Tag;


/**
 * Represents a Housekeeper in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Housekeeper extends Person {
    /**
     * Every field must be present and not null.
     *
     * @param name
     * @param phone
     * @param email
     * @param address
     * @param tags
     */
    public Housekeeper(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        super(name, phone, email, address, tags);
    }
}
