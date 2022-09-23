package json;

import core.ShoppingList;
import core.GroceryItem;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for JacksconController class
 */
public class JacksonControllerTest {
    /********************************************************************
    * Run only JacksonControllerTest:
    * mvn test "-Dtest=json.JacksonControllerTest" -DfailIfNoTests=false
    *********************************************************************/

    private static boolean didDirExist;
    private JacksonController jackson;
    private ShoppingList shoppingList;
    private static ObjectMapper mapper;
    private static JsonNode schemaJsonNode;
    private static URL jsonSchemaFile = JacksonController.class.getResource("schema.json");
    private JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory.byDefault();

    /**
     * Sets boolean variable didDirExist before all tests.
     * If storage directory did not exist beforehand, it will be deleted after tests are run.
     */
    @BeforeAll
    public static void dirPathExists() {
        File pathAsFile = new File(JacksonController.pathToDir.toString());
        if(!Files.exists(Path.of(JacksonController.pathToDir.toString()))){
            pathAsFile.mkdir();
            didDirExist = false;
        } else {
            didDirExist = true;
        }
    }

    /**
     * Delete storage directory after tests if didDirExist is false.
     */
    @AfterAll
    public static void deleteDir() throws IOException {
        if (didDirExist == false) {
            Files.delete(JacksonController.pathToDir);
        }
    }

    /**
     * Creates JsonNode object containing our parsed JSON schema.
     * Used to later validate JSON schema itself and individual JSON files.
     * 
     * @throws IOException from com.fasterxml.jackson.databind
     */
    @BeforeAll
    public static void initSchemaJsonNode() throws IOException {
        mapper = new ObjectMapper();
        schemaJsonNode = mapper.readTree(jsonSchemaFile);
    }

    /**
     * Initializes new JacksonController and ShoppingList objects before each test.
     */
    @BeforeEach
    public void setup() {
        jackson = new JacksonController();
        shoppingList = new ShoppingList();
    }

    /**
     * Create hard-coded ShoppingList object and save to disk.
     * Assert that saved file is identical to hard-coded expectation.
     */
    @Test
    public void testWriteToDisk() throws IOException {
        
        // Populate ShoppingList object
        shoppingList.setName("writeToDiskTest");
        shoppingList.addGroceryItem("Chocolate");
        shoppingList.addGroceryItem("Ice cream", 5);
        shoppingList.addGroceryItem("More candy", 42, true);

        // Save ShoppingList object to JSON file
        jackson.save(shoppingList);

        // Check if file was created
        Path pathToFile = Path.of(JacksonController.pathToDir + File.separator + shoppingList.getName() + ".json");
        assertTrue(Files.exists(pathToFile), "JSON file was not created");
        
        // Set expected content of file
        String expectedString = "{\"name\":\"Writetodisktest\"," + 
                                "\"groceryItems\":" +
                                    "[{\"item\":\"Chocolate\",\"units\":1,\"checked\":false}," +
                                    "{\"item\":\"Ice cream\",\"units\":5,\"checked\":false}," +
                                    "{\"item\":\"More candy\",\"units\":42,\"checked\":true}]}";

        // Compare with actual content of file
        String readFromDisk = Files.readString(pathToFile);
        Assertions.assertEquals(expectedString, readFromDisk);

        // Delete JSON file
        jackson.delete(shoppingList.getName());

        // Verify that file was deleted
        assertFalse(Files.exists(pathToFile), "JSON test file was not deleted");
    }


    /**
     * Load ShoppingList object from hard-coded JSON file.
     * Assert attributes of ShoppingList object are identical to hard-coded expectations.
     */
    @Test
    public void testReadFromDisk() throws IOException {
        // Create valid JSON file
        String fileName = "Readfromdisktest";
        Path pathToFile = Path.of(JacksonController.pathToDir + File.separator + fileName + ".json");
        String jsonFile = "{\"name\":\"Readfromdisktest\"," + 
                            "\"groceryItems\":" +
                                "[{\"item\":\"Chocolate\",\"units\":1,\"checked\":false}," +
                                "{\"item\":\"Ice cream\",\"units\":5,\"checked\":false}," +
                                "{\"item\":\"More candy\",\"units\":42,\"checked\":true}]}";

        // Write String to disk as JSON file
        Files.write((pathToFile), jsonFile.getBytes());

        // Create ShoppingList object from file
        shoppingList = jackson.load(fileName);

        // Verify attributes of ShoppingList objects
        Assertions.assertEquals("Readfromdisktest", shoppingList.getName());
        
        List<GroceryItem> groceryList = shoppingList.getGroceryItems();
        Assertions.assertEquals(3, groceryList.size());
        Assertions.assertEquals("Chocolate", groceryList.get(0).getGroceryText());
        Assertions.assertEquals(1, groceryList.get(0).getGroceryUnits());
        Assertions.assertEquals(false, groceryList.get(0).isChecked());
        Assertions.assertEquals("More candy", groceryList.get(2).getGroceryText());
        Assertions.assertEquals(42, groceryList.get(2).getGroceryUnits());
        Assertions.assertEquals(true, groceryList.get(2).isChecked());

        // Delete JSON file
        Files.delete(pathToFile);
    }

