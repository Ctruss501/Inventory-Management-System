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
 * This is the controller for the modifyProduct.fxml
 */
public class modifyProduct implements Initializable {
    public TextField productIDTextField;
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

    public int productIndex;

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

        if (selectedAssociatedPart != null) {
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
     * Save button on modify product screen will save the updates that you made to a product.
     * Once save is pressed, user will be sent back to the main screen where they will see the updated product
     * in the product table.
     */
    public void saveButtonOnAction(ActionEvent actionEvent) {

      try {
          int id = Integer.parseInt(productIDTextField.getText());
          String name = productNameTextField.getText();
          double price = Double.parseDouble(productPriceTextField.getText());
          int stock = Integer.parseInt(productInvTextField.getText());
          int max = Integer.parseInt(productMaxTextField.getText());
          int min = Integer.parseInt(productMinTextField.getText());

          if(stock < min || stock > max){
              Alert alert = new Alert(Alert.AlertType.ERROR);
              alert.setTitle("Invalid");
              alert.setContentText("Inventory stock cannot be less than minimum and cannot exceed maximum.");
              alert.showAndWait();
              return;
          }

          Product saveModifiedProduct = new Product(id, name, price, stock, min, max);
          saveModifiedProduct.setId(id);
          saveModifiedProduct.getAllAssociatedParts().clear();
          saveModifiedProduct.getAllAssociatedParts().addAll(associatedParts);
          Inventory.updateProduct(productIndex, saveModifiedProduct);

          cancelButtonOnAction(actionEvent);

      }catch(Exception error){
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Invalid");
          alert.setContentText("Invalid Data Input");
          alert.showAndWait();
      }
    }

    /**
     *Cancel button on the Modify Product screen will send you back to the main screen.
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
     * This will set the text fields and associated parts in the Modify Product screen with the data of the selected product
     * when the modify button is pressed on the on the main screen.
     */
    public void sendSP(Product selectedProduct){

        productIDTextField.setText(Integer.toString(selectedProduct.getId()));
        productNameTextField.setText(selectedProduct.getName());
        productPriceTextField.setText(Double.toString(selectedProduct.getPrice()));
        productInvTextField.setText(Integer.toString(selectedProduct.getStock()));
        productMaxTextField.setText(Integer.toString(selectedProduct.getMax()));
        productMinTextField.setText(Integer.toString(selectedProduct.getMin()));
        productIndex = Inventory.getAllProducts().indexOf(selectedProduct);
        associatedParts.addAll(selectedProduct.getAllAssociatedParts());
        associatedPartTableView.setItems(associatedParts);
    }

    /**
     * Setting the column values for the part and associated part tables on the Modify Product Screen.
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
