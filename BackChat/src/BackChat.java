import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.InputMismatchException;
import java.util.List;

public class BackChat implements Serializable {
    private Map<String, User> users;
    private User currentUser;
    private static Scanner scanner = new Scanner(System.in);

    public BackChat() {
        users = new HashMap<>();
    }
    
    public void addUser(User user) {
        users.put(user.getUserID(), user);
    }
    
    public User findUserByID(String userID) {
        return users.get(userID);
    }
    
    public boolean login(String userID) {
        if (users.containsKey(userID)) {
            currentUser = users.get(userID);
            return true;
        }
        return false;
    }
    
    public void loadNetworkFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("networkData.ser"))) {
            users = (Map<String, User>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }
    
    public void saveNetworkToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("networkData.ser"))) {
            out.writeObject(users);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    // Adds a friend to the current user's friend list using the friend's userID.
    public void addFriendById(User currentUser, String friendUserId) {
        if (friendUserId.equals(currentUser.getUserID())) {
            System.out.println("You cannot add yourself as a friend.");
            return;
        }
        User friend = findUserByID(friendUserId);
        if (friend == null) {
            System.out.println("User with ID " + friendUserId + " does not exist.");
            return;
        }
        if (!currentUser.getFriends().contains(friend)) {
            currentUser.getFriends().add(friend);
            // Mutual addition: add currentUser to the friend's list as well.
            friend.getFriends().add(currentUser);
            System.out.println(friend.getName() + " has been added as a friend.");
        } else {
            System.out.println(friend.getName() + " is already your friend.");
        }
    }
    
    // Stub method for deleting a friend using userID.
    public void deleteFriend(User currentUser) {
        System.out.print("Enter the userID of the friend to remove: ");
        String friendUserId = scanner.nextLine();
        User friend = findUserByID(friendUserId);
        if (friend == null) {
            System.out.println("User with ID " + friendUserId + " does not exist.");
            return;
        }
        if (currentUser.getFriends().remove(friend)) {
            friend.getFriends().remove(currentUser);
            System.out.println(friend.getName() + " has been removed from your friends list.");
        } else {
            System.out.println(friend.getName() + " is not in your friends list.");
        }
    }
    
    // Stub method for viewing a friend’s friends.
    public void viewFriendOfFriend(User currentUser) {
        System.out.print("Enter the userID of the friend whose friends you want to see: ");
        String friendUserId = scanner.nextLine();
        User friend = findUserByID(friendUserId);
        if (friend == null) {
            System.out.println("User with ID " + friendUserId + " does not exist.");
            return;
        }
        System.out.println(friend.getName() + "'s friends: ");
        List<User> friendList = friend.getFriends();
        
        if (friendList.isEmpty()) {
            System.out.println(friend.getName() + " has no friends.");
            return;
        }
        
        for (User f : friendList) {
            System.out.print(f.getName());
            
            // Check if the current user is also friends with this person
            if (currentUser.getFriends().contains(f)) {
                System.out.println(" (Mutual Friend)");
            } else {
                System.out.println();
            }
        }
    }

    public void recommendFriends(User currentUser) {
        Set<User> potentialFriends = new HashSet<>();
    
        // Gather friends of friends (excluding existing friends and the user)
        for (User friend : currentUser.getFriends()) {
            for (User friendOfFriend : friend.getFriends()) {
                if (!currentUser.getFriends().contains(friendOfFriend) && !friendOfFriend.equals(currentUser)) {
                    potentialFriends.add(friendOfFriend);
                }
            }
        }
    
        if (potentialFriends.isEmpty()) {
            System.out.println("No friend recommendations available at this time.");
            return;
        }
    
        // Let the user choose between filtering by hometown or workplace
        System.out.println("Filter recommendations by:");
        System.out.println("1. Hometown");
        System.out.println("2. Workplace");
        System.out.print("Enter your choice: ");
    
        int choice;
        try {
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter 1 or 2.");
            scanner.nextLine(); // Consume invalid input
            return;
        }
    
        String filterValue = (choice == 1) ? currentUser.getHometown() : currentUser.getWorkplace();
        String filterType = (choice == 1) ? "Hometown" : "Workplace";
    
        // Filter recommendations based on the selected criteria
        List<User> filteredRecommendations = new ArrayList<>(potentialFriends);
        filteredRecommendations.removeIf(user -> 
            choice == 1 ? !user.getHometown().equalsIgnoreCase(filterValue) 
                        : !user.getWorkplace().equalsIgnoreCase(filterValue)
        );
    
        if (filteredRecommendations.isEmpty()) {
            System.out.println("No recommended friends match your " + filterType.toLowerCase() + " (" + filterValue + ").");
        } else {
            System.out.println("Recommended friends based on your " + filterType.toLowerCase() + " (" + filterValue + "):");
            for (User recommended : filteredRecommendations) {
                System.out.println(recommended);
            }
        }
    }
    

}
