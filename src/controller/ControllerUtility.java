package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;

/** This creates the ControllerUtility class which is used to store functions that are used throughout various class files */
public class ControllerUtility {

    /** This function sets up a TableView object by configuring columns and populating the data from an ObservableList.
     *
     * @param tableView the TableView in which is being setup
     * @param idCol the column in which the ID of each item will be displayed
     * @param nameCol the column in which the name of each item will be displayed
     * @param stockCol the column in which the stock of each item will be displayed
     * @param priceCol the column in which the price of each item will be displayed
     * @param data the data in which the items that will be displayed within the TableView come from
     */
    public static void setUpTableView(TableView tableView, TableColumn idCol, TableColumn nameCol, TableColumn stockCol, TableColumn priceCol, ObservableList data) {
        tableView.setItems(data);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /** This function searches for products based on the term entered in the search bar. It displays an error message if no products are found or sets the table items to the found products.
     *
     * @param searchBar the search bar the user uses to input their search term
     * @param productsTable the table that is being searched by the user
     */
    public static void searchProducts(TextField searchBar, TableView<Product> productsTable) {
        String term = searchBar.getText();
        ObservableList<Product> foundProducts = Inventory.lookupProduct(term);
        if (foundProducts.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("No Match Found");
            alert.setHeaderText("Unable to locate a product using: " + term);
            alert.showAndWait();
        } else {
            productsTable.setItems(foundProducts);
        }
    }

    /** This method searches for parts based on the term entered in the search bar. It displays an error message if no parts are found or sets the table items to the found parts.
     *
     * @param searchBar the search bar the user uses to input their search term
     * @param partsTable the table that is being searched by the user
     */
    public static void searchParts(TextField searchBar, TableView<Part> partsTable) {
        String term = searchBar.getText();
        ObservableList<Part> foundParts = Inventory.lookupPart(term);
        if (foundParts.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("No Match Found");
            alert.setHeaderText("Unable to locate a part using: " + term);
            alert.showAndWait();
        } else {
            partsTable.setItems(foundParts);
        }
    }


    /** This function displays a confirmation dialog box with the specified title and message.
     *
     * @param title title of the dialog box
     * @param message message that is displayed in the dialog box
     * @return a boolean indicating whether the user has clicked the OK button results in true, or the Cancel button which results in false.
     */
    static boolean showConfirmationDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText("Confirmation Required");
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    /** This function displays an information dialog box with the specified title, header, and message.
     *
     * @param title title of the dialog box
     * @param header the header text displayed in the dialog box
     * @param message the message displayed in the dialog box
     */
    static void showInformationDialog(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /** This function loads a new scene and sets it as the current scene in the window.
     *
     * @param location location of the FXML file to load
     * @param title title of the new window
     * @param actionEvent event that triggered the method
     * @throws IOException
     */
    public static void loadScene(String location, String title, ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(ControllerUtility.class.getResource(location));
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.show();
    }

    /** This function creates a new product with the specified name, price, stock, min, max, and associated parts, generates a unique ID, and then adds it to the inventory.
     *
     * @param name the name of the new product
     * @param price the price of the new product
     * @param stock the stock level of the new product
     * @param min the minimum allowed stock of the new product
     * @param max the maximum allowed stock of the new product
     * @param associatedParts the list of associated parts of the new product
     */
    public static void createNewProduct(String name, double price, int stock, int min, int max, ObservableList<Part> associatedParts) {
        Product newproduct = new Product();
        newproduct.setId(generateID());
        newproduct.setName(name);
        newproduct.setPrice(price);
        newproduct.setStock(stock);
        newproduct.setMin(min);
        newproduct.setMax(max);
        newproduct.getAllAssociatedParts().addAll(associatedParts);
        Inventory.addProduct(newproduct);
    }

    /** This function creates a new part with the specified name, price, stock, min, and max, generates a unique ID, and then adds it to the inventory.
     *
     * @param name the name of the new part
     * @param price the price of the new part
     * @param stock the stock level of the new part
     * @param min the minimum allowed stock of the new part
     * @param max the maximum allowed stock of the new part
     */
    public static void createNewPart(String name, double price, int stock, int min, int max, boolean isInHouse, String companyNameOrMachineID) {
        int id = generateID();
        Part newPart;

        if (isInHouse) {
            int machineID = Integer.parseInt(companyNameOrMachineID);
            newPart = new InHouse(id, name, price, stock, min, max, machineID);
        } else {
            newPart = new Outsourced(id, name, price, stock, min, max, companyNameOrMachineID);
        }

        Inventory.addPart(newPart);
    }

    /** This function generates a unique ID for a new part or product
     *
     * @return id that was generated by method
     */
    public static int generateID() {
        int id = 0;
        boolean idExists = true;
        while (idExists) {
            id = new Random().nextInt(1000000); // Generate a random integer between 0 and 999999
            idExists = false;
            for (Part part : Inventory.getAllParts()) {
                if (part.getId() == id) {
                    idExists = true;
                    break;
                }
            }
        }
        return id;
    }

    /** This function checks if the maximum value is less than the minimum value. If maximum value is less than the minimum value, it displays a dialog box and returns false.
     *
     * @param minValue the minimum allowed stock value
     * @param maxValue the maximum allowed stock value
     * @return true if the maximum value is greater than or equal to the minimum value, otherwise it indicates the input is invalid and returns false.
     */
    public static boolean checkMinMaxInputs(int minValue, int maxValue) {
        if (maxValue < minValue) {
            ControllerUtility.showInformationDialog("Input Error", "Error in Min or Max Input", "Minimum value is greater than maximum value");
            return false;
        } else {
            return true;
        }
    }

    /** This function checks if the value input for stock is within the minimum and maximum stock values. If stock value is not within the minimum and maximum range, it displays a dialog box and returns false.
     *
     * @param inventoryValue the value input for current stock level
     * @param minValue the minimum allowed stock level value
     * @param maxValue the maximum allowed stock level value
     * @return true if the inventoryValue is within the minimum and maximum range, otherwise it indicates the input is invalid and returns false.
     */
    public static boolean checkInventoryLevelWithinMinMax(int inventoryValue, int minValue, int maxValue) {
        if (inventoryValue > maxValue || inventoryValue < minValue) {
            ControllerUtility.showInformationDialog("Input Error", "Error in Inventory Input", "Inventory value is not within minimum-maximum range");
            return false;
        } else {
            return true;
        }
    }

    /** This function checks to see if a product has parts associated with it.
     *
     * @param selectedProduct the product that was selected
     * @return true if the product does not have parts associated, otherwise it indicates the product has parts associated with it and returns false.
     */
    public static boolean checkProductforAssociatedParts(Product selectedProduct) {
        if (!selectedProduct.getAllAssociatedParts().isEmpty()) {
            ControllerUtility.showInformationDialog("Error Deleting Product", "Cannot Delete Product", "Product has parts associated with it. All associated parts must be deleted before product can be deleted.");
            return false;
        }
        return true;
    }

    /** This function checks to see if the name field is empty.
     *
     * @param partName part that is being validated
     * @throws Exception
     */
    public static void validateNameField(String partName) throws Exception {
        if (partName.isEmpty()) {
            throw new Exception("Name value cannot be empty.");
        }
    }

    /** This function checks to see if the price field contains only valid number(s).
     *
     * @param partPriceStr the string of the price being validated
     * @throws Exception
     */
    public static void validatePriceField(String partPriceStr) throws Exception {
        if (!partPriceStr.matches("^\\d+(\\.\\d+)?$")) {
            throw new Exception("Price value must be a valid number.");
        }
    }

    /** This function checks to see if the Inv field contains a valid number(s).
     *
     * @param inventoryValue the string of the stock level being validated
     * @return returns true if the stock level is a valid number, otherwise it indicates the input is invalid and returns false.
     * @throws Exception
     */
    public static boolean validateInvField(String inventoryValue) throws Exception {
        if (!inventoryValue.matches("^\\d+$")) {
            throw new Exception("Invalid input. Check the Inv value.");
        }
        return true;
    }

    /** This function checks to make sure that the minimum and maximum stock levels are greater than 0.
     *
     * @param minInventory the minimum allowed stock level value
     * @param maxInventory the minimum allowed stock level value
     * @throws Exception
     */
    public static void validateMinMaxFields(int minInventory, int maxInventory) throws Exception {

            if (minInventory < 0 || maxInventory < 0) {
                throw new Exception("Min and max inventory values cannot be negative.");
            }
            if (minInventory > maxInventory) {
                throw new Exception("Min inventory value must be less than or equal to max inventory value.");
        }
    }

    /** This function checks the text field that contains either a Machine ID, or Company Name is valid, based on the value of isInHouse.
     *
     * @param companyNameOrMachineID the string of the company name or the machine ID being validated
     * @param isInHouse a boolean that indicates whether the part is an InHouse or Outsourced part
     * @throws Exception
     */
    public static void validateSourceField(String companyNameOrMachineID, boolean isInHouse) throws Exception {
        if (isInHouse) {
            if (!companyNameOrMachineID.matches("^\\d+$")) {
                throw new Exception("Machine ID must be a valid number.");
            }
        } else {
            if (!companyNameOrMachineID.matches("^[a-zA-Z\\s]+$")) {
                throw new Exception("Company name input must contain only letters and spaces.");
            }
        }
    }


}
