package json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import core.ShoppingList;
import java.io.IOException;

/**
 * Implementation of custom Jackson deserializer.
 */
public class ShoppingListDeserializer extends StdDeserializer<ShoppingList> {
    
    public ShoppingListDeserializer() {
        this(null);
    }

    public ShoppingListDeserializer(Class<ShoppingList> t) {
        super(t);
    }

    /**
     * Deserializes ShoppingList object in compliance with custom specification,
     * including JSON schema in /json/src/main/resources/json/schema.json.
     * Registered with Jackson ObjectMapper via SimpleModule().addDeserializer.
     * 
     * @return ShoppingList object
     * @throws IOException from com.fasterxml.jackson.databind.JsonNode
     */
    @Override
    public ShoppingList deserialize(JsonParser parser, DeserializationContext deserializer) throws IOException {
        
        // Create JSON node
        JsonNode json = parser.getCodec().readTree(parser);

        // Create ShoppingList object
        ShoppingList shoppingList = new ShoppingList();

        // Extract ShoppingList name from JSON node
        shoppingList.setName(json.get("name").asText());

        // Extract GroceryItems from JSON node
        for (JsonNode node : json.path("groceryItems")) {
            String groceryItem = node.get("item").asText();
            int groceryUnits = node.get("units").asInt();
            boolean checked = node.get("checked").asBoolean();
            shoppingList.addGroceryItem(groceryItem, groceryUnits, checked);
        }

        // Return ShoppingList object
        return shoppingList;
    }

}
