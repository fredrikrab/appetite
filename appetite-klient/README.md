# appetite React-klient

- Frontend for Java server-applikasjon
- Kommuniserer via REST API
	- Begrenset funksjonalitet uten kobling til server
	- Begrenset lagring av lokal tilstand som kan sendes til server ved midlertidig tap av forbindelse
- Responsivt design
	- Tilpasser seg forskjellige skjermstørrelser
- Bygd med [Create React App](https://github.com/facebook/create-react-app).

## Organisering av koden

### App.js

App.js er utgangspunktet for hele applikasjonen.[^1] App.js eksporterer en [funksjonell React-komponent](https://reactjs.org/docs/components-and-props.html) som rendres i nettleseren. App-komponenten selv er bygd opp av flere delkomponenter (se under).

Tilstanden til React-appen gjenspeiler i hovedsak tilstanden på Java-serveren. Øyeblikkstilstanden er likevel lagret i App-komponenten via [React State](https://reactjs.org/docs/state-and-lifecycle.html) og oppdateres via [State Hook](https://reactjs.org/docs/hooks-state.html) og [Effect Hook](https://reactjs.org/docs/hooks-effect.html). App-komponenten inneholder alene appens globale tilstand og videresender nødvendig informasjon til andre delkomponenter.

Dersom applikasjonen mister forbindelsen til servereren vil den fortsatt huske lokal tilstand og alle eventuelle endringer. Disse vil bli forsøkt sendt til serveren dersom forbindelsen blir gjenopprettet, men vil forsvinne neste innlastning.

Til slutt inneholder App.js det meste av kjernelogikken til applikasjonen.

##### Skjermbilde av app med komopnenter, tilstand og hooks

<img src="/docs/react-components-and-state.png" width="600" height="887">


[^1]: React tar egentlig utgangspunkt i index.js, men denne filen gjør lite annet enn å kalle på App-komponenten som App.js eksporterer.

### Komponenter

Applikasjonen består av fire hovedkomponenter som utgår fra App-komponenten i App.js. Disse komponentene er igjen (hovedsaklig) bygd opp av flere mindre og spesialiserte komponenter. Denne oppdelingen er valgt for å gi en forståelig og oversiktlig struktur.

📦components  
 ┣ 📂Footer  
 ┃ ┗ 📜Footer.js  
 ┣ 📂Header  
 ┃ ┣ 📜Header.js  
 ┃ ┗ 📜MenuButton.js  
 ┣ 📂Navigation  
 ┃ ┣ 📜CreateShoppingList.js  
 ┃ ┣ 📜DropdownSelect.js  
 ┃ ┗ 📜Navigation.js  
 ┗ 📂ShoppingList  
 ┃ ┣ 📜AddGrocery.js  
 ┃ ┣ 📜AnimationWrapper.js  
 ┃ ┣ 📜Button.js  
 ┃ ┣ 📜Checkbox.js  
 ┃ ┣ 📜Grocery.js  
 ┃ ┣ 📜ShoppingList.js  
 ┃ ┗ 📜ShoppingListTitle.js

#### - Header

Inneholder logo og meny-knapp.

#### - Navigation

Utgjør menyen som vises ved klikk på meny-knapp. Inneholder rullegardingmeny for bytte av handleliste og funksjonalitet for å opprette ny handleliste.

#### - ShoppingList

Viser valgt handleliste og har funksjonalitet for å legge til, fjerne og avkrysse varer.

#### - Footer

Viser tilkoblingsstatus og har funksjonalitet for å slette valgt handleliste.

### index.css

index.css inneholder nesten all [CSS](https://www.w3schools.com/Css/css_intro.asp) for applikasjonen. Kun unntaksvis blir CSS definert i React-komponentene. Strukturen i index.css gjenspeiler likefullt oppbyggingen av applikasjonen med komponenter. For eksempel så er navnet på [CSS-klassene](https://www.w3schools.com/cssref/sel_class.asp) nær identisk med navnene på React-komponentene.

## Kodekvalitet

### ESLint

Vi har brukt [ESLint](https://eslint.org/) med [eslint-plugin-react](https://www.npmjs.com/package/eslint-plugin-react) for å oppdage feil, dårlig kodepraksis og sikre at gruppen skriver kode med lik stil. Dette er noe tilsvarende bruk av [SpotBugs](https://spotbugs.readthedocs.io/en/stable/maven.html) og [CheckStyle](https://maven.apache.org/plugins/maven-checkstyle-plugin/) som blir brukt med Maven på Java-backend.

For å genrere rapporten kjøres `npx eslint ./*.js --ext .js -o eslint-rapport.html -f html` inne i [src-mappa](/appetite-klient/src/). Da vil `eslint-rapport.html`å denne filen vil ligge inne i [src-mappa](/appetite-klient/src/).  

### Tester
Applikasjonen bruker Cypress til å teste nettsiden med. For å kunne kjøre testene må man først starte springboot serveren og deretter starte selve applikasjonen (se [Kjøring av applikasjon](#Kjoring-av-applikasjon)). Deretter bruker man npm test til å kjøre cypress appen og testene (applikasjonen må være kjørende mens testene kjører). 
[Cypress](https://www.cypress.io/). 

### Hva testes
I disse testene testes de grunnleggende funksjonene av appen. Når testen startes i cypress simuleres det en kjøring av applikasjonen hvor det lages en ny midlertidig handleliste som brukes gjennom testene. Testen legger først til melkesjokoladekjeks, for så å trykke på "legg til" knappen. Og gjør det samme med melk. Så sjekker den at melk og melkesjokoladekjeks har blitt lagt til i listen. Tilslutt trykker den på slett handleliste, og sletter den midlertidige handlelisten som har blitt laget i testene. 


## Kjøring av applikasjon

#### 1. Installer [npm](https://www.npmjs.com/)

  
#### 2. `npm install` i prosjektmappen

Dette installerer nødvendige avhengigheter for å kjøre applikasjonen. Avhengighetene er definert i filen package-lock.json og kan endre seg etter hvert som appen blir utviklet.

Når man kjører kommandoen vil det dukke opp en rekke sårbarheter, og det er dessverre slik npm fungerer siden v6. Merk at du kan kjøre npm install --no-audit for å undertrykke dem.

#### 3. `npm start`

Starter appen som blir tilgjengelig på [http://localhost:3000](http://localhost:3000) i nettleseren. Ment til å brukes under utvikling.

### `npm test`

Kjører [tester](#Tester) i Cypress for lokal bruk. NB: i gitpod brukes `npm run cypress-cli` for å kjøre [tester](#Tester).


### `npm run build`

Bygger appen i `build`-mappen som er ment til å brukes av sluttbrukere. Denne finnes [her](/appetite-klient/build/index.html).

### `npm audit --production`

npm-audit er ødelagt for frontend-verktøy etter design. Dene kommandoen vil dermed generere en rekke med sårbarheter i react, som ikke nødvendigvis er realterte til programmet vårt. De samme sårbarhetene vil dukke opp når en kjører npm intall, se over. 