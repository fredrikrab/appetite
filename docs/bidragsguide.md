# Bidragsguide

**Innholdsfortegnelse**

1. [GitLab](#gitlab)
    - [Issues](#issues)
    - [Labels](#labels)
    - [README](#readme)
    - [PlantUML](#plantuml)
2. [git](#git)
    - [Branching](#branching)
    - [Merging](#merging)
    - [Commits](#commits)
3. [Slack](#slack)

## GitLab

### Issues
- Brukes til å organisere oppgaver og gruppediskusjoner.
- Issues bør ha en god overskrift og gjerne en kort beskrivelse med utdypning, eksempler, link til relevante ressurser eller lignende
- Så fort et team-medlem bestemmer seg for å jobbe med et issue, bør de assigne det til seg selv.

### Labels
- Labels brukes for å kategorisere issues.
- I de fleste tilfeller bør to labels brukes: en for modulen det gjelder og en for kategori (f.eks. bug, dokumentasjon, ny funksjon, forbedring eller testing).

### README
- En overordnet README-fil ligger på toppnivå i prosjektet.
- Det skal ligge en README-fil i hver modul i prosjektet sammen med kodefilene (dvs. i `appetite/modul/src/main/java/modul/`).
- README-filen bør inneholde en kort beskrivelse av modulens oppgave, et klassediagram, beskrivelse av evt. objekter, beskrivelse av de viktigste metodene, og gjerne noen kode-eksempler.
- README-filer må oppdateres med nye funksjoner og ved endringer som ugyldiggjør eksisterende dokumentasjon.

### PlantUML
- Klassediagram, tilstandsdiagram og sekvensdiagram lages med PlantUML.
- GitLab har [innebygd støtte for PlantUML-syntaks](https://docs.gitlab.com/ee/administration/integration/plantuml.html).
- Andre gode ressurser er [plantuml.com](https://plantuml.com/class-diagram), [temasiden i TDT4100](https://www.ntnu.no/wiki/display/tdt4100/Klassediagrammer), og en live editor (f.eks. [denne](https://plantuml-editor.kkeisuke.com/))

##### Eksempel

````
```plantuml
class Object {
  - field 1
  # field 2
  ~ method1()
  + method2()
}
```
````

```plantuml
class Object {
  - field 1
  # field 2
  ~ method1()
  + method2()
}
```

## git

### Branching
- Vi oppretter en ny branch når vi jobber med issues, med mindre kun en liten endring skal til.
- Relaterte issues kan løses på samme branch.
- Vi ønsker ikke at branches skal vokse seg store. Som hovedregel skal en branch ha relativt kort levetid før den merges med master.
    - Større utviklingsoppgaver deles helst inn i mindre issues, som iterativt merges med master etter hvert som de blir løst.
- Brancher som ikke lenger brukes skal slettes.
- Det er lurt å hente siste endringer fra master før man lager ny branch.

### Merging
- Når et issue er løst - og både tester og kodesjekker bestås - ønsker vi å merge en branch med master.
- Med unntak av ved små endringer, ønsker vi at et annet gruppemedlem reviewer merge requesten i GitLab.

##### Mini-oppskrift

###### 1. git-kommandoer
```
git checkout branch         # Bekreft at du er på ønsket branch
git status                  # Bekreft at siste commits er gjort
git log                     # Se over commits hvis ønskelig

git pull origin master      # Hent eventuelle endringer fra master inn i branch
                            # Frivillig: evt. konflikter kan løses, men det er ikke nødvendig på dette stadiet
```

###### 2. Kodesjekk
``` 
mvn verify                  # Kjør alle tester og kodesjekker
```

###### 3. Merge request (GitLab)
- Gå til oversikt over branches og klikk "merge request".
- Lag en god overskrift og gjerne en kort beskrivelse.
- Velg et annet gruppemedlem som "assignee" og "reviewer".
- Legg til eventuell labels og milestone.

###### 4. Review merge request (GitLab)
- Den som reviewer ser snarlig gjennom endringene og vurderer kodekvaliteten.
- Eventuelle konflikter løses etter skjønn og gjerne i samråd med flere i gruppa.
- Det er greit å ikke godkjenne kode ved mangler. Dette løses gjennom samtaler og samarbeid for å sikre så god kvalitet på prosjektet,
- Godkjent kode merges snarlig.

### Commits
- Commits brukes for å markere en fremgang i koden, slik at det også er mulig å gå tilbake til den tilstanden.
- Det er mulig å gjøre flere lokale commits før alt blir pushet til GitLab.
- Ingen commits skal pushes til master før alle tester er kjørt (se under).

#### Commit-meldinger
- Vi skriver commit-meldinger på norsk og hovedmeldingen skal være kort.
- Commit-meldinger skal inneholde noe konkret om hva som er gjort.
    - "La til ny funksjonalitet", "Fikset bug", "Liten endring" er eksempler på dårlige commit-meldinger.
    - "Laget metode for avkrysning av groceryItem i ListView", "Lagt til sekvensdiagram i restapi sin README" er eksempler på gode commit-meldinger.
- Det er en god idé å referere til en issue eller skrive mer utfyllende i footer om nødvendig.

##### Referere til en issue
- Dersom `#123` er en del av meldingen, vil committen linke til issue 123.
- `Closes #123` linker til issue 123 og lukker den automatisk. Da lagres samtidig historikk om hvilken commit som løste en issue.

##### Flere forfattere / footer
- Første linjen i en commit er hovedmeldingen. Dersom man legger til to linjeskift og skriver noe _under_ de to linjeskiftene (dvs. på linje 4), så legger man til footer.
- "Medforfatter: @brukernavn" bør brukes i footer hvis flere har jobbet sammen (hvor @brukernavn er brukernavnet på GitLab).

#### Tester
- Vi bruker tester for å unngå bug og sikre høy kodekvalitet.
- Alle tester skal kjøres før endringer pushes eller merges til master.
- Før bidrag pushed bør man prøve løse eventuelle "problems" VSCode rapporterer om.

```
mvn test                    # kjører alle tester
mvn checkstyle:check        # sjekker kodestil
mvn spotbugs:check          # leter etter mulige bugs
```

## Slack
- Vi bruker Slack for planlegging av møter og annen lavterskel kommunikasjon.
- Vi ønsker hjelpe hverandre og ingen issue er for stor eller liten til å diskutere over Slack eller i et gruppemøte.