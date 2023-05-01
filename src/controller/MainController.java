package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static controller.ControllerUtility.*;

/** This creates the MainController class which is used on the Main screen of the program. The main screen is what allows the user to interact with parts and products within the inventory management system. */
public class MainController implements Initializable {

    // Parts table
    @FXML private TextField PartSearchBarText;
    @FXML private TableView<Part> PartTableView;
    @FXML private TableColumn<Part, Integer> PartIDColumn;
    @FXML private TableColumn<Part, String> PartNameColumn;
    @FXML private TableColumn<Part, Integer> PartStockColumn;
    @FXML private TableColumn<Part, Double> PartPriceColumn;

    // Products table
    @FXML private TextField ProductSearchBarText;
    @FXML private TableView<Product> ProductTableView;
    @FXML private TableColumn<Product, Integer> ProductIDColumn;
    @FXML private TableColumn<Product, String> ProductNameColumn;
    @FXML private TableColumn<Product, Integer> ProductStockColumn;
    @FXML private TableColumn<Product, Double> ProductPriceColumn;

    private Parent scene;

    /**
     * <p>
     * Error Addressed: java.lang.RuntimeException: java.lang.reflect.InvocationTargetException
     * Thoughts/How I Fixed It: This error was another silly mistake on my end. The error message was appearing almost immediately after re-naming the AddPartController. After I re-named the file, I tried to run the application again, and it worked, but the Add Part button wouldn't open the AddPart.fxml file anymore even though no other code had changed. If I undid the changes, it would run perfectly fine. After walking away from the code for a bit, I came back and realized that rather than using the IDE's refactor button to re-name the class, I manually did it through the File Explorer. The key to fixing this issue if I didn't use the refactor button built in the IDE to rename the class would have been to edit the fx:controller within the AddPart.fxml file. By re-naming it there, it would ensure that the controller and FXML file were linked up together properly. Using the IDE's built in refactor is the best way to do this though in general as it automatically takes care of updating this for you.
     * </p>
     * This method loads AddPart.fxml which allows the user to add a part to the inventory.
     *
     * @param event user clicks the Add button within the Parts section
     * @throws IOException
     */
    public void onActionAddPart(ActionEvent event) throws IOException {
        loadScene("/view/AddPart.fxml", "Add Part", event);
    }

    /** This method is used to the selected part from the inventory. After clicking Delete in the parts section, the user is asked to confirm that they want to delete the selected part before deleting.
     *
     * @param event user clicks the Delete button within the Parts section after selecting a part.
     */
    public void onActionDeletePart(ActionEvent event) {
        if (PartTableView.getSelectionModel().isEmpty()){
            showInformationDialog("Warning!", "No Part Selected","Select which part you'd like to delete and try again");
            return;
        }
        if (showConfirmationDialog("Warning!", "Are you sure you want to delete this part?")){
            Part selectedPart = PartTableView.getSelectionModel().getSelectedItem();
            Inventory.deletePart(selectedPart);
        }
    }

    /** This method loads ModifyPart.fxml which allows the user to modify the selected part within the inventory.
     *
     * @param event @param event user clicks the Modify button within the Parts section
     */
    public void onActionModifyPart(ActionEvent event){
        try {
            Part selectedPart = PartTableView.getSelectionModel().getSelectedItem();
            if (selectedPart == null) {
                showInformationDialog("Warning!", "No Part Selected","Select which part you'd like to modify and try again");
                return;
            }
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyPart.fxml"));
            scene = loader.load();
            ModifyPartController controller = loader.getController();
            controller.setPart(selectedPart);
            stage.setTitle("Modify Part");
            stage.setScene(new Scene(scene));
            stage.show();

        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to generate window", e);
        }
    }

    /** This method searches the parts inventory whenever text is input into the search bar within the parts section. */
    public void searchParts() {
        ControllerUtility.searchParts(PartSearchBarText, PartTableView);
    }

    /** This method loads AddProduct.fxml which allows the user to add a product to the inventory.
     *
     * @param event user clicks the Add button within the Products section
     * @throws IOException
     */
    @FXML public void onActionAddProduct(ActionEvent event) throws IOException {
        loadScene("/view/AddProduct.fxml", "Add Product", event);
    }

    /** This method is used to the selected product from the inventory. After clicking Delete in the products section, the user is asked to confirm that they want to delete the selected product before deleting.
     *
     * @param event user clicks the Delete button within the Products section after selecting a product.
     */
    public void onActionDeleteProduct(ActionEvent event) {

        if (ProductTableView.getSelectionModel().isEmpty()){
            showInformationDialog("Warning!", "No Product Selected","Select which product you'd like to delete and try again");
            return;
        }
            Product selectedProduct = ProductTableView.getSelectionModel().getSelectedItem();
            if (checkProductforAssociatedParts(selectedProduct)) {
                if (showConfirmationDialog("Warning!", "Are you sure you want to delete this product?")){
                Inventory.deleteProduct(selectedProduct);
            }
        }
    }

    /** This method loads ModifyProduct.fxml which allows the user to modify the selected product within the inventory.
     *
     * @param event user clicks the Modify button within the Products section
     */
    public void onActionModifyProduct(ActionEvent event){
        try {
            Product selectedProduct = ProductTableView.getSelectionModel().getSelectedItem();
            if (selectedProduct == null) {
                showInformationDialog("Warning!", "No Product Selected","Select which product you'd like to modify and try again");
                return;
            }
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyProduct.fxml"));
            scene = loader.load();
            ModifyProductController controller = loader.getController();
            controller.setProduct(selectedProduct);
            stage.setTitle("Modify Product");
            stage.setScene(new Scene(scene));
            stage.show();

        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to generate window", e);
        }
    }

    /** This method searches the products inventory whenever text is input into the search bar within the products section. */
    public void searchProducts() {
        ControllerUtility.searchProducts(ProductSearchBarText, ProductTableView);
    }

    /** This method exits the application.
     *
     * @param event user clicks the Exit button
     */
    @FXML public void onActionExitProgram(ActionEvent event) {
        if (showConfirmationDialog("Exit", "Are you sure you want to exit this program?")) {
            System.exit(0);
        }
    }

    /** This method sets up the table views within the Parts and Products sections, and adds a listener to the search bars of each section that searches for a match when text is input.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Sets up the parts table
        setUpTableView(PartTableView, PartIDColumn, PartNameColumn, PartStockColumn, PartPriceColumn, Inventory.getAllParts());

        // Sets up the products table
        setUpTableView(ProductTableView, ProductIDColumn, ProductNameColumn, ProductStockColumn, ProductPriceColumn, Inventory.getAllProducts());

        // Adds a listener to the PartsTableSearchText object which triggers a search for matching parts whenever the text input changes
        ProductSearchBarText.textProperty().addListener((observable, oldValue, newValue) -> searchProducts());

        // Adds a listener to the PartSearchBarText object which triggers a search for matching parts whenever the text input changes
        PartSearchBarText.textProperty().addListener((observable, oldValue, newValue) -> searchParts());
        }
}


