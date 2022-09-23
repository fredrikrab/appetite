# Release 3
I den tredje utgangven av prosjektet er det implementert en REST-tjensete i appen gjennom modulene [restapi-modulen](/appetite-server/restapi/src/main/java/restapi/README.md). I tillegg er også appen utvidet med en ny brukerhistorie (us-3) samt at det er laget en ny klient ved bruk av web-klient React. Dette finnes i [appetite-klient](../appetite-klient/README.md). Appen startes nå i et nytt grensesnitt og er koblet opp til restapi-et når det kjøres på serveren. 


## Funksjonalitet i release 3
Basert på `Issues` som ligger under `Milestone` - release 3, er det i denne siste utgaven av prosjektet implementert logikk for nytt frontend, REST-tjeneste og nye funksjonalitet.


### Tilbakemeldinger fra release 2
Basert på tilbakemedlinger vi fikk på release 2, er dette noen av punktene vi har valgt å ta tak i, samt gjøre noe med, for den siste innleveringen av oppgaven: 

#### Kodekvalitet:
- Testdekning core: 64%: Vi har i denne utgaven utvidet testene i core slik at testdekningsgraden har gått fra 64% til 91%, se [core-modulen](/appetite-server/core/src/test/java/core/README.md). 
- Mer komentering av kode: Det er nå kommentarer over all kode hvor det er viktig med kommentarer for å forstå hva metodene gjør. Det kommenteres på dette formatet:
    /**
     * Hva metoden gjør.
     * 
     * @param xxx (hvilken paramtetre som blir tatt inn) 
     * @throws xxx (en type argument som kastes, og hvorfor det kastes) 
     * @return xxx (hva metoden eventuelt returnerer)
    */

#### Dokumentasjon: 
- Arkitekturdiagram: Det blir gitt tilbakemelding om at det ikke finnes noe arkitekturdiagram til release 2, men ettersom dette var et dokumentasjons-krav til release 3 eksisterte det ikke før nå. Man finner nå et pakkediagram for strukturen i [appetite-server](/appetite-server/README.md).

#### Arbeidsvaner:
- Legg ved #issues ved hver commit: Dette er noe vi etterstereber i arbeidet vårt med prosjektet ettersom det er med på å gjøre prosjektarbeidet mer ryddig for enhver som skal delta eller se på prosjektet. Dette er også noe som er kommentert under `### Dokumentere valg knyttet til arbeidsvaner, arbeidsflyt og kodekvalitet` i [docs](../docs/release2/README.md). Dette er også noe vi jobber med nå i release 3. 

### Kodekvalitet, dokumentasjon og arbeidsvaner for release 3
For release 3 av prosjektet er det jobbet med kvalitet på eksisterernde systemer som REST.API-et, utvikling av ny klient og arbeidsvaner som bruk av Gitlab og strukturering. 

#### Kodekvalitet
Funksjonelitet som skal jobbes med for release 3 vil i all hovedsak innebære å skrive om klientsiden med ny teknologi, utvikle et REST-API for det nye grensesnittet og skrive tester til det nye grensesnittet, både på junit nivå samt integrasjonstester. 

Maven brukes fortsatt for å bygge systemet, men vi bruker npm for å bygge React-delen. Se [appetite-klient](../appetite-klient/README.md). Kvaliteten til koden sjekkes fortsatt med bruke av CheckStyle, Spotbugs og Jacoco, se hvordan i [release2-mappa](/ocs/release2/README.md). På grunn av nytt frontend, bruker vi ESLint for å sjekke at kodekvalitet i alle filer godkjent. Detaljert informasjo om brukt av ESLint finnes [her](../appetite-klient/README.md).

#### Dokumentasjon
I denne utgaven av prosjektet er det utvilket et UML-digram, et arkitetkur-diagram for appen og et sekvensdiagfram som viser viktige brukstilfeller i appen og viser koblingen mellom brukerinteraksjon og hva som skjer inni systemet inkl. REST-kall. Diagrammet for appen finnes i [appetite-server](../appetite-server/README.md).

Det er også utviklet dokumentasjon til REST-tjenesten som inneholder klassediagram for viktige deler av systemet, samt infomasjon om tjestens format for forespørsler som støttes. Informasjon finnes i [restapi-modulen](/appetite-server/restapi/src/main/java/restapi/README.md). 

Det er i tillegg utvikler klassedigrammer for viktige deler av systemene i appen. Vi har valgt ut viktige deler av systemet til å være [core-modulen](/appetite-server/core/src/main/java/core/README.md), [json-modulen](/appetite-server/json/src/main/java/json/README.md), [ui-modulen](/appetite-server/ui/src/main/java/ui/README.md), [restapi-modulen](/appetite-server/restapi/src/main/java/restapi/README.md) og i respektive modulers README.md finner man klassedigrammene.

Det er også laget en bidragsguide til denne delen av prosjektet, se vår [bidragsguide](../docs/bidragsguide.md).

#### Arbeidsvaner
For den tredje itterasjonen av prosjektet strukturere vi oss i stpr grad likt som beskrevet i [release2](../docs/release2/README.md) med fokus på å bruke utviklingsoppgaver aktivt som struktur for arbeidet vi gjør. For dette bruker vi stor grad `Issues`på Gitlab for å strukturere arbeidet og la tydelige punkter å jobbe med når vi utvikler prosjektet. En endring fra forrige utgave, er at vi nå også aktivt bruker `labels` på `Issues`for å skrukturer hvor det må gjlpres et arbeid i koden. Vi etterstreber å bruke minst 2 `labels`per `issue`. 

