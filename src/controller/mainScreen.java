package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;


import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This is the controller for the mainScreen.fxml
 */
public class mainScreen implements Initializable {

    public TextField searchPart;
    public TextField searchProduct;

    public TableView<Part> partTableView;
    public TableColumn partIDColumn;
    public TableColumn partNameColumn;
    public TableColumn partInvColumn;
    public TableColumn partPriceColumn;

    public TableView<Product> productTableView;
    public TableColumn productIDColumn;
    public TableColumn productNameColumn;
    public TableColumn productInvColumn;
    public TableColumn productPriceColumn;


    /**
     * Setting the column values for the parts and products tables on the main screen.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        partTableView.setItems(Inventory.getAllParts());
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTableView.setItems(Inventory.getAllProducts());
        productIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    /**
     * Closes the Inventory Management System.
     */
    public void exitButtonOnAction(ActionEvent actionEvent) {

        System.exit(0);
    }

    /**
     * Add button underneath the parts table will bring you to the Add Part screen.
     * FUTURE ENHANCEMENT: Create a "committed field" and make it so that whenever a part is associated with a product,
     * that field increases by 1. Then user will see how many parts are in stock and how many of that part
     * are committed to products.
     */
    public void addPartButtonOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/addPart.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 534, 530);
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Modify button underneath the parts table will bring you to the Modify Part screen.
     * The data for whichever part is selected will also be sent to, and populate the Modify Part screen.
     */
    public void modifyPartButtonOnAction(ActionEvent actionEvent) throws IOException {
        Part sp = partTableView.getSelectionModel().getSelectedItem();
        if (sp != null) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/view/modifyPart.fxml"));
            Parent scene = fxmlLoader.load();
            modifyPart modifyPartController = fxmlLoader.getController();
            modifyPartController.sendPart(sp);
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * Selecting a part and clicking delete button under port table will remove that selected part.
     * Confirmation box will pop up to make sure user wants to delete.
     * If no part is selected before clicking delete, popup will state that a selection needs to be made.
     */
    public void deletePartButtonOnAction(ActionEvent actionEvent) {

       Part selectedPart = partTableView.getSelectionModel().getSelectedItem();
       if(selectedPart != null) {
           Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
           alert.setTitle("Confirm");
           alert.setContentText("Do you wish to delete this part??");

           Optional<ButtonType> result = alert.showAndWait();

           if (result.get() == ButtonType.OK) {
               Inventory.deletePart(selectedPart);
               partTableView.setItems(Inventory.getAllParts());
               for(int i = 0; i < Inventory.getAllProducts().size(); i++) {
                  if(Inventory.getAllProducts().get(i).getAllAssociatedParts().contains(selectedPart)){
                      Inventory.getAllProducts().get(i).deleteAssociatedPart(selectedPart);
                  }
               }
           }
           if (result.get() == ButtonType.CANCEL) {
           }
       }

       else{
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setTitle("Invalid");
           alert.setContentText("Select the part you wish to delete.");
           alert.showAndWait();
          }


    }

    /**
     * Selecting a product and clicking delete button under product table will remove that selected product.
     * Confirmation box will pop up to make sure user wants to delete.
     * If no product is selected before clicking delete, popup will state that a selection needs to be made.
     */
    public void deleteProductButtonOnAction(ActionEvent actionEvent) {

        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();
        if(selectedProduct != null && selectedProduct.getAllAssociatedParts().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm");
            alert.setContentText("Are you sure you wish to delete?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                Inventory.deleteProduct(selectedProduct);
            }
            if (result.get() == ButtonType.CANCEL) {
            }
        }
        else if(selectedProduct != null && selectedProduct.getAllAssociatedParts().size() > 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid");
            alert.setContentText("This product had associated part(s) and cannot be deleted.");
            alert.showAndWait();
            return;
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid");
            alert.setContentText("Select the part you wish to delete.");
            alert.showAndWait();
        }
    }

    /**
     * Modify button underneath the products table will bring you to the Modify Product screen.
     * The data for whichever product is selected will also be sent to, and populate the Modify Product screen.
     */
    public void modifyProductButtonOnAction(ActionEvent actionEvent) throws IOException {
        Product sp = productTableView.getSelectionModel().getSelectedItem();
        if(sp != null){
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/view/modifyProduct.fxml"));
            Parent scene = fxmlLoader.load();
            modifyProduct modifyProductController = fxmlLoader.getController();
            modifyProductController.sendSP(sp);
            Stage stage = (Stage)((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * Add button underneath the products table will bring you to the Add Product screen.
     */
    public void addProductButtonOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/addProduct.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 778, 586);
        stage.setTitle("Add Product");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Searches by part name first and if found, will return. If not found, will search for id next and return result.
     * Error will pop up if nothing is found, letting user know to start with capital letter.
     * FUTURE ENHANCEMENT: Make it so when searching by name, you can use either capital or lower case letters
     */
    public void searchPartOnEnter(ActionEvent actionEvent) {

        String q = searchPart.getText();

        ObservableList<Part> parts = Inventory.lookupPart(q);

        if (parts.size() == 0) {

            try {
                int partId = Integer.parseInt(q);
                Part pn = Inventory.lookupPart(partId);
                if (pn != null)
                    parts.add(pn);
            } catch (Exception error) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid");
                alert.setContentText("No Part Found. Be sure to start name search with capital letter if looking for a specific part.");
                alert.showAndWait();
                return;
            }
        }

        partTableView.setItems(parts);
    }

    /**
     * Searched by product name first and if found, will return. If not found, will search for id next and return result.
     * Error will pop up if nothing is found, letting user know to start with capital letter.
     */
    public void searchProductOnEnter (ActionEvent actionEvent){

            String q = searchProduct.getText();

            ObservableList<Product> products = Inventory.lookupProduct(q);

        if (products.size() == 0) {

             try {
                int productId = Integer.parseInt(q);
                Product pn = Inventory.lookupProduct(productId);
                if (pn != null)
                    products.add(pn);
            } catch (Exception error) {
                 Alert alert = new Alert(Alert.AlertType.ERROR);
                 alert.setTitle("Invalid");
                 alert.setContentText("No Product Found. Be sure to start name search with capital letter if looking for a specific product.");
                 alert.showAndWait();
                 return;
            }
       }

       productTableView.setItems(products);
    }
}