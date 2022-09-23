# Kildekode for REST-tjenesten (restapi)

## 1. Oversikt
Vi bruker Spring Boot for webserver og REST API-tjeneste. HTTP-forespørsler kan sendes til API-et, og hvor data og tilstand overføres som del av kroppen i HTTP-pakkene. `restapi`-modulen kan følgelig levere informasjon om tilstand til klient og motta forespørsler som endrer tilstanden. Tilstandsinformasjon sendes og mottas som JSON-objekter.

## 2. Klassediagram
`restapi`-modulen består av fire klasser:

- **ShoppingListApplication**: Starter serveren.
- **WebSecurityConfig**: Konfigurerer HTTP sikkerhetsregler. Brukes for å endre standardinnstillinger slik at vi tillater [CORS](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS) HTTP-forespørsler, ettersom klient og server begge kjører på samme host.
- **ShoppingListService**: Service-klasse som kommuniserer med domenelogikk og persistens (dvs. `core`- og `json`-modulene).
- **ShoppingListController**: Kontroller-klasse som definerer API-grensesnittet. Bruker metodene i service-klassen for å håndtere API-forespørsler.

Ved endringer i domenelogikk eller persistens så må service-klassen oppdateres tilsvarende. Ettersom kontroller-klassen bruker metodene i service-klassen, så er det meningen at denne klassen sjeldent må oppdateres ved endringer i eksisterende funksjonalitet. Resultatet er at API-grensesnittet enklere kan holdes likt for klientene som er avhengig av det, samtidig som intern funksjonalitet kan utvikles uavhengig.

Java-annotasjoner ved hver metode i kontroller-klassen angir hvilket verb HTTP-forespørselen må bruke, samt eventuelle parametere og data som skal registreres. F.eks. vil en metode annotert med @GET bare kalles når HTTP-metoden er "GET".

Under er klassediagrammet for service- og kontroller-klassen:

```plantuml

class ShoppingListService {
    - ShoppingList shoppingListServiceObject
    - JacksonController json

    + void setShoppingList(ShoppingList shoppingList)
    - boolean serviceIsInitialized()
    + List<String> getAllShoppingListNames()
    + ShoppingList getShoppingList(String shoppingListName)
    + void SaveState()
    + String getShoppingListName(String shoppingListName)
    + void createShoppingList(String shoppingListName)
    + void deleteShoppingList(String shoppingListName)
    + void addGroceryItem(GroceryItem groceryItem)
    + void removeGroceryItem(String groceryItemName)
    + void toggleGroceryItem(String groceryItemName, boolean checked)
}

class ShoppingListController {
    - ShoppingListService service
    - ObjectMapper mapper

    + HttpStatus RespondToPing()
    + List<String> allShoppingLists()
    + ShoppingList getShoppingList()
    + HttpStatus createShoppingList()
    + HttpStatus deleteShoppingList()
    - void setShoppingList()
    + HttpStatus addGrocery()
    + HttpStatus removeGrocery()
    + HttpStatus toggleGrocery()
}

```

## 3. API-grensesnitt

| Beskrivelse                               | URL                                   | Verb   | Kropp                                                                                     |      Parameter      |
| ----------------------------------------- | ------------------------------------- | ------ | ----------------------------------------------------------------------------------------- | :-----------------: |
| Svarer på ping                            | `/shoppinglist/ping`                  | GET    | -                                                                                         |         ---         |
| Returnerer liste med alle handlelister    | `/shoppinglist/getAll`                | GET    | -                                                                                         |         ---         |
| Returnerer forespurt handleliste          | `/shoppinglist/get/{name}`            | GET    | -                                                                                         | overwrite (boolean) |
| Oppretter ny handleliste                  | `/shoppinglist/create`                | POST   | <pre>{<br>  name: string, <br>  groceryItems: []<br>}</pre>                               |         ---         |
| Sletter handleliste                       | `/shoppinglist/delete`                | DELETE | <pre>{<br>  name: string <br>}</pre>                                                      |         ---         |
| Legger til vare i handleliste             | `/shoppinglist/{name}/grocery/add`    | PUT    | <pre>{<br>  groceryText: string,<br>  groceryUnits: int,<br>  checked: boolean<br>}</pre> |         ---         |
| Fjerner vare fra handleliste              | `/shoppinglist/{name}/grocery/remove` | PUT    | <pre>{<br>  groceryText: string <br>}</pre>                                               |         ---         |
| Setter eller fjerner avkryssning for vare | `/shoppinglist/{name}/grocery/toggle` | PUT    | <pre>{<br>  groceryText: string,<br>  checked: boolean<br>}</pre>                         |         ---         |


## 4. Sekvensdiagram
Et sekvensdiagram for et viktig brukstilfeller i appen, som viser koblingen mellom brukerinteraksjon og hva som skjer inni systemet inkludert REST-API. 

```plantuml

Client -> SpringController: GET /getShoppingLists
SpringController --> Client: {Middag}
Client -> SpringController: PUT /shoppinglist/create {Taco: salat, bønner, tacokrydder}
SpringController --> Client: 0
Client -> SpringController: GET /getShoppingLists
SpringController --> Client: {Middag, Taco: salat, bønner, tacokrydder}
Client -> SpringController: DELETE /shoppinglist/delete {Middag}
SpringController --> Client: 0
Client -> SpringController: GET /getShoppingLists
SpringController --> Client: {Taco: salat, bønner, tacokrydder}

```
Først finnes det kun en tom tilgjengelig handeliste; Middag. Deretter lages det en ny liste som heter Taco, som inneholder salat, bønner og tacokrydder. Legg merke til at elementene deserialiseres og serialiseres i henhold til [her](appetite-server/json/src/main/resources/json/schema.json). I diagrammet vises det til slutt hva som skjer når en liste slettes. 


## 5. Tester
Det er skrevet tester for metodene i kontroller-klassen. Disse vil også indirekte teste service-klassen og videre at serveren er satt opp meg gyldig konfigurasjon. Testene verifiserer at REST API-serveren. Disse finnes [her](appetite-server/restapi/src/test/java/restapi/README.md).