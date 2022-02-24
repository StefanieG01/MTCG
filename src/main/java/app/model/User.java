package app.model;

import app.model.Card;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class User
{
    @Getter
    @JsonAlias({"Username"})
    private String username;

    @Getter
    @Setter
    @JsonAlias({"Name"})
    private String name;

    @Getter
    @Setter
    @JsonAlias({"Password"})
    private String password;

    @Getter
    @Setter
    private int coins;

    @Getter
    @Setter
    private int eloValue;

   // public List<Card> stack;
    //public List<Card> deck;
    @Getter
    @Setter
    private String token;

    @Getter
    @Setter
    @JsonAlias({"Bio"})
    private String bio;

    @Getter
    @Setter
    @JsonAlias({"Image"})
    private String image;

    @Getter
    @Setter
    private int wonGames;

    @Getter
    @Setter
    private int lostGames;


    public User(){
        //this.stack = new ArrayList<>();
        //this.deck = new ArrayList<>();
    }

    public User(String username, String password)
    {
        this.username = username;
        this.password = password;
        this.coins = 20;
        this.eloValue = 100;
        this.token = username + "-mtcgToken";
        this.wonGames = 0;
        this.lostGames = 0;
        //this.stack = new ArrayList<>();
        //this.deck = new ArrayList<>();
    }
}
