package app.service;

import app.model.Card;
import app.model.User;

import java.sql.*;
import java.util.*;
import java.util.concurrent.ExecutionException;

import app.model.cardType;
import server.Request;

public class UserService
{

    Connection conn = null;

    private List<User> userData;

    public UserService(Connection conn) {
        try{
            this.conn = conn;
            Statement stat = conn.createStatement();
            String createStatement = "CREATE TABLE IF NOT EXISTS users(username VARCHAR(255) NOT NULL UNIQUE,name VARCHAR(255), password varchar(255) NOT NULL, coins INTEGER, eloValue INTEGER, bio VARCHAR(255), image VARCHAR(255), lostGames INTEGER, wonGames InTEGER, token VARCHAR(255) NOT NULL, PRIMARY KEY(username))";
            stat.executeUpdate(createStatement);


        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        userData = new ArrayList<>();
        userData.add(new User("Stefanie", "SuperSicher1"));
    }

    // GET /users/:username     --> zeigt einzelne User an
    public User getUser(String username)
    {

        try{
            PreparedStatement prepStat = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
            prepStat.setString(1, username);

            ResultSet result = prepStat.executeQuery();
            while(result.next())
            {
                User user = new User(result.getString("username"), result.getString("password"));
                user.setCoins(result.getInt("coins"));
                user.setEloValue(result.getInt("eloValue"));
                user.setToken(result.getString("token"));
                user.setImage(result.getString("image"));
                user.setBio(result.getString("bio"));
                user.setWonGames(result.getInt("wonGames"));
                user.setLostGames(result.getInt("lostGames"));

                return user;

            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }

        /*User foundUser = userData.stream()
                .filter(user -> username.equals(user.getUsername()))
                .findAny()
                .orElse(null);*/
        return null;
    }

    // GET /users       --> zeigt alle User an
    public List<User> getUser() {

        List<User> userList = new ArrayList<>();

        try{
            Statement stat = conn.createStatement();
            String statement = "SELECT * FROM users";
            ResultSet result = stat.executeQuery(statement);
            while(result.next())
            {
                User user = new User(result.getString("username"), result.getString("password"));
                user.setCoins(result.getInt("coins"));
                user.setEloValue(result.getInt("eloValue"));
                user.setToken(result.getString("token"));
                user.setImage(result.getString("image"));
                user.setBio(result.getString("bio"));
                user.setWonGames(result.getInt("wonGames"));
                user.setLostGames(result.getInt("lostGames"));

                userList.add(user);

            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }

        return userList;
    }

    // POST /users          --> fügt User hinzu
    public String addUser(User user) {
        user.setCoins(20);
        user.setEloValue(100);
        user.setToken(user.getUsername() + "-mtcgToken");
        user.setLostGames(0);
        user.setWonGames(0);
        user.setBio("");
        user.setImage("");
        user.setName("");


        try{
            PreparedStatement stat = conn.prepareStatement("SELECT COUNT(*) AS foundUsers FROM users WHERE username = ?");
            stat.setString(1, user.getUsername());
            ResultSet result = stat.executeQuery();
            if(result.next())
            {
                int foundUsers = result.getInt("foundUsers");

                if(foundUsers >= 1)
                {
                    return "Failure! This username already exists in database. Please choose another username!";
                }
            }

        }
        catch(Exception e)
        {
            System.out.println(e);
        }

        try{
            PreparedStatement stat = conn.prepareStatement("INSERT INTO users (username, name, password, coins, eloValue, bio, image, lostGames, wonGames, token) VALUES (?,'',?,?,?,'', '', 0,0, ?)");
            stat.setString(1, user.getUsername());
            stat.setString(2, user.getPassword());
            stat.setInt(3, user.getCoins());
            stat.setInt(4, user.getEloValue());
            stat.setString(5, user.getToken());
            stat.executeUpdate();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }

        //userData.add(user);
        return "Successfully added user.";
    }

    // POST /sessions       --> login
    public boolean login(User user)
    {
        try{
            PreparedStatement stat = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
            stat.setString(1, user.getUsername());
            stat.setString(2, user.getPassword());
            return stat.executeQuery().next();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }


        /*for(User u : userData)
        {

            if(u.getUsername().equals(user.getUsername()) && u.getPassword().equals(user.getPassword()))
            {
                return true;
            }
        }*/
        return false;
    }

    public List<Card> getStack(User user)
    {
        List<Card> cardsInStack = new ArrayList<>();

        try{
            PreparedStatement stat = conn.prepareStatement("SELECT * FROM cards WHERE username = ?");
            stat.setString(1, user.getUsername());
            ResultSet result = stat.executeQuery();

            while(result.next())
            {
                Card card = new Card(result.getString(1), result.getString(2), result.getInt(5));
                String cardType = result.getString(3);
                card.setCardType(app.model.cardType.valueOf(cardType));
                String elementType = result.getString(4);
                card.setElementType(app.model.elementType.valueOf(elementType));

                cardsInStack.add(card);
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        return cardsInStack;
    }

    public List<Card> getDeck(User user)
    {
        List<Card> cardsInDeck = new ArrayList<>();

        try{
            PreparedStatement stat = conn.prepareStatement("SELECT * FROM cards WHERE username = ? AND deck = true");
            stat.setString(1, user.getUsername());
            ResultSet result = stat.executeQuery();

                while(result.next())
                {
                    Card card = new Card(result.getString(1), result.getString(2), result.getInt(5));
                    String cardType = result.getString(3);
                    card.setCardType(app.model.cardType.valueOf(cardType));
                    String elementType = result.getString(4);
                    card.setElementType(app.model.elementType.valueOf(elementType));
                    String username = result.getString(6);
                    card.setUsername(username);
                    card.setDeck(true);

                    cardsInDeck.add(card);
                }
                return cardsInDeck;


        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        return null;
        //return user.deck;
    }

    public boolean setDeck(User user, String[] ids)
    {
        int cntFoundCards = 0;
        int cntAvailableCards = 0;

        try{
// erst schauen, ob user bereits 4 karten als deck definiert hat
            PreparedStatement stat = conn.prepareStatement("SELECT COUNT(*) FROM cards WHERE username = ? AND deck = true");
            stat.setString(1, user.getUsername());
            ResultSet result = stat.executeQuery();
            result.next();
            cntFoundCards = result.getInt(1);

// schauen, ob der user auch alle angegebenen karten besitzt

            cntAvailableCards += checkIDSinStack(user, ids[0]);
            cntAvailableCards += checkIDSinStack(user, ids[1]);
            cntAvailableCards += checkIDSinStack(user, ids[2]);
            cntAvailableCards += checkIDSinStack(user, ids[3]);

            if(cntAvailableCards != 4)  // user hat nicht alle 4 karten in seinem stack
            {
                return false;
            }

            // user hat alle 4 karten in seinem stack --> sein altes deck wird erst gelöscht
            if(cntFoundCards > 0)
            {
                //System.out.println("decks false setzen");
                PreparedStatement stat6 = conn.prepareStatement("UPDATE cards SET deck = false WHERE username = ?");
                stat6.setString(1, user.getUsername());
                stat6.executeQuery();
            }

            // dann das neue gesetzt
            setNewDeck(ids[0]);
            setNewDeck(ids[1]);
            setNewDeck(ids[2]);
            setNewDeck(ids[3]);

            return true;

        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        return false;
    }

    int checkIDSinStack(User user, String id)
    {
        try{
            PreparedStatement stat2 = conn.prepareStatement("SELECT COUNT(*) FROM cards WHERE username = ? AND id = ?");
            stat2.setString(1, user.getUsername());
            stat2.setString(2, id);
            ResultSet result2 = stat2.executeQuery();
            result2.next();
            return result2.getInt(1);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return 0;
    }

    void setNewDeck(String id)
    {
        try{
            PreparedStatement stat2 = conn.prepareStatement("UPDATE cards SET deck = true WHERE id = ?");
            stat2.setString(1, id);
            stat2.executeUpdate();
            return;
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

        /*List<Card> stack = getStack(user);

        Card[] foundCards = stack.stream()
                .filter(card -> Arrays.stream(ids).anyMatch(id -> id.equals(card.getId())))
                .toArray(Card[]::new);

        if(foundCards.length !=4)
        {
            return false;
        }

        user.deck.clear();
        Collections.addAll(user.deck, foundCards);

        for(Card card : foundCards)
        {
            stack.remove(card);
        }
        return true;
    }*/

    public void updateProfile(String username, Map<String, String> input)
    {
        if(input.containsKey("Name"))
        {
            try{
                PreparedStatement stat1 = conn.prepareStatement("UPDATE users SET name = ? WHERE username = ?");
                stat1.setString(1, input.get("Name"));
                stat1.setString(2, username);
                stat1.executeUpdate();
            }
            catch (Exception e)
            {
                System.out.println(e);
            }
        }


        if(input.containsKey("Bio"))
        {
            try{
                PreparedStatement stat1 = conn.prepareStatement("UPDATE users SET bio = ? WHERE username = ?");
                stat1.setString(1, input.get("Bio"));
                stat1.setString(2, username);
                stat1.executeUpdate();
            }
            catch (Exception e)
            {
                System.out.println(e);
            }
        }

        if(input.containsKey("Image"))
        {
            try{
                PreparedStatement stat1 = conn.prepareStatement("UPDATE users SET image = ? WHERE username = ?");
                stat1.setString(1, input.get("Image"));
                stat1.setString(2, username);
                stat1.executeUpdate();
            }
            catch (Exception e)
            {
                System.out.println(e);
            }
        }
    }

    public void updateStats(User user)
    {
            try
            {
                PreparedStatement stat1 = conn.prepareStatement("UPDATE users SET eloValue = ?, wonGames = ?, lostGames = ? WHERE username = ?");
                stat1.setInt(1, user.getEloValue());
                stat1.setInt(2, user.getWonGames());
                stat1.setInt(3, user.getLostGames());
                stat1.setString(4, user.getUsername());

                stat1.executeUpdate();

            } catch (Exception e)
            {
                System.out.println(e);
            }
    }
}
