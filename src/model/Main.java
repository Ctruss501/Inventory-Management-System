package model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This is the Main class creates the inventory management system.
 *
 * Javadoc is in Inventory Management System > src > model > Javadoc
 */
public class Main extends Application {

    /**
     * Starts the application on the Inventory Management System main screen, mainScreen.fxml
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../view/mainScreen.fxml"));
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(root, 850, 355));
        primaryStage.show();
    }

    /**
     * Added test products and parts.
     * RUNTIME ERROR: When I first added parts and products to my main screen tables, I was able to have the parts populate
     * correctly but the products table just had a ton of rows but no data. Everything else in the program was running fine at the time.
     * I found that in my mainScreen.fxml the products tableview had a fixed cell size value and my parts tableview did not.
     * I deleted that value and my products were then able to populate into their table.
     */
    public static void addTestData(){

        InHouse part1 = new InHouse(1, "Case", 69.99, 5, 3, 20, 10);
        InHouse part2 = new InHouse(2, "Cables", 25.99, 3, 2, 15, 11);
        Outsourced part3 = new Outsourced(3, "Processor", 399.99, 2, 4, 18, "Company 1");
        Outsourced part4 = new Outsourced(4, "SSD", 129.99, 1, 1, 3, "Company 2");
        Inventory.addPart(part1);
        Inventory.addPart(part2);
        Inventory.addPart(part3);
        Inventory.addPart(part4);
        Product product1 = new Product(1, "Game PC", 1599.99, 2, 1, 5);
        Product product2 = new Product(2, "Casual PC", 799.99, 1, 1, 3);
        Inventory.addProduct(product1);
        Inventory.addProduct(product2);
        product1.addAssociatedPart(part1);
        product1.addAssociatedPart(part3);
        product2.addAssociatedPart(part2);
        product2.addAssociatedPart(part4);
    }


    public static void main(String[] args) {

        addTestData();

        launch(args);
    }
}
