import java.util.*;

public class RiskLookup {

    // 🔹 Linear Search (unsorted)
    public static void linearSearch(int[] arr, int target) {
        int comparisons = 0;
        boolean found = false;

        for (int i = 0; i < arr.length; i++) {
            comparisons++;
            if (arr[i] == target) {
                System.out.println("Linear Found at index " + i);
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Linear: Not Found");
        }
        System.out.println("Comparisons: " + comparisons);
    }

    // 🔹 Binary Search - Insertion Point (lower_bound)
    public static int lowerBound(int[] arr, int target) {
        int low = 0, high = arr.length;
        int comparisons = 0;

        while (low < high) {
            int mid = (low + high) / 2;
            comparisons++;

            if (arr[mid] < target) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }

        System.out.println("Insertion Index (lower_bound): " + low +
                ", Comparisons: " + comparisons);
        return low;
    }

    // 🔹 Floor (largest ≤ target)
    public static Integer floor(int[] arr, int target) {
        int low = 0, high = arr.length - 1;
        Integer result = null;
        int comparisons = 0;

        while (low <= high) {
            int mid = (low + high) / 2;
            comparisons++;

            if (arr[mid] == target) {
                result = arr[mid];
                break;
            } else if (arr[mid] < target) {
                result = arr[mid]; // possible floor
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        System.out.println("Floor: " + result + ", Comparisons: " + comparisons);
        return result;
    }

    // 🔹 Ceiling (smallest ≥ target)
    public static Integer ceiling(int[] arr, int target) {
        int low = 0, high = arr.length - 1;
        Integer result = null;
        int comparisons = 0;

        while (low <= high) {
            int mid = (low + high) / 2;
            comparisons++;

            if (arr[mid] == target) {
                result = arr[mid];
                break;
            } else if (arr[mid] > target) {
                result = arr[mid]; // possible ceiling
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        System.out.println("Ceiling: " + result + ", Comparisons: " + comparisons);
        return result;
    }

    // 🔹 Main
    public static void main(String[] args) {

        int[] risks = {10, 25, 50, 100}; // already sorted
        int target = 30;

        System.out.println("Risk Bands: " + Arrays.toString(risks));
        System.out.println("Target: " + target);

        // Linear Search (unsorted scenario simulation)
        linearSearch(risks, target);

        // Binary Search operations
        lowerBound(risks, target);
        floor(risks, target);
        ceiling(risks, target);
    }
}