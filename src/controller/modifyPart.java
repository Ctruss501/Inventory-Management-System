package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
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
import model.Part;

import java.io.IOException;

/**
 * This is the controller for the modifyPart.fxml
 */

public class modifyPart {
    public RadioButton modifyPartInHouse;
    public RadioButton modifyPartOutsourced;

    public TextField modifyPartIDTextField;
    public TextField modifyPartNameTextField;
    public TextField modifyPartInvTextField;
    public TextField modifyPartPriceTextField;
    public TextField modifyPartMaxTextField;
    public TextField modifyPartMachineIDCompanyNameTextField;
    public TextField modifyPartMinTextField;

    public Label modifyPartMachineIDCompanyName;

    public int partIndex;

    /**
     *Using the radio button for In-House will set text of the bottom label to Machine ID.
     */
    public void modifyPartInHouseOnAction(ActionEvent actionEvent) {

        modifyPartMachineIDCompanyName.setText("Machine ID");
    }

    /**
     *Using the radio button for Outsourced will set text of the bottom label to Company Name.
     */
    public void modifyPartOutsourcedOnAction(ActionEvent actionEvent) {

        modifyPartMachineIDCompanyName.setText("Company Name");
    }

    /**
     * Save button on modify part screen will save the updates that you made to a part.
     * Once save is pressed, user will be sent back to the main screen where they will see the updated part
     * in the part table.
     */
    public void modifyPartSaveOnAction(ActionEvent actionEvent) {

      try {
          int id = Integer.parseInt(modifyPartIDTextField.getText());
          String name = modifyPartNameTextField.getText();
          double price = Double.parseDouble(modifyPartPriceTextField.getText());
          int stock = Integer.parseInt(modifyPartInvTextField.getText());
          int max = Integer.parseInt(modifyPartMaxTextField.getText());
          int min = Integer.parseInt(modifyPartMinTextField.getText());


          if (stock < min || stock > max) {
              Alert alert = new Alert(Alert.AlertType.ERROR);
              alert.setTitle("Invalid");
              alert.setContentText("Inventory stock cannot be less than minimum and cannot exceed maximum.");
              alert.showAndWait();
              return;
          }
          if (modifyPartInHouse.isSelected()) {

              int machineID = Integer.parseInt(modifyPartMachineIDCompanyNameTextField.getText());
              InHouse saveModifiedPart = new InHouse(id, name, price, stock, min, max, machineID);
              Inventory.updatePart(partIndex, saveModifiedPart);

          } else if (modifyPartOutsourced.isSelected()) {

              String companyName = modifyPartMachineIDCompanyNameTextField.getText();
              Outsourced saveModifiedPart = new Outsourced(id, name, price, stock, min, max, companyName);
              Inventory.updatePart(partIndex,saveModifiedPart);

          }

          modifyPartCancelOnAction(actionEvent);

      }catch (Exception error){
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Invalid");
          alert.setContentText("Invalid Data Input");
          alert.showAndWait();
      }

    }

    /**
     * Cancel button on the Modify Part screen will send you back to the main screen.
     */
    public void modifyPartCancelOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/mainScreen.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 850,355);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This will set the text fields in the Modify Part screen with the data of the selected part
     * when the modify button is pressed on the on the main screen.
     */
    public void sendPart(Part selectedPart){

        modifyPartIDTextField.setText(Integer.toString(selectedPart.getId()));
        modifyPartNameTextField.setText(selectedPart.getName());
        modifyPartPriceTextField.setText(Double.toString(selectedPart.getPrice()));
        modifyPartInvTextField.setText(Integer.toString(selectedPart.getStock()));
        modifyPartMaxTextField.setText(Integer.toString(selectedPart.getMax()));
        modifyPartMinTextField.setText(Integer.toString(selectedPart.getMin()));
        partIndex = Inventory.getAllParts().indexOf(selectedPart);
        if(selectedPart instanceof InHouse){
            modifyPartInHouse.setSelected(true);
            modifyPartMachineIDCompanyName.setText("Machine ID");
            modifyPartMachineIDCompanyNameTextField.setText(Integer.toString(((InHouse) selectedPart).getMachineId()));
        }
        else if(selectedPart instanceof Outsourced){
            modifyPartOutsourced.setSelected(true);
            modifyPartMachineIDCompanyName.setText("Company Name");
            modifyPartMachineIDCompanyNameTextField.setText(((Outsourced)selectedPart).getCompanyName());
        }
    }
}