En tilbakemelding vi fikk fra release 2 er å i større grad knytte utviklingsoppgaver til commit-meldniger, og dette er noe vi etterstreber i enda støre grad til release 3. Som gruppe støtter vi oss mye på hverandre for å skrive gode commit-meldinger som skaper forståelse for hvilke endringer vi har gjort i koden, og går ofte sammen gjennom kode når vi skal merge slik at vi får med alle delene av kode som vi trenger på gode måter. 

##### Reflektere over og velge mellom dokumentmetafor (desktop) og implisitt lagring (app):
I denne versjonen av appen, blir det lagret implisitt. Brukerer forholder seg ikke til noen mapper eller filer på desktop som må åpnes manuelt i appen, og hver gang det gjøres endringer blir det lagret automatisk. I appen har bruker mulighet til å bytte mellom de ulike handlelistene vedkommende har lagret gjennom en dropdown-meny. 

##### Dokumentere valg knyttet til arbeidsvaner, arbeidsflyt og kodekvalitet
Det står en del om hvordan vi har valgt å strukturere arbeidet med prosjektet generelt [her](../docs/release2/README.md), men til denne innelveringen av prosjektet har vi valgt i sturukturere noe av arbeidet ulikt. Ettersom det ble laget en ny klient, gikk vi over til at alle jobbet med denne utviklignen. Vi har fortsatt strukturert oss etter en type parprogrammering, men det har vært veldig flytende hvem som jobber med hva til hvilken tid, og vi har oftere hatt arbeidsøkter hvpr alle fire sitter på samme sted å programmerer sammen. Arbeidsflyten har vært veldig fin, og det har vært gost å kunne støtte seg på ulike kunnskap folk har i gruppa til ulike utviklingspunkter i prosjektet når vi har laget ny klient. Som kommentert tidligere i dokumentet blir nå kodekvalitet av 

## Brukerhistorie 3
> Som bruker ønsker jeg å kunne krysse av elementer når man har puttet dem i handlekurven.

### Avhuke elementer i lista når de er handlet
Ved siden av elementer i handlelista skal de være bokser som man kan huke av når et element er handlet. Når elementet hukes av, vil det bli tydeliggjort at det er handlet ved å få en strek over elementet, samt at elementet flyttes nederst i lista. Der er også mulig å unhuke det igjen, hvor det vil bli flyttet opp i lista igjen. 

## Domenelogikk
Data som som ligger til grunn for appen håndteres og valideres av [core-modulen](/appetite-server/core/src/main/java/core/README.md).

## Persistens
I prosjektet brukes det json-filer for lagrelogikken til handlelista. Det er gjort en del endringer i denne modulen for å få all kodelogiskken til å virke med det nye grensesnittet vårt i React, som blandt annet at json.save() tar inn ett argument i stedet for to, json-modul gir feilmelding dersom load() kalles på en fil som ikke eksisterer, lagre alle serialiserte ShoppingList-objekter i én fil, som er blandt noen av dem. Informasjon om hvordan denne delen av prosjektet fungerer finnes i [json-modulen](/appetite-server/json/src/main/java/json/README.md).

Det er også laget et JSON-skjema som verifiserer JSON-filer før de brukes i grensesnittet. JSON-skjema finnes [her](appetite-server/json/src/main/resources/json/schema.json).

## Brukergrensesnitt 
I denne delene av prosjektet er brukergrensesnittet endret fra JavaFX til å bruke JavaScript i React. Dette betyr at all koden som er skrevet i [ui-modelen](/appetite-server/ui/src/main/java/ui/README.md) nå ikke brukes for å bygge grensesnittet. I steden er det skrevet i React hvor vi bruker JavaScript for å skrive kode. React er et JavaScript-bibliotek for å lage og bygge brukergrensesnitt. React kan brukes som et utgangspunkt i utviklingen av enkeltside eller mobil applikasjon, da det er optimalisert for å raskt hente skiftende data som må registreres. Informasjon om hvordan det nye grensesnittet fungerer finnes i [appetite-klient](../appetite-klient), og informasjon om hvordan mappa fungerer finnes her [her](../appetite-klient/README.md).

Vi valgte å utvikle en ny klient som matcher tidligere funksjonalitet, fordi vi øsnker å lære med om React og det å skrive samt bruke React og JavaScript for å bygge en applikasjon. 

## REST-tjenesten
Gruppeinnlevering 3 inneholder et krav om å bygge en REST-API. Dette funker i utgangspunkte på samme måte som en nettside, hvor det er gjort et kall fra en klient til en server, og data kommer tilbake over HTTP-protokollen. Dette REST-API-et er bygget rundt kjernelogikken som ligger i [core-modulen](/appetite-server/core/src/main/java/core/README.md) og [json-modulen](/appetite-server/json/src/main/java/json/README.md). REST-API-et tilbys av en web-server. React-ui-et vårt bruker dette REST-API-et for for å sende data over nettet. Se mer informasjon om rest-tjenesten i [rest-modulen](/appetite-server/restapi/src/main/java/restapi/README.md)