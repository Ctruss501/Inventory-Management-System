package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
 * This is the controller for the addProduct.fxml
 */
public class addProduct implements Initializable {
    public TextField productNameTextField;
    public TextField productInvTextField;
    public TextField productPriceTextField;
    public TextField productMaxTextField;
    public TextField productMinTextField;

    public TextField searchPartTextField;

    public TableView<Part> partTableView;
    public TableColumn partIDTableColumn;
    public TableColumn partNameTableColumn;
    public TableColumn partInvTableColumn;
    public TableColumn partPriceTableColumn;
    public TableView<Part> associatedPartTableView;
    public TableColumn associatedPartIDTableColumn;
    public TableColumn associatedPartNameTableColumn;
    public TableColumn associatedPartInvTableColumn;
    public TableColumn associatedPartPriceTableColumn;

    ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**
     * Searches by part name first and if found, will return. If not found, will search for id next and return result.
     * Error will pop up if nothing is found, letting user know to start with capital letter.
     */
    public void searchPartOnEnter(ActionEvent actionEvent) {

        String q = searchPartTextField.getText();

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
     * Takes a selected part from the part table and moves it down to the associated part table.
     * RUNTIME ERROR: I couldn't get the associated part to save to a product. At first I had the add button just
     * sending a part over to the associated part table view and it wouldn't save to a product.
     * I brought over the observable list for associated parts, add the associated part, and then set the part in the table view.
     */
    public void addPartButtonOnAction(ActionEvent actionEvent) {

        Part selectedPart = partTableView.getSelectionModel().getSelectedItem();
        if(selectedPart != null)
            associatedParts.add(selectedPart);
        associatedPartTableView.setItems(associatedParts);
    }

    /**
     * Removes an associated part from the associated part table.
     */
    public void removeAssociatedPartButtonOnAction(ActionEvent actionEvent) {

        Part selectedAssociatedPart = associatedPartTableView.getSelectionModel().getSelectedItem();
        if(selectedAssociatedPart != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm");
            alert.setContentText("Are you sure you wish to remove this part from the product?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                associatedParts.remove(selectedAssociatedPart);
                associatedPartTableView.setItems(associatedParts);
            }
            if (result.get() == ButtonType.CANCEL) {
            }
        }
    }

    /**
     * This allows the user to add a product to the inventory. It will take the previously used Product ID and adds 1.
     * Once the user clicks the save button, they will be directed back to the main screen and see their new product.
     */
    public void saveButtonOnAction(ActionEvent actionEvent) throws IOException {

        int id = 0;
        for(int i = 0; i < Inventory.getAllParts().size(); i++) {
            if (id <= Inventory.getAllParts().get(i).getId())
                id = Inventory.getAllParts().get(i).getId() + 1;
        }
        try{
            String name = productNameTextField.getText();
            double price = Double.parseDouble(productPriceTextField.getText());
            int stock = Integer.parseInt(productInvTextField.getText());
            int min = Integer.parseInt(productMinTextField.getText());
            int max = Integer.parseInt(productMaxTextField.getText());

            if(stock < min || stock > max){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid");
                alert.setContentText("Inventory stock cannot be less than minimum and cannot exceed maximum.");
                alert.showAndWait();
                return;
            }

            Product newProduct = new Product(id, name, price, stock, min, max);
            newProduct.setId(id);
            newProduct.setName(name);
            newProduct.setPrice(price);
            newProduct.setMin(min);
            newProduct.setMax(max);
            Inventory.addProduct(newProduct);

            cancelButtonOnAction(actionEvent);

        } catch (Exception error){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid");
            alert.setContentText("Invalid Data Input");
            alert.showAndWait();
        }
    }

    /**
     *Cancel button on the Add Product screen will send you back to the main screen.
     */
    public void cancelButtonOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/mainScreen.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 850,355);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Setting the column values for the parts and associated parts tables on the Add Product screen.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        partTableView.setItems(Inventory.getAllParts());
        partIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvTableColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceTableColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        associatedPartIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartInvTableColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartPriceTableColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}
