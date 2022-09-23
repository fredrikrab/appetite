# Testkode for handleliste-grensesnittet

Denne mappen inneholder tester basert på testFX for handleliste-controlleren og appen vår. 

<<<<<<< HEAD:appetite/ui/src/test/java/ui/README.md
Når appen starter opp inneholder grensesnittet ett felt hvor man kan skrive inn overskift for handlelisten øverst, og under finner man to testfelt: 1) et felt hvor man kan legge til navn på vare og 2) et felt for antall av varen. Under feltene hvor man kan legge til varer og antallet, ligger listen med varer som er lagt til fra før av. Til høyre i grensesnittet har grensesnittet to knapper, + for å legge til varer og - for å fjerne varer. 

## Hva testes
Når testen startes simuleres det en kjøring av appen hvor man legger til varer i handlelisten med det gitte navnet. Altså, hvis man skriver `sjokolade` i vare-feltet, og ikke legger til et spesifikt antall, for å så trykke på `+`-knappen, vil det legges til `1 sjokolade`i listen med elementer man ønsker å handle. Når du trykker på `+`-knappen skal feltet hvor man kan skrive inn vare tømmes, og elementet leggs til nederst i lista. Når man ønsker å fjerne et element, trykker man på elementet i lista man ønsker å fjerne, og så `-`-knappen, så skal elementet fjernes fra handlelista. 
=======
Når appen starter opp inneholder grensesnittet en felt hvor man kan skrive inn overskift for handlelisten øverst, og under finder man to testfelt: 1) et felt hvor man kan legge til navn på vare og 2) et felt for antall av varen. Under feltene hvor man kan legge til varer og antallet, ligger listen med varer som er lagt til fra før av. Til høyre i grensesnittet har grensesnittet to knapper, + for å legge til varer og - for å fjerne varer. 

## Hva testes
Når testen startes simuleres det en kjøring av appen hvor man legger til varer i handleisliten me det gitte navnet. Altså, hvis man skriver `sjokolade` i vare-feltet, og ikke legger til et spesifikt antall, for å så trykke på `+`-knappen, vil det legges til `1 sjokolade` i listen med elementer man ønsker å handle. Når du trykker på `+`-knappen skal feltet hvor man kan skrive inn vare tømmes, og elementet leggs til nederst i lista. Når man ønsker å fjerne et element, trykker man på elementet i lisa man ønsker å fjerne, og så `-`-knappen, så skal elementet fjernes fra handlelista. 
>>>>>>> react-dokumentasjon:appetite-server/ui/src/test/java/ui/README.md

Under ligger spesifikke tilfeller vi har testet for i klassen:

### addHeadLine
- `Uke 38` => `Uke 38`

### addGrocertyItem
- `sjokolade "1" +`=> `sjokolade 1`
- `sjokolade "" +`=> `sjokolade 1`
- `sjokomelk "4" +` => `sjokomelk 4`

### removeGroceryItem
- `sjokolade "" +`=> `sjokolade 1`
- `iskrem "" +`=> `iskrem 1`
- `sjokolade 1 -`=> `iskrem 1` //fjerner sjokolade så iskrem blir det øverste elementet i lista
- `saltstenger "" +`=> `saltstenger 1`
- `burger "" +`=> `burger 1`
- `saltstenger 1 -`=> `burger 1`//fjerner saltstenger så burger blir stående på saltstenger´s plass
