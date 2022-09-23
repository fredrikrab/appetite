# Testkode for domenelogikk

Denne mappen inneholder tester for handleliste-logikken som ligger til grunn for appen Appetite. 

ShoppingListTest, som er testklassen for [core-modulen], tester implititt både **GroceryItem** og **ShoppingList** og bruker **Validate**-klassen for å sjekke at `Exceptions` blir kastet på rett sted. [Core-modulen] har en testdekningsgrad 91% som er innafor de 80% som i utgangspunktet er forventet av en god test. Ikke alle metoder har fått en egendedikert test, men dersom for eksempel `get-metoder`brukes i en metode, vil dette også testes av fungerer i den respektive testen. 

Vi bruker jUnit, som er et rammeverk som brukes for å teste de minste delene av kode. I denne testen har vi 5 testklasser, inkludert setUp().

## Hva testes
Når testen startes opp, setter den alltid opp en ny handleliste kalt `Test shopping` ved bruk av setUp(). Det genereres en handleliste hvor det sjekkes ut at den viktigste funksjonaliteten, som å legge til elementer i lista samt kunne fjerne dem, funker som det skal. Tester i testklassen er `testConstructor`, `testAddRemove`, `testCheck` og `testExceptions`.

 Under ligger noen spesifikke tilfeller vi har testet for i klassen: 

### testConstructor 
Tester å sette navn på handlelista, samt at navnet kan endres. 
- `Test shopping`=> `Test shopping`

### testAddRemove
Tester funksjonalitet for å legge til og fjerne varer.
- `Banan "1" +` => `Banan 1`
- `Øl "12" +`=> `Banan 1, Øl 12`
- `Øl -`=> `Banan 1`