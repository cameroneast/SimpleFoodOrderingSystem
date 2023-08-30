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

/**
 * Current user of the system.
 *
 */
public class User {
    protected String username;
    protected String password;
    protected List<MenuItem> cart = new ArrayList<MenuItem>();
    protected final String ORDER_DATA_FILE_PATH = "maven_task2\\demo\\src\\main\\java\\com\\order_data.txt";

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(){
        this.username = "";
        this.password = "";
    }

    // Getters
    public String getUsername() {
        return this.username;
    }
    
    public String getPassword() {
        return this.password;
    }

    public ArrayList<MenuItem> getCart() {
        return this.cart;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //Add an item to the cart
    public void addToCart(){
        Scanner s = new Scanner(System.in);
        System.out.println("What item would you like to add to your cart?");
        String item = s.nextLine();
        while(!App.restaurant.isMenuItemExists(item)){
            System.out.println("Invalid input. Please try again.");
            item = s.nextLine();
        }
        // Find the MenuItem based on the user's input
        MenuItem selectedMenuItem = null;
        for (String category : App.restaurant.menuItems.keySet()) {
            for (MenuItem menuItem : App.restaurant.menuItems.get(category)) {
                if (menuItem.name.equals(item)) {
                    selectedMenuItem = menuItem;
                    break;
                }
            }
            if (selectedMenuItem != null) {
                break;
            }
        }
        // Add the selected item to the cart
        if (selectedMenuItem != null) {
            cart.add(selectedMenuItem);
            System.out.println(selectedMenuItem.name + " has been added to your cart.");
            writeCartToFile();
            System.out.println("Press Enter to continue.");
            s.nextLine();
        } else {
            System.out.println("Item not found.");
        }
    }

    //Remove an item from the cart
    public void removeFromCart(){
        Scanner s = new Scanner(System.in);
        System.out.println("What item would you like to remove from your cart?");
        String ritem = s.nextLine();
        while(!App.restaurant.isMenuItemExists(ritem)){
            System.out.println("Invalid input. Please try again.");
            ritem = s.nextLine();
        }
        // Find the MenuItem based on the user's input
        MenuItem selectedMenuItem = null;
        for (String category : App.restaurant.menuItems.keySet()) {
            for (MenuItem menuItem : App.restaurant.menuItems.get(category)) {
                if (menuItem.name.equals(ritem)) {
                     selectedMenuItem = menuItem;
                    break;
                }
            }
            if (selectedMenuItem != null) {
                break;
            }
        }
        if (selectedMenuItem != null) {
        // Remove the selected item from the cart

        //Check to see if item already exists in cart
        if(!cart.contains(selectedMenuItem)){
            System.out.println("Item not found in cart.");
            System.out.println("Press Enter to continue.");
            s.nextLine();
            return;
        }
        else{
            cart.remove(selectedMenuItem);
        }
        System.out.println(selectedMenuItem.name + " has been removed from your cart.");
        writeCartToFile();
        System.out.println("Press Enter to continue.");
        s.nextLine();
        } else {
        System.out.println("Item not found.");
        System.out.println("Press Enter to continue.");
        s.nextLine();
        }
    }

    //View cart
    public void displayCartOrder() {
        App.clearConsole();
        System.out.println("User: " + this.username);
        System.out.println("Your order:");
        System.out.println("---------------");
        
        double orderTotal = 0.0;
        
        for (String category : App.restaurant.menuItems.keySet()) {
            System.out.println(category + ":");
            for (MenuItem item : cart) {
                if (item.category.equals(category)) {
                    System.out.println(item.name + " - $" + item.price);
                    orderTotal += item.price;
                }
            }
        }
        
        System.out.println("---------------");
        System.out.println("Order Total:");
        System.out.println(orderTotal);
        System.out.println("Press Enter to Continue.");
        Scanner s = new Scanner(System.in);
        s.nextLine();
    }
    
    public void writeCartToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ORDER_DATA_FILE_PATH))) {
            writer.write("User: " + this.username);
            writer.newLine();
            writer.write("Your order:");
            writer.newLine();
            writer.write("---------------");
            writer.newLine();

            double orderTotal = 0.0;

            for (String category : App.restaurant.menuItems.keySet()) {
                writer.write(category + ":");
                writer.newLine();
                for (MenuItem item : cart) {
                    if (item.category.equals(category)) {
                        writer.write(item.name + " - $" + item.price);
                        writer.newLine();
                        orderTotal += item.price;
                    }
                }
            }

            writer.write("---------------");
            writer.newLine();
            writer.write("Order Total:");
            writer.newLine();
            writer.write(String.valueOf(orderTotal));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCartFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ORDER_DATA_FILE_PATH))) {
            String line;
            boolean readingOrder = false;
            String currentCategory = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("User:")) {
                    String fileUsername = line.substring("User: ".length()).trim();
                    if (!fileUsername.equals(this.getUsername())) {
                        System.out.println("Cart data does not match the current user.");
                        break;
                    }
                } else if (line.equals("Your order:")) {
                    readingOrder = true;
                } else if (readingOrder && line.equals("---------------")) {
                    readingOrder = true;
                } else if (readingOrder) {
                    if (line.endsWith(":")) {
                        currentCategory = line.substring(0, line.length() - 1);
                    } else if (!line.isEmpty() && currentCategory != null) {
                        String[] parts = line.split(" - ");
                        if (parts.length == 2) {
                            String itemName = parts[0];
                            double price = Double.parseDouble(parts[1].substring(1)); // Removing the "$" sign
                            for (String category : App.restaurant.menuItems.keySet()) {
                                for (MenuItem menuItem : App.restaurant.menuItems.get(category)) {
                                    if (menuItem.name.equals(itemName)) {
                                        cart.add(menuItem);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
