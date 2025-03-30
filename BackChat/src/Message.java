import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
	private String senderID;
	private String recipientID;
	private String content;
	private LocalDateTime timestamp;

	//constructor to iniatlise the new message with sender, recipient, content and timestamp
	public Message(String senderID, String recipientID, String content) {
		this.senderID = senderID;
		this.recipientID = recipientID;
		this.content = content;
		this.timestamp = LocalDateTime.now();
	}

	//override to allow the toString method to provide string representation of the message
	@Override
	public String toString() {
		//returns a formatted string showing, sender, recipient, content and timestamp
		return "From: " + senderID + " to: " + recipientID + "\nMessage: " + content+ "\nTime: " + timestamp;
	}

}
