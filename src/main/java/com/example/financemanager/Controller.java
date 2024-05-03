package com.example.financemanager;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;


public class Controller {
    private double value;
    private double value2;
    @FXML
    private Label balance;

    @FXML
    protected void add() {
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
                if (secondValue <= 0) {
                    // Input is smaller than 0, repeat input
                    showAlert("Invalid input! Please enter a number greater than 0.");
                } else if (secondValue > value) {
                    // Perform addition
                    value += secondValue;
                    StringInputAdd(new Stage());
                } else {
                    // Perform addition
                    value += secondValue;
                    StringInputAdd(new Stage());
                }
            } catch (NumberFormatException e) {
                // Input is not a valid double, show error message and repeat input
                showAlert("Invalid input! Please enter a valid number.");
            }
        });
        balance.setText(String.valueOf(value));
    }
    private void StringInputAdd(Stage primaryStage) {
        // Create input dialog for string message
        TextInputDialog stringInputDialog = new TextInputDialog();
        stringInputDialog.setHeaderText(null);
        stringInputDialog.setTitle("Text");
        stringInputDialog.setContentText("Enter addition:");


        // Show string input dialog
        stringInputDialog.showAndWait().ifPresent(message -> {
            // Print the message
            System.out.println("Message: " + message);
            // Call the method to save to CSV
            saveToCSVPlus(value, message, LocalDateTime.now());
        });
    }
    private String operatorPlus;

    int index = 0;
    private void saveToCSVPlus(double secondInput, String text, LocalDateTime dateTime) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("output.csv", true))) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = dateTime.format(formatter);

            // Increment index by 1
            index++;

            // Use semicolons as separators instead of commas
            String line = String.format("%d;%.2f;%s;%s", index, secondInput, text, formattedDateTime);
            writer.println(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    protected void remove() {
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
                if (secondValue <= 0) {
                    // Second value is smaller than 0, repeat input
                    showAlert("Invalid input! Please enter a number greater than 0.");
                } else if (secondValue > value) {
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
                        StringInputRemove(new Stage());
                    }
                }
            } catch (NumberFormatException ex) {
                // Input is not a valid double, show error message and repeat input
                showAlert("Invalid input! Please enter a valid number.");
            }
        });
        balance.setText(String.valueOf(value));
    }
    private void StringInputRemove(Stage primaryStage) {
        // Create input dialog for string message
        TextInputDialog stringInputDialog = new TextInputDialog();
        stringInputDialog.setHeaderText(null);
        stringInputDialog.setTitle("Message");
        stringInputDialog.setContentText("Enter expenditure:");

        // Show string input dialog
        stringInputDialog.showAndWait().ifPresent(message -> {
            // Print the message
            System.out.println("Message: " + message);
            saveToCSVMinus(value2, message, LocalDateTime.now());
        });
    }
    private void saveToCSVMinus(double secondInput, String text, LocalDateTime dateTime) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("output.csv", true))) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = dateTime.format(formatter);

            index++;

            // Make the secondInput negative if it's positive
            if (secondInput > 0) {
                secondInput *= -1;
            }

            // Use semicolons as separators instead of commas
            String line = String.format("%d;%.2f;%s;%s", index, secondInput, text, formattedDateTime);
            writer.println(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void transactionHistory(){
        balance.setText("Button transaction history");
    }




    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    protected void clearHistory(){
        try {
            // Create FileWriter with append mode set to false
            FileWriter writer = new FileWriter("output.csv", false);
            // Close the writer to clear the content of the file
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
        }
        System.out.println("CSV file cleared");
    }
}