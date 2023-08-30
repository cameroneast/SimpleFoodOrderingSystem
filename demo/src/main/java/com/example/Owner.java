package com.example;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import com.example.User;
import com.example.Restaurant;
import com.example.App;

public class Owner extends User {
    protected final String ORDER_DATA_FILE_PATH = "maven_task2\\demo\\src\\main\\java\\com\\order_data.txt";
    protected String username = "owner";
    protected String password = "ownerpass";

    public Owner(){
        this.username = "owner";
        this.password = "ownerpass";
    }

    public Owner(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters
    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //Display the order history
    public void displayOrderHistory(){
        try {
            BufferedReader br = new BufferedReader(new FileReader(ORDER_DATA_FILE_PATH));
            String line = br.readLine();
            while (line != null) {
                System.out.println(line);
                line = br.readLine();
            }
        } catch(IOException e){
            System.out.println("Error reading file.");
        }
    }

    //Display owner menu
    public void displayOwnerMenu(){
        while(true){
                App.clearConsole();
                System.out.print("Welcome, " + this.getUsername() + "!\nTo view your restaurant's menu, type 'M'.\nTo view your restaurant's orders, type 'O'.\nTo exit the owner screen, type 'L': ");
                Scanner s = new Scanner(System.in);
                char choice4 = s.next().charAt(0);

                while(choice4 != 'M' && choice4 != 'O' && choice4 != 'L') {
                    System.out.println("Invalid input. Please try again.");
                    choice4 = s.next().charAt(0);
                }

                if(choice4 == 'M'){
                    App.clearConsole();
                    App.restaurant.printMenu();
                    System.out.println("Press any key to continue...");
                    s.next();
                }
                else if(choice4 == 'O'){
                    App.clearConsole();
                    displayOrderHistory();
                    System.out.println("Press any key to continue...");
                    s.next();
                }
                else
                if(choice4 == 'L'){
                    break;
                }
         }
    }
}
