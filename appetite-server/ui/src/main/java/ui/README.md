# Kildekode for Brukergrensesnitt

## 1. Oversikt

Denne modulen kobler sammen koden i datahåndteringsklassene og grensesnittet lagret i en .fxml som er koblet opp mot verktøyet Scene Builer.

- **ShoppingListController** - kobler sammen koden i core med .fxml fila i denne modulen
- **ListChooserController** - kobler sammen ListChooser.fxml med logikken i core og grensesnittet ShoppingList.fxml
- **ShoppingListApp** - starter opp appen

## 2. Klassediagram

```plantuml
class ShoppinglistController{
    - ShoppingList shoppingList
    - JacksonController json
    - Button inputButton
    - Button removeButton
    - Label headLine
    - TextField itemInput
    - TextField numberOfItems
    - ListView<String> listOfItems
    - ListView<String> listOfNumberOfItems

    + void initialize ()
    + void loadShoppingList (String)
    + void updateBoard ()
    + void addGroceryToList ()
    + void removeGroceryFromList ()
    + void save ()
    + boolean isValidText (String)
    + boolean isValidNumberInput ()
}

class ListChooserController{
    - JacksonController json
    - Button loadList
    - Button removeButton
    - Button newList
    - ListView availableShoppingLists 
    - TextField textInput    

    + void initialize ()
    + void getShoppingLists ()
    + void createShoppingList ()
    + void removeShoppingList ()
    + void openShoppingList ()
    + openShoppingList ()
    + openShoppingList (String)
}

class ShoppingListApp{
    + void start (Stage stage)
    + void main (String[] args)
}
```

## 3. FXML-bibliotek
- [FXML](https://docs.oracle.com/javafx/2/api/javafx/fxml/doc-files/introduction_to_fxml.html) gjør det mulig å koble koden i core-modulen opp mot grensesnittet laget i Scene Builder 

## 4. UI-modulen

- _ShoppingListController.java_ kobler sammen kode og grensesnittet. Ved hjelp av metoden `initialize()` starter den opp grensesnittet i samme statiske tilstand. Klassen leverer metoder for legge til og fjerne elementer, samt lagre og laste opp tekst fra json-fil til sende brukergrensesnittet. 
- _ShoppingListApp.java_ kobler kjører grensesnittet ved hjelp av metoden `start(Stage stage)`
- _ListChooserController.java_ kobler sammen koden i ListChooser.fxml med datahåndteringslogikken i core. Denne kontrolleren linker også sammen de to grensensittene ListChooser.fxml og ShoppingList.fxml slik at man kan velge en fil i det ene grensensittet og åpne denne spesifikke filen i det andre grensensittet. Ved hjelp av metoden `initialize()` starter den opp gresesnittet, og den inneholder metoder for å åpne nye handlelister, samt laste opp lagrede handlelister. 

### 4.1 Eksempel på bruk

```java
private JacksonController jackson;
private ShoppingList mainShoppingList;

//Legge til varer
Integer n = Integer.parseInt(numberOfItems.getText()); 
mainShoppingList.addGroceryItem(itemInput.getText(), n);

//Fjerne varer
int index = listOfItems.getSelectionModel().getSelectedIndex();
String str = listOfItems.getItems().get(index);
mainShoppingList.removeGroceryItem(str);

// Lagring fra grensensittet 
jackson.save(mainShoppingList, "filnavn.json");

// Opplasting til grensesnittet 
return jackson.load("filnavn.json");
```
