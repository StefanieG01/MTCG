package Klassen;

import app.model.User;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserTest
{
    //User exampleUser = new User("superTester123", "sicheresPasswort1");

    User exampleUser;
    @BeforeEach
    void addNewUser()
    {
        exampleUser = new User("superTester123", "sicheresPasswort1");
    }

    @Test
    void testName()
    {
        //arrange
        exampleUser.setName("PipiLangstrumpf");
        //act
        exampleUser.setName("Wickie");
        //assert
        assertEquals(exampleUser.getName(), "Wickie");
    }

    @Test
    void testCoins()
    {
        //arrange
        int coins = 10;
        //act
        exampleUser.setCoins(coins);
        //assert
        assertEquals(exampleUser.getCoins(), coins);
    }

    @Test
    void eloValue()
    {
        //arrange
        int eloValue = 95;
        //act
        exampleUser.setEloValue(eloValue);
        //assert
        assertEquals(exampleUser.getEloValue(), eloValue);
    }

    @Test
    void testBio()
    {
        //arrange
        String bio = "am debuggen...";
        //act
        exampleUser.setBio(bio);
        //assert
        assertEquals(exampleUser.getBio(), bio);
    }

    @Test
    void testImage()
    {
        //arrange
        String image = "=)";
        //act
        exampleUser.setImage(image);
        //assert
        assertEquals(exampleUser.getImage(), image);
    }

    @Test
    void testWonGames()
    {
        //arrange
        int wonGames = 8;
        //act
        exampleUser.setWonGames(wonGames);
        //assert
        assertEquals(exampleUser.getWonGames(), wonGames);
    }

    @Test
    void testLostGames()
    {
        //arrange
        int lostGames = 3;
        //act
        exampleUser.setLostGames(lostGames);
        //assert
        assertEquals(exampleUser.getLostGames(), lostGames);
    }

}
   /* void testUser()
    {

        //arrange
        // --> @BeforeEach
        //User exampleUser = new User("superTester123", "sicheresPasswort1");

        //act
        String uName = exampleUser.getUsername();
        String pw = exampleUser.getPassword();
        int elo = exampleUser.getEloValue();
        int coins = exampleUser.getCoins();

        //assert            test getter
        assertEquals(uName, "superTester123");
        assertEquals(pw, "sicheresPasswort1");
        assertEquals(elo, 100);
        assertEquals(coins, 20);

        //act
        exampleUser.setEloValue(90);
        exampleUser.setCoins(25);
        exampleUser.setPassword("sicheresPasswort2");

        //assert            // test setter
        assertEquals(exampleUser.getEloValue(), 90);
        assertEquals(exampleUser.getCoins(), 25);
        assertEquals(exampleUser.getPassword(), "sicheresPasswort2");

    /*void testUserSetter()
    {
        //act
        exampleUser.setEloValue(90);
        exampleUser.setCoins(25);
        exampleUser.setPassword("sicheresPasswort2");

        //assert
        assertEquals(exampleUser.getEloValue(), 90);
        assertEquals(exampleUser.getCoins(), 25);
        assertEquals(exampleUser.getPassword(), "sicheresPasswort1");
    }
    }
}
        */