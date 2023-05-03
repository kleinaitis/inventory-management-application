package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static controller.ControllerUtility.*;

/** This creates the AddPartController class which is used to add parts to the inventory.
 */
public class AddPartController implements Initializable {

    @FXML private RadioButton InHouseRadioButton;
    @FXML private Label SourcedFromLabel;
    @FXML private TextField PartNameText;
    @FXML private TextField PartStockText;
    @FXML private TextField PartPriceText;
    @FXML private TextField SourcedFromText;
    @FXML private TextField PartMaxText;
    @FXML private TextField PartMinText;

    /** This method is for changing the text on both the SourcedFromLabel and the SourcedFromText to Company Name if the OutSourcedRadioButton is selected.
     *
     * @param ignoredActionEvent OutsourcedRadioButton is selected by the user
     */
    @FXML private void onActionOutsourcedRadio(ActionEvent ignoredActionEvent) {
        SourcedFromLabel.setText("Company Name");
        SourcedFromText.setPromptText("Company Name");
    }

    /** This method is for changing the text on both the SourcedFromLabel and the SourcedFromText to Machine ID if the InHouseRadioButton is selected.
     *
     * @param actionEvent InHouseRadioButton is selected by the user
     */
    @FXML private void onActionInHouseRadio(ActionEvent actionEvent) {
        SourcedFromLabel.setText("Machine ID");
        SourcedFromText.setPromptText("Machine ID");
    }

    /** This method returns the program to the Main.fxml screen. No information that was input is saved.
     *
     * @param actionEvent user clicks the Cancel button
     */
    @FXML
    private void onActionCancel(ActionEvent actionEvent) throws IOException {
        if (showConfirmationDialog("Cancel?", "Are you sure you want to cancel adding this part?")) {
            loadScene("/view/Main.fxml", "Main Menu", actionEvent);
        }
    }

    /** This method saves a new part to the inventory with user inputted values, and returns the user to the main screen if input validation succeeds.
     *
     * @param actionEvent Save button is pressed by the user
     */
    @FXML
    private void onActionSave(ActionEvent actionEvent) {
        try {
            boolean isInHouse = InHouseRadioButton.isSelected();
            String companyNameOrMachineID = SourcedFromText.getText();

            // validate that PartMinText, and PartMaxText are valid integers
            int minInventory, maxInventory;
            try {
                minInventory = Integer.parseInt(PartMinText.getText());
                maxInventory = Integer.parseInt(PartMaxText.getText());
            } catch (NumberFormatException e) {
                throw new Exception("Invalid numerical input. Check the Min and Max values.");
            }

            validateNameField(PartNameText.getText());
            validatePriceField(PartPriceText.getText());
            validateMinMaxFields(minInventory, maxInventory);
            validateSourceField(companyNameOrMachineID, isInHouse);
            validateInvField(PartStockText.getText());

            if (checkInventoryLevelWithinMinMax(Integer.parseInt(PartStockText.getText()), minInventory, maxInventory)) {
                createNewPart(PartNameText.getText(), Double.parseDouble(PartPriceText.getText()), Integer.parseInt(PartStockText.getText()), minInventory, maxInventory, isInHouse, companyNameOrMachineID);
                loadScene("/view/Main.fxml", "Main Menu", actionEvent);
            }
        } catch (Exception e) {
            showInformationDialog("Data Entry Error", "Part Not Added", e.getMessage());
        }
    }

    /**
     * This method initializes the AddPartController class.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}
}

