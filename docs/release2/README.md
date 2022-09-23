# Release 2
I den andre utgangven av prosjektet ønsker vi å utvide koden vår slik at den innneholder to grensesnitt som er koblet sammen. det er ønsket at det ene grensesnittet ListChooser.fxml skal være grensesnittet som åpnes når appen starter, og at dette grensesnittet er koblet til ShoppingList.fxml. Når man åpner en eksisterende handleliste, eller lager en ny handlelsite i grensesnittet ListChooser.fxml, skal denne spesifikke listen åpnes i Shoppinglist.fxml grensesnittet. 

Release 2 som tar utgangspunkt i [brukerhistorie 2 (us-2)](docs/brukerhistorier.md), ønskes det også å inplementere logikk slik at bruker også kan laste opp ulike handldister som tidligere er laget, samt lage nye handlelister og lagre edringer til denne. 

Når appen states opp skal et grensensitt med tilgang på alle eksisterende lister åpnes. Det skal være mulighet å lage en ny handleliste også. Handlelista autolaster opp varene som har blitt lagt til i lista ved en tidligere bruk av handlelista. Når appen lukkes, vil varene som allerede ligger i handlelista bli autolagret til en json-fil. Åpnes en ny handleliste, skal denne være tom, og innholdet lagres til en ny fil med samme navn som handlelista. 

## Funksjonalitet i release 2
Basert på `Issues` som ligger under `Milestone` - release 2, er det i denne andre utgaven av prosjektet hvor implementert logikk for:
 - Validering av filnavn ved lagring og lesing av JSON-fil
 - Implementere Maven Checkstyle Plugin
 - Implementere Maven Spotbugs Plugin
 - Implementere Maven Checkstyle Plugin
 - Endre slik at ShoppingListControllerTest ikke endrer tilstand til appen
 - Lag funksjon for å slette JSON filer
 - Lag funksjon for å returnere eksisterende handlelister
 - Dokumentere lagringsformat med JSON-skjema
 - Utvide UI-grensesnittet for lagring/lasting av ulike filer
 - Utvide testlogikk for nytt lagrinsformat fra UI
 - Kjør spotbugs automatisk på "mvn verify"
 - Lag dokumentasjon til Release 2
 - Lag ny brukerhistorie
 - Definer og konfigurer checkstyle
 - Fjern ubrukute dependencies fra POM-filer 
 - Fiks diverse bugs

## Brukerhistorie 2 (us-2)
> Som bruker ønsker jeg å velge hvilken av de ulike handlelistene jeg har tilgang på, jeg skal legge til varer i.

### Åpne en eksisterende liste
I grensesnittet er det essensielt å kunne åpne en handleliste som er laget og skrevet til tidligere. Dersom man åpnet lista, vil alle de autolagrede elementene lastes opp i handleliste-grensesnittet. 

### Lage en ny liste 
Dersom bruker ønsker å lage en ny liste som ikke allerede eksisterer, er det en knapp i grensesnittet som åpner en ny og tom handlelsite. 

### Lagre elementer i lista som er åpnet/laget
Når man åpner en eksisterende liste eller en liste man har laget, skal elementer som legges til i lista autolagres til filen med samme navn som handlelista. 

## Brukergrensesnitt 
Koden som skal tilfredstille brukerhistorie 2 er linket til et to brukergrensensitt. Klassen **ShoppingListApp** brukes for å starte opp grensensittet ListChooser.fxml bygget i Scene Builer ved kommandoen `mvn javafx:run` dersom du først har kjørt `mvn install` og `cd ui`. Når en handleliste-fil velges vil denne listen åpnes i ShoppingList.fxml, og her vil det være mulighet fom å legge til, samt fjerne varer i handlelista. Informasjon om hvordan brukergrensesnittet er rygget opp skal skal fungere finnes i [ui-modulen](/appetite-server/ui/src/main/java/ui/README.md).

## Domenelogikk
Data som tastes inn i appen håndteres og valideres av [core-modulen](/appetite-server/core/src/main/java/core/README.md).

## Arbeidsprosess 
Inneholder refleksjoner og tanker om arbeidsprosess og kodestil.

### Reflektere over og velge mellom dokumentmetafor (desktop) og implisitt lagring (app):
- Appens tilstand lares implisitt hver gang det blir gjort en endring. Dersom en vare legges inn med et antall, vil grensesnitt automatisk oppdateres samt lagre nye endringer til fil gjennom metoden updateBoard(). Når appen åpnes igjen, vil grensesnittet umiddelbart laste opp elementene som ligger i filen som elementene ble lagret til før appen ble lukket. Dermed vil appen i utgangspunktet ta i bruke implisitt lagring, ettersom dette også er noe man finner i vanlige apper på telefonen. Det gjør at grensesnittet blir mest mulig knyttet til det vi kjenner fra før at, samt et ryddig og enkel app å forholde seg til. 

