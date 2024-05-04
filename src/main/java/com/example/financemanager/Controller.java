package com.example.financemanager;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;

public class Controller {
    private double value;
    private double value2;
    @FXML
    private Label balance;

    @FXML
    protected void add() {
        // Create a GridPane for input fields
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Number input field
        TextInputDialog numberInputDialog = new TextInputDialog();
        numberInputDialog.setHeaderText(null);
        numberInputDialog.setTitle("Input");
        numberInputDialog.setContentText("Enter a number:");
        gridPane.add(new Label("Number:"), 0, 0);
        gridPane.add(numberInputDialog.getEditor(), 1, 0);

        // String input field
        TextInputDialog stringInputDialog = new TextInputDialog();
        stringInputDialog.setHeaderText(null);
        stringInputDialog.setTitle("Input");
        stringInputDialog.setContentText("Enter your text:");
        gridPane.add(new Label("Text:"), 0, 1);
        gridPane.add(stringInputDialog.getEditor(), 1, 1);

        // Create a dialog to contain the GridPane
        Alert dialog = new Alert(Alert.AlertType.NONE);
        dialog.getDialogPane().setContent(gridPane);
        dialog.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Show the dialog and wait for the user's response
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // OK button was clicked, process the inputs
            inputProcessAdd(numberInputDialog, stringInputDialog);
        }
    }


    private void inputProcessAdd(TextInputDialog numberInputDialog, TextInputDialog stringInputDialog) {
        // Validate number input
        try {
            double secondValue = Double.parseDouble(numberInputDialog.getEditor().getText());
            if (secondValue <= 0) {
                showAlert("Invalid input! Please enter a number greater than 0.");
                return;
            }
            // Get string input
            String text = stringInputDialog.getEditor().getText();
            // Save to CSV
            saveToCSVPlus(secondValue, text, LocalDateTime.now());
            // Update balance
            value += secondValue;
            balance.setText(String.valueOf(value));
        } catch (NumberFormatException e) {
            showAlert("Invalid input! Please enter a valid number.");
        }
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
        // Create a GridPane for input fields
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Number input field
        TextInputDialog numberInputDialog = new TextInputDialog();
        numberInputDialog.setHeaderText(null);
        numberInputDialog.setTitle("Input");
        numberInputDialog.setContentText("Enter a number:");
        gridPane.add(new Label("Number:"), 0, 0);
        gridPane.add(numberInputDialog.getEditor(), 1, 0);

        // String input field
        TextInputDialog stringInputDialog = new TextInputDialog();
        stringInputDialog.setHeaderText(null);
        stringInputDialog.setTitle("Input");
        stringInputDialog.setContentText("Enter expenditure:");
        gridPane.add(new Label("Expenditure:"), 0, 1);
        gridPane.add(stringInputDialog.getEditor(), 1, 1);

        // Create a dialog to contain the GridPane
        Alert dialog = new Alert(Alert.AlertType.NONE);
        dialog.getDialogPane().setContent(gridPane);
        dialog.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Show the dialog and wait for the user's response
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // OK button was clicked, process the inputs
            inputProcessRemove(numberInputDialog, stringInputDialog);        }
    }
    private void inputProcessRemove(TextInputDialog numberInputDialog, TextInputDialog stringInputDialog) {
        // Validate number input
        try {
            double secondValue = Double.parseDouble(numberInputDialog.getEditor().getText());
            if (secondValue <= 0) {
                showAlert("Invalid input! Please enter a number greater than 0.");
                return;
            }
            if (secondValue > value) {
                showAlert("Second value cannot be greater than the first value.");
                // Repeat the input dialog
                remove();
                return;
            }
            // Get string input
            String message = stringInputDialog.getEditor().getText();
            // Save to CSV
            saveToCSVMinus(secondValue, message, LocalDateTime.now());
            // Update balance
            value -= secondValue;
            balance.setText(String.valueOf(value));
        } catch (NumberFormatException e) {
            showAlert("Invalid input! Please enter a valid number.");
        }
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