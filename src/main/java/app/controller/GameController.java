package app.controller;

import app.model.Card;
import app.model.User;
import app.service.CardService;
import app.service.GameService;
import com.fasterxml.jackson.core.JsonProcessingException;
import http.ContentType;
import http.HttpStatus;
import server.Request;
import server.Response;

public class GameController extends Controller
{
    private GameService gameService;

    public GameController(GameService gameService) {
        super();
        this.gameService = gameService;
    }

    public Response fight(User user)
    {

        String returnMessage = this.gameService.registerForBattle(user);

        return new Response(
                HttpStatus.OK,
                ContentType.JSON,
                returnMessage
        );
    }
}
