package json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import core.GroceryItem;
import core.ShoppingList;
import java.io.IOException;


/**
 * Implementation of custom Jackson serializer.
 */
public class ShoppingListSerializer extends StdSerializer<ShoppingList> {
    
    public ShoppingListSerializer() {
        this(null);
    }

    public ShoppingListSerializer(Class<ShoppingList> t) {
        super(t);
    }


    /**
     * Serializes ShoppingList object in compliance with custom specification,
     * including JSON schema in /json/src/main/resources/json/schema.json.
     * Registered with Jackson ObjectMapper via SimpleModule().addSserializer.
     * 
     * @param shoppingList ShoppingList object to serialize
     * @throws IOException from com.fasterxml.jackson.core.JsonGenerator
     */
    @Override
    public void serialize(ShoppingList shoppingList, JsonGenerator json, SerializerProvider serializer) throws IOException {

        // Create object and let first field be name of shoppingList
        json.writeStartObject();
        json.writeStringField("name", shoppingList.getName());

        // Create array for GroceryItem objects
        json.writeArrayFieldStart("groceryItems");

        // Create one object per groceryItem, which contains the associated attributes
        for (GroceryItem groceryItem : shoppingList.getGroceryItems()) {
            json.writeStartObject();
            json.writeStringField("item", groceryItem.getGroceryText());
            json.writeNumberField("units", groceryItem.getGroceryUnits());
            json.writeBooleanField("checked", groceryItem.isChecked());
            json.writeEndObject();
        }
        // End of GroceryItem array
        json.writeEndArray();

        // End of ShoppingList object
        json.writeEndObject();
    }
}