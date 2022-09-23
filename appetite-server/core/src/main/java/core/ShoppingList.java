package core;

import java.util.ArrayList;
import java.util.List;

public class ShoppingList {

    private String name;
    private List<GroceryItem> items = new ArrayList<GroceryItem>();
    

    /** Creates new empty ShoppingList. 
    */
    public ShoppingList() {
        super();
    }
   
    /** Creates new ShoppingList with given <code>name</code>.
    * @param name name of the new ShoppingList
    * @throws IllegalArgumentException if <code>name</code> is invalid
    */ 

    public ShoppingList(String name) {
        setName(name);
    }


    /** Adds GroceryItem to ShoppingList.
     * @param groceryItem GroceryItem object to add.
     */

    public void addGroceryItem(GroceryItem groceryItem) {
        if (isDuplicate(groceryItem.getGroceryText())) {
            addDuplicate(groceryItem.getGroceryText(), groceryItem.getGroceryUnits());
        } else {
            items.add(groceryItem);
        }
    }


    /** Adds GroceryItem in ShoppingList, with default units = <code>1</code> and checked = <code>false</code>.
    * @param name name on GroceryItem
    * @throws IllegalArgumentException if <code>name</code> is invalid
    */ 

    public void addGroceryItem(String name) {

        if (isDuplicate(name)) {
            addDuplicate(name, 1);
        } else {
            items.add(new GroceryItem(name));
        }
    }
    
    
    /** Adds GroceryItem in ShoppingList, with the given number of units, and with default checked = <code>false</code>.
    * @param name name on GroceryItem
    * @param n number of units
    * @throws IllegalArgumentException if <code>name</code> is invalid or <code>n</code> <= 0
    */ 

    public void addGroceryItem(String name, int n) {
        if (isDuplicate(name)) {
            addDuplicate(name, n);
        } else {
            items.add(new GroceryItem(name, n));
        }
    }

    
    /** Adds GroceryItem in ShoppingList.
    * @param name name on GroceryItem
    * @param n number of units
    * @param checked <code>boolean</code> to simulate if the GroceryItem is checked or not
    * @throws IllegalArgumentException if <code>name</code> is invalid or <code>n</code> <= 0
    */

    public void addGroceryItem(String name, int n, boolean checked) {
        if (isDuplicate(name)) {
            addDuplicate(name, n);
        } else {
            items.add(new GroceryItem(name, n, checked));
        }
    }

    
    /** Removes GroceryItem from ShoppingList.
    * @param name name on GroceryItem
    * @throws IllegalArgumentException if <code>name</code> is invalid or if a GroceryItem with the given name doesn't exist
    */ 

    public void removeGroceryItem(String name) {
        if ((items.remove(getItem(name)) == true)) {
            return;
        } else {
            throw new IllegalArgumentException(String.format("removeGroceryItem(%s) did not remove anything. Does %s exist?", name, name));
        }
    }

    
    /** Inverts <code>boolean</code> attribute of the GroceryItem.
    * @param name name on GroceryItem
    * @throws IllegalArgumentException if <code>name</code> is invalid
    */ 

    public void toggleGroceryItem(String name) {
        if (getItem(name) == null) {
            throw new IllegalArgumentException(String.format("Attempted to toggle %s but %s does not exist.", name, name));
        } else {
            getItem(name).toggleCheck();
        }
    }

    
    /** Adds additional groceryUnits if GroceryItem allready exists in ShoppingList.
    * This method should not be used before a validation of <code>name</code>
    * @param name name on GroveryItem
    * @param n number of units
    */

    private void addDuplicate(String name, int n) {
        if (n <= 0) {
            throw new IllegalThreadStateException(String.format("%d, can't be <= 0", n));
        }
        GroceryItem item = getItem(name);
        item.setGroceryUnits(item.getGroceryUnits() + n);
    }

    
    /** 
     * Returns a list of GroceryItems.
     * @return returns a <code>List</code> of all the GrocyerItems in ShoppingList
    */ 
    public List<GroceryItem> getGroceryItems() {
        return new ArrayList<GroceryItem>(items);
    }

    
    /** 
    * Returns a GroceryItem.
    * @param name name on GroceryItem
    * @return returns a GrocyerItems with given <code>name</code>, returns <code>null</code> if it doesn't exist
    */ 

    public GroceryItem getItem(String name) {
        if (Validate.isValidText(name)) {
            for (GroceryItem groceryItem : items) {
                if (groceryItem.getGroceryText().equals(Validate.setSpellingCapitalization(name))) {
                    return groceryItem;
                }
            }
        }
        return null;
    }

    
    /** 
    * Sets <code>name</code> to be the name of ShoppingList.
    * @param name name on ShoppingList
    * @throws IllegalArgumentException if <code>name</code> is invalid
    */ 

    public void setName(String name) {
        if (Validate.isValidText(name)) {
            this.name = Validate.setSpellingCapitalization(name);
        } else {
            throw new IllegalArgumentException(String.format("isValidText() returned false for: %s", name));
        }
    }

    
    /** 
    * Returns the name of ShoppingList.
    * @return returns the name of ShoppingList
    */
    public String getName() {
        return name;
    }
    
    
    /** 
     * Checks if GroceryItem with the given <code>name</code> exists in ShoppingList.
     * @param name name on groceryitem
     * @return <code>true</code> if it exist, otherwise <code>false</code>
     */

    private boolean isDuplicate(String name) {
        return items.contains(getItem(Validate.setSpellingCapitalization(name)));
    }

}