package seedu.address.model.person;

import seedu.address.model.tag.Tag;

import java.util.Set;

public class Client extends Person{

    /**
     * Every field must be present and not null.
     *
     * @param name
     * @param phone
     * @param email
     * @param address
     * @param tags
     */
    public Client(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        super(name, phone, email, address, tags);
    }
}
