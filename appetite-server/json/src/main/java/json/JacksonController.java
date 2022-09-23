package json;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import core.ShoppingList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Class for JSON (de-)serialization of ShoppingList objects.
 * Includes methods to list and delete existing JSON files.
 */
public class JacksonController {

    // Path to storage directory
    static final Path pathToDir = Path.of(Path.of(System.getProperty("user.home")).toString(), "appetite-db");
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * Save ShoppingList to disk.
     * 
     * @param shoppingList the ShoppingList object
     * @throws IllegalArgumentException if shoppingList.getName() fails validation
     * @throws IOException from java.io.File, java.nio.file.Files
     */
    public void save(ShoppingList shoppingList) throws IOException {

        // Use ShoppingList name as file name (also confirm that file name is valid)
        final String fileName = validatedFileName(shoppingList.getName());

        // Create storage directory if it doesn't exist (ie. if app is running on new computer)
        Files.createDirectories(pathToDir);
        
        // Register cusom serializer module
        SimpleModule moduleSerializer = new SimpleModule("ShoppingListSerializer", new Version(1, 0, 0, "release1", "gr2158", "json"));
        moduleSerializer.addSerializer(ShoppingList.class, new ShoppingListSerializer());
        mapper.registerModule(moduleSerializer);

        // Save ShoppingList to file as JSON
        mapper.writeValue(new File(pathToDir.toString() + File.separator + fileName + ".json"), shoppingList);
    }


    /**
     * Load ShoppingList from disk.
     * 
     * @param loadFileName file name of ShoppingList object
     * @return ShoppingList object as created from file
     * @throws IOException from java.nio.file.Files
     */
    public ShoppingList load(String loadFileName) throws IOException {

        // Validate file name
        final String fileName = validatedFileName(loadFileName);

        // Throw FileNotFoundException if file does not exist
        if (!Files.exists(Path.of(pathToDir + File.separator + fileName + ".json"))) {
            throw new FileNotFoundException(String.format("Cannot load %s", fileName));
        }

        // Register custom deserializer module
        SimpleModule moduleDeserializer = new SimpleModule("ShoppingListDeserializer", new Version(1, 0, 0, "release1", "gr2158", "json"));
        moduleDeserializer.addDeserializer(ShoppingList.class, new ShoppingListDeserializer());
        mapper.registerModule(moduleDeserializer);

        // Create a ShoppingList object
        ShoppingList shoppingList;

        // Populate ShoppingList object from JSON file
        shoppingList = mapper.readValue(new File(pathToDir.toString() + File.separator + fileName + ".json"), ShoppingList.class);

        // Return ShoppingList object
        return shoppingList;
    }


    /**
     * Delete ShoppingList file from disk.
     * 
     * @param deleteFileName file name to delete
     * @throws FileNotFoundException if file name does not exist
     * @throws IOException from java.nio.file.Files 
     */ 
    public void delete(String deleteFileName) throws IOException {

        // Validate file name
        final String fileName = validatedFileName(deleteFileName);

        // Throw FileNotFoundException if file does not exist
        if (!Files.exists(Path.of(pathToDir + File.separator + fileName + ".json"))) {
            throw new FileNotFoundException(String.format("Cannot load %s", fileName));
        }

        // Delete file
        Files.delete(Path.of(JacksonController.pathToDir.toString() + File.separator + fileName + ".json"));
    }


    /**
     * Validates file names used by this module.
     *
     * @param fileName file name to be validated
     * @return String of validated file name
     * @throws IllegalArgumentException if unsuccessful validation
     */
    private String validatedFileName(String fileName) {
        if (fileName == null) {
            throw new IllegalArgumentException("Filename cannot be null");
        } else if (fileName.isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be empty");
        } else if (!fileName.substring(0, 1).matches("[a-zA-ZæøåÆØÅ]")) {
            throw new IllegalArgumentException(String.format("Illegal filename: %s", fileName));
        } else if (!fileName.matches("^[a-zA-ZæøåÆØÅ -]*$")) {
            throw new IllegalArgumentException(String.format("Illegal filename: %s", fileName));
        } else if (fileName.endsWith(".json")) {
            return  fileName.substring(0, 1).toUpperCase()
                    + fileName.substring(1, fileName.length() - 5).toLowerCase();
        } else {
            return fileName.substring(0, 1).toUpperCase()
                    + fileName.substring(1).toLowerCase();
        }
    }


    /**
     * Find and return exisiting ShoppingList objects.
     *
     * @return List of existing ShoppingList objects
     * @throws IOException from java.nio.file.Files 
     */
    public List<String> getShoppingLists() throws IOException {
        Files.createDirectories(pathToDir);                         // Create storage directory if it doesn't exist

        return Files.walk(pathToDir, 1)                             // Traverses pathToDir at depth=1 (no sub-folders) as Stream
            .map(file -> file.getFileName())                        // Get file names instead of absolute path
            .map(file -> file.toString())                           // Convert file names to String
            .filter(f -> Character.isUpperCase(f.charAt(0)))        // Require first character to be uppercase
            .filter(f -> f.endsWith(".json"))                       // Filter for .json files
            .map(file -> file.substring(0, file.length() - 5))      // Remove .json from filename
            .filter(f -> f.substring(1).matches("^[a-zæøå -]*$"))   // Require remaining letters to be lowercase, space or -
            .sorted()                                               // Sort by natural order
            .collect(Collectors.toList());                          // Collect resulting Stream as List
    }

}