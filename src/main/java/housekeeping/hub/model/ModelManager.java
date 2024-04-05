package housekeeping.hub.model;

import static java.util.Objects.requireNonNull;
import static housekeeping.hub.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import housekeeping.hub.commons.core.GuiSettings;
import housekeeping.hub.commons.core.LogsCenter;
import housekeeping.hub.model.person.Client;
import housekeeping.hub.model.person.Housekeeper;
import housekeeping.hub.model.person.Person;

/**
 * Represents the in-memory model of the hub book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Client> filteredClients;
    private final FilteredList<Housekeeper> filteredHousekeepers;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with hub book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredClients = new FilteredList<>(this.addressBook.getClientList());
        filteredHousekeepers = new FilteredList<>(this.addressBook.getHousekeeperList());
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasClient(Client client) {
        requireNonNull(client);
        return addressBook.hasClient(client);
    }

    @Override
    public boolean hasHousekeeper(Housekeeper housekeeper) {
        requireNonNull(housekeeper);
        return addressBook.hasHousekeeper(housekeeper);
    }

    @Override
    public void deleteClient(Client target) {
        addressBook.removeClient(target);
    }

    @Override
    public void deleteHousekeeper(Housekeeper target) {
        addressBook.removeHousekeeper(target);
    }

    @Override
    public void addClient(Client client) {
        addressBook.addClient(client);
        updateFilteredClientList(PREDICATE_SHOW_ALL_CLIENTS);
    }

    @Override
    public void addHousekeeper(Housekeeper housekeeper) {
        addressBook.addHousekeeper(housekeeper);
        updateFilteredHousekeeperList(PREDICATE_SHOW_ALL_HOUSEKEEPERS);
    }

    @Override
    public void setClient(Client target, Client editedClient) {
        requireAllNonNull(target, editedClient);

        addressBook.setClient(target, editedClient);
    }

    @Override
    public void setHousekeeper(Housekeeper target, Housekeeper editedHousekeeper) {
        requireAllNonNull(target, editedHousekeeper);

        addressBook.setHousekeeper(target, editedHousekeeper);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Client> getFilteredClientList() {
        return filteredClients;
    }

    @Override
    public ObservableList<Housekeeper> getFilteredHousekeeperList() {
        return filteredHousekeepers;
    }

    @Override
    public void updateFilteredClientList(Predicate<? extends Person> predicate) {
        requireNonNull(predicate);
        filteredClients.setPredicate((Predicate<? super Client>) predicate);
    }

    @Override
    public void updateAndSortFilteredClientList(Predicate<Client> predicate, Comparator<Client> comparator) {
        requireNonNull(predicate);
        addressBook.sortClients(comparator);
        filteredClients.setPredicate(predicate);
    }

    @Override
    public void updateFilteredHousekeeperList(Predicate<? extends Person> predicate) {
        requireNonNull(predicate);
        filteredHousekeepers.setPredicate((Predicate<? super Housekeeper>) predicate);
    }

    @Override
    public void updateFilteredHousekeeperListWithHousekeeperPredicate(Predicate<Housekeeper> housekeeperPredicate) {
        requireNonNull(housekeeperPredicate);
        filteredHousekeepers.setPredicate(housekeeperPredicate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredClients.equals(otherModelManager.filteredClients)
                && filteredHousekeepers.equals(otherModelManager.filteredHousekeepers);
    }
}
