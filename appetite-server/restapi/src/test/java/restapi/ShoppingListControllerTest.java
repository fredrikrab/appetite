package restapi;



import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import core.GroceryItem;
import core.ShoppingList;
import json.JacksonController;


@WebMvcTest(controllers = ShoppingListController.class)
public class ShoppingListControllerTest {

    private JacksonController json = new JacksonController();
    private ObjectMapper mapper = new ObjectMapper();
    
	@Autowired
    private MockMvc mockMvc;

    @Test
    public void checkContent() throws Exception {
        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setName("Apitest");
        shoppingList.addGroceryItem("Chocolate", 1, false);
        shoppingList.addGroceryItem("Ice cream", 5, false);
        shoppingList.addGroceryItem("More candy", 42, true);
        json.save(shoppingList);
        
        Assertions.assertTrue(mockMvc.perform(MockMvcRequestBuilders
                                        .get("/shoppinglist/getAll"))
                                        .andExpect(MockMvcResultMatchers.status().isOk())
                                        .andReturn()
                                        .getResponse()
                                        .getContentAsString()
                                        .contains("Apitest"));
        
        String expectedString = "{\"name\":\"Apitest\"," + 
                                "\"groceryItems\":" +
                                    "[{\"groceryText\":\"Chocolate\",\"groceryUnits\":1,\"checked\":false}," +
                                    "{\"groceryText\":\"Ice cream\",\"groceryUnits\":5,\"checked\":false}," +
                                    "{\"groceryText\":\"More candy\",\"groceryUnits\":42,\"checked\":true}]}";

        Assertions.assertEquals(expectedString, mockMvc.perform(MockMvcRequestBuilders
                                                        .get("/shoppinglist/get/"+shoppingList.getName())
                                                        .accept(MediaType.APPLICATION_JSON))
                                                        .andExpect(MockMvcResultMatchers.status().isOk())
                                                        .andReturn()
                                                        .getResponse()
                                                        .getContentAsString());
                                                        
        Object item = new GroceryItem("Chocolate", 2, false);
        mockMvc.perform(MockMvcRequestBuilders.put("/shoppinglist/" + shoppingList.getName() + "/grocery/add")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(mapper.writeValueAsString(item)))
                                              .andExpect(MockMvcResultMatchers.status().isOk())
                                              .andReturn();
        shoppingList = json.load("Apitest");
        Assertions.assertEquals(3, shoppingList.getItem("Chocolate").getGroceryUnits());

        Assertions.assertFalse(shoppingList.getItem("Ice cream").isChecked());
        
        
        // I react så lagres endringene lokalt før de sendes, derfor gjør vi det også her.
        mockMvc.perform(MockMvcRequestBuilders.put("/shoppinglist/" + shoppingList.getName() + "/grocery/toggle")
                                              .content("{\"groceryText\":\"Ice cream\",\"groceryUnits\":5,\"checked\":true}"))
                                              .andExpect(MockMvcResultMatchers.status().isOk())
                                              .andReturn();

        
        Assertions.assertFalse(shoppingList.getItem("Ice cream").isChecked());
        shoppingList = json.load("Apitest");
        
        Assertions.assertTrue(shoppingList.getItem("Ice cream").isChecked());

        item = shoppingList.getItem("Ice cream");
        mockMvc.perform(MockMvcRequestBuilders.delete("/shoppinglist/" + shoppingList.getName() + "/grocery/remove")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(mapper.writeValueAsString(item)))
                                              .andExpect(MockMvcResultMatchers.status().isOk());

        Assertions.assertEquals(3, shoppingList.getGroceryItems().size());
        shoppingList = json.load("Apitest");
        Assertions.assertEquals(2, shoppingList.getGroceryItems().size());

        item = shoppingList.getItem("Chocolate");
        mockMvc.perform(MockMvcRequestBuilders.delete("/shoppinglist/" + shoppingList.getName() + "/grocery/remove")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(mapper.writeValueAsString(item)))
                                              .andExpect(MockMvcResultMatchers.status().isOk());

        item = shoppingList.getItem("More candy");
        mockMvc.perform(MockMvcRequestBuilders.delete("/shoppinglist/" + shoppingList.getName() + "/grocery/remove")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(mapper.writeValueAsString(item)))
                                              .andExpect(MockMvcResultMatchers.status().isOk());
                                              
        shoppingList = json.load("Apitest");
        Assertions.assertTrue(shoppingList.getGroceryItems().isEmpty());
        json.delete("Apitest");

        Assertions.assertFalse(mockMvc.perform(MockMvcRequestBuilders
                                        .get("/shoppinglist/getAll"))
                                        .andExpect(MockMvcResultMatchers.status().isOk())
                                        .andReturn()
                                        .getResponse()
                                        .getContentAsString()
                                        .contains("Apitest"));  

        shoppingList = new ShoppingList("Created with API");
        mockMvc.perform(MockMvcRequestBuilders.post("/shoppinglist/create")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(mapper.writeValueAsString(shoppingList)))
                                              .andExpect(MockMvcResultMatchers.status().isOk())
                                              .andReturn();

        Assertions.assertTrue(mockMvc.perform(MockMvcRequestBuilders
                                        .get("/shoppinglist/getAll"))
                                        .andExpect(MockMvcResultMatchers.status().isOk())
                                        .andReturn()
                                        .getResponse()
                                        .getContentAsString()
                                        .contains("Created with api"));

        mockMvc.perform(MockMvcRequestBuilders.delete("/shoppinglist/delete")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content("{\"name\":\"Created with api\"}"))
                                              .andExpect(MockMvcResultMatchers.status().isOk())
                                              .andReturn();

        Assertions.assertFalse(mockMvc.perform(MockMvcRequestBuilders
                                        .get("/shoppinglist/getAll"))
                                        .andExpect(MockMvcResultMatchers.status().isOk())
                                        .andReturn()
                                        .getResponse()
                                        .getContentAsString()
                                        .contains("Created with api"));
                                            
    }

    @Test
    public void checkExceptions() throws Exception {
        ShoppingList list = new ShoppingList("eksisterer-ikke");
        
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/shoppinglist/get/"+list.getName())
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isNotFound())
                        .andReturn();

        Object listAsObject = new ShoppingList("Eksisterer");

        mockMvc.perform(MockMvcRequestBuilders.post("/shoppinglist/create")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(mapper.writeValueAsString(listAsObject)))
                                              .andExpect(MockMvcResultMatchers.status().isOk())
                                              .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.post("/shoppinglist/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(listAsObject)))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.delete("/shoppinglist/delete")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content("{\"name\":\"Eksisterer\"}"))
                                              .andExpect(MockMvcResultMatchers.status().isOk())
                                              .andReturn();
        
        
        mockMvc.perform(MockMvcRequestBuilders.delete("/shoppinglist/delete")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content("null"))
                                              .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                                              .andReturn();
        
        mockMvc.perform(MockMvcRequestBuilders.delete("/shoppinglist/delete")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content("{\"name\":\"Eksisterer ikke\"}"))
                                              .andExpect(MockMvcResultMatchers.status().isNotFound())
                                              .andReturn();

        Object item = new GroceryItem("Chocolate", 2, false);
        mockMvc.perform(MockMvcRequestBuilders.put("/shoppinglist/grocery/add")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(mapper.writeValueAsString(item)))
                                              .andExpect(MockMvcResultMatchers.status().isNotFound())
                                              .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.delete("/shoppinglist/grocery/remove")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(mapper.writeValueAsString(item)))
                                              .andExpect(MockMvcResultMatchers.status().isNotFound())
                                              .andReturn();
        

        
        
        
    
    }
}
