import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class BackChat implements Serializable{
	private Map<String, User> users;
	private User currentUser;
	
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

	
}