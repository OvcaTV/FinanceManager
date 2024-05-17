package com.example.financemanager;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.BufferedReader;
import java.io.FileReader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.application.Platform;


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

        // String input field
        TextInputDialog stringInputDialog = new TextInputDialog();
        stringInputDialog.setHeaderText(null);
        stringInputDialog.setTitle("Input");
        stringInputDialog.setContentText("Enter your text:");
        gridPane.add(new Label("Text:"), 0, 1);
        gridPane.add(stringInputDialog.getEditor(), 1, 1);

        // Number input field
        TextInputDialog numberInputDialog = new TextInputDialog();
        numberInputDialog.setHeaderText(null);
        numberInputDialog.setTitle("Input");
        numberInputDialog.setContentText("Enter a number:");
        gridPane.add(new Label("Number:"), 0, 0);
        gridPane.add(numberInputDialog.getEditor(), 1, 0);

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
            // Update balance
            value += secondValue;
            balance.setText(String.valueOf(value));
            // Get string input
            String text = stringInputDialog.getEditor().getText();
            // Save to CSV
            saveToCSVPlus(secondValue, text, LocalDateTime.now());
        } catch (NumberFormatException e) {
            showAlert("Invalid input! Please enter a valid number.");
        }
    }

    int index = 0;
    private void saveToCSVPlus(double secondInput, String text, LocalDateTime dateTime) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("output.csv", true))) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = dateTime.format(formatter);

            // Increment index by 1
            index++;

            // Use semicolons as separators instead of commas
            String line = String.format("%s;%s;%s;%.2f", secondInput, text, formattedDateTime, value);
            writer.println(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void remove() {
        // Create a GridPane for input fields
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // String input field
        TextInputDialog stringInputDialog = new TextInputDialog();
        stringInputDialog.setHeaderText(null);
        stringInputDialog.setTitle("Input");
        stringInputDialog.setContentText("Enter expense:");
        gridPane.add(new Label("Expense:"), 0, 1);
        gridPane.add(stringInputDialog.getEditor(), 1, 1);

        // Number input field
        TextInputDialog numberInputDialog = new TextInputDialog();
        numberInputDialog.setHeaderText(null);
        numberInputDialog.setTitle("Input");
        numberInputDialog.setContentText("Enter a number:");
        gridPane.add(new Label("Number:"), 0, 0);
        gridPane.add(numberInputDialog.getEditor(), 1, 0);

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
            // Update balance
            value -= secondValue;
            balance.setText(String.valueOf(value));
            // Get string input
            String message = stringInputDialog.getEditor().getText();
            // Save to CSV
            saveToCSVMinus(secondValue, message, LocalDateTime.now());

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
            String line = String.format("%s;%s;%s;%.2f", secondInput, text, formattedDateTime, value);
            writer.println(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void transactionHistory(){
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);

        TableView<String[]> tableView = new TableView<>();

        // Read CSV file and populate TableView
        try {
            ObservableList<String[]> data = FXCollections.observableArrayList();
            BufferedReader reader = new BufferedReader(new FileReader("output.csv"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                data.add(parts);
            }
            tableView.getItems().addAll(data);
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        // Creating columns for TableView
        String[] columnNames = {"Value", "Reason", "Date, time", "Current money"};
        for (int i = 0; i < columnNames.length; i++) {
            final int index = i;
            TableColumn<String[], String> column = new TableColumn<>(columnNames[i]);
            column.setCellValueFactory(data -> {
                String[] rowData = data.getValue();
                return javafx.beans.binding.Bindings.createStringBinding(() -> rowData[index]);
            });
            tableView.getColumns().add(column);
        }

        VBox root = new VBox(10, tableView);
        Scene scene = new Scene(root, 350, 300);
        popupStage.setScene(scene);
        popupStage.setTitle("CSV Data");
        popupStage.showAndWait();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    protected void clearHistory(){
        // Create custom ButtonTypes for Yes and No
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");

        // Create the confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText(null);
        alert.setContentText("Do you really want to delete all transactions?");

        // Set the custom buttons on the alert
        alert.getButtonTypes().setAll(yesButton, noButton);

        // Display the alert and set the focus to the dialog itself
        Platform.runLater(() -> alert.getDialogPane().requestFocus());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == yesButton) {
            try {
                // Create FileWriter with append mode set to false
                FileWriter writer = new FileWriter("output.csv", false);
                // Close the writer to clear the content of the file
                writer.close();
                System.out.println("CSV file cleared");
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception (e.g., show an error message)
            }
        } else {
            // User chose "No" or closed the dialog
            System.out.println("Deletion cancelled");
        }
    }
}