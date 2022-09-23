//Note when running the test: Both the server and the application must be running

describe("Integration test appetite", () => {
    it("user can add items to shoppinglist", () => {
        
        
        cy.visit('http://localhost:3000/');
        cy.wait(500);
        //Click on the menu if its not alreadu open
        cy.get(".menu").should("not.be.visible").then(() =>{
            cy.findByRole('button', { name: /meny/i }).click();
        })
        
        cy.wait(500);
        // Click on ny handleliste
        cy.findByRole('button', {  name: /ny handleliste/i}).click();
        const name = "Cypresshandleliste"
        //Click on the text input Navn på handleliste and type Cypresshandleliste
        cy.findByPlaceholderText("Navn på handleliste").type(name);
        //Click on opprett
        cy.findByRole('button', {  name: /opprett/i}).click();
        cy.wait(500)
        //Check that we enter the Cypresshoppinglist
        cy.contains(name)
        //Add the item Melkesjokoladekjeks
        const item1 = "Melkesjokoladekjeks"
        cy.findByPlaceholderText("Legg til vare").type(item1);
        // press the button Legg til
        cy.findByRole('button', {  name: /legg til/i}).click();
        // Do the same again, now just with milk and specify number of items to be 14
        const item2 = "Melk"
        const numberOfItems = "14"
        cy.findByPlaceholderText("Legg til vare").type(item2);
        cy.findByPlaceholderText("Antall").type(numberOfItems)
        cy.findByRole('button', {  name: /legg til/i}).click();
        
        //Assert that the two items has been added to the choopinglist
        cy.contains(item1)
        cy.contains(item2)
        //Assert that Melkesjokoladekjeks was added with 1 item automaticly.
        cy.get(".grocery-item").filter(':contains("Melkesjokoladekjeks")').should('contain', "1 stk")
        //Assert that Melk was added with 14 number of items. 
        cy.get(".grocery-item").filter(':contains("Melk")').should('contain', numberOfItems)
        //Add duplicate with Melkesjokoladekjeks and number of items to be 3.
        cy.findByPlaceholderText("Legg til vare").type(item1);
        cy.findByPlaceholderText("Antall").type("3");
        cy.findByRole('button', {  name: /legg til/i}).click();
        //Assert that the item added now has 4 number of items. 
        cy.get(".grocery-item").filter(':contains("Melkesjokoladekjeks")').should('contain', "4 stk")
        cy.wait(500)

        // Delete the list
        cy.findByRole('button', {  name: /slett handleliste/i}).click();
        cy.findByRole('button', {  name: /ja/i}).click();
        
    })
})

