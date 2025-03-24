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
       
