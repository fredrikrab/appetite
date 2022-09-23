package restapi;

import core.GroceryItem;
import core.ShoppingList;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import json.JacksonController;
import org.springframework.stereotype.Service;

/**
 * Configures the ShoppingList service.
 * This class is used as the interface between ShoppingListController and 
 */

@Service
public class ShoppingListService {

    private ShoppingList shoppingListServiceObject;
    private JacksonController json = new JacksonController();

    /**
     * Initializes the service with the given ShoppingList.
     * 
     * @param shoppingList the ShoppingList object
     */

    public void setShoppingList(ShoppingList shoppingList) {

        // Save and load using (de)serializers. This ensures the integrity of inputted ShoppingList object.
        try {
            json.save(shoppingList);
            shoppingListServiceObject = json.load(shoppingList.getName());
        } catch (IOException e) {
            System.err.println(e);
        }

    }
    
    /**
     * Helper function to confirm that shoppingList is initialized.
     * 
     * @return true if ShoppingList is initialized, false otherwise
     */
    private boolean serviceIsInitialized() {
        if (shoppingListServiceObject != null) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * Returns list with the names of all existing ShoppingLists.
     * 
     * @return List of existing ShoppingLists
     */
    public List<String> getAllShoppingListNames() {

        // Initialize empty list
        List<String> allShoppingListNames = new ArrayList<String>();

        // Populate with all exisiting ShoppingList names by using method from the json module
        try {
            allShoppingListNames = json.getShoppingLists();
        } catch (IOException e) {
            System.err.println("Could not get list of ShoppingLists from disk: " + e);
        }
        
        // Return list
        return allShoppingListNames;
    }


    /**
     * Given a ShoppingList name, return the corresponding ShoppingList object from disk.
     * 
     * @param shoppingListName name of the ShoppingList object
     * @return the ShoppingList object
     * @throws FileNotFoundException if requested ShoppingList object does not exist
     * @throws IOException from json.load()
     */
    public ShoppingList getShoppingList(String shoppingListName) throws IOException {
        
        // Throw FileNotFoundException if ShoppingList does not exist
        if (!json.getShoppingLists().contains(shoppingListName)) {
            throw new FileNotFoundException();
        }

        // Return requested ShoppingList object
        return json.load(shoppingListName);
    }


    /**
     * Saves the current ShoppingList state to disk.
     * Creates a new file if current ShoppingList does exist (ie. has not been saved before).
     * Will overwrite (ie. update) if a ShoppingList with the same name exists.
     * Should be called after each update to an initialized ShoppingList.
     * 
     * @throws IllegalStateException if no ShoppingList is currently loaded
     */
    public void saveState() {

        // Check that ShoppingList is initialized
        if (!serviceIsInitialized()) {
            throw new IllegalStateException("Asked to save ShoppingList but no ShoppingList is initialized");
        }

        // Save ShoppingList
        try {
            json.save(shoppingListServiceObject);
        } catch (IOException e) {
            System.err.println("Could not save the current state: " + e);
        }
    }


    /**
     * Provides name of currently initialized ShoppingList.
     * 
     * @return name of initialized ShoppingList or null if ShoppingList is not initialized
     */

    public String getShoppingListName() {

        // Return null if no ShoppingList is initialized
        if (!serviceIsInitialized()) {
            return null;
        }

        // Return ShoppingList name as String
        return shoppingListServiceObject.getName();
    }


    /**
     * Saves new ShoppingList to disk. Will not accept ShoppingList with name that already exists.
     * 
     * @param shoppingList the ShoppingList object to save
     * @throws FileAlreadyExistsException if ShoppingList with same name exists.
     * @throws IOException from json.getShoppingLists()
     */
    public void createShoppingList(String shoppingListName) throws IOException {
        
        // Throw FileAlreadyExistsException if ShoppingList already exists
        if (json.getShoppingLists().contains(shoppingListName)) {
            throw new FileAlreadyExistsException("Cannot create new ShoppingList with the same name as an existing ShoppingList");
        }

        // Initialize new ShoppingList
        setShoppingList(new ShoppingList(shoppingListName));
    }


    /**
     * Deletes ShoppingList object from disk.
     * 
     * @param shoppingListName name of ShoppingList object
     * @throws FileNotFoundException if ShoppingList object is not previously saved
     * @throws IOException from json.delete()
     */
    public void deleteShoppingList(String shoppingListName) throws IOException {

        // Throw FileNotFoundException if ShoppingList does not exist
        if (!json.getShoppingLists().contains(shoppingListName)) {
            System.out.println(shoppingListName);
            throw new FileNotFoundException();
        }

        // Delete ShoppingList from disk
        json.delete(shoppingListName);
    }


    /**
     * Add GroceryItem to the current ShoppingList.
     * 
     * @param groceryItem the GroceryItem object
     * @throws IllegalStateException if no ShoppingList is currently loaded
     */
    public void addGroceryItem(GroceryItem groceryItem) throws IllegalStateException {

        // Check that ShoppingList is initialized
        if (!serviceIsInitialized()) {
            throw new IllegalStateException("Asked to add GroceryItem but no ShoppingList is initialized");
        }

        // Add GroceryItem to ShoppingList
        shoppingListServiceObject.addGroceryItem(groceryItem);

        // Save ShoppingList state to disk
        saveState();
    }


    /**
     * Remove GroceryItem, identified by name (ie. GroceryText), from current ShoppingList.
     * 
     * @param groceryItemName name of GroceryItem
     * @throws IllegalStateException if no ShoppingList is currently loaded
     */
    public void removeGroceryItem(String groceryItemName) throws IllegalStateException {

        // Check that ShoppingList is initialized
        if (!serviceIsInitialized()) {
            throw new IllegalStateException("Asked to remove GroceryItem but no ShoppingList is initialized");
        }

        // Remove GroceryItem
        shoppingListServiceObject.removeGroceryItem(groceryItemName);

        // Save ShoppingList state to disk
        saveState();
    }


    /**
     * Toggles GroceryItem, identified by name (ie. GroceryText), in current ShoppingList.
     * 
     * @param groceryItemName name of GroceryItem
     * @throws IllegalStateException if no ShoppingList is currently loaded
     */
    public void toggleGroceryItem(String groceryItemName, boolean checked) throws IllegalStateException {

        // Check that ShoppingList is initialized
        if (!serviceIsInitialized()) {
            throw new IllegalStateException("Asked to toggle GroceryItem but no ShoppingList is initialized");
        }

        // Toggle GroceryItem
        shoppingListServiceObject.toggleGroceryItem(groceryItemName);

        // Return error and fix state if client is out of sync with backend
        if (shoppingListServiceObject.getItem(groceryItemName).isChecked() != checked) {
            System.err.println("out of sync");
            shoppingListServiceObject.toggleGroceryItem(groceryItemName);
        }

        // Save ShoppingList state to disk
        saveState();
    }

}
