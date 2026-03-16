import java.util.*;

public class SocialMedia {
    static void main() {
                UsernameChecker checker = new UsernameChecker();

                checker.register("john_doe", 101);

                System.out.println(checker.checkAvailability("john_doe"));
                System.out.println(checker.checkAvailability("jane_smith"));

                System.out.println(checker.suggestAlternatives("john_doe"));

                System.out.println(checker.getMostAttempted());
            }
        }
