import Klassen.Menu;
import app.App;
import server.Server;

import java.io.IOException;
import java.sql.DriverManager;
import java.util.concurrent.ExecutionException;

public class Main
{
    public static void main(String[] args)
    {




        /*Menu menu = new Menu();
        int loginOrSignIn = menu.welcome();
        boolean loggedIn = false;

        switch (loginOrSignIn)
        {
            case 1: // LOGIN
                System.out.println("Login ausgewählt");
                break;
            case 2: // SIGNIN
                System.out.println("Signin ausgewählt");
                break;
        }*/

        App app = new App();
        Server server = new Server(10001, app);
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
