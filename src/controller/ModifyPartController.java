package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import model.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static controller.ControllerUtility.*;

/** This creates the ModifyPartController class which is used to modify fields on already existing parts within the inventory. */
public class ModifyPartController implements Initializable {

    @FXML private RadioButton InHouseRadioButton;
    @FXML private RadioButton OutsourcedRadioButton;
    @FXML private Label SourcedFromLabel;
    @FXML private TextField PartIDText;
    @FXML private TextField PartNameText;
    @FXML private TextField PartStockText;
    @FXML private TextField PartPriceText;
    @FXML private TextField SourcedFromText;
    @FXML private TextField PartMaxText;
    @FXML private TextField PartMinText;

    private int partID;

    /** This method is for changing the text on both the SourcedFromLabel and the SourcedFromText to Machine ID if the InHouseRadioButton is selected.
     *
     * @param actionEvent InHouseRadioButton is selected by the user
     */
    @FXML private void onActionInHouseRadio(ActionEvent actionEvent) {
        SourcedFromLabel.setText("Machine ID");
    }

    /** This method is for changing the text on both the SourcedFromLabel and the SourcedFromText to Company Name if the OutSourcedRadioButton is selected.
     *
     * @param actionEvent OutsourcedRadioButton is selected by the user
     */
    @FXML private void onActionOutsourcedRadio(ActionEvent actionEvent) {
        SourcedFromLabel.setText("Company Name");
    }

    /**
     * <p>
     * Error Addressed: java.lang.IndexOutOfBoundsException
     * Thoughts/How I Fixed It: I spent longer than I'd like to admit troubleshooting this error message, and I believe part of that was because I was attempting to fix it in a single spot rather than checking two spots of the code together. The error message was thrown whenever I used the onActionSaveModify method to save the changes that I made after modifying a part. It turns out that my setPart method and the updatePart method within the onActionSaveModifyPart method weren't working together properly. My original code for the setPart method had the line, "partID = Inventory.getAllProducts().indexOf(selectedPart);" in it. This code set the value of partID to the index of the selected part in the list of all products in the Inventory class. In the onActionSaveModify method, I was then using partID for this line of code, "Inventory.updatePart(id, updatedinhousepart);" While this looks like it should theoretically work, the updatePart method code was expecting the index of the part rather than id. At the time the code for id was, "int id = Integer.parseInt(idtxt.getText());". Fixing this was as simple as passing in partID rather than id in the updatePart method. I re-wrote parts of the method over and over and tried to figure it out without using the debugger as I didn't feel like I knew how to use it very well. This ended up being a great learning experience because with the help of the debugger, I was able to realize that the return value for partID was completely different then the return value for id.
     * </p>
     * This method takes the selected part from the main screen and updates the corresponding text fields on the ModifyPart.fxml screen to display the part's details.
     *
     * @param selectedPart part that was selected on main screen whenever Modify was clicked by the user
     */
    public void setPart(Part selectedPart) {
        partID = Inventory.getAllParts().indexOf(selectedPart);
        PartIDText.setText(Integer.toString(selectedPart.getId()));
        PartNameText.setText(selectedPart.getName());
        PartStockText.setText(Integer.toString(selectedPart.getStock()));
        PartPriceText.setText(Double.toString(selectedPart.getPrice()));
        PartMaxText.setText(Integer.toString(selectedPart.getMax()));
        PartMinText.setText(Integer.toString(selectedPart.getMin()));

        if (selectedPart instanceof InHouse inhouse) {
            InHouseRadioButton.setSelected(true);
            this.SourcedFromLabel.setText("Machine ID");
            SourcedFromText.setText(String.valueOf(inhouse.getMachineid())); }
        else {
            Outsourced outsourced = (Outsourced) selectedPart;
            OutsourcedRadioButton.setSelected(true);
            this.SourcedFromLabel.setText("Company Name");
            SourcedFromText.setText(String.valueOf(outsourced.getCompanyName()));
        }
    }

    /** This method updates the selected part with user inputs, saves the modified version of the part to the inventory, then returns the user to the main screen if input validation succeeds.
     *
     * @param actionEvent user clicks the Save button
     */
    @FXML
    private void onActionSaveModifyPart(ActionEvent actionEvent) {
        try {
            boolean isInHouse = InHouseRadioButton.isSelected();

            int inventoryValue, minInventory, maxInventory;
            try {
                minInventory = Integer.parseInt(PartMinText.getText());
                maxInventory = Integer.parseInt(PartMaxText.getText());
            } catch (NumberFormatException e) {
                throw new Exception("Invalid input. Check the Min and Max values.");
            }

            validatePriceField(PartPriceText.getText());
            validateNameField(PartNameText.getText());
            validateInvField(PartStockText.getText());
            validateMinMaxFields(minInventory, maxInventory);
            validateSourceField(SourcedFromText.getText(), isInHouse);
            checkMinMaxInputs(minInventory, maxInventory);
            checkInventoryLevelWithinMinMax(Integer.parseInt(PartStockText.getText()), minInventory, maxInventory);

            int id = Integer.parseInt(PartIDText.getText());
            String name = PartNameText.getText();
            String companyNameOrMachineID = SourcedFromText.getText();
            inventoryValue = Integer.parseInt(PartStockText.getText());

            if (isInHouse) {
                int machineid = Integer.parseInt(companyNameOrMachineID);
                InHouse updatedinhousepart = new InHouse(id, name, Double.parseDouble(PartPriceText.getText()), inventoryValue, minInventory, maxInventory, machineid);
                Inventory.updatePart(partID, updatedinhousepart);
                loadScene("/view/Main.fxml", "Main Menu", actionEvent);
                } else if (OutsourcedRadioButton.isSelected()) {
                Outsourced updatedoutsourcedpart = new Outsourced(id, name, Double.parseDouble(PartPriceText.getText()), inventoryValue, minInventory, maxInventory, companyNameOrMachineID);
                Inventory.updatePart(partID, updatedoutsourcedpart);
                loadScene("/view/Main.fxml", "Main Menu", actionEvent);
                }
        } catch (Exception e) {
            showInformationDialog("Data Entry Error", "Part Not Modified", e.getMessage());
        }
    }
    /**
     * This method returns the program to the Main screen. No information that was input is saved.
     *
     * @param actionEvent user clicks the Cancel button
     */
    @FXML
    private void onActionCancelModifyPart(ActionEvent actionEvent) throws IOException {
        if (showConfirmationDialog("Cancel?", "Are you sure you want to cancel modifying this part?")) {
            loadScene("/view/Main.fxml", "Main Menu", actionEvent);
        }
    }
    /** This method initializes the ModifyPartController class.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

}
