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
}
