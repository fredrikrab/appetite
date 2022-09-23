# Release 1
I den første utgaven av prosjektet er det implementert grunnleggende domenelogikk, et enkelt brukergrensesnitt og persistens. Release 1 tar utgangspunkt i [brukerhistorie 1 (us-1)](docs/brukerhistorier.md) hvor det skal være implementert logikk slik at en bruker kan legge til og slette elementer fra en handleliste. 

Når appen startes opp skal handlelista autoloade varene som har blitt lagt til i lista ved en tidligere bruk av handlelista. Når appen lukkes, vil varene som allerede ligger i handlelista bli autolagret til en json-fil. 

## Funksjonalitet i release 1
Basert på `Issues` som ligger under `Milestone` - release 1, er det i denne første utgaven av prosjektet implementert logikk for:
 - Sette opp kodeprosjektet i gitpod
 - Håndtere å legge til varer til en liste
 - Håndtere å fjerne en bestemt vare fra en liste
 - Lagre lista fra grensesnittet til en fil
 - Laste inn lista fra en fil til grensensittet 
 - Testing av lagring til og loading fra fil
 - Testing av Controller-logikken og oppstart av appen
 - Testing av domenelogikk
 - Tilfredsstillende innkapsling og validering i klasser
 - Tilfredsstillende dokumentasjon i de ulike modulene gjennom README-filer

## Brukerhistorie 1
> Som en student ønsker jeg å lage en oversikt slik at jeg har kontroll på hva jeg trenger å handle inn til uka.

### Legge til varer
I handlelista er det essensielt å kunne legge til en vare, og et spesifikt antall av denne varen. Dersom et antall ikke spesifiseres, skal antallet av den spesifikke varen settes til 1. 

### Fjerne varer
Dersom en bruker legger til for mye av en vare, eller ikke ønsker å handle en spesifikk vare likevel, skal det være muligheter for å fjerne denne varen. 

## Persistens
I prosjektet brukes det json-filer for å lagre handlelista til og loade fra. Informasjon om hvordan denne delen av prosjektet fungerer finnes i [json-modulen](/appetite-server/json/src/main/java/json/README.md).

## Brukergrensesnitt 
Koden som skal tilfredstille brukerhistorie 1 er linket til et enkelt brukergrensensitt. Klassen **ShoppingListApp** brukes for å starte opp grensensittet bygget i Scene Builer ved kommandoen `mvn javafx:run` dersom du først har kjørt `mvn install` og `cd ui`. Informasjon om hvordan brukergrensesnittet er rygget opp skal skal fungere finnes i [ui-modulen](/appetite-server/ui/src/main/java/ui/README.md).

## Domenelogikk
Data som tastes inn i appen håndteres og valideres av [core-modulen](/appetite-server/core/src/main/java/core/README.md).
