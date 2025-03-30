import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements Serializable {
    // Global registry for all users
    private static Map<String, User> allUsers = new HashMap<>();
    
    private String userID;
    private String name;
    private String birthday;
    private String workplace;
    private String hometown;
    private List<User> friends;
    private List<Message> messages;
    
    public User(String userID, String name, String birthday, String workplace, String hometown) {
        this.userID = userID;
        this.name = name;
        this.birthday = birthday;
        this.workplace = workplace;
        this.hometown = hometown;
        this.friends = new ArrayList<>();
        this.messages = new ArrayList<>();
        // Register this user in the global registry
        allUsers.put(userID, this);
    }
    
    public String getUserID() {
        return userID;
    }
    
    public String getName() {
        return name;
    }
    
    public String getBirthday() {
        return birthday;
    }
    
    public String getWorkplace() {
        return workplace;
    }
    
    public String getHometown() {
        return hometown;
    }
    
    public List<User> getFriends() {
        return friends;
    }
    
    public List<Message> getMessages() {
        return messages;
    }
    
    // Lookup a user by userID using the global registry.
    public static User getUserById(String userID) {
        return allUsers.get(userID);
    }
    
    // Direct friend addition using a User object.
    public void addFriend(User friend) {
        if (friend == null) {
            System.out.println("Cannot add a null friend.");
            return;
        }
        if (friend == this) {
            System.out.println("You cannot add yourself as a friend.");
            return;
        }
        if (!friends.contains(friend)) {
            friends.add(friend);
            // Ensure mutual friendship by adding the current user to the friend's list.
            if (!friend.friends.contains(this)) {
                friend.friends.add(this);
            }
            System.out.println(friend.getName() + " has been added as a friend.");
        } else {
            System.out.println(friend.getName() + " is already your friend.");
        }
    }
    
    // Remove friend using a User object.
    public void removeFriend(User friend) {
        if (friend == null) {
            System.out.println("Cannot remove a null friend.");
            return;
        }
        if (friends.remove(friend)) {
            friend.friends.remove(this);
            System.out.println(friend.getName() + " has been removed from your friends list.");
        } else {
            System.out.println(friend.getName() + " is not in your friends list.");
        }
    }
    
    // Send a message to another user.
    public void sendMessage(User recipient, String content) {
        if (recipient == null) {
            System.out.println("Recipient not found.");
            return;
        }
        Message message = new Message(this.userID, recipient.getUserID(), content);
        recipient.receiveMessage(message);
    }
    
    // Receive a message.
    public void receiveMessage(Message message) {
        messages.add(message);
    }
    
    // View all messages.
    public void viewMessages() {
        if (messages.isEmpty()) {
            System.out.println("No messages.");
        } else {
            for (Message message : messages) {
                System.out.println(message);
            }
        }
    }
    
    @Override
    public String toString() {
        return "UserID: " + userID + ", Name: " + name + ", Birthday: " + birthday +
               ", Workplace: " + workplace + ", Hometown: " + hometown;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        User other = (User) obj;
        return userID != null ? userID.equals(other.userID) : other.userID == null;
    }
    
    @Override
    public int hashCode() {
        return userID != null ? userID.hashCode() : 0;
    }

}
