package app.service;

import app.model.Card;
import app.model.Game;
import app.model.User;

import java.sql.Connection;
import java.util.Random;

public class GameService
{
    Connection conn;
    private User waitingUser;
    public CardService cardService;

    public GameService(Connection conn)
    {
        this.conn = conn;
    }

    //public CardService cardService = new CardService(conn);

    public String registerForBattle(User user)
    {
        if(waitingUser == null)
        {
            waitingUser = user;
            return "Waiting for other player";
        }
        else
        {
            User tmp = waitingUser;
            waitingUser = null;
            return fight(tmp, user);
        }
    }

    public String fight(User user1, User user2)
    {
        Game game = new Game();
        UserService userService = new UserService(conn);
        CardService cardService = new CardService(conn);
        Random rand = new Random();
        String log = "\n\t\tBATTLE START";

        //System.out.println(userService.getDeck(user1).size());

        while(userService.getDeck(user1).size() != 0 && userService.getDeck(user2).size() != 0 && game.getRoundNumber() < 100)
        {
            Card card1 = userService.getDeck(user1).get(rand.nextInt(userService.getDeck(user1).size()));
            Card card2 = userService.getDeck(user2).get(rand.nextInt(userService.getDeck(user2).size()));

            log += game.fight(user1, card1, user2, card2) + "\n\n";
            cardService.updateUsername(card1);
            cardService.updateUsername(card2);

            if(game.getRoundNumber() == 50)
            {
                log += "IT'S ROUND 50 => YOU'RE NOW GETTING THE SPECIAL CARD!";
                cardService.addSpecialCard(user1, user2);
            }
        }
        userService.updateStats(user1);
        userService.updateStats(user2);



        return log;
    }
}
