package com.example;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.example.Owner;


/**
 * Main menu screen where user chooses login or registration.
 *
 */
public class App 
{
    private static final String CRED_FILE_PATH = "maven_task2\\demo\\src\\main\\java\\com\\user_credentials.txt";

    //Initialize users and load from file
    public static Map<String, String> users = loadUsersFromFile();
    //Initialize restaurant
    public static Restaurant restaurant = new Restaurant();
    //Initialize scanner
    static Scanner s;

    public static void main( String[] args )
    {
        while(true){
        clearConsole();
        //User login screen
        s = new Scanner(System.in);
        System.out.println( "Welcome to " + restaurant.name + "!" );
        System.out.print( "Type 'R' to register an account or type 'L' to login (Type 'Q' to quit): ");
        char choice = s.next().charAt(0);
        if(choice == 'Q'){
            break;
        }

        while(choice != 'R' && choice != 'L' && choice != 'Q') {
            System.out.println("Invalid input. Please try again.");
            choice = s.next().charAt(0);
        }

        User currentUser = null;
        Owner currentOwner = new Owner();
        if (choice == 'R') {
            // Register
            clearConsole();
            currentUser = registerNewUser();
        } else if (choice == 'L') {
            // Login
            clearConsole();
            currentUser = loginExistingUser();
            if(currentUser.getUsername().equals(currentOwner.getUsername())){
                currentOwner.displayOwnerMenu();
                System.out.println("Press enter to continue.");
                s.nextLine();
            }
            else{
            currentUser.loadCartFromFile();
            } 
        }

        //Logged-in user screen
        while(true){
        clearConsole();
        System.out.print("What would you like to do?\nTo browse the menu, type 'B'.\nTo view your cart, type 'C'.\nTo logout, type 'L': ");
        char choice2 = s.next().charAt(0);

        while(choice2 != 'B' && choice2 != 'C' && choice2 != 'L') {
            System.out.println("Invalid input. Please try again.");
            choice2 = s.next().charAt(0);
        }

        switch(choice2){
            case 'B':
                clearConsole();
                restaurant.printMenu();
                System.out.print("To add an item to your cart, type 'A'.\nTo remove an item from your cart, type 'R'.\nTo checkout, type 'C'.\nTo logout, type 'L': ");
                char choice3 = s.next().charAt(0);

                while(choice3 != 'A' && choice3 != 'R' && choice3 != 'C' && choice3 != 'L') {
                    System.out.println("Invalid input. Please try again.");
                    choice3 = s.next().charAt(0);
                }

                switch(choice3){
                    case 'A':
                        currentUser.addToCart();
                        break;
                    case 'R':
                        currentUser.removeFromCart();
                        break;
                    case 'C':
                        currentUser.displayCartOrder();
                        break;
                    case 'L':
                        break;
                }
                break;
            case 'C':
                currentUser.displayCartOrder();
                break;
            case 'L':
                break;
        }
        if(choice2 == 'L'){
            break;
        }
        }
    }
    }

    private static Map<String, String> loadUsersFromFile() {
        Map<String, String> loadedUsers = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(CRED_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    loadedUsers.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return loadedUsers;
    }

    public static User registerNewUser(){
        Scanner t = new Scanner(System.in);
        User newUser;
        System.out.print("Enter a username: ");
        String username = t.nextLine();

        //Vet out bad username inputs
        if(username == null || username.length() == 0){
            System.out.println("Invalid username. Please try again.");
            System.out.print("Enter a username: ");
            username = t.nextLine();
        }

        //Vet out already used usernames
        while(users.containsKey(username)){
            System.out.println("Username already exists. Please try again.");
            System.out.print("Enter a username: ");
            username = t.nextLine();
        }

        System.out.print("Enter a password: ");
        String password = t.nextLine();

        //Vet out bad password inputs
        if(password == null || password.length() == 0){
            System.out.println("Invalid password. Please try again.");
            System.out.print("Enter a password: ");
            password = s.nextLine();
        }

        clearConsole();

        //Add new user to users map
        users.put(username, password);

        //Write new user to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CRED_FILE_PATH, true))) {
            writer.write(username + ":" + password);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Login successful! Welcome, " + username + "!");

        newUser = new User(username, password);
        return newUser;
    }

    public static User loginExistingUser(){
        User existingUser;
        Scanner p = new Scanner(System.in);
        System.out.print("Enter your username: ");
        String username = p.nextLine();

        //Vet out bad username inputs
        if(username == null || username.length() == 0){
            System.out.println("Invalid username. Please try again.");
            System.out.print("Enter your username: ");
            username = p.nextLine();
        }

        //Vet out non-existent usernames
        while(!users.containsKey(username)){
            System.out.println("Username does not exist. Please try again.");
            System.out.print("Enter your username: ");
            username = p.nextLine();
        }

        System.out.print("Enter your password: ");
        String password = p.nextLine();

        //Vet out bad password inputs
        if(password == null || password.length() == 0){
            System.out.println("Invalid password. Please try again.");
            System.out.print("Enter your password: ");
            password = p.nextLine();
        }

        //Vet out incorrect passwords
        while(!users.get(username).equals(password)){
            System.out.println("Incorrect password. Please try again.");
            System.out.print("Enter your password: ");
            password = p.nextLine();
        }

        clearConsole();

        System.out.println("Login successful! Welcome, " + username + "!");
        existingUser = new User(username, password);

        return existingUser;
    }

    public static void clearConsole(){
        try {
            // Clear the console using system-specific commands
            new ProcessBuilder("cmd", "/c", "cls", "clear").inheritIO().start().waitFor();
            // On non-Windows systems, you can use "clear" command instead of "cls"

            // Your other code here
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    
}


