package restapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.GroceryItem;
import core.ShoppingList;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/shoppinglist")
public class ShoppingListController {  

    private ShoppingListService service = new ShoppingListService();
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * Responds to ping with HTTP Status 200.
     * 
     * @return HTTP Status 200 (OK)
     */
    @GetMapping("/ping")
    public HttpStatus respondToPing() {
        return HttpStatus.OK;
    }

    /**
     * Returns list of all exisiting ShoppingList names.
     * 
     * @return List of existing ShoppingList names
     */
    @GetMapping(path = "/getAll")
    public List<String> allShoppingLists() {
        return service.getAllShoppingListNames();
    }

    /**
     * Given name of ShoppingList, return corresponding ShoppingList object.
     * 
     * @param shoppingListName name of ShoppingList
     * @return ShoppingList object
     * @throws ResponseStatusException with HTTP Status 404 (Not found if ShoppingList was not found
     */
    @GetMapping("/get/{shoppingListName}")
    public ShoppingList getShoppingList(@PathVariable String shoppingListName) {

        ShoppingList shoppingList = null;

        try {
            shoppingList = service.getShoppingList(shoppingListName);
        } catch (Exception e) {
            if (e instanceof FileNotFoundException) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested shopping list was not found");
            } else {
                System.err.println(e);
            }
        }

        return shoppingList;
    }

    /**
     * Saves given ShoppingList as new ShoppingList.
     * Will not overwrite on conflict, unless the overwrite parameter is set.
     * 
     * @param newShoppingList new ShoppingList object to save
     * @param overwrite will overwrite existing ShoppingList if true. Set to false is not provided
     * @return HTTP Status 201 (Created)
     * @throws ResponseStatusException with HTTP Status 409 (Conflict) if conflict and overwrite not set to true
     * @throws ResponseStatusException with HTTP Status 500 (Internal Server Error) if otherwise unsuccessful
     */
    @PostMapping("/create")
    public HttpStatus createShoppingList(@RequestBody String requestBody, @RequestParam(required = false) boolean overwrite) {
        
        try {
            JsonNode jsonNode = mapper.readTree(requestBody);
            String shoppingListName = jsonNode.get("name").asText();

            if (overwrite) {
                // Set and save new ShoppingList (will overwrite ShoppingList with existing name)
                service.setShoppingList(new ShoppingList(shoppingListName));

                // Add GroceryItems to ShoppingList
                for (JsonNode node : jsonNode.path("groceryItems")) {
                    GroceryItem groceryItem = mapper.treeToValue(node, GroceryItem.class);
                    service.addGroceryItem(groceryItem);
                }

            // Create new ShoppingList. Will throw FileAlreadyExistsException if attempting overwrite
            } else {
                service.createShoppingList(shoppingListName);
            }

        } catch (FileAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Shopping list already exists");
        }
        catch (IOException e) {
            System.err.println(e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        } 

        return HttpStatus.CREATED;
    }

    /**
     * Delete ShoppingList from disk, identified by ShoppingList name.
     * 
     * @param requestBody HTTP Delete request with body containing field for shoppingListName
     * @return HTTP Status 200 (OK)
     * @throws ResponseStatusException with HTTP Status 404 (Not found) if ShoppingList was not found
     * @throws ResponseStatusException with HTTP Status 500 (Internal Server Error) if otherwise unsuccessful
     */
    @DeleteMapping("/delete")
    public HttpStatus deleteShoppingList(@RequestBody String requestBody) {

        try {
            JsonNode jsonNode = mapper.readTree(requestBody);
            String shoppingListName = jsonNode.get("name").asText();
            service.deleteShoppingList(shoppingListName);
        } catch (Exception e) {
            System.err.println(e);
            if (e instanceof FileNotFoundException) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shopping list not found");
            } else throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return HttpStatus.OK;
    }

    /**
     * Helper function which ensures that the correct ShoppingList object is set in the service.
     * 
     * @param shoppingListName name of ShoppingList
     * @throws FileNotFoundException if asked to load non-existent ShoppingList
     * @throws IOException from service.getShoppingList()
     */
    private void setShoppingList(String shoppingListName) throws FileNotFoundException, IOException {

        // Return if already initialized
        if (Objects.equals(service.getShoppingListName(), shoppingListName)) {
            return;
        }

        // Otherwise load specified ShoppingList to service
        ShoppingList shoppingList = service.getShoppingList(shoppingListName);
        service.setShoppingList(shoppingList);
    }



    /**
     * Add GroceryItem to specified ShoppingList.
     * 
     * @param grocery HTTP Put request with body containing GroceryItem object
     * @return HTTP Status 200 (OK)
     * @throws ResponseStatusException with HTTP Status 500 (Internal Server Error) if unsuccessful
     */
    @PutMapping("/{shoppingListName}/grocery/add")
    public HttpStatus addGrocery(@PathVariable String shoppingListName, @RequestBody GroceryItem grocery) {
        
        // Verify that correct ShoppingList is set
        try { 
            setShoppingList(shoppingListName);
        } catch (FileNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested shopping list was not found");
        } catch (IOException e) {
            System.err.println(e);
        }

        // Add GroceryItem
        try {
            service.addGroceryItem(grocery);
        } catch (IllegalStateException e) {
            System.err.println(e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return HttpStatus.OK;
    }

    /**
     * Removes GroceryItem from specified ShoppingList.
     * 
     * @param requestBody HTTP Delete request with body containing name (groceryText) of GroceryItem object
     * @return HTTP Status 200 (OK)
     * @throws ResponseStatusException with HTTP Status 500 (Internal Server Error) if unsuccessful
     */
    @DeleteMapping("/{shoppingListName}/grocery/remove")
    public HttpStatus removeGrocery(@PathVariable String shoppingListName, @RequestBody String requestBody) {

        // Verify that correct ShoppingList is set
        try { 
            setShoppingList(shoppingListName);
        } catch (FileNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested shopping list was not found");
        } catch (IOException e) {
            System.err.println(e);
        }

        // Remove GroceryItem 
        try {
            JsonNode jsonNode = mapper.readTree(requestBody);
            String groceryItemText = jsonNode.get("groceryText").asText();
            service.removeGroceryItem(groceryItemText);
        } catch (JsonProcessingException | IllegalStateException e) {
            System.err.println(e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return HttpStatus.OK;
    }
    
    /**
     * Toggles (checks or unchecks) GroceryItem in specified ShoppingList.
     * 
     * @param requestBody HTTP Put request with body containing groceryText and current checked status
     * @return HTTP Status 200 (OK)
     * @throws ResponseStatusException with HTTP Status 500 (Internal Server Error) if unsuccessful
     */
    @PutMapping("/{shoppingListName}/grocery/toggle")
    public HttpStatus toggleGrocery(@PathVariable String shoppingListName, @RequestBody String requestBody) {
        
        // Verify that correct ShoppingList is set
        try { 
            setShoppingList(shoppingListName);
        } catch (FileNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested shopping list was not found");
        } catch (IOException e) {
            System.err.println(e);
        }

        // Toggle GroceryItem
        try {
            JsonNode jsonNode = mapper.readTree(requestBody);
            String groceryItemText = jsonNode.get("groceryText").asText();
            boolean checked = jsonNode.get("checked").asBoolean();
            service.toggleGroceryItem(groceryItemText, checked);
        } catch (JsonProcessingException | IllegalStateException e) {
            System.err.println(e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return HttpStatus.OK;
    }

}
