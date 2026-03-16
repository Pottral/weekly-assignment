import java.util.*;

public class UsernameChecker {

    private Set<String> usernames = new HashSet<>();
    private Map<String, Integer> attempts = new HashMap<>();

    public void register(String username, int id) {
        usernames.add(username);
        attempts.put(username, 1);
    }

    public boolean checkAvailability(String username) {
        attempts.put(username, attempts.getOrDefault(username, 0) + 1);
        return !usernames.contains(username);
    }

    public List<String> suggestAlternatives(String username) {
        List<String> list = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            list.add(username + i);
        }

        return list;
    }

    public String getMostAttempted() {

        String user = "";
        int max = 0;

        for (String u : attempts.keySet()) {
            if (attempts.get(u) > max) {
                max = attempts.get(u);
                user = u;
            }
        }

        return user;
    }
}