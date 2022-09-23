package core;

public class GroceryItem {

    private String groceryText;
    private int groceryUnits = 1;
    private boolean checked = false;

    /**
     * Creates new empty GroceryItem.
     */
    public GroceryItem() {
        super();
    }

    /**
     * Creates new GroceryItem, with default groceryUnits = <code>1</code> and checked = <code>false</code>.
     * @param groceryText name on groceryItem
     * @throws IllegalArgumentException if <code>groceryText</code> is invalid
     */
    public GroceryItem(String groceryText) {
        setGroceryText(groceryText);
    }

    /**
     * Creates new GroceryItem with default checked = <code>false</code>.
     * @param groceryText name on groceryItem
     * @param groceryUnits number of units
     * @throws IllegalArgumentException if <code>groceryText</code> is invalid or if <code>groceryUnits</code> <= 0
     */
    public GroceryItem(String groceryText, int groceryUnits) {
        setGroceryText(groceryText);
        setGroceryUnits(groceryUnits);
    }

    /**
     * Creates new GroceryItem.
     * @param groceryText name on GroceryItem
     * @param groceryUnits number of units
     * @param checked <code>boolean</code> to simulate if the GroceryItem is checked or not
     * @throws IllegalArgumentException if <code>groceryText</code> is invalid or if <code>groceryUnits</code> <= 0
     */
    public GroceryItem(String groceryText, int groceryUnits, boolean checked) {
        setGroceryText(groceryText);
        setGroceryUnits(groceryUnits);
        setChecked(checked);
    }

    /**
     * Returns the name on GroceryItem.
     * @return name on GroceryItem
     */
    public String getGroceryText() {
        return groceryText;
    }

    /**
     * Sets the name of this GroceryItem.
     * @param groceryText name on GroceryItem
     * @throws IllegalArgumentException if <code>groceryText</code> is invalid
     */
    private void setGroceryText(String groceryText) {
        if (Validate.isValidText(groceryText)) {
            this.groceryText = Validate.setSpellingCapitalization(groceryText);
        } else {
            throw new IllegalArgumentException(String.format("isValidText() returned false for groceryText = %s", groceryText));
        }
    }

    /**
     * Returns number og groceryunits.
     * @return number of units
     */
    public int getGroceryUnits() {
        return groceryUnits;
    }


    /**
     * Sets number of GroceryUnits.
     * @param groceryUnits number of units
     * @throws IllegalArgumentException if <code>groceryUnits</code> <= 0
     */
    protected void setGroceryUnits(int groceryUnits) {
        if (groceryUnits <= 0) {
            throw new IllegalArgumentException(String.format("Argument groceryUnit = %d but cannot be <= 0", groceryUnits));
        } else {
            this.groceryUnits = groceryUnits;
        }
    }

    /**
     * Returns boolean attribute of GroceryItem.
     * @return <code>boolean</code> of the GroceryItem
     */
    public boolean isChecked() {
        return checked;
    }

    /**
     * Sets checked attribute of the GroceryItem.
     * @param checked <code>boolean</code>
     */
    private void setChecked(boolean checked) {
        this.checked = checked; 
    }

    /**
     * Inverts <code>boolean</code> attribute of the GroceryItem.
     */
    protected void toggleCheck() {
        this.checked = !checked;
    }
   
}
