package com.example.financemanager;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Controller {
    @FXML
    private Label welcomeText;

    @FXML
    protected void addMoney() {
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setHeaderText(null);
        inputDialog.setTitle("Input");
        inputDialog.setContentText("Enter your text:");

        // Show input dialog
        inputDialog.showAndWait().ifPresent(input -> {
            // Validate input as double
            try {
                double value = Double.parseDouble(input);
                // Create confirmation dialog
                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                confirmation.setHeaderText(null);
                confirmation.setContentText("Do you want to save this number?");
                ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
                confirmation.getButtonTypes().setAll(ButtonType.CANCEL, saveButton);

                // Show confirmation dialog
                confirmation.showAndWait().ifPresent(buttonType -> {
                    if (buttonType == saveButton) {
                        // Save the input (you can replace this with your saving logic)
                        System.out.println("Number saved: " + value);
                    }
                });
            } catch (NumberFormatException ex) {
                // Input is not a valid double, show error message and repeat input
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Invalid input! Please enter a valid number.");
                alert.showAndWait();
            }
        });
    }

    @FXML
    protected void setBallance(){
        welcomeText.setText("Button set ballance works");
    }
    @FXML
    protected void sendMoney(){
        welcomeText.setText("Button send money works");
    }
    @FXML
    protected void transactionHistory(){
        welcomeText.setText("Button transaction history");
    }
    @FXML
    protected void removeMoney(){
        welcomeText.setText("Button remove money");
    }

}