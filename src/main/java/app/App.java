package app;

import app.controller.CardController;
import app.controller.GameController;
import app.controller.UserController;
import app.model.Game;
import app.model.User;
import app.service.GameService;
import app.service.UserService;
import http.ContentType;
import http.HttpStatus;
import http.Method;
import server.Request;
import server.Response;
import server.ServerApp;
import app.service.CardService;
import app.service.UserService;

import java.sql.Connection;
import java.sql.DriverManager;

public class App implements ServerApp {
    private final UserController userController;
    private final CardController cardController;
    private final UserService userService;
    private final GameController gameController;
    private final CardService cardService;
    //private final GameService gameService;

    public App() {

        Connection conn = null;
        try{
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/projekt",
                    "postgres",
                    "postgres");
        }
        catch (Exception e){
            System.out.println("Connection to DB failed!");
            System.out.println(e);
        };

        this.userService = new UserService(conn);
        this.cardService = new CardService(conn);
        this.userController = new UserController(this.userService);
        this.cardController = new CardController(this.cardService);
        this.gameController = new GameController(new GameService(conn));
    }

    @Override
    public Response handleRequest(Request request)
    {
        if (request.getPathname().equals("/users") && request.getMethod() == Method.GET)
        {
            return this.userController.getUsers();
        }
        else if(request.getPathname().contains("/users/") && request.getMethod() == Method.GET)
        {
            if(checkToken(request) && (request.getPathname().substring(request.getPathname().lastIndexOf("/")+1).equals((request.getToken()).split("-")[0]) ))
            {
                return this.userController.getUserByName(request.getPathname().substring(request.getPathname().lastIndexOf("/")+1));
            }
            else
            {
                return this.cardController.missingToken();
            }

        }
        else if(request.getPathname().contains("/users/") && request.getMethod() == Method.PUT)
        {
            if(checkToken(request) && (request.getPathname().substring(request.getPathname().lastIndexOf("/")+1).equals((request.getToken()).split("-")[0]) ))
            {
                return this.userController.updateProfile(request.getPathname().substring(request.getPathname().lastIndexOf("/")+1), request);
            }
            else
            {
                return this.cardController.missingToken();
            }

        }
        else if (request.getPathname().equals("/users") && request.getMethod() == Method.POST)
        {
            return this.userController.addUser(request);
        }
        else if(request.getPathname().equals("/sessions") && request.getMethod() == Method.POST)    //LOGIN
        {
            return this.userController.login(request);
        }
        else if(request.getPathname().equals("/packages") && request.getMethod() == Method.POST)    // neue Packages
        {
            return this.cardController.addPackages(request);
        }
        else if(request.getPathname().equals("/packages") && request.getMethod() == Method.GET)    // Packages anzeigen
        {
            return this.cardController.showPackages();
        }
        else if(request.getPathname().equals("/transactions/packages") && request.getMethod() == Method.POST)    // Packages anzeigen
        {
            if(checkToken(request))
            {
                User user = this.userService.getUser(request.getToken().split("-")[0]);
                if(user == null)
                {
                    return null;
                }
                return this.cardController.acquirePackage(user);
            }
            else
            {
                return this.cardController.missingToken();
            }

        }
        else if(request.getPathname().equals("/cards") && request.getMethod() == Method.GET)    // Packages von User anzeigen
        {
            if(checkToken(request))
            {
                User user = this.userService.getUser(request.getToken().split("-")[0]);
                return this.userController.getStack(user);
            }
            else
            {
                return this.cardController.missingToken();
            }

        }
        else if(request.getPathname().equals("/deck") && request.getMethod() == Method.GET)    // Deck von User anzeigen
        {
           if(checkToken(request))
           {
               User user = this.userService.getUser(request.getToken().split("-")[0]);
               return this.userController.getDeck(user);
           }
           else
           {
               return this.cardController.missingToken();
           }

        }
        else if(request.getPathname().equals("/deck") && request.getMethod() == Method.PUT)    // Deck für user erstellen
        {
            if(checkToken(request))
            {
                User user = this.userService.getUser(request.getToken().split("-")[0]);
                return this.userController.setDeck(user, request);
            }
            else
            {
                return this.cardController.missingToken();
            }

        }
        else if(request.getPathname().equals("/battles") && request.getMethod() == Method.GET)    // Deck für user erstellen
        {
            if(checkToken(request))
            {
                User user = this.userService.getUser(request.getToken().split("-")[0]);
                return this.gameController.fight(user);
            }
            else
            {
                return this.cardController.missingToken();
            }
            //User user = this.userService.getUser(request.getToken().split("-")[0]);
            //return this.gameController.fight(user);
            //System.out.println(request.getBody());
        }
        else if(request.getPathname().equals("/stats") && request.getMethod() == Method.GET)    // Deck für user erstellen
        {
            if(checkToken(request))
            {
                User user = this.userService.getUser(request.getToken().split("-")[0]);
                return this.userController.getStats(user);
            }
            else
            {
                return this.cardController.missingToken();
            }

        }

        else if(request.getPathname().equals("/score") && request.getMethod() == Method.GET)    // Deck für user erstellen
        {
            if(checkToken(request))
            {
                return this.userController.getScore();
            }
            else
            {
                return this.cardController.missingToken();
            }

        }

        return new Response(
            HttpStatus.BAD_REQUEST,
            ContentType.JSON,
            "[]"
        );
    }

    public boolean checkToken(Request request)
    {
        if(request.getToken() == null)
        {
            return false;
        }
        return true;
    }
}
