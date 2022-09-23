package ui;

import core.ShoppingList;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import json.JacksonController;

public class ListChooserController {

    private JacksonController json = new JacksonController();

    @FXML
    Button loadList;

    @FXML
    Button removeButton;

    @FXML
    Button newList;

    @FXML
    ListView<String> availableShoppingLists;

    @FXML 
    TextField textInput;

    /**
     * Initializes the app with the available saved lists. One can upload or the option to create a new shopping list.
     * @throws IOException if getShoppingLists cant read json
    */
    @FXML
    private void initialize() throws IOException {
        getShoppingLists(); 
        textInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    createShoppingList();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        availableShoppingLists.setOnMouseClicked(new javafx.event.EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        try {
                            openShoppingList();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }
    
    /**
     * Method to add previously saved lists to ListView availableShoppingLists.
     * 
     * @throws IOException if json cant get shoppingLists
    */
    private void getShoppingLists() throws IOException {
        availableShoppingLists.getItems().clear();
        for (String shoppingList : json.getShoppingLists()) { 
            availableShoppingLists.getItems().add(shoppingList);
        }
    } 

    /**
     * Creates a new, empty Shoppinglist-interface with the textInput as the name of the file.
     * 
     * @throws IOException if json cant save the newShoppingList
    */
    @FXML
    private void createShoppingList() throws IOException {
        if (textInput.getText().isBlank()) {
            return;
        }
        String shoppingListName = textInput.getText();
        ShoppingList newShoppingList = new ShoppingList(shoppingListName);
        json.save(newShoppingList);
        openShoppingList(shoppingListName);
    }

    /**
     * Allows you to remove a list that is available in the interface.
     *
     * @throws IOException if json cant delete shoppingList
    */
    @FXML
    private void removeShoppingList() throws IOException {
        MultipleSelectionModel<String> selectedShoppingList = availableShoppingLists.getSelectionModel();
        if (selectedShoppingList.isEmpty()) {
            return;
        }
        json.delete(selectedShoppingList.getSelectedItem());
        availableShoppingLists.getItems().remove(selectedShoppingList.getSelectedIndex());
    }

    /**
     * Opens the selected shoppinglist in the Shoppinglist-interface.
     *
     * @throws IOException if you openShoppingList(name) fails
    */
    @FXML
    private void openShoppingList() throws IOException {
        if (availableShoppingLists.getSelectionModel().isEmpty()) {
            return;
        }
        String shoppingListName = availableShoppingLists.getSelectionModel().getSelectedItem();
        openShoppingList(shoppingListName);
    }

    /**
     * With the text given in the input, the method will open the spesific shoppinglist in the Shoppinglist-interface. 
     *
     * @param shoppingListName the name of the shoppingList
     * @throws IOException if the shoppingList cant be loaded by json. 
    */
    private void openShoppingList(String shoppingListName) throws IOException {
        if (shoppingListName == null) {
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("ShoppingList.fxml"));
        Stage stage = (Stage) availableShoppingLists.getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load()));
        ShoppingListController controller = fxmlLoader.getController();
        controller.loadShoppingList(shoppingListName);
        stage.show();
    }
}
