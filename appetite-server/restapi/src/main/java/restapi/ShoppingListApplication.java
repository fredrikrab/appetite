package restapi;

import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The Spring application.
 */

@SpringBootApplication
public class ShoppingListApplication {
    
    /**
     * Starts up the REST-API.
     * 
     * @param args file name of ShoppingList object
     * @throws IOException if there is something wrong with the network file and user got disconnected
    */
    public static void main(String[] args) throws IOException {
        SpringApplication.run(ShoppingListApplication.class, args);
    }
}