- I en senere iterasjon av appen har vi en tanke om at nå appen åpnes så skal det være mulig å velge mellom ulike filer å laste opp til grensesnittet, litt lignende hvordan man kunne velge ulike grupper i appetite-prosjektet. Load-skjermen vår er midlertidig, hvor planen er drop-down og dette er et mellomstadiet mens vi jobber med å lære med om JavaFX. På den måten vil det være mulig å knytte ulike grupper med ulike deltakere til en spesifikk handleliste. Dermed er det tenkt at det blir en type dokument metafor når det kommer til lagring også. Det er ønsket at elementene i den spesifikke listen vil lagres til filen det er valgt å laste opp fra, hver gang det gjøres en endring. Så dette vil fortsatt være implisitt lagring, men dette å velge hvilken fil man ønsker å laste opp i et eget grensesnitt kan linkes med opp mot dokument metafor. Dermed vil denne appen inneholde både dokumentmetafor og implisitt lagring, men det vil implementeres slik at appen får en ryddig og enkel oppbygging det vil være lett for en bruker å forstå. 


### Dokumentere valg knyttet til arbeidsvaner, arbeidsflyt og kodekvalitet
- Det første vi gjorde var å bestemme hvordan vi skulle organisere arbeidet. Vi startet med å gjøre oss kjent med hverandres erfaringsnivåer og ambisjonsnivå. Etter å ha kartlagt erfaring og kompetanse i gruppen ble vi enige om hvordan Scrum-modellen skulle løses. Møter er  fastsatt til å være torsdager 10:15 - 12:00. I tillegg skulle vi ha korte Scrum-møter på mandag og andre dager hvor det er nødvendig. Det er også lav terskel for å sende melding og ringe hverandre, og vi har en gruppekontrakt med avklaring av forventninger
Arbeidsøktene og møtene fungerte bra,og de korte møtene gir oss alle en god oppdatering på hvordan alle ligger ann.  

- Arbeidsøktene ble en arena for diskusjon, problemløsing og utveksling av kompetanse. Siden samtlige i gruppen var interesserte i å få mest mulig ut av dette faget, ble det et sterkt fokus på å få muligheten til å sitte sammen og jobbe i arbeidsøkter, samt parprogrammere. Møtene begynte alltid med en gjennomgang av hva folk har gjort siden sist og hvordan man ligger ann. 

- Arbeidsvaner inkluderer issues på GitLab som tar for seg arbeidsoppgaver og arbeidsfordeling, issues er knyttet til milepæler hvor vi jobber med en milepæl om gangen. Vi pusher til branches for å løse hver issues uten å “gå i veien for hverandre”. Et mål med arbeidet er å legge ved et `issue` ved hver commit, ettersom dette er med på å gjøre arbeidet mer ryddig for gruppemedlemmene og eventuelle andre som skal se/delta på prosjektet, og deremde er dette noe vi etterstreber. For øyeblikket blir programmeringen utført som parprogrammering to og to. 

- Kodekvalitet inkluderer bruk av maven checkstyle plugin hvor kodestil som definert i XML-fil (config/checkstyle/checks.xml). Vi har også implementer Jacoco plugin for testdekningsgrad, som tar for seg tester til kjernelogikk og hovedfunksjonalitet. Vi tester ikke teste “trivielle funksjoner” som uansett blir kalt via andre funksjoner som feks. set-funksjoner. Vi har også implementert Spotbug check som gir ut automatisk sjekk for mulige svakheter som kan gi bugs. prosjektet er delt inn i forskjellige moduler for fleksibilitet (domenelogikk, persistens, brukergrensesnitt).

#### JaCoCo 

Kommando:
`mvn jacoco:report`

Dette genererer en rapport i **appetite-server/\<modul\>/target/site/jacoco/index.html** som kan åpnes i nettleseren.

Hjemmeside: [https://www.jacoco.org/jacoco/](https://www.jacoco.org/jacoco/)

#### Checkstyle

Kommando:
`mvn checkstyle:check`

Dette genererer en rapport i **appetite-server/\<modul\>/target/surefire-reports/checkstyle-results.xml**. Resultatene blir også printet til Terminal. Detaljert beskrivelse av hva hver sjekk gjør, ofte med eksempler, finnes [her](https://checkstyle.org/checks.html).

Vi har tatt utgangspunkt i [Google Java Style Guide](https://checkstyle.org/styleguides/google-java-style-20180523/javaguide.html). Vår implementasjon ligger i [appetite-server/config/checkstyle/google_checks.xml](appetite-server/config/checkstyle/google_checks.xml). Foreløpig har vi kun endret standard indent fra nivå 2 til nivå 4, men vil vurdere å gjøre flere tilpasninger etter hvert som vi blir bedre kjent med verktøyet og diskuterer i gruppa.

Hjemmeside: [https://checkstyle.org/](https://checkstyle.org/)

#### Spotbugs

Kommando:
`mvn spotbugs:check`

Dette genererer en rapport i **appetite-server/\<modul\>/target/spotbugsXml.xml** og printer resultatene til Terminal. Man kan også kjøre `mvn spotbugs:gui` for å åpne brukergrensensittet til spotbugs hvor man kan finne flere detaljer om evt. problemer.

Hjemmeside: [https://spotbugs.github.io/](https://spotbugs.github.io/)