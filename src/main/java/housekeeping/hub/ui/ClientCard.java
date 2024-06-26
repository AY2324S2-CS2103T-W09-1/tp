package housekeeping.hub.ui;

import housekeeping.hub.model.person.Client;
import housekeeping.hub.model.person.HousekeepingDetails;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * An UI component that displays information of a {@code Client}.
 */
public class ClientCard extends PersonCard {
    private static final String FXML = "ClientListCard.fxml";
    @FXML
    private Label details;

    /**
     * Creates a {@code ClientCard} with the given {@code Client} and index to display.
     */
    public ClientCard(Client client, int displayedIndex) {
        super(client, displayedIndex, FXML);
        HousekeepingDetails housekeepingDetails = client.getDetails();
        if (housekeepingDetails == null) {
            details.setText(HousekeepingDetails.NO_DETAILS_PROVIDED);
        } else {
            details.setText(HousekeepingDetails.makeStoredDetailsReadable(housekeepingDetails.toString()));
        }
    }

}
