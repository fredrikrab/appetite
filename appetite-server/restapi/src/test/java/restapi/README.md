# Testkode for REST-tjenesten (restapi)

Denne mappen inneholder test for REST-API-et til Appetite. Denne testen bruker `MockMvc`verktøyet for å utføre testene. Spring MockMvc brukes til å utføre integrasjonstesting av Spring webmvc-kontrolleren, og er en del av Spring MVC-testrammeerket som hjelper med å teste kontrollerlogikk. At testen er en itegrasjonstest betyr at man kombinerer individuelle moduler, og tester disse som en gruppe. Integrasjonstesting utføres for å evluere om et system eller komponenter samsvarer med spesifikke fuksjonalitetskrav. 

Vi bruker jUnit, som er et rammeverk som brukes for å teste de minste delene av kode. Dette inkluderer i stor grad å bruke `org.junit.jupiter.api.Assertions` i testene. 

## Hva testes
Denne testen inneholder en test, `checkContent`, som sjekker om ulike metoder i **SpringController** utfører den fuksjonaliteten de skal. MockMvc inneholder en del metoder, for eksepmel `MockMvcRequestBuilders`, `MockMvcResultMatchers`, osv. som innkapsler nettapplikasjon-JavaBeans og gjør dem tilgjengelige for testing. JavaBeans er en spesialkonstruert klasse i Java som er kodet i henhold til JavaBeans API-spesifikasjoner. 

**ApiTest** sjekker at klassen sender riktig verdi til server, og at når det gjøres endringer på informasjoene for eksempel en handleliste inneholder, blir dette også sendt op oppdattert riktig på server. 

Under ligger noen spesifikke tilfeller vi har testet for i klassen:

### checkContent
- Her har vi et utdrag av hvordan testkoden fungerer. Akkurat dette utdraget sjekker at server har riktige verdier for innnholdet i listen:
<!-- String expectedString = "{\"name\":\"Apitest\"," + 
                        "\"groceryItems\":" +
                            "[{\"groceryText\":\"Chocolate\",\"groceryUnits\":1,\"checked\":false}," +
                            "{\"groceryText\":\"Ice cream\",\"groceryUnits\":5,\"checked\":false}," +
                            "{\"groceryText\":\"More candy\",\"groceryUnits\":42,\"checked\":true}]}";

Assertions.assertEquals(expectedString, mockMvc.perform(MockMvcRequestBuilders
                                                .get("/shoppinglist/get/"+shoppingList.getName())
                                                .accept(MediaType.APPLICATION_JSON))
                                                .andExpect(MockMvcResultMatchers.status().isOk())
                                                .andReturn()
                                                .getResponse()
                                                .getContentAsString()); -->