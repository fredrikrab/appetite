# Testkode for persistens (fillagring)

Denne mappen inneholder test for lagringslogikken til handleliste appen Appetite. 

[Main-mappen] inneholder klassene **JacksonController**, **ShoppingListDeserializer** og **ShoppingListSerializer** for alle tre testes gjennom testklassen **JacksonControllerTest**. Det er ikke alle metoder som har egne tester, men dersom for eksmepel en get-metode brukes gjennom en metode for større funksjonalitet, vil denne også testes gjennom terstmetoden. Testen vil i utgangspunkte sjekke at logikken for oppretting av nye lister, langrin av listene, sletting av lister, og opplastning av lister fungerer. Dette er logikken det er viktigst at funker for at appen skal fungere når det kommer til langring og loading av både tilstand og arbeid. 

## Hva testes
Når testen starter vil det aller først sette opp en ny `ObjectMpper` og sette en global variabel til å lese inn [json-skjema](/appetite/json/main/resources/schema.json). Deretter settes det en ny `JacksonController`og en ny tom `ShoppingList` opp gjennom setUp()-metoden. Testen inneholder 7 tester ekskludert dirPathExists() og setup(): `testWriteToDisk`, `testReadFromDisk`, `testNameValidation`, `fileExcists`, `testGetShppingLists`, `testJsonSchemaIsValid`og  `testSerializerConformsToSchema`. Disse testene sjekker om det lagres riktig fra appen til disken, og det lastes opp riktig andre vei, at navnet er riktigog at filen eksisterer, samt at alle listene som eksisterer hentes ut rett. I tillegg er det test som passer på at alt er i henhold til json-skjema.

Under ligger noen spesifikke tilfeller vi har testet for i klassen:

### testWriteToDisk
- `jackson.save(shoppingList)` //lagrer lista med innehold som eksisterer i den
- `Assertions.assertEquals(expectedString, Files.readString(pathToFile);)`// sjekker at lista som ble lagret er lik og inneholder det samme som lista som ligger på disk. 

### testReadFromDisk
- ` shoppingList = jackson.load(fileName);` //laster opp lista fra disk
- `Assertions.assertEquals(fileName, shoppingList.getName());` //sjekket at lista som er lastet opp er lik op inneholder det sammen som den som lå på disk