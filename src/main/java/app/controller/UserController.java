package app.controller;

import app.model.Card;
import app.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import http.ContentType;
import http.HttpStatus;
import server.Request;
import server.Response;
import app.model.User;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class UserController extends Controller
{
    private UserService userService;

    public UserController(UserService userService) {
        super();
        this.userService = userService;
    }

    // GET /users       --> alle User
    public Response getUsers() {
        try {
            List userData = this.userService.getUser();
            // "[ { \"id\": 1, \"city\": \"Vienna\", \"temperature\": 9.0 }, { ... }, { ... } ]"
            String userDataJSON = this.getObjectMapper().writeValueAsString(userData);

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

    // GET /users       --> einzelne User
    public Response getUserByName(String username) {
        try {
            User userData = this.userService.getUser(username);
            // "[ { \"id\": 1, \"city\": \"Vienna\", \"temperature\": 9.0 }, { ... }, { ... } ]"
            String userDataJSON = this.getObjectMapper().writeValueAsString(userData);

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

    // POST /users
    public Response addUser(Request request) {
        User user = null;
        try {

            // request.getBody() => "{ \"id\": 4, \"city\": \"Graz\", ... }
            user = this.getObjectMapper().readValue(request.getBody(), User.class);
            String returnMessage = this.userService.addUser(user);

            return new Response(
                    HttpStatus.CREATED,
                    ContentType.JSON,
                    returnMessage
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.JSON,
                "{ \"message\" : \"Internal Server Error\" }"
        );
    }

    // POST /sessions
    public Response login(Request request) {
        User user = null;
        try {
            // request.getBody() => "{ \"id\": 4, \"city\": \"Graz\", ... }
            user = this.getObjectMapper().readValue(request.getBody(), User.class);
            if(this.userService.login(user))
            {
                return new Response(
                        HttpStatus.CREATED,
                        ContentType.JSON,
                        "{ message: \"Logged in!\" }"
                );
            }
            else
            {
                return new Response(
                        HttpStatus.BAD_REQUEST,
                        ContentType.JSON,
                        "{ message: \"Login failed!\" }"
                );
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.JSON,
                "{ \"message\" : \"Internal Server Error\" }"
        );
    }

    public Response getStack(User user) {

            try {
                List<Card> stack = this.userService.getStack(user);
                // "[ { \"id\": 1, \"city\": \"Vienna\", \"temperature\": 9.0 }, { ... }, { ... } ]"
                String userDataJSON = this.getObjectMapper().writeValueAsString(stack);

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

    public Response getDeck(User user) {
        try {
            List<Card> deck = this.userService.getDeck(user);
            //System.out.println(deck);
            if(deck == null)
            {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        "{ \"message\" : \"Deck is empty\" }"
                );
            }
            // "[ { \"id\": 1, \"city\": \"Vienna\", \"temperature\": 9.0 }, { ... }, { ... } ]"
            String userDataJSON = this.getObjectMapper().writeValueAsString(deck);

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

    public Response setDeck(User user, Request request) {
        try {
            String[] ids = this.getObjectMapper().readValue(request.getBody(), String[].class);
            String responseText = "added Cards to deck";
            if(ids.length == 4)
            {
                if(!this.userService.setDeck(user, ids))
                {
                    responseText = "You don't have this card ids";
                }
            }
            else
            {
                responseText = "Failure! You have to add exactly 4 cards!";
            }

            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    responseText
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

    public Response updateProfile(String username, Request request)
    {

        try
        {
            Map<String, String> userInput = this.getObjectMapper().readValue(request.getBody(), Map.class);
            /*for(String u : userInput.values())
            {
                System.out.println(u);
            }*/


            this.userService.updateProfile(username, userInput);

            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    "updated profile"
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

    public Response getStats(User user)
    {
        int playedGames = user.getWonGames() + user.getLostGames();
        String returnMessage = "Elo Value: " + user.getEloValue() + "\nCoins: " + user.getCoins() + "\nTotal Games played: " + playedGames + "\nLost Games: " + user.getLostGames() + "\nWon Games: " + user.getWonGames();

        return new Response(
                HttpStatus.OK,
                ContentType.JSON,
                returnMessage
        );
    }

    public Response getScore()
    {
        List<User> userList = this.userService.getUser();
        //Collections.sort(userList, (o1, o2) -> o1.getEloValue() < o2.getEloValue());
        userList.sort(Comparator.comparingInt(User::getEloValue).reversed());

        String returnMessage = "";

        for(User u : userList)
        {
            returnMessage += (u.getUsername());
            returnMessage += (" ");
            returnMessage += (u.getEloValue() + "");
            returnMessage += ("\n");
        }

        return new Response(
                HttpStatus.OK,
                ContentType.JSON,
                returnMessage
        );
    }
}
