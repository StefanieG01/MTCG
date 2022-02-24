package app.model;

import app.model.*;
import lombok.Getter;

public class Game
{
    @Getter
    private int roundNumber;
    private boolean gameOver;

    public Game()
    {
        this.roundNumber = 0;
        this.gameOver = false;
    }

    public String fight(User user1, Card card1, User user2, Card card2)
    {
        String battleLog = "";
        this.roundNumber++;
        battleLog += "\nRound Number: " + this.roundNumber;
        battleLog += "\n\t\t" + card1.getName() + " damage: " + card1.getDamage() + " from " + user1.getUsername() + "\tFIGHTS AGAINST\t" + card2.getName() + " damage: " +  card2.getDamage() + " from " + user2.getUsername();


        int tmpDamage1 = card1.getDamage();
        int tmpDamage2 = card2.getDamage();

        if(card1.getCardType() != cardType.SPELL && card2.getCardType() != cardType.SPELL)  // MONSTER FIGHT
        {
            String specialMonsterCheck = "";
            specialMonsterCheck += checkSpecialMonsterFight(card1, card2);
            specialMonsterCheck += checkSpecialMonsterFight(card2, card1);

            if(!specialMonsterCheck.equals(""))
            {
                battleLog += "\n" + specialMonsterCheck;
               return battleLog;
            }


            if(card1.getDamage() > card2.getDamage())   // User1 wins
            {
                battleLog += "\n" + changePoints(user1, card1, user2, card2);
            }
            else if(card1.getDamage() < card2.getDamage())  // User2 wins
            {
                battleLog += "\n" + changePoints(user2, card2, user1, card1);
            }
            else
            {
                battleLog += "\n\tDRAW";
            }
        }
        else
        {
            if (card1.getElementType() == elementType.WATER && card2.getElementType() == elementType.FIRE)
            {
                //changeTmpDamageValue(tmpDamage1, tmpDamage2);
                tmpDamage1 *= 2;
                tmpDamage2 /= 2;
            } else if (card1.getElementType() == elementType.FIRE && card2.getElementType() == elementType.REGULAR)
            {
                //changeTmpDamageValue(tmpDamage1, tmpDamage2);
                tmpDamage1 *= 2;
                tmpDamage2 /= 2;
            } else if (card1.getElementType() == elementType.REGULAR && card2.getElementType() == elementType.WATER)
            {
                //changeTmpDamageValue(tmpDamage1, tmpDamage2);
                tmpDamage1 *= 2;
                tmpDamage2 /= 2;
            } else if (card2.getElementType() == elementType.WATER && card1.getElementType() == elementType.FIRE)
            {
                //changeTmpDamageValue(tmpDamage2, tmpDamage1);
                tmpDamage2 *= 2;
                tmpDamage1 /= 2;
            } else if (card2.getElementType() == elementType.FIRE && card1.getElementType() == elementType.REGULAR)
            {
                //changeTmpDamageValue(tmpDamage2, tmpDamage1);
                tmpDamage2 *= 2;
                tmpDamage1 /= 2;
            } else if (card2.getElementType() == elementType.REGULAR && card1.getElementType() == elementType.WATER)
            {
                //changeTmpDamageValue(tmpDamage2, tmpDamage1);
                tmpDamage2 *= 2;
                tmpDamage1 /= 2;
            }

            if (tmpDamage1 > tmpDamage2)     // User1 wins
            {
                battleLog += "\n" +  changePoints(user1, card1, user2, card2);
            } else if (tmpDamage1 < tmpDamage2)    // User2 wins
            {
                battleLog += "\n" + changePoints(user2, card2, user1, card1);
            }
            else
            {
                battleLog += "\n\tDRAW";
            }
        }
        return battleLog;
    }

    public String changePoints(User winner, Card winnerCard, User loser, Card loserCard)
    {
        winner.setEloValue(winner.getEloValue() + 3);
        loser.setEloValue((loser.getEloValue()) - 5);
        winner.setWonGames(winner.getWonGames() + 1);
        loser.setLostGames(loser.getLostGames() + 1);

        //winner.deck.add(winnerCard);
        //loser.deck.remove(loserCard);
       // System.out.println("winner username: " + winner.getUsername());
        loserCard.setUsername(winner.getUsername());
        //loserCard.setDeck(false);
        //System.out.println("loser card username: " + loserCard.getUsername());
        //System.out.println("loser card deck " + loserCard.isDeck());

        return "\n\t\t" + (winnerCard.getName() + " from " + winner.getUsername() + " wins");
    }

    String checkSpecialMonsterFight(Card card1, Card card2)
    {
        if(card1.getCardType() == cardType.GOBLIN && card2.getCardType() == cardType.DRAGON)
        {
            return "\nYour Goblin is afraid of the Dragon, you can't attack it.";
        }
        else if(card1.getCardType() == cardType.ORK && card2.getCardType() == cardType.WIZZARD)
        {
            return "\nYour Ork is controlled by the Wizzard, you can't attack him.";
        }
        else if(card1.getCardType() == cardType.DRAGON && card2.getCardType() == cardType.ELF)
        {
            return "\nThe FireElve evaded your attack.";
        }
        return "";
    }

    /*public void changeTmpDamageValue(int tmpDamageWinner, int tmpDamageLoser)
    {
        tmpDamageWinner *= 2;
        tmpDamageLoser /= 2;
    }*/


}