    /**
     * Load ShoppingList object from hard-coded JSON file.
     * Assert attributes of ShoppingList object are identical to hard-coded expectations.
     */
    @Test
    public void testNameValidation() throws IOException {
        // Create list to store name of temporary files the test creates
        List<String> validFileNames = Arrays.asList("A", "Abc", "Family", "Æøå");
        List<String> invalidFileNames = Arrays.asList("?", "??", "", " ", ".dot", "test?", "../test", "test]fail");

        for (String fileName : validFileNames) {

            // Check if file exists (to avoid changing already existing files)
            if (!fileExists(fileName)) {

                // Create shoppingList with fileName and verify that an exception isn't thrown
                shoppingList.setName(fileName);
                assertDoesNotThrow(() -> jackson.save(shoppingList));

                // Verify that files are created and deleted on disk
                if (fileName.endsWith(".json")) {
                    assertTrue(Files.exists(Path.of(JacksonController.pathToDir + File.separator + fileName)), String.format("JSON file %s was not created", fileName));
                    jackson.delete(fileName);
                    assertFalse(Files.exists(Path.of(JacksonController.pathToDir + File.separator + fileName)), String.format("JSON file %s was not deleted", fileName));
                } else {
                    assertTrue(Files.exists(Path.of(JacksonController.pathToDir + File.separator + fileName + ".json")), String.format("JSON file %s was not created", fileName));
                    jackson.delete(fileName);
                    assertFalse(Files.exists(Path.of(JacksonController.pathToDir + File.separator + fileName + ".json")), String.format("JSON file %s was not deleted", fileName));
                }
            }
        }
        
        // Assert that invalid file names results causes IllegalArgumentException
        for (String fileName : invalidFileNames) {
            assertThrows(IllegalArgumentException.class, ()->{
                shoppingList.setName(fileName);
                jackson.save(shoppingList);
            });
        }     
    }


    /**
     * Helper function which checks whether given file name exists in storage directory.
     * 
     * @param fileName file name to check
     * @return <code>true</code> if file exists; <code>false</code> if file does not exists
     */
    public boolean fileExists(String fileName) {
        if (fileName.endsWith(".json")) {
            return Files.exists(Path.of(JacksonController.pathToDir.toString() + File.separator + fileName));
        } else {
            return Files.exists(Path.of(JacksonController.pathToDir.toString() + File.separator + fileName + ".json"));
        }
    }

    /**
     * Save a couple of ShoppingList objects to disk, and verify
     * that the method getShoppingLists() returns all of them
     */
    @Test
    public void testGetShoppingLists() throws IOException {
        List<String> testShoppingLists = Arrays.asList("Test-a", "Test-handleliste", "Test-liste");
        List<String> didExist = new ArrayList<>();

        // Save test lists unless they overwrite an existing list
        for (String shoppingListName : testShoppingLists) {
            if(fileExists(shoppingListName)) {
                didExist.add(shoppingListName);
            } else {
                shoppingList.setName(shoppingListName);
                jackson.save(shoppingList);
            }
        }

        // Call getShoppingLists()
        List<String> fileList = jackson.getShoppingLists();

        // Verify that getShoppingLists() returns expected elements
        for (String shoppingListName : testShoppingLists) {
            assertTrue(fileList.contains(shoppingListName));
        }

        // Only delete lists which didn't exist prior to the test
        for (String shoppingListName : testShoppingLists) {
            if (!didExist.contains(shoppingListName)) {
                jackson.delete(shoppingListName);
            }
        }
    }

    /**
     * Validates syntax of JSON schema
     * 
     */
    @Test
    public void testJsonSchemaIsValid() {
        // Create report of JSON schema
        ProcessingReport report = jsonSchemaFactory.getSyntaxValidator().validateSchema(schemaJsonNode);

        // Assert that schema is valid
        assertTrue(report.isSuccess(), report.toString());
  }

    /**
     * Verifies that the serializer outputs a JSON file that conforms to our JSON schema
     * 
     * @throws ProcessingException from com.github.fge.jsonschema
     * @throws JsonMappingException from com.fasterxml.jackson.databind
     * @throws JsonProcessingException from com.fasterxml.jackson.core
     * @throws IOException from java.nio.file.Files
     */
    @Test
    public void testSerializerConformsToSchema() throws ProcessingException, JsonMappingException, JsonProcessingException, IOException {
        // Load JSON schema
        JsonSchema jsonSchema = jsonSchemaFactory.getJsonSchema(schemaJsonNode);

        // Create ShoppingList object
        shoppingList.setName("JsonSchemaSerializerTest");
        shoppingList.addGroceryItem("Chocolate");
        shoppingList.addGroceryItem("Ice cream", 5);
        shoppingList.addGroceryItem("More candy", 42, true);
        shoppingList.addGroceryItem("Vegetables", 13, false);

        // Serialize ShoppingList object to JSON file
        jackson.save(shoppingList);

        // Create JsonNode of saved JSON file
        JsonNode jsonNode = mapper.readTree(Files.readString(Path.of(JacksonController.pathToDir + File.separator + shoppingList.getName() + ".json")));

        // Process file against JSON schema
        ProcessingReport report = jsonSchema.validate(jsonNode, true);  // deepCheck = true

        // Assert that file conforms to schema
        assertTrue(report.isSuccess(), report.toString());

        // Delete JSON file
        jackson.delete(shoppingList.getName());
    }

}