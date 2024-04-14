package com.example.financemanager;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Controller {
    private double value;
    private double value2;
    @FXML
    private Label balance;

    @FXML
    protected void addMoney() {
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setHeaderText(null);
        inputDialog.setTitle("Input");
        inputDialog.setContentText("Enter a number:");

        // Show input dialog
        inputDialog.showAndWait().ifPresent(input -> {
            // Validate input as double
            try {
                double inputValue = Double.parseDouble(input);
                // If it's the first input, store it
                if (value == 0.0) {
                    value = inputValue;
                } else {
                    // Add the input value to the first value
                    value += inputValue;
                }
            } catch (NumberFormatException ex) {
                // Input is not a valid double, show error message and repeat input
                showAlert("Invalid input! Please enter a valid number.");
            }
            balance.setText(String.valueOf(value));
        });
    }
    @FXML
    protected void setBallance(){
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setHeaderText(null);
        inputDialog.setTitle("Input");
        inputDialog.setContentText("Enter your text:");

        // Show input dialog
        inputDialog.showAndWait().ifPresent(input -> {
            // Validate input as double
            try {
                value = Double.parseDouble(input);
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
            balance.setText(String.valueOf(value));
        });
    }
    @FXML
    protected void sendMoney(){
        // Create input dialog
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setHeaderText(null);
        inputDialog.setTitle("Input");
        inputDialog.setContentText("Enter a number:");

        // Show input dialog
        inputDialog.showAndWait().ifPresent(input -> {
            // Validate input as double
            try {
                double secondValue = Double.parseDouble(input);
                if (secondValue > value) {
                    // Second value is greater than the first value, repeat input
                    showAlert("Second value cannot be greater than the first value.");
                } else {
                    // Perform subtraction
                    value -= secondValue;
                    // If result is negative, repeat the second input
                    if (value < 0) {
                        showAlert("Subtraction result is negative. Please enter a smaller value.");
                    } else {
                        // Ask for message
                        showStringInputDialog(new Stage());
                    }
                }
            } catch (NumberFormatException ex) {
                // Input is not a valid double, show error message and repeat input
                showAlert("Invalid input! Please enter a valid number.");
            }
        });
        balance.setText(String.valueOf(value));
    }

    private void showStringInputDialog(Stage primaryStage) {
        // Create input dialog for string message
        TextInputDialog stringInputDialog = new TextInputDialog();
        stringInputDialog.setHeaderText(null);
        stringInputDialog.setTitle("Message");
        stringInputDialog.setContentText("Enter a message:");

        // Show string input dialog
        stringInputDialog.showAndWait().ifPresent(message -> {
            // Print the message
            System.out.println("Message: " + message);
        });
    }

    @FXML
    protected void transactionHistory(){
        balance.setText("Button transaction history");
    }
    @FXML
    protected void removeMoney(){
        // Create input dialog
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setHeaderText(null);
        inputDialog.setTitle("Input");
        inputDialog.setContentText("Enter the second number:");

        // Show input dialog
        inputDialog.showAndWait().ifPresent(input -> {
            // Validate input as double
            try {
                value2 = Double.parseDouble(input);
                if (value2 > value) {
                    // Second value is greater than the first value, repeat input
                    showAlert("Second value cannot be greater than the first value.");
                } else {
                    // Perform subtraction
                    double result = value - value2;
                    System.out.println("Number: " + result);
                }
            } catch (NumberFormatException ex) {
                // Input is not a valid double, show error message and repeat input
                showAlert("Invalid input! Please enter a valid number.");
            }
        });
        balance.setText(String.valueOf(value));
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}