package com.example;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Current restaurant of the system.
 *
 */
public class Restaurant {
    private static final String MENU_FILE_PATH = "maven_task2\\demo\\src\\main\\java\\com\\restaurant_menu.txt";
    private static final String RESTAURANT_DETAILS_FILE_PATH = "maven_task2\\demo\\src\\main\\java\\com\\restaurant_details.txt";

    public String name;
    public String owner;
    public Map<String, List<MenuItem>> menuItems = new HashMap<>();

    public Restaurant() {
        loadRestaurantDetails();
        loadMenuItems();
    }

    private void loadMenuItems() {
        String currentCategory = null;
    
        try (BufferedReader br = new BufferedReader(new FileReader(MENU_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.endsWith(":")) {
                    currentCategory = line.substring(0, line.length() - 1);
                    menuItems.put(currentCategory, new ArrayList<>());
                } else if (!line.isEmpty() && currentCategory != null) {
                    String[] parts = line.split(" - ");
                    if (parts.length == 2) {
                        String itemName = parts[0];
                        double price = Double.parseDouble(parts[1]);
                        menuItems.get(currentCategory).add(new MenuItem(itemName, currentCategory, price));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    private void loadRestaurantDetails(){
        //Load restaurant details from file
        try (BufferedReader br = new BufferedReader(new FileReader(RESTAURANT_DETAILS_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    this.name = parts[0];
                    this.owner = parts[1];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printMenu() {
        System.out.println("Menu:");
        for (String category : menuItems.keySet()) {
            System.out.println(category + ":");
            for (MenuItem item : menuItems.get(category)) {
                System.out.println("- " + item.name + " - $" + item.price);
            }
            System.out.println();
        }
    }

    public boolean isMenuItemExists(String itemName) {
        for (List<MenuItem> itemList : menuItems.values()) {
            for (MenuItem item : itemList) {
                if (item.name.equalsIgnoreCase(itemName)) {
                    return true;
                }
            }
        }
        return false;
    }
    
}

