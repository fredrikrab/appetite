package core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

public class ShoppingListTest {
   

    private ShoppingList handleliste;

    @BeforeEach
    public void setup(){
        this.handleliste = new ShoppingList("Test shopping");
    }

    @Test
    public void testConstructor(){
        Assertions.assertEquals("Test shopping", handleliste.getName());
        Assertions.assertTrue(handleliste.getGroceryItems().isEmpty());
        handleliste.setName("Øl og Pølse");
        Assertions.assertEquals("Øl og pølse", handleliste.getName());  // Første bokstav stor, resten små
    }

    @Test
    public void testAddRemove(){
        handleliste.addGroceryItem("Banan");
        List<GroceryItem> liste = handleliste.getGroceryItems();
        Assertions.assertEquals("Banan", liste.get(0).getGroceryText());
        Assertions.assertEquals(1, liste.get(0).getGroceryUnits());
        handleliste.addGroceryItem("baNaN");
        Assertions.assertEquals(1, liste.size()); 
        Assertions.assertEquals("Banan", liste.get(0).getGroceryText());
        Assertions.assertEquals(2, liste.get(0).getGroceryUnits()); //Listene er forskjellige, men peker på samme objekt, derfor går denne igjennom
        handleliste.addGroceryItem("ØL", 12);
        Assertions.assertEquals(1, liste.size()); //Kopien liste skal ikke være det samme som lista inne i ShoppingList nå

        liste = handleliste.getGroceryItems(); //Men nå er den det
        Assertions.assertEquals(2, liste.size());
        Assertions.assertEquals("Øl", liste.get(1).getGroceryText());
        Assertions.assertEquals(12, liste.get(1).getGroceryUnits());
        handleliste.removeGroceryItem("BaNAN");
        handleliste.addGroceryItem("Øl");

        liste = handleliste.getGroceryItems();
        Assertions.assertEquals(1, liste.size());
        Assertions.assertEquals("Øl", liste.get(0).getGroceryText());
        Assertions.assertEquals(13, liste.get(0).getGroceryUnits());
        handleliste.removeGroceryItem("Øl");

        liste = handleliste.getGroceryItems();
        Assertions.assertTrue(handleliste.getGroceryItems().isEmpty());
        handleliste.addGroceryItem("øl og pølse");
        assertEquals(null, handleliste.getItem("finnes ikke"));

        liste = handleliste.getGroceryItems();
        Assertions.assertEquals("Øl og pølse", liste.get(0).getGroceryText()); 
        Assertions.assertEquals(1, liste.get(0).getGroceryUnits());
        handleliste.addGroceryItem("is", 10);
        Assertions.assertEquals(10, handleliste.getItem("is").getGroceryUnits());
        Assertions.assertEquals(1, liste.size());
        handleliste.addGroceryItem("plumbo", 2, true);
        Assertions.assertEquals(2, handleliste.getItem("plumbo").getGroceryUnits());
        Assertions.assertTrue(handleliste.getItem("plumbo").isChecked());
        liste = handleliste.getGroceryItems();
        Assertions.assertEquals(3, liste.size());
    }

    @Test
    public void testCheck() {
        handleliste.addGroceryItem("øl og pølse");
        List<GroceryItem> liste = handleliste.getGroceryItems();
        Assertions.assertFalse(handleliste.getItem("øl og Pølse").isChecked());
        liste.get(0).toggleCheck();
        Assertions.assertTrue(handleliste.getItem("øl og pølse").isChecked());
        handleliste.getItem("øl OG pølse").toggleCheck();
        Assertions.assertFalse(liste.get(0).isChecked());
    }

    @Test
    public void testExceptions() {
        handleliste.addGroceryItem("øl og pølse");
        Assertions.assertThrows(IllegalArgumentException.class, ()->{
            handleliste.toggleGroceryItem("øl"); //finnes ikke
        });
        
        Assertions.assertThrows(IllegalArgumentException.class, ()->{
            handleliste.removeGroceryItem("Øl"); //finnes ikke
        });

        Assertions.assertThrows(IllegalArgumentException.class, ()->{
            handleliste.addGroceryItem("123"); //ugyldig navn
        });
        Assertions.assertThrows(IllegalArgumentException.class, ()->{
            handleliste.addGroceryItem("Øl og pølse", 0); //ikke lov med antall <= 0
        });
        Assertions.assertThrows(IllegalArgumentException.class, ()->{
            handleliste.addGroceryItem("Øl og Pølse", -1); //ikke lov med antall <= 0
        });

        Assertions.assertThrows(IllegalArgumentException.class, ()->{
            handleliste.setName("ok?"); //ugyldig navn
        });

        Assertions.assertThrows(IllegalArgumentException.class, ()->{
            handleliste.getItem("ØL OG PØLSE").setGroceryUnits(0); //ikke lov med antall <= 0
        });

    }
}