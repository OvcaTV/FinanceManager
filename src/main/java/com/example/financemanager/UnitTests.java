package com.example.financemanager;

import javafx.scene.control.TextInputDialog;

import java.time.LocalDateTime;
public class UnitTests {
    public static void main(String[] args) {
        UnitTests test = new UnitTests();
        test.testAddTransaction();
        test.testRemoveTransaction();
        test.testSaveToCSVPlus();
        test.testSaveToCSVMinus();
        test.testClearHistory();
    }


    public void testAddTransaction() {
        Controller controller = new Controller();
        controller.setValue(100.0);

        double secondValue = 100.0;
        String text = "Test Income";
        LocalDateTime dateTime = LocalDateTime.now();

        // Simulate user input
        UserInput userInput = new UserInput(String.valueOf(secondValue), text);


        // Assertions
        assertEquals(controller.getValue(), 100.0, "Balance should be updated correctly");
        assertNotNull(controller.balance, "Balance label should not be null");
        assertTrue(controller.getValue() > 0, "Value should be positive after adding income");
        assertFalse(controller.getValue() < 0, "Value should not be negative after adding income");
    }

    private class UserInput {
        private String numberInput;
        private String stringInput;

        public UserInput(String numberInput, String stringInput) {
            this.numberInput = numberInput;
            this.stringInput = stringInput;
        }

        public String getNumberInput() {
            return numberInput;
        }

        public String getStringInput() {
            return stringInput;
        }
    }

    public void testRemoveTransaction() {
        Controller controller = new Controller();
        controller.setValue(100.0);

        double secondValue = 50.0;
        String text = "Test Expense";
        LocalDateTime dateTime = LocalDateTime.now();

        // Simulate remove transaction
        controller.inputRemove(createDialogWithText(String.valueOf(secondValue)), createDialogWithText(text));

        // Assertions
        assertEquals(controller.getValue(), 50.0, "Balance should be updated correctly");
        assertTrue(controller.getValue() >= 0, "Value should not be negative after removing expense");
    }

    public void testSaveToCSVPlus() {
        Controller controller = new Controller();
        double secondInput = 150.0;
        String text = "Test Save Plus";
        LocalDateTime dateTime = LocalDateTime.now();

        // Save transaction
        controller.saveToCSVPlus(secondInput, text, dateTime);

        // Assertions
        assertNotEquals(controller.index, 0, "Index should be incremented");
    }

    public void testSaveToCSVMinus() {
        Controller controller = new Controller();
        double secondInput = -50.0;
        String text = "Test Save Minus";
        LocalDateTime dateTime = LocalDateTime.now();

        // Save transaction
        controller.saveToCSVMinus(secondInput, text, dateTime);

        // Assertions
        assertNotEquals(controller.index, 0, "Index should be incremented");
    }

    public void testClearHistory() {
        Controller controller = new Controller();

        // Simulate clearing history
        controller.clearHistory();

        // Assertions
        assertEquals(controller.getValue(), 0.0, "Balance should be reset after clearing history");
    }

    private TextInputDialog createDialogWithText(String text) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.getEditor().setText(text);
        return dialog;
    }

    // Assertion methods
    private void assertEquals(double actual, double expected, String message) {
        if (actual != expected) {
            throw new AssertionError(message + ": expected " + expected + " but was " + actual);
        }
    }

    private void assertNotEquals(int actual, int expected, String message) {
        if (actual == expected) {
            throw new AssertionError(message + ": expected not to be " + expected);
        }
    }

    private void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private void assertFalse(boolean condition, String message) {
        if (condition) {
            throw new AssertionError(message);
        }
    }

    private void assertNotNull(Object obj, String message) {
        if (obj == null) {
            throw new AssertionError(message);
        }
    }

}