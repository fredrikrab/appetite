package ui;

import core.GroceryItem;
import core.ShoppingList;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import json.JacksonController;

public class ShoppingListController {

    private ShoppingList shoppingList;
    private JacksonController json = new JacksonController();

    @FXML
    Button inputButton;

    @FXML
    Label headLine;

    @FXML
    TextField itemInput;

    @FXML
    TextField numberOfItems;

    @FXML
    ListView<String> listOfItems;

    @FXML
    ListView<String> listOfNumberOfItems;

    @FXML
    Button removeButton;

    /**
     * Starts up the interface as the shoppinglist which is loaded from ListChooserController.
     *
    */
    @FXML
    private void initialize() { 
        itemInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                addGroceryToList();
            }
        });
    }

    /**
     * The specific file entered as input, shoppingListName, is uploaded to the interface.
     *
     * @param shoppingListName the name of the shoppingList that should be loaded
     * @throws IOException if json cant load the shoppingList
    */
    protected void loadShoppingList(String shoppingListName) throws IOException {
        if (!isValidText(shoppingListName)) {
            throw new IllegalArgumentException(
                String.format("isValidText() returned false for shoppingListName = %s", shoppingListName));
        }
        shoppingList = json.load(shoppingListName);
        updateBoard();
    }

    
    /**
     * Clears everything in the interface and uploads the contents of the file again; updates both list of items and list of number of items.
     *
    */
    private void updateBoard() { 
        if (headLine.getText().equals("Handleliste")) {
            headLine.setText(shoppingList.getName());
        }
        listOfItems.getItems().clear();
        listOfNumberOfItems.getItems().clear();
        for (GroceryItem item : shoppingList.getGroceryItems()) {
            listOfItems.getItems().add(item.getGroceryText());
            listOfNumberOfItems.getItems().add("" + item.getGroceryUnits());
        }
        itemInput.clear();
        numberOfItems.clear();
        try {
            save();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        
    }  

    /**
     * Adds an element to the loaded list and sets the number og items to 1 if the nuber is´nt specified.
     *
    */
    @FXML
    private void addGroceryToList() {
        if (isValidText(itemInput.getText())) {
            if (isValidNumberInput()) {
                shoppingList.addGroceryItem(itemInput.getText(), Integer.parseInt(numberOfItems.getText()));
            } else {
                shoppingList.addGroceryItem(itemInput.getText()); 
            }
            updateBoard();
        }
    }

    /**
     * Removes the item that has been selected in the shopping list.
     *
    */
    @FXML
    private void removeGroceryFromList() { 
        String groceryText = listOfItems.getSelectionModel().getSelectedItem();
        if (isValidText(groceryText)) {
            shoppingList.removeGroceryItem(groceryText);
            updateBoard();
        }
    }

    /**
     * Saves the elements in the list to the .json-file.
     * 
     * @throws IOException if json cant save the file
    */
    private void save() throws IOException {
        json.save(shoppingList);
    }
    
    /**
     * Checks whether the the text is valid.
     *
     * @param text the text that is input
    */
    private boolean isValidText(String text) {
        if (text == null) {
            return false;
        }
        if (text.isBlank()) {
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * Checks whether the number is a valid number.
     * 
    */
    private boolean isValidNumberInput() {
        String numberString = numberOfItems.getText();
        if (numberString == null) {
            return false;
        }
        try {
            int n = Integer.parseInt(numberString);
            if (n <= 0) {
                return false;
            } 
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
