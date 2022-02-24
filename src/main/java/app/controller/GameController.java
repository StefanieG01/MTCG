package app.controller;


import app.model.User;
import app.service.GameService;
import http.ContentType;
import http.HttpStatus;
import server.Response;

public class GameController extends Controller
{
    private GameService gameService;

    public GameController(GameService gameService)
    {
        super();
        this.gameService = gameService;
    }

    // GET /battles     --> battle beginnen
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
