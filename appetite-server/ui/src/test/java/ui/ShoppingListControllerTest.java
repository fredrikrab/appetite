package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import core.GroceryItem;
import core.ShoppingList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import json.JacksonController;

public class ShoppingListControllerTest extends ApplicationTest{


    ShoppingListController mainListController;
    String saveString = "Testfil";
    JacksonController json = new JacksonController();

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ShoppingListApp.class.getResource("ShoppingList.fxml"));
        Parent parent = fxmlLoader.load();
        this.mainListController = fxmlLoader.getController();
        ShoppingList lst = new ShoppingList(saveString);
        json.save(lst);
        this.mainListController.loadShoppingList(saveString);
        stage.setScene(new Scene(parent));
        stage.show();
    }
    
    public void removeAllItemsInList() throws IOException{ //hjelpefunksjon som fjerner alt i list
        ShoppingList savedList = json.load(saveString);
        for (GroceryItem item : savedList.getGroceryItems()) {
            System.out.println(item.getGroceryText());
            mainListController.listOfItems.getSelectionModel().select(item.getGroceryText());
            clickOn("#removeButton");
        }
    }
    

    @Test
    public void addGroceryItemTest() throws IOException{
        removeAllItemsInList();
        String input = "Sjokolade";
        clickOn("#itemInput").write(input); //legger til sjokolade i posisjon 0 i ListView og antallet skal være 1 siden det ikke blir spesifisert
        clickOn("#inputButton");
        assertEquals("Sjokolade", mainListController.listOfItems.getItems().get(0)); //sjekker at inputen ble skrevet og lagt riktig
        assertEquals("1", mainListController.listOfNumberOfItems.getItems().get(0));
        String input2 = "Sjokomelk";
        clickOn("#itemInput").write(input2);
        String inputNum = "4";
        clickOn("#numberOfItems").write(inputNum); 
        clickOn("#inputButton");
        assertEquals("Sjokomelk", mainListController.listOfItems.getItems().get(1));
        assertEquals("4", mainListController.listOfNumberOfItems.getItems().get(1)); //sjekker at antall ble lagt inn riktig
    }

    @Test
    public void removeGroceryItem() throws IOException{ //teste knappen som fjerner items
        removeAllItemsInList();
        String input = "Sjokolade";
        clickOn("#itemInput").write(input);
        clickOn("#inputButton");
        String input2 = "Iskrem";
        clickOn("#itemInput").write(input2);
        clickOn("#inputButton");
        mainListController.listOfItems.getSelectionModel().select(input); //fjerner sjokolade, da står Iskrem igjen. 
        clickOn("#removeButton");
        assertEquals(input2, mainListController.listOfItems.getItems().get(0));
        String input3 = "Saltstenger"; //index 1 i listen
        clickOn("#itemInput").write(input3);
        clickOn("#inputButton");
        String input4 = "Burger"; //index 2 i listen
        clickOn("#itemInput").write(input4);
        clickOn("#inputButton");
        mainListController.listOfItems.getSelectionModel().select("Saltstenger"); //fjerner fra index 1, da står burger igjen
        clickOn("#removeButton");
        assertNotEquals(input3, mainListController.listOfItems.getItems().get(1));
        json.delete(saveString);
    }

}