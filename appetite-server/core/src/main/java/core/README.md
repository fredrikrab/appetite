# Kildekode for domenelogikk

## 1. Klassediagram

Domenelogikken for handlelisten består av to klasser, i tilegg til en klasse som tar for seg validering:

- **GroceryItem** - en vare brukeren ønsker å handle.
- **ShoppingList** - en samling av GroceryItem-objekter.
- **Validate** - en klasse som brukes for validering av argumenter

```plantuml
ShoppingList "owner: 1" *--> "groceries: *" GroceryItem

class GroceryItem {
    - String groceryText
    - int groceryUnits=1
    - boolean checked=false
    
    + GroceryItem()
    + GroceryItem(String groceryText)
    + GroceryItem(String, int)
    + GroceryItem(String, int, boolean)
    + String getGroceryText()
    + bool isChecked()
    + int getGroceryUnits()
    # setGroceryUnits(int)
    # void toggleCheck()
    - setChecked(boolean)
    - void setGroceryText(String)
}

class ShoppingList {
  - String name
  - List<GroceryItem> items
  
  + ShoppingList()
  + ShoppingList(String)
  + void addGroceryItem(String)
  + void addGroceryItem(GroceryItem)
  + void addGroceryItem(String, int)
  + void addGroceryItem(String, int, bool)
  + void removeGroceryItem(String)
  + void toggleGroceryItem(String)
  + List<GroceryItem> getGroceryItems()
  + GroceryItem getItem(String)
  + void setName(String)
  + String getName()
  - void addDuplicate(String, int)
  - boolean isValidText(String)
}

class Validate {
  # isValidText(String)
  # setSpellingCapitalization(String)
}
```

## 2. GroceryItem
Et **GroceryItem**-objekt har 3 attributter:

|Attributt|Type|Beskrivelse|
|:--------|:---|:----------|
|`name`|_String_|Navn på vare|
|`groceryUnits`|_int_|Antall som skal kjøpes inn|
|`checked`|_boolean_|Om varen allerede er kjøpt eller ikke|

Hvert attributt har `get()`- og `set()`-metoder:

- `setGroceryText()` og `getGroceryText()`
- `setGroceryUnits()` og `getGroceryUnits()`
- `setChecked()` og `isChecked()`

### 2.1 Innkapsling
**GroceryItem** initialiseres med `name` og med standardverdier `groceryUnits=1` og `checked=false`.

### 2.2 Validering
- `setGroceryText()` kaller på `isValidText()` som returnerer **true** kun hvis teksten i argumentet bare inneholder bokstaver (a-å, A-Å) og mellomrom. Dersom `isValidText()` returnerer **false**, kastes en **IllegalArgumentException**.
- `setGroceryUnits()` kaster **IllegalArgumentException** dersom argumentet er <= 0.

## 3. ShoppingList

Et **ShoppingList**-objekt har 2 attributter:

|Attributt|Type|Beskrivelse|
|:--------|:---|:----------|
|`name`|_String_|Navn på handleliste|
|`items`|_List<GroceryItem>_|_ArrayList_ bestående av varer som tilhører handlelisten|

- `name` kan aksesseres via metodene `setName()` og `getName()`
- `items` kan hentes som liste via `getGroceryItems()`

### 3.1 Innkapsling
**ShoppingList** initialiseres med `name`.

### 3.2 Metoder

- **GroceryItem**-objekter legges til i **ShoppingList** med `addGroceryItem()`
  - `addGroceryItem()` kaller på `isDuplicate()` som sjekker om objektet allerede eksisterer.
    - `isDuplicate()` kaller på `getItem()` som itererer gjennom **GroceryItem**-objektene i **ShoppingList**.
    - `getItem()` returnerer objektet dersom det eksisterer.
    - I så fall kalles `addDuplicate()` som øker `groceryUnits`-attributtet.
  - Ellers opprettes et nytt **GroceryItem**-objekt.
- `removeGroceryItem()` fjerner et **GroceryItem**-objekt fra lista, dersom det eksisterer.

### 3.3 Validering

- `setName()` kaller på `isValidText()` som returnerer **true** kun hvis argumentet er gyldig.
- `setSpellingCapitalization()` sørger for stor forbokstav der det er aktuelt (og kaller selv på `isValidText()`)