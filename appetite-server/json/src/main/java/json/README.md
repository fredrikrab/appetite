# Kildekode for persistens (fillagring)

## 1. Oversikt

Denne modulen implementerer metoder fra [Jackson JSON Processor](https://github.com/FasterXML/jackson-docs)-bibliotek for å lagre og laste inn tilstanden til **ShoppingList**-objekter. Den gir også en metode for å slette eksisterende filer.

- **Serialisering:** Fra java-objekt til JSON
- **Deserialisering:** Fra JSON til Java-objekt

Modulen spesifiserer videre hvor på datamaskinen JSON-filer skal lagres. Det sjekkes om alle filnavn er gyldig.

## 2. Klassediagram

```plantuml
package "appetite.json" as a {
  
  class JacksonController {
    ~ {static} pathToDir
    - ObjectMapper mapper
    
    + save(ShoppingList, String)
    + load(String)
    + delete(String)
    + getShoppingLists()
    - validatedFileName(String)
  }
}

package "com.fasterxml.jackson" as b #DDDDDD {
  
  JacksonController -- ObjectMapper
  ObjectMapper -- SimpleModule

  
  class ObjectMapper {
    + registerModule()
    + writeValue()
    + readValue()
  }
  
  class SimpleModule {
    # String name
    # Version version
    
    + addSerializer()
    + addDeserializer()
  }
}

package "appetite.json" as c {
  
  SimpleModule -- ShoppingListSerializer
  SimpleModule -- ShoppingListDeserializer
  
  class ShoppingListSerializer<Extends StdSerializer> {
    + ShoppingListSerializer()
    + void serialize()
  }

  class ShoppingListDeserializer<Extends StdDeserializer> {
    + ShoppingListDeserializer()
    + deserialize()
  }
}

package "com.fasterxml.jackson" as d #DDDDDD {
  
  ShoppingListSerializer <|-- StdSerializer
  ShoppingListDeserializer <|-- StdDeserialzier
}
```

## 3. Jackson-bibliotek

- [ObjectMapper](https://fasterxml.github.io/jackson-databind/javadoc/2.12/com/fasterxml/jackson/databind/ObjectMapper.html) gir funksjonalitet for å skrive og lese JSON-format
- [SimpleModule](https://fasterxml.github.io/jackson-databind/javadoc/2.12/com/fasterxml/jackson/databind/module/SimpleModule.html) muliggjør registrering av egne moduler for tilpasset serialisering og deserialisering
- [StdSerializer](https://fasterxml.github.io/jackson-databind/javadoc/2.12/com/fasterxml/jackson/databind/ser/std/StdSerializer.html) og [StdDeserializer](https://fasterxml.github.io/jackson-databind/javadoc/2.12/com/fasterxml/jackson/databind/deser/std/StdDeserializer.html) er grunnklasser for hhv. serialisering og deserialisering

## 4. Detaljer

### 4.1 Filer

- _ShoppingListSerializer.java_ og _ShoppingListDeserializer.java_ implementerer tilpasset serialisering og deserialisering
- _JacksonController.java_ leverer metodene `save()` og `load()` som hhv. lagrer og genererer **ShoppingList**-objekter
- _JacksonController.java_ leverer også de resterende metodene vist i klassediagrammet

### 4.2 Lokasjon for lagring og innlasting

- Variabelen **pathToDir** i _JacksonController.java_ spesifiserer den lokale mappen modulen forholder seg til
- Vi bruker mappen "Shoppinglists" i brukerens hjem-katalog (identifisert via Java-metoden `System.getProperty("user.home")`)

### 4.3 Filvalidering

Alle filnavn blir validert via `validatedFileName()` som:

- Verifiserer at input ikke er null eller tomt
- Verifiserer at første tegn er alfanumerisk (a-å, A-Å, 0-9)
- Verifiserer at følgende tegn kun er:
  - alfanumerisk
  - binde- eller understrek
  - punktum eller mellomrom
- Legger til filendelsen ".json" dersom ikke spesifisert
  - Dette betyr at alle metoder kan kalles både med og uten spesifisert filendelse

### 4.4 JSON-skjema
JSON-skjema finnes i [/src/main/resources/json/schema.json](appetite-server/json/src/main/resources/json/schema.json).

## 5. Eksempel på bruk

```java
JacksonController jackson;

// Serialisering
ShoppingList shoppingList;
jackson.save(shoppingList, "filnavn.json");

// Deserialisering
newShoppingList = jackson.load("filnavn.json");

// Sletting
jackson.delete("filnavn.json");

// Liste over eksisterende handlelister
List<String> everyShoppingList = jackson.getShoppingLists();