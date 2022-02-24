package app.controller;

import app.model.User;
import app.service.CardService;
import com.fasterxml.jackson.core.JsonProcessingException;
import http.ContentType;
import http.HttpStatus;
import server.Response;
import app.model.Card;
import server.Request;

import java.util.Collections;
import java.util.List;

public class CardController extends Controller
{
    private CardService cardService;

    public CardController(CardService cardService) {
        super();
        this.cardService = cardService;
    }

    // POST /packages       --> packages hinzufügen
    public Response addPackages(Request request) {
        try {
            Card[] cards = null;
            cards = this.getObjectMapper().readValue(request.getBody(), Card[].class);
            this.cardService.addCards(cards);

            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    "{ message: \"Successfully added Package\" }"
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"message\" : \"Internal Server Error\" }"
            );
        }
    }

    public Response showPackages() {
        try {
            List cards = this.cardService.getCards();
            // "[ { \"id\": 1, \"city\": \"Vienna\", \"temperature\": 9.0 }, { ... }, { ... } ]"
            String userDataJSON = this.getObjectMapper().writeValueAsString(cards);

            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    userDataJSON
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"message\" : \"Internal Server Error\" }"
            );
        }
    }

    // POST /transactions/packages      --> packages kaufen
    public Response acquirePackage(User user)
    {
        //System.out.println(user.getCoins());
        if(user.getCoins() >= 5)
        {
            String returnMessage = this.cardService.acquireCards(user);
            //Collections.addAll(user.stack, cards);
            user.setCoins(user.getCoins()-5);

            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    returnMessage
            );
        }
        else
        {
            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    "{ message: \"Caution! Not enough coins!\" }"
            );
        }

    }

    public Response missingToken()
    {
        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "{ \"message\" : \"Wrong input - token is missing or wrong!\" }"
        );
    }
}
