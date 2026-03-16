import java.util.*;

public class problem1 {


    private HashMap<String, Integer> usernameMap = new HashMap<>();
    private HashMap<String, Integer> attemptCount = new HashMap<>();

    public boolean checkAvailability(String username) {

        attemptCount.put(username, attemptCount.getOrDefault(username, 0) + 1);

        return !usernameMap.containsKey(username);
    }

    public void registerUser(String username, int userId) {
        usernameMap.put(username, userId);
    }

    public List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            String candidate = username + i;

            if (!usernameMap.containsKey(candidate)) {
                suggestions.add(candidate);
            }
        }

        String alt = username.replace("_", ".");
        if (!usernameMap.containsKey(alt)) {
            suggestions.add(alt);
        }

        return suggestions;
    }

    public String getMostAttempted() {

        String maxUser = "";
        int maxCount = 0;

        for (String user : attemptCount.keySet()) {

            int count = attemptCount.get(user);

            if (count > maxCount) {
                maxCount = count;
                maxUser = user;
            }
        }

        return maxUser + " (" + maxCount + " attempts)";
    }

    public static void main(String[] args) {

        problem1 checker = new problem1();
        Scanner scanner = new Scanner(System.in);

        checker.registerUser("john_doe", 101);
        checker.registerUser("admin", 102);
        checker.registerUser("user123", 103);

        while (true) {

            System.out.println("\n===== Username System =====");
            System.out.println("1. Check Username");
            System.out.println("2. Register Username");
            System.out.println("3. Suggest Alternatives");
            System.out.println("4. Most Attempted Username");
            System.out.println("5. Exit");

            System.out.print("Choose option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();

                    boolean available = checker.checkAvailability(username);

                    if (available) System.out.println("Username available");
                    else System.out.println("Username already taken");

                    break;

                case 2:
                    System.out.print("Enter username: ");
                    String newUser = scanner.nextLine();

                    if (checker.checkAvailability(newUser)) {

                        System.out.print("Enter user ID: ");
                        int id = scanner.nextInt();

                        checker.registerUser(newUser, id);
                        System.out.println("User registered");
                    } else {
                        System.out.println("Username already exists");
                    }

                    break;

                case 3:
                    System.out.print("Enter username: ");
                    String name = scanner.nextLine();

                    List<String> suggestions = checker.suggestAlternatives(name);

                    System.out.println("Suggestions: " + suggestions);

                    break;

                case 4:
                    System.out.println("Most attempted username: " + checker.getMostAttempted());
                    break;

                case 5:
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid option");
            }
        }
    }
}
