package Klassen;

import java.util.Scanner;



public class Menu
{
    Scanner read = new Scanner(System.in);
    int choice = 0;

    public int welcome()
    {
        while(choice != 1 && choice != 2)
        {
            System.out.println("Welcome to Monster Trading Cards Game");
            System.out.println("Please select:");
            System.out.println("(1) Login");
            System.out.println("(2) Create new account");
            choice = read.nextInt();
        }
        return choice;
    }

    public int showMenu()
    {
        System.out.println("Please select:");
        System.out.println("(1)");

        choice = read.nextInt();
        return choice;
    }
}

