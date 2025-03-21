import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class User implements Serializable {
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
	
	public void addFriend(User user) {
		if (!friends.contains(user)) {
			friends.add(user);
		}
	}
	
	public void removeFriend(User user) {
		friends.remove(user);
	}
	
	public void sendMessage(User recipient, String content) {
		Message message = new Message(this.userID, recipient.getUserID(), content);
		recipient.receiveMessage(message);
	}
	
	public void receiveMessage(Message message) {
		messages.add(message);
	}
	
	public void viewMessages() {
		for (Message message : messages) {
			System.out.println(message);
			
		}
	}
	
	@Override
	public String toString() {
		return "UserID: " + userID + ", Name: " + name + ", Birthday: " + birthday + ", Workplace: " + workplace + ", Hometown:" + hometown;
	}
	
}
