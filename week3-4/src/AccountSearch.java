import java.util.*;

public class AccountSearch {

    // 🔹 Linear Search (first occurrence)
    public static int linearFirst(String[] arr, String target) {
        int comparisons = 0;

        for (int i = 0; i < arr.length; i++) {
            comparisons++;
            if (arr[i].equals(target)) {
                System.out.println("Linear First Index: " + i + ", Comparisons: " + comparisons);
                return i;
            }
        }

        System.out.println("Not Found, Comparisons: " + comparisons);
        return -1;
    }

    // 🔹 Linear Search (last occurrence)
    public static int linearLast(String[] arr, String target) {
        int comparisons = 0;
        int index = -1;

        for (int i = 0; i < arr.length; i++) {
            comparisons++;
            if (arr[i].equals(target)) {
                index = i;
            }
        }

        System.out.println("Linear Last Index: " + index + ", Comparisons: " + comparisons);
        return index;
    }

    // 🔹 Binary Search (find any occurrence)
    public static int binarySearch(String[] arr, String target) {
        int low = 0, high = arr.length - 1;
        int comparisons = 0;

        while (low <= high) {
            int mid = (low + high) / 2;
            comparisons++;

            int cmp = arr[mid].compareTo(target);

            if (cmp == 0) {
                System.out.println("Binary Found Index: " + mid + ", Comparisons: " + comparisons);
                return mid;
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        System.out.println("Not Found, Comparisons: " + comparisons);
        return -1;
    }

    // 🔹 Count occurrences using Binary Search
    public static int countOccurrences(String[] arr, String target) {
        int first = firstOccurrence(arr, target);
        int last = lastOccurrence(arr, target);

        if (first == -1) return 0;
        return last - first + 1;
    }

    // 🔹 First occurrence (Binary)
    private static int firstOccurrence(String[] arr, String target) {
        int low = 0, high = arr.length - 1, result = -1;

        while (low <= high) {
            int mid = (low + high) / 2;

            if (arr[mid].equals(target)) {
                result = mid;
                high = mid - 1; // move left
            } else if (arr[mid].compareTo(target) < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return result;
    }

    // 🔹 Last occurrence (Binary)
    private static int lastOccurrence(String[] arr, String target) {
        int low = 0, high = arr.length - 1, result = -1;

        while (low <= high) {
            int mid = (low + high) / 2;

            if (arr[mid].equals(target)) {
                result = mid;
                low = mid + 1; // move right
            } else if (arr[mid].compareTo(target) < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return result;
    }

    // 🔹 Main
    public static void main(String[] args) {

        String[] logs = {"accA", "accB", "accB", "accC"};
        Arrays.sort(logs); // Required for Binary Search

        System.out.println("Sorted Logs: " + Arrays.toString(logs));

        // Linear Search
        linearFirst(logs, "accB");
        linearLast(logs, "accB");

        // Binary Search
        binarySearch(logs, "accB");

        // Count duplicates
        int count = countOccurrences(logs, "accB");
        System.out.println("Total Occurrences of accB: " + count);
    }
}