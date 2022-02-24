package app.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

public class Card
{
    //only getter --> no setter (final)
    @Getter
    @JsonAlias({"Name"})
    private String name;
    @Getter
    @Setter
    @JsonAlias({"CardType"})
    private app.model.cardType cardType;
    @Getter
    @Setter
    @JsonAlias({"ElementType"})
    private app.model.elementType elementType;
    @Getter
    @JsonAlias({"Damage"})
    private int damage;
    @Getter
    @JsonAlias({"Id"})
    private String id;
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private boolean deck;

    public Card(){}

    public Card(String id, String name, int damage)
    {
        this.id = id;
        this.name = name;
        this.damage = damage;
        this.username = "";
        this.deck = false;
    }
}
