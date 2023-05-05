package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static controller.ControllerUtility.*;
import static controller.ControllerUtility.setUpTableView;

/** This creates the AddProductController class which is used to add products to the inventory. */
public class AddProductController implements Initializable {

    @FXML private TableColumn<Part, Double> allPartsPriceColumn;
    @FXML private TableColumn<Part, Integer> allPartsStockColumn;
    @FXML private TableColumn<Part, String> allPartsNameColumn;
    @FXML private TableColumn<Part, Integer> allPartsIDColumn;
    @FXML private TableView<Part> associatedPartsTableView;
    @FXML private TableColumn<Product, Integer> associatedPartsPriceColumn;
    @FXML private TableColumn<Product, Double> associatedPartsStockColumn;
    @FXML private TableColumn<Product, String> associatedPartsNameColumn;
    @FXML private TableColumn<Product, Integer> associatedPartsIDColumn;
    @FXML private TableView<Part> allPartsTableView;
    @FXML private TextField PartsTableSearchText;
    @FXML private TextField ProductMinText;
    @FXML private TextField ProductMaxText;
    @FXML private TextField ProductPriceText;
    @FXML private TextField ProductStockText;
    @FXML private TextField ProductNameText;

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /** This method updates the allPartsTableView in the controller to reflect new parts added to the system */
    public void updatePartTable() {
        allPartsTableView.setItems(Inventory.getAllParts());
    }

    /** This method updates the associatedPartsTableView in the controller to reflect newly associated parts */
    private void updateAssociatedPartTable() {
        associatedPartsTableView.setItems(associatedParts);
    }

    /** This method searches the parts inventory whenever text is input into the search bar */
    @FXML
    public void searchParts() {
        ControllerUtility.searchParts(PartsTableSearchText, allPartsTableView);
    }

    /** This method adds the selected part from allPartsTableView to associatedParts, and updates associatedPartsTableView
     *
     * @param actionEvent user clicks the Add button
     */
    @FXML
    private void onActionAddAssociatedPart(ActionEvent actionEvent) {
        Part selectedItem = allPartsTableView.getSelectionModel().getSelectedItem();
        if(selectedItem != null) {
            associatedParts.add(selectedItem);
            updatePartTable();
            updateAssociatedPartTable();
        }
        else {
            showInformationDialog("No Parts Selected","Select a part", "Select a part to add to the Product");
        }
    }

    /** This method removes the selected associated part from the product being created
     *
     * @param actionEvent user clicks the Remove Associated Part button
     */
    @FXML
    private void onActionRemove(ActionEvent actionEvent) {
        Part selectedPart = associatedPartsTableView.getSelectionModel().getSelectedItem();

        if (selectedPart != null) {
            if (showConfirmationDialog("Warning!", "Are you sure you want to remove this associated part? Select OK to continue.")) {
                associatedParts.remove(selectedPart);
                updateAssociatedPartTable();
            }
        }
    }

    /** This method saves a new product to the inventory with user inputted values and associated parts, and returns the user to the main screen if input validation succeeds.
     *
     * @param actionEvent user clicks the Save button
     */
    @FXML
    private void onActionSave(ActionEvent actionEvent) {
        try {
            int minInventory, maxInventory;
            try {
                minInventory = Integer.parseInt(ProductMinText.getText());
                maxInventory = Integer.parseInt(ProductMaxText.getText());
            } catch (NumberFormatException e) {
                throw new Exception("Invalid input. Check the Min and Max values.");
            }

            validateNameField(ProductNameText.getText());
            validatePriceField(ProductPriceText.getText());
            validateMinMaxFields(minInventory, maxInventory);
            validateInvField(ProductStockText.getText());

            if (checkInventoryLevelWithinMinMax(Integer.parseInt(ProductStockText.getText()), minInventory, maxInventory)) {
                createNewProduct(ProductNameText.getText(), Double.parseDouble(ProductPriceText.getText()), Integer.parseInt(ProductStockText.getText()), minInventory, maxInventory, associatedParts);
                loadScene("/view/Main.fxml", "Main Menu", actionEvent);
            }
        } catch (Exception e) {
            showInformationDialog("Data Entry Error", "Product Not Added", e.getMessage());
        }
    }

    /** This method returns the program to the Main screen. No information that was input is saved.
     *
     * @param actionEvent user clicks the Cancel button
     */
    @FXML
    private void onActionCancel(ActionEvent actionEvent) throws IOException {
        if (showConfirmationDialog("Cancel?", "Are you sure you want to cancel adding this product?")) {
            loadScene("/view/Main.fxml", "Main Menu", actionEvent);
        }
    }

    /** This method sets up the table views within the AddProductController, and adds a listener to the search bar that searches for a match when text is input.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setUpTableView(allPartsTableView, allPartsIDColumn, allPartsNameColumn, allPartsStockColumn, allPartsPriceColumn, Inventory.getAllParts());

        setUpTableView(associatedPartsTableView, associatedPartsIDColumn, associatedPartsNameColumn, associatedPartsStockColumn, associatedPartsPriceColumn, associatedParts);

        // Adds a listener to the PartsTableSearchText object which triggers a search for matching parts whenever the text input changes
        PartsTableSearchText.textProperty().addListener((observable, oldValue, newValue) -> searchParts());
    }
}
