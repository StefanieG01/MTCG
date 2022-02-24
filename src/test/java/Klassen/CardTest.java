package Klassen;

import app.model.Card;
import app.model.User;
import app.model.cardType;
import app.model.elementType;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CardTest
{
    Card exampleCard;

    @BeforeEach
    void addNewCard()
    {
        exampleCard = new Card("1", "WaterSpell", 20);
    }

    @Test
    void cardTypeTest()
    {
        //arrange
        exampleCard.setCardType(cardType.ELF);
        //act
        exampleCard.setCardType(cardType.SPELL);
        //assert
        assertEquals(exampleCard.getCardType(), cardType.SPELL);
    }

    @Test
    void elementTypeTest()
    {
        //arrange
        exampleCard.setElementType(elementType.FIRE);
        //act
        exampleCard.setElementType(elementType.WATER);
        //assert
        assertEquals(exampleCard.getElementType(), elementType.WATER);
    }

    @Test
    void getterID() // no setter
    {
        //arrange

        //act

        //assert
        assertEquals(exampleCard.getId(), "1");
    }

    @Test
    void getterName() // no setter
    {
        //arrange

        //act

        //assert
        assertEquals(exampleCard.getName(), "WaterSpell");
    }

    @Test
    void changeUsernameTest()
    {
        //arrange
        User user = new User("Peppi", "PeppisPW");
        //act
        exampleCard.setUsername(user.getUsername());
        //assert
        assertEquals(exampleCard.getUsername(), "Peppi");
    }

    @Test
    void changeDeckTest()
    {
        //arrange
        boolean newBoolean = true;
        //act
        exampleCard.setDeck(newBoolean);
        //assert
        assertEquals(exampleCard.isDeck(), newBoolean);
    }

}
