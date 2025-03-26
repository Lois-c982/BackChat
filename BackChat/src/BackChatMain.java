import java.util.Scanner;
import java.util.InputMismatchException;

public class BackChatMain {
    private static BackChat backChat = new BackChat();

    public static void main(String[] args) {
        backChat.loadNetworkFromFile();

        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("Welcome to SocialConnect!");
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
                    createUser(scanner);
                    break;
                case 2:
                    login(scanner);
                    break;
                case 3:
                    isRunning = false;
                    backChat.saveNetworkToFile();
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }

        scanner.close();
    }

    private static void createUser(Scanner scanner) {
        System.out.print("Enter User ID: ");
        String userID = scanner.nextLine();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Birthday (yyyy-mm-dd): ");
        String birthday = scanner.nextLine();
        System.out.print("Enter Workplace: ");
        String workplace = scanner.nextLine();
        System.out.print("Enter Hometown: ");
        String hometown = scanner.nextLine();

        User newUser = new User(userID, name, birthday, workplace, hometown);
        backChat.addUser(newUser);
        System.out.println("User created successfully!");
    }

    private static void login(Scanner scanner) {
        System.out.print("Enter User ID: ");
        String userID = scanner.nextLine();

        if (backChat.login(userID)) {
            System.out.println("Login successful! Welcome, " + backChat.getCurrentUser().getName() + "!");
            User currentUser = backChat.getCurrentUser();

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
                        System.out.println(currentUser);
                        break;
                    case 2:
                        viewFriends(currentUser);
                        break;
                    case 3:
                        addFriend(scanner, currentUser);
                        break;
                    case 4:
                        backChat.deleteFriend(currentUser);
                        break;
                    case 5:
                        backChat.viewFriendOfFriend(currentUser);
                        break;
                    case 6:
                        sendMessage(scanner, currentUser);
                        break;
                    case 7:
                        currentUser.viewMessages();
                        break;
                    case 8:
                        isLoggedIn = false;
                        System.out.println("Logged out successfully!");
                        break;
                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            }
        } else {
            System.out.println("Invalid User ID!");
        }
    }

    private static void viewFriends(User currentUser) {
        if (currentUser.getFriends().isEmpty()) {
            System.out.println("You have no friends yet.");
        } else {
            System.out.println("Your friends:");
            for (User friend : currentUser.getFriends()) {
                System.out.println(friend);
            }
        }
    }

    private static void addFriend(Scanner scanner, User currentUser) {
        System.out.print("Enter the User ID of the friend to add: ");
        String friendUserId = scanner.nextLine();
        backChat.addFriendById(currentUser, friendUserId);
    }

    private static void sendMessage(Scanner scanner, User currentUser) {
        System.out.print("Enter the User ID of the recipient: ");
        String recipientID = scanner.nextLine();
        User recipient = backChat.findUserByID(recipientID);
        if (recipient != null && !recipient.equals(currentUser)) {
            System.out.print("Enter your message: ");
            String messageContent = scanner.nextLine();
            currentUser.sendMessage(recipient, messageContent);
            System.out.println("Message sent!");
        } else {
            System.out.println("Invalid recipient or you cannot message yourself.");
        }
    }
}
