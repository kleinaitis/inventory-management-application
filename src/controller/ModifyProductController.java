package controller;

import model.Product;
import model.Part;
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

/** This creates the ModifyPartController class which is used to modify fields on already existing products within the inventory. */
public class ModifyProductController implements Initializable {

    @FXML private TextField ProductNameText;
    @FXML private TextField ProductStockText;
    @FXML private TextField ProductIDText;
    @FXML private TextField ProductPriceText;
    @FXML private TextField ProductMaxText;
    @FXML private TextField ProductMinText;
    @FXML private TextField PartsTableSearchText;
    @FXML private TableView<Part> allPartsTableView;
    @FXML private TableColumn<Part, Integer> allPartsIDColumn;
    @FXML private TableColumn<Part, String> allPartsNameColumn;
    @FXML private TableColumn<Part, Integer> allPartsStockColumn;
    @FXML private TableColumn<Part, Double> allPartsPriceColumn;
    @FXML private TableView<Part> associatedPartsTableView;
    @FXML private TableColumn<Product, Integer> associatedPartsIDColumn;
    @FXML private TableColumn<Product, String> associatedPartsNameColumn;
    @FXML private TableColumn<Product, Integer> associatedPartsStockColumn;
    @FXML private TableColumn<Product, Double> associatedPartsPriceColumn;

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int productID;

    /** This method updates the allPartsTableView in the controller to reflect new parts added to the system */
    public void updatePartTable() {
        allPartsTableView.setItems(Inventory.getAllParts());
    }

    /** This method updates the associatedPartsTableView in the controller to reflect newly associated parts */
    private void updateAssociatedPartTable() {
        associatedPartsTableView.setItems(associatedParts);
    }

    /** This method searches the parts inventory whenever text is input into the search bar */
    public void searchParts() {
        ControllerUtility.searchParts(PartsTableSearchText, allPartsTableView);
    }

    /** This method adds the selected part from allPartsTableView to associatedParts, and updates associatedPartsTableView
     *
     * @param event user clicks the Add button
     */
    @FXML void onActionAddAssociatedPart(ActionEvent event) {
        Part selectedPart = allPartsTableView.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            associatedPartsTableView.getItems().add(selectedPart);
            updatePartTable();
            updateAssociatedPartTable();
        } else {
            // display an error message if no part is selected
            showInformationDialog("Error", "No part selected", "Please select a part to add.");
        }
    }

    /** This method returns the program to the Main.fxml screen. No information that was input is saved.
     *
     * @param event user clicks the Cancel button
     */
    @FXML
    public void onActionCancelModify(ActionEvent event) throws IOException {
        if (showConfirmationDialog("Cancel?", "Are you sure you want to cancel modifying this product?")) {
            loadScene("/view/Main.fxml", "Main Menu", event);
        }
    }

    /** This method takes the selected product from the main screen and updates the corresponding text fields on the ModifyProduct.fxml screen to display the product's details.
     *
     * @param selectedProduct product that was selected on main screen whenever Modify was clicked by the user
     */
    public void setProduct(Product selectedProduct) {
        productID = Inventory.getAllProducts().indexOf(selectedProduct);
        ProductIDText.setText(Integer.toString(selectedProduct.getId()));
        ProductNameText.setText(selectedProduct.getName());
        ProductStockText.setText(Integer.toString(selectedProduct.getStock()));
        ProductPriceText.setText(Double.toString(selectedProduct.getPrice()));
        ProductMaxText.setText(Integer.toString(selectedProduct.getMax()));
        ProductMinText.setText(Integer.toString(selectedProduct.getMin()));
        associatedParts.setAll(selectedProduct.getAllAssociatedParts());
    }

    /** This method removes associated part from the product's associated parts list and updates the table that shows the product's associated parts.
     *
     * @param event user clicks the Remove Associated Part button
     */
    @FXML
    void onActionRemoveAssociatedPart(ActionEvent event) {
        Part selectedPart = associatedPartsTableView.getSelectionModel().getSelectedItem();

        if (selectedPart != null) {
            if (showConfirmationDialog("Warning!", "Are you sure you want to remove this associated part? Select OK to continue.")) {
                associatedParts.remove(selectedPart);
                updateAssociatedPartTable();
            }
        }
    }

    /** This method updates the selected product with user inputs, saves the modified version of the product to the inventory, then returns the user to the main screen if input validation succeeds.
     *
     * @param event user clicks the Save button
     */
    @FXML
    void onActionSave(ActionEvent event) {
        try {
            int inventoryValue, minInventory, maxInventory;
            try {
                minInventory = Integer.parseInt(ProductMinText.getText());
                maxInventory = Integer.parseInt(ProductMaxText.getText());
            } catch (NumberFormatException e) {
                throw new Exception("Invalid input. Check the Min and Max values.");
            }

            validatePriceField(ProductPriceText.getText());
            validateNameField(ProductNameText.getText());
            validateInvField(ProductStockText.getText());
            validateMinMaxFields(minInventory, maxInventory);
            checkMinMaxInputs(minInventory, maxInventory);
            checkInventoryLevelWithinMinMax(Integer.parseInt(ProductStockText.getText()), minInventory, maxInventory);

            int id = Integer.parseInt(ProductIDText.getText());
            String name = ProductNameText.getText();
            inventoryValue = Integer.parseInt(ProductStockText.getText());

            if (checkInventoryLevelWithinMinMax(inventoryValue, minInventory, maxInventory)) {
                Product updatedProduct = new Product(id, name, Double.parseDouble(ProductPriceText.getText()), inventoryValue, minInventory, maxInventory);
                updatedProduct.getAllAssociatedParts().clear();
                updatedProduct.getAllAssociatedParts().addAll(associatedParts);
                Inventory.updateProduct(productID, updatedProduct);
                loadScene("/view/Main.fxml", "Main Menu", event);
            }
        } catch (Exception e) {
            showInformationDialog("Data Entry Error", "Product Not Added", e.getMessage());
        }
    }

    /** This method sets up the table views within the ModifyProductController and adds a listener to the search bar that searches for a match when text is input.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Sets up the all parts table view with the specified columns and data from the Inventory's getAllParts() method
        setUpTableView(allPartsTableView, allPartsIDColumn, allPartsNameColumn, allPartsStockColumn, allPartsPriceColumn, Inventory.getAllParts());
        // Sets up the associated parts table view with the specified columns and data from the onActionAddAssociatedPart() method
        setUpTableView(associatedPartsTableView, associatedPartsIDColumn, associatedPartsNameColumn, associatedPartsStockColumn, associatedPartsPriceColumn, associatedParts);

        updatePartTable();
        updateAssociatedPartTable();

        // Adds a listener to the PartsTableSearchText object which triggers a search for matching parts whenever the text input changes
        PartsTableSearchText.textProperty().addListener((observable, oldValue, newValue) -> searchParts());
    }
}