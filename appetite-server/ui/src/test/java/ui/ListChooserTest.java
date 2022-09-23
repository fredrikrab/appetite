package ui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import core.ShoppingList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import json.JacksonController;

public class ListChooserTest extends ApplicationTest{


    ListChooserController lstChooser;
    String saveString = "testfil.json"; //Denne blir lagret av testen til ShoppingListController test

    JacksonController json = new JacksonController();
    ShoppingList lst = new ShoppingList("testfil");

    @Override
    public void start(Stage stage) throws IOException {
        json.save(lst);
        FXMLLoader fxmlLoader = new FXMLLoader(ShoppingListApp.class.getResource("ListChooser.fxml"));
        Parent parent = fxmlLoader.load();
        this.lstChooser = fxmlLoader.getController();
        stage.setScene(new Scene(parent));
        stage.show();
    }

    @Test
    public void testNewButton() throws IOException{
        clickOn("#textInput").write("testNewFile");
        clickOn("#newList");
        ShoppingList lst = json.load("Testnewfile");
        assertTrue(lst.getGroceryItems().isEmpty());
        json.delete(lst.getName());
    }

    @Test
    public void testRemoveButton() {
        assertTrue(lstChooser.availableShoppingLists.getItems().contains("Testfil"));
        lstChooser.availableShoppingLists.getSelectionModel().select("Testfil");
        clickOn("#removeButton");
        assertFalse(lstChooser.availableShoppingLists.getItems().contains("Testfil"));
    }

}

