package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This is the controller for the addPart.fxml
 */

public class addPart implements Initializable {

    public RadioButton addPartInHouseRadio;
    public RadioButton addPartOutsourced;

    public TextField addPartIdTextField;
    public TextField addPartNameTextField;
    public TextField addPartInvTextField;
    public TextField addPartPriceTextField;
    public TextField addPartMaxTextField;
    public TextField addPartMachineIDCompanyNameTextField;
    public TextField addPartMinTextField;

    public Label addPartMachineIDCompanyName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     *Using the radio button for In-House will set text of the bottom label to Machine ID.
     */
    public void addPartInHouseOnAction(ActionEvent actionEvent) {

        addPartMachineIDCompanyName.setText("Machine ID");
    }

    /**
     *Using the radio button for Outsourced will set text of the bottom label to Company Name.
     */
    public void addPartOutsourcedOnAction(ActionEvent actionEvent) {
        addPartMachineIDCompanyName.setText("Company Name");
    }

    /**
     * This allows the user to add a part to the inventory. It will take the previously used Part ID and adds 1.
     * Once the user clicks the save button, they will be directed back to the main screen and see their new part.
     */
    public void addPartSaveOnAction(ActionEvent actionEvent) throws IOException {

        int id = 0;
        for(int i = 0; i < Inventory.getAllParts().size(); i++) {
            if (id <= Inventory.getAllParts().get(i).getId())
                id = Inventory.getAllParts().get(i).getId() + 1;
        }

        try{
            String name = addPartNameTextField.getText();
            double price = Double.parseDouble(addPartPriceTextField.getText());
            int stock = Integer.parseInt(addPartInvTextField.getText());
            int min = Integer.parseInt(addPartMinTextField.getText());
            int max = Integer.parseInt(addPartMaxTextField.getText());
            String companyName = addPartMachineIDCompanyNameTextField.getText();
            int machineId = Integer.parseInt(addPartMachineIDCompanyNameTextField.getText());

            if(stock < min || stock > max){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid");
                alert.setContentText("Inventory stock cannot be less than minimum and cannot exceed maximum.");
                alert.showAndWait();
                return;
            }

            if (addPartInHouseRadio.isSelected()) {
                InHouse part = new InHouse(id, name, price, stock, min, max, machineId);
                part.setId(id);
                part.setName(name);
                part.setPrice(price);
                part.setStock(stock);
                part.setMin(min);
                part.setMax(max);
                part.setMachineId(machineId);
                Inventory.addPart(part);
            } else if (addPartOutsourced.isSelected()) {
                Outsourced part = new Outsourced(id, name, price, stock, min, max, companyName);
                part.setId(id);
                part.setName(name);
                part.setPrice(price);
                part.setStock(stock);
                part.setMin(min);
                part.setMax(max);
                part.setCompanyName(companyName);
                Inventory.addPart(part);
            }

            addPartCancelOnAction(actionEvent);

        } catch(Exception error){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid");
            alert.setContentText("Invalid Data Input");
            alert.showAndWait();
        }
    }

    /**
     *Cancel button on the Add Part screen will send you back to the main screen.
     */
    public void addPartCancelOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/mainScreen.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 850,355);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }
}
