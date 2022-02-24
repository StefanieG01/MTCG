package app.service;

import app.model.Card;
import app.model.User;
import app.model.cardType;
import app.model.elementType;

import javax.print.attribute.HashPrintRequestAttributeSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CardService
{
    private List<Card[]> cardList;
    Connection conn;

    public CardService(Connection conn)
    {
        this.conn = conn;
        try{
            Statement stat = conn.createStatement();
            String createStatement = "CREATE TABLE IF NOT EXISTS cards(id VARCHAR(255) NOT NULL UNIQUE, name VARCHAR(255) NOT NULL, cardType VARCHAR(255) NOT NULL, elementType VARCHAR (255) NOT NULL, damage INTEGER, username VARCHAR(255) NULL, deck BOOLEAN NULL, PRIMARY KEY(id))";
            stat.executeUpdate(createStatement);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }

        //cardList = new ArrayList<>();
    }

    public void addCards(Card[] cards)
    {
        for (Card card: cards)
        {
            if (card.getName().contains("Spell"))
            {
                card.setCardType(cardType.SPELL);
            } else if (card.getName().contains("Troll"))
            {
                card.setCardType(cardType.TROLL);
            } else if (card.getName().contains("Goblin"))
            {
                card.setCardType(cardType.GOBLIN);
            } else if (card.getName().contains("Dragon"))
            {
                card.setCardType(cardType.DRAGON);
            } else if (card.getName().contains("Wizzard"))
            {
                card.setCardType(cardType.WIZZARD);
            } else if (card.getName().contains("Ork"))
            {
                card.setCardType(cardType.ORK);
            } else if (card.getName().contains("Knight"))
            {
                card.setCardType(cardType.KNIGHT);
            } else if (card.getName().contains("Kraken"))
            {
                card.setCardType(cardType.KRAKEN);
            } else if (card.getName().contains("Elf"))
            {
                card.setCardType(cardType.ELF);
            }


            if (card.getName().contains("Water"))
            {
                card.setElementType(elementType.WATER);
            } else if (card.getName().contains("Fire"))
            {
                card.setElementType(elementType.FIRE);
            } else
            {
                card.setElementType(elementType.REGULAR);
            }

            try{
                PreparedStatement stat = conn.prepareStatement("INSERT INTO cards(id, name, cardType, elementType, damage, username, deck) VALUES (?,?,?,?,?,?,?)");
                stat.setString(1, card.getId());
                stat.setString(2, card.getName());
                stat.setString(3, card.getCardType().name());
                stat.setString(4, card.getElementType().name());
                stat.setInt(5, card.getDamage());
                stat.setString(6, null);
                stat.setBoolean(7, false);
                stat.executeUpdate();
            }
            catch(Exception e)
            {
                System.out.println(e);
            }

        }

        //this.cardList.add(cards);
    }

    public String acquireCards(User user)
    {
        try{
            Statement stat1 = conn.createStatement();
            String stm = "SELECT COUNT(*) AS foundFreeCards FROM cards WHERE username IS NULL";
            ResultSet result1 = stat1.executeQuery(stm);
            result1.next();

                int foundFreeCards = result1.getInt("foundFreeCards");

                if(foundFreeCards <= 4)
                {
                    return "Failure! Not enough packages/cards available to buy!";
                }



            PreparedStatement stat = conn.prepareStatement("UPDATE cards SET username = ? WHERE CTID IN (SELECT CTID FROM cards WHERE username IS NULL LIMIT 5);");
            stat.setString(1, user.getUsername());
            stat.executeUpdate();

            PreparedStatement stat2 = conn.prepareStatement("UPDATE users SET coins = coins - 5 WHERE username = ?");
            stat2.setString(1, user.getUsername());
            stat2.executeUpdate();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }

        return "Successfully added Package to Stack";
        /* Card[] tmp = this.cardList.get(0);
        this.cardList.remove(0);
        return tmp;*/
    }

    public List<Card[]> getCards() {
        return cardList;
    }


    public void updateUsername(Card card)
    {
        try
        {
            /*System.out.println("\ncard-username: " + card.getUsername());
            System.out.println("\ncard-deck: " + card.isDeck());
            System.out.println("\ncard-id: " + card.getId());*/
            PreparedStatement stat1 = conn.prepareStatement("UPDATE cards SET username = ? WHERE id = ?");
            stat1.setString(1, card.getUsername());
            stat1.setString(2, card.getId());
            stat1.executeUpdate();

        } catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public void addSpecialCard(User user1, User user2)
    {
        Card card1 = new Card(getRandomString(), "SpecialWinnerCard", 150);
        try{
            PreparedStatement stat1 = conn.prepareStatement("INSERT INTO cards(id, name, cardType, elementType, damage, username, deck) VALUES (?,?,?,?,?,?,?)");
            stat1.setString(1, card1.getId());
            stat1.setString(2, card1.getName());
            stat1.setString(3, cardType.SPELL.name());
            stat1.setString(4, elementType.REGULAR.name());
            stat1.setInt(5, card1.getDamage());
            stat1.setString(6, user1.getUsername());
            stat1.setBoolean(7, true);
            stat1.executeUpdate();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }

        Card card2 = new Card(getRandomString(), "SpecialWinnerCard", 150);
        try{
            PreparedStatement stat2 = conn.prepareStatement("INSERT INTO cards(id, name, cardType, elementType, damage, username, deck) VALUES (?,?,?,?,?,?,?)");
            stat2.setString(1, card2.getId());
            stat2.setString(2, card2.getName());
            stat2.setString(3, cardType.SPELL.name());
            stat2.setString(4, elementType.REGULAR.name());
            stat2.setInt(5, card2.getDamage());
            stat2.setString(6, user2.getUsername());
            stat2.setBoolean(7, true);
            stat2.executeUpdate();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    public void deleteSpecialCard(String username1, String username2)
    {
        try{
            PreparedStatement stat1 = conn.prepareStatement("DELETE from cards WHERE name = 'SpecialWinnerCard' AND username = ?");
            stat1.setString(1, username1);
            stat1.executeUpdate();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        try{
            PreparedStatement stat2 = conn.prepareStatement("DELETE from cards WHERE name = 'SpecialWinnerCard' AND username = ?");
            stat2.setString(1, username2);
            stat2.executeUpdate();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    public String getRandomString()
    {
        String randomString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                            + "0123456789"
                            + "abcdefghijklmnopqrstuvwxyz";

        StringBuilder sb = new StringBuilder(10);

        for (int i = 0; i < 10; i++) {

            // generate a random number between 0 and 10
            int index
                    = (int)(randomString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(randomString
                    .charAt(index));
        }

        return sb.toString();
    }

}
