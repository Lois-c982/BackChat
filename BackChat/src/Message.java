import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
	private String senderID;
	private String recipientID;
	private String content;
	private LocalDateTime timestamp;
	
	public Message(String senderID, String recipientID, String content) {
		this.senderID = senderID;
		this.recipientID = recipientID;
		this.content = content;
		this.timestamp = LocalDateTime.now();
	}
	
	@Override
	public String toString() {
		return "From: " + senderID + " to: " + recipientID + "\nMessage: " + content+ "\nTime: " + timestamp;
	}

}