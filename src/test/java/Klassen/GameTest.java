package Klassen;

import app.model.*;
import org.junit.jupiter.api.*;

import java.util.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameTest
{
    Game game;
    User user1;
    User user2;

    @BeforeEach
    void newGameTest()
    {
        game = new Game();
        user1 = new User("Hansi", "Sicher1");
        user2 = new User("Franzi", "Sicher2");
    }

    @Test
    void monsterFightTest1()
    {
        //arrange
        Card card1 = new Card("1", "WaterGoblin", 10);
        card1.setCardType(cardType.GOBLIN);
        card1.setElementType(elementType.WATER);

        Card card2 = new Card("2", "FireTroll", 15);
        card2.setCardType(cardType.TROLL);
        card2.setElementType(elementType.FIRE);

        //act
        game.fight(user1, card1, user2, card2);

        //assert            TROLL defeats GOBLIN => user 2 wins
        assertEquals(game.getRoundNumber(), 1);

        assertEquals(user1.getLostGames(), 1);
        assertEquals(user1.getWonGames(), 0);
        assertEquals(user1.getEloValue(), 95);

        assertEquals(user2.getLostGames(), 0);
        assertEquals(user2.getWonGames(), 1);
        assertEquals(user2.getEloValue(), 103);

        assertEquals(card1.getUsername(), user2.getUsername());
    }

    @Test
    void monsterFightTest2()
    {
        //arrange
        Card card1 = new Card("1", "FireTroll", 15);
        card1.setCardType(cardType.TROLL);
        card1.setElementType(elementType.FIRE);

        Card card2 = new Card("2", "WaterGoblin", 10);
        card2.setCardType(cardType.GOBLIN);
        card2.setElementType(elementType.WATER);

        //act
        game.fight(user1, card1, user2, card2);

        //assert            TROLL defeats GOBLIN => user 1 wins
        assertEquals(game.getRoundNumber(), 1);

        assertEquals(user1.getLostGames(), 0);
        assertEquals(user1.getWonGames(), 1);
        assertEquals(user1.getEloValue(), 103);

        assertEquals(user2.getLostGames(), 1);
        assertEquals(user2.getWonGames(), 0);
        assertEquals(user2.getEloValue(), 95);

        assertEquals(card2.getUsername(), user1.getUsername());
    }

    @Test
    void spellFight1()
    {
        //arrange
        Card card1 = new Card("1", "FireSpell", 10);
        card1.setCardType(cardType.SPELL);
        card1.setElementType(elementType.FIRE);

        Card card2 = new Card("2", "WaterSpell", 20);
        card2.setCardType(cardType.SPELL);
        card2.setElementType(elementType.WATER);

        //act
        game.fight(user1, card1, user2, card2);

        //assert            WaterSpell wins => user 2 wins
        assertEquals(game.getRoundNumber(), 1);

        assertEquals(user1.getLostGames(), 1);
        assertEquals(user1.getWonGames(), 0);
        assertEquals(user1.getEloValue(), 95);

        assertEquals(user2.getLostGames(), 0);
        assertEquals(user2.getWonGames(), 1);
        assertEquals(user2.getEloValue(), 103);

        assertEquals(card1.getUsername(), user2.getUsername());
    }


    @Test
    void spellFight2()
    {
        //arrange
        Card card1 = new Card("1", "FireSpell", 20);
        card1.setCardType(cardType.SPELL);
        card1.setElementType(elementType.FIRE);

        Card card2 = new Card("2", "WaterSpell", 05);
        card2.setCardType(cardType.SPELL);
        card2.setElementType(elementType.WATER);

        //act
        game.fight(user1, card1, user2, card2);

        //assert            DRAW
        assertEquals(game.getRoundNumber(), 1);

        assertEquals(user1.getLostGames(), 0);
        assertEquals(user1.getWonGames(), 0);
        assertEquals(user1.getEloValue(), 100);

        assertEquals(user2.getLostGames(), 0);
        assertEquals(user2.getWonGames(), 0);
        assertEquals(user2.getEloValue(), 100);
    }

    @Test
    void spellFight3()
    {
        //arrange
        Card card1 = new Card("1", "FireSpell", 90);
        card1.setCardType(cardType.SPELL);
        card1.setElementType(elementType.FIRE);

        Card card2 = new Card("2", "WaterSpell", 05);
        card2.setCardType(cardType.SPELL);
        card2.setElementType(elementType.WATER);

        //act
        game.fight(user1, card1, user2, card2);

        //assert            FireSpell wins => user1 wins
        assertEquals(game.getRoundNumber(), 1);

        assertEquals(user1.getLostGames(), 0);
        assertEquals(user1.getWonGames(), 1);
        assertEquals(user1.getEloValue(), 103);

        assertEquals(user2.getLostGames(), 1);
        assertEquals(user2.getWonGames(), 0);
        assertEquals(user2.getEloValue(), 95);
    }


    @Test
    void mixedFight1()
    {
        //arrange
        Card card1 = new Card("1", "FireSpell", 10);
        card1.setCardType(cardType.SPELL);
        card1.setElementType(elementType.FIRE);

        Card card2 = new Card("2", "WaterGoblin", 10);
        card2.setCardType(cardType.GOBLIN);
        card2.setElementType(elementType.WATER);

        //act
        game.fight(user1, card1, user2, card2);

        //assert            WaterGoblin wins => user2 wins
        assertEquals(game.getRoundNumber(), 1);

        assertEquals(user1.getLostGames(), 1);
        assertEquals(user1.getWonGames(), 0);
        assertEquals(user1.getEloValue(), 95);

        assertEquals(user2.getLostGames(), 0);
        assertEquals(user2.getWonGames(), 1);
        assertEquals(user2.getEloValue(), 103);
    }

    @Test
    void mixedFight2()
    {
        //arrange
        Card card1 = new Card("1", "WaterSpell", 10);
        card1.setCardType(cardType.SPELL);
        card1.setElementType(elementType.WATER);

        Card card2 = new Card("2", "WaterGoblin", 10);
        card2.setCardType(cardType.GOBLIN);
        card2.setElementType(elementType.WATER);

        //act
        game.fight(user1, card1, user2, card2);

        //assert            DRAW
        assertEquals(game.getRoundNumber(), 1);

        assertEquals(user1.getLostGames(), 0);
        assertEquals(user1.getWonGames(), 0);
        assertEquals(user1.getEloValue(), 100);

        assertEquals(user2.getLostGames(), 0);
        assertEquals(user2.getWonGames(), 0);
        assertEquals(user2.getEloValue(), 100);
    }

    @Test
    void mixedFight3()
    {
        //arrange
        Card card1 = new Card("1", "RegularSpell", 10);
        card1.setCardType(cardType.SPELL);
        card1.setElementType(elementType.REGULAR);

        Card card2 = new Card("2", "WaterGoblin", 10);
        card2.setCardType(cardType.GOBLIN);
        card2.setElementType(elementType.WATER);

        //act
        game.fight(user1, card1, user2, card2);

        //assert            RegularSpell wins => user1 wins
        assertEquals(game.getRoundNumber(), 1);

        assertEquals(user1.getLostGames(), 0);
        assertEquals(user1.getWonGames(), 1);
        assertEquals(user1.getEloValue(), 103);

        assertEquals(user2.getLostGames(), 1);
        assertEquals(user2.getWonGames(), 0);
        assertEquals(user2.getEloValue(), 95);
    }

    @Test
    void mixedFight4()
    {
        //arrange
        Card card1 = new Card("1", "RegularSpell", 10);
        card1.setCardType(cardType.SPELL);
        card1.setElementType(elementType.REGULAR);

        Card card2 = new Card("2", "Knight", 15);
        card2.setCardType(cardType.KNIGHT);
        card2.setElementType(elementType.REGULAR);

        //act
        game.fight(user1, card1, user2, card2);

        //assert            Knight wins => user2 wins
        assertEquals(game.getRoundNumber(), 1);

        assertEquals(user1.getLostGames(), 1);
        assertEquals(user1.getWonGames(), 0);
        assertEquals(user1.getEloValue(), 95);

        assertEquals(user2.getLostGames(), 0);
        assertEquals(user2.getWonGames(), 1);
        assertEquals(user2.getEloValue(), 103);
    }
}
