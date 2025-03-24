import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.InputMismatchException;

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
        for (User f : friend.getFriends()) {
            System.out.println(f.getName());
        }
    }
    
    public static void main(String[] args) {
        BackChat backChat = new BackChat();
        
        System.out.print("Enter your userID: ");
        String userID = scanner.nextLine();
        // For simplicity, we create a user with default values.
        User currentUser = new User(userID, userID, "01/01/1970", "Unknown", "Unknown");
        backChat.addUser(currentUser);
        backChat.currentUser = currentUser; // set the current user
        
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. View Friend List");
            System.out.println("2. Add Friend");
            System.out.println("3. Delete Friend");
            System.out.println("4. View a Friend's Friends");
            System.out.println("5. Exit");
            System.out.print("Select an option: ");
            int option = -1;
            try {
                option = scanner.nextInt();
                scanner.nextLine(); // consume newline
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // consume invalid input
                continue;
            }
            
            switch (option) {
                case 1:
                    System.out.println("Your friends: ");
                    for (User friend : currentUser.getFriends()) {
                        System.out.println(friend.getName());
                    }
                    break;
                case 2:
                    System.out.print("Enter the userID of the friend to add: ");
                    String friendUserId = scanner.nextLine();
                    backChat.addFriendById(currentUser, friendUserId);
                    break;
                case 3:
                    backChat.deleteFriend(currentUser);
                    break;
                case 4:
                    backChat.viewFriendOfFriend(currentUser);
                    break;
                case 5:
                    exit = true;
                    System.out.println("Exiting BackChat. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please select a valid option.");
            }
        }
    }
}
