package app;

import app.controller.CardController;
import app.controller.GameController;
import app.controller.UserController;
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

import java.sql.Connection;
import java.sql.DriverManager;

public class App implements ServerApp
{
    private final UserController userController;
    private final CardController cardController;
    private final UserService userService;
    private final GameController gameController;
    private final CardService cardService;

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
        // alle user anzeigen
        if (request.getPathname().equals("/users") && request.getMethod() == Method.GET)
        {
            return this.userController.getUsers();
        }
        // einzelnen user anzeigen
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
        // userprofil updaten
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
        // user neu anlegen
        else if (request.getPathname().equals("/users") && request.getMethod() == Method.POST)
        {
            return this.userController.addUser(request);
        }
        // login
        else if(request.getPathname().equals("/sessions") && request.getMethod() == Method.POST)
        {
            return this.userController.login(request);
        }
        // packages hinzufügen
        else if(request.getPathname().equals("/packages") && request.getMethod() == Method.POST)
        {
            if(checkToken(request) && (request.getToken()).split("-")[0].equals("admin"))
            {
                return this.cardController.addPackages(request);
            }
            else
            {
                return this.cardController.missingToken();
            }
        }
        // packages anzeigen --> nur zu debug-zwecken
        else if(request.getPathname().equals("/packages") && request.getMethod() == Method.GET)
        {
            return this.cardController.showPackages();
        }
        // user kauft package
        else if(request.getPathname().equals("/transactions/packages") && request.getMethod() == Method.POST)
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
        // Stack von user anzeigen
        else if(request.getPathname().equals("/cards") && request.getMethod() == Method.GET)
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
        // Deck von User anzeigen
        else if(request.getPathname().equals("/deck") && request.getMethod() == Method.GET)
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
        // Deck für user erstellen
        else if(request.getPathname().equals("/deck") && request.getMethod() == Method.PUT)
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
        // battle
        else if(request.getPathname().equals("/battles") && request.getMethod() == Method.GET)
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
        }
        // stats von user anzeigen
        else if(request.getPathname().equals("/stats") && request.getMethod() == Method.GET)
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

        // Highscore-Liste anzeigen
        else if(request.getPathname().equals("/score") && request.getMethod() == Method.GET)
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

    // token prüfen
    public boolean checkToken(Request request)
    {
        if(request.getToken() == null)
        {
            return false;
        }
        return true;
    }
}
