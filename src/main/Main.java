package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;
import java.util.Arrays;
import static controller.ControllerUtility.generateID;
import static model.Inventory.*;


 /*
 CREATED BY: Jeff Kleinaitis
 PRESENTATION: File titled, "Jeff Kleinaitis - C482 Presentation," can be found within the Test folder of the project.
 JAVADOC COMMENTS: Folder titled JavaDoc can be found within the Test folder of the project.
 ERRORS ADDRESSED: Please see the setPart method within the ModifyPartController class, and the onActionAddPart method within the MainController class for the error messages I ran into, and how I addressed them.
 FUTURE ENHANCEMENT: There are a few enhancements that I'd like to make to this project in the future. The main feature that this application is currently missing is the storage of inventory items. The program currently only creates a few test items within the Main file, but doesn't store any parts or products that are added, modified, or deleted on exit. Because of this, the application isn't useful in a real world sense. By integrating a database like MariaDB or MongoDB to work with some of the current features of the program, it would fix this issue and allow for the application to be used as an actual inventory management system. On top of adding a database to work with the application, adding user authentication and a login page to enter the program would be smart. This way, credentials can be setup for individual users and any changes to the database can be tracked back to the user if a problem occurs. @author Jeff Kleinaitis
 */

/** The main class creates an application for inventory management.*/
public class Main extends Application {


    /** The start method initializes fxml file Main.fxml
     *
     * @param primaryStage
     */
    @Override
    public void start (Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(new Scene(root, 1280, 400));
        primaryStage.show();

    }

    /** The main method loads test data and launches the application.
     *
     * @param args
     * */
    public static void main(String[] args) {

        for (InHouse inHouse : Arrays.asList(new InHouse(generateID(), "Custom Joy-Cons: Gold/Green", 99.99, 2, 1, 5, 1444), new InHouse(generateID(), "Custom Joy-Cons: Red/Blue", 99.99, 2, 1, 5, 1445))) {
            addPart(inHouse);
        }
        for (String string : Arrays.asList("Mario Kart 8", "The Legend of Zelda: Breath of the Wild")) {
            addPart(new Outsourced(generateID(), string, 59.99,3,1,5, "Nintendo"));
        }
        for (Outsourced outsourced : Arrays.asList(new Outsourced(generateID(), "Screen Protector", 9.99, 5, 5, 10, "Zagg"), new Outsourced(generateID(), "Electronics Screen Cleaner", 4.99, 5, 5, 10, "AliExpress"), new Outsourced(generateID(), "Blue Light Glasses", 34.99, 3, 1, 5, "AliExpress"))) {
            addPart(outsourced);
        }
        for (String string : Arrays.asList("Nintendo Zelda Bundle", "Nintendo Mario Bundle")) {
            addProduct(new Product(generateID(), string,159.99,2,1, 5));
        }
        addProduct(new Product(generateID(),"Accessories Bundle",64.99,3,1, 5));

        launch(args);

        }
    }
