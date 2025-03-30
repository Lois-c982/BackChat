import java.util.Scanner;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;

public class BackChatMain {
    private static BackChat backChat = new BackChat();

    public static void main(String[] args) {
        //load network data from file
        backChat.loadNetworkFromFile();

        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;
        
        //main loop for interacting with the user
        while (isRunning) {
            System.out.println("Welcome to BackChat!");
            System.out.println("1. Create New User");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");

            int choice = -1;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume invalid input
                continue;
            }

            switch (choice) {
                case 1:
                    createUser(scanner); //create a new user
                    break;
                case 2:
                    login(scanner); //login for an existing user
                    break;
                case 3:
                    isRunning = false;
                    backChat.saveNetworkToFile(); //exit program and save network data to file
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }

        scanner.close();
    }
    //method to create new user
    private static void createUser(Scanner scanner) {
        String userID;
        
        // Ensure the user ID is unique
        while (true) {
            System.out.print("Enter User ID: ");
            userID = scanner.nextLine().trim();
            
            if (backChat.findUserByID(userID) != null) {
                System.out.println("User ID already exists. Please choose a different one.");
            } else {
                break; // Exit the loop if the userID is unique
            }
        }
        //get other details from user
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Birthday (yyyy-mm-dd): ");
        String birthday = scanner.nextLine();
        System.out.print("Enter Workplace: ");
        String workplace = scanner.nextLine();
        System.out.print("Enter Hometown: ");
        String hometown = scanner.nextLine();
        //creates the new user object then add to network
        User newUser = new User(userID, name, birthday, workplace, hometown);
        backChat.addUser(newUser);
        System.out.println("User created successfully!");
    }
    
    //login method for existing user
    private static void login(Scanner scanner) {
        System.out.print("Enter User ID: ");
        String userID = scanner.nextLine();

        if (backChat.login(userID)) {
            System.out.println("Login successful! Welcome, " + backChat.getCurrentUser().getName() + "!");
            User currentUser = backChat.getCurrentUser();

            //loop for main menu of a logged in user
            boolean isLoggedIn = true;
            while (isLoggedIn) {
                System.out.println("\n--- Main Menu ---");
                System.out.println("1. View Profile");
                System.out.println("2. View Friends");
                System.out.println("3. Add Friend");
                System.out.println("4. Delete Friend");
                System.out.println("5. View a Friend's Friends");
                System.out.println("6. Send Message");
                System.out.println("7. View Messages");
                System.out.println("8. Logout");
                System.out.println("9. Friend Recommendations");
                System.out.print("Select an option: ");

                int option = -1;
                try {
                    option = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.nextLine(); // Consume invalid input
                    continue;
                }

                switch (option) {
                    case 1:
                        System.out.println(currentUser); //display user's profile
                        break;
                    case 2:
                        viewFriends(currentUser); //view users friends
                        break;
                    case 3:
                        addFriend(scanner, currentUser); //add new friend
                        break;
                    case 4:
                        backChat.deleteFriend(currentUser); //delete a friend
                        break;
                    case 5:
                        backChat.viewFriendOfFriend(currentUser); //view user's friend's friends
                        break;
                    case 6:
                        sendMessage(scanner, currentUser); //send message to a friend
                        break;
                    case 7:
                        currentUser.viewMessages();//view received messages of the user
                        break;
                    case 8:
                        isLoggedIn = false; // logs out user
                        System.out.println("Logged out successfully!");
                        break;
                    case 9:
                        backChat.recommendFriends(currentUser); //friend recommendation for user's based on mutuals
                        break;
                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            }
        } else {
            System.out.println("Invalid User ID!");
        }
    }
    //method for viewing current user's friends using filtering options
    private static void viewFriends(User currentUser) {
    Scanner scanner = new Scanner(System.in);

    //check if user has any friends
    if (currentUser.getFriends().isEmpty()) {
        System.out.println("You have no friends yet.");
        return;
    }
    //display filtering options to user
    System.out.println("Filter by:");
    System.out.println("1. Show all friends (sorted A-Z)");
    System.out.println("2. Filter by hometown");
    System.out.println("3. Filter by workplace");
    System.out.print("Select an option: ");

    int filterOption = -1;
    try {
        filterOption = scanner.nextInt();
        scanner.nextLine(); // Consume newline
    } catch (InputMismatchException e) {
        System.out.println("Invalid input. Please enter a number.");
        scanner.nextLine(); // Consume invalid input
        return;
    }

    List<User> filteredFriends = new ArrayList<>(currentUser.getFriends());
    //apply user selected by user
    switch (filterOption) {
        case 1:
            filteredFriends.sort(Comparator.comparing(User::getName, String.CASE_INSENSITIVE_ORDER)); //sort friends by name
            break;
        case 2:
            System.out.print("Enter hometown to filter by: ");
            String hometown = scanner.nextLine();
            filteredFriends.removeIf(friend -> !friend.getHometown().equalsIgnoreCase(hometown)); //filter by hometown
            break;
        case 3:
            System.out.print("Enter workplace to filter by: ");
            String workplace = scanner.nextLine();
            filteredFriends.removeIf(friend -> !friend.getWorkplace().equalsIgnoreCase(workplace)); //filter by workplace
            break;
        default:
            System.out.println("Invalid option. Showing all friends.");
    }

    //display filtered friends list
    if (filteredFriends.isEmpty()) {
        System.out.println("No friends matched your filter.");
    } else {
        System.out.println("Your friends:");
        for (User friend : filteredFriends) {
            System.out.println(friend);
            }
        }
    }
    //method to add new friends to user's friend list
    private static void addFriend(Scanner scanner, User currentUser) {
        System.out.print("Enter the User ID of the friend to add: ");
        String friendUserId = scanner.nextLine();
        backChat.addFriendById(currentUser, friendUserId); //calls method for adding friend
    }
    //method for sending a message to a friend
    private static void sendMessage(Scanner scanner, User currentUser) {
        System.out.print("Enter the User ID of the recipient: ");
        String recipientID = scanner.nextLine();
        User recipient = backChat.findUserByID(recipientID);
        if (recipient != null && !recipient.equals(currentUser)) {
            System.out.print("Enter your message: ");
            String messageContent = scanner.nextLine();
            currentUser.sendMessage(recipient, messageContent); //sends the message to recipient
            currentUser.sendMessage(currentUser, messageContent); //adds message to sender's inbox
            System.out.println("Message sent!");
        } else {
            System.out.println("Invalid recipient or you cannot message yourself.");
        }
    }
    
}
