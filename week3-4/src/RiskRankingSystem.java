import java.util.*;

class Client {
    String id;
    int riskScore;
    double accountBalance;

    public Client(String id, int riskScore, double accountBalance) {
        this.id = id;
        this.riskScore = riskScore;
        this.accountBalance = accountBalance;
    }

    public String toString() {
        return id + "(" + riskScore + ", $" + accountBalance + ")";
    }
}

public class RiskRankingSystem {

    // 🔹 Bubble Sort (Ascending riskScore with visualization)
    public static void bubbleSort(Client[] arr) {
        int n = arr.length;
        int swaps = 0;

        System.out.println("\n--- Bubble Sort Steps ---");

        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;

            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j].riskScore > arr[j + 1].riskScore) {
                    // Swap
                    Client temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;

                    swaps++;
                    swapped = true;

                    // Visualization
                    System.out.println("Swap: " + Arrays.toString(arr));
                }
            }

            if (!swapped) break; // Early stop
        }

        System.out.println("Bubble Sorted (ASC): " + Arrays.toString(arr));
        System.out.println("Total Swaps: " + swaps);
    }

    // 🔹 Insertion Sort (DESC riskScore + accountBalance)
    public static void insertionSort(Client[] arr) {
        int n = arr.length;

        for (int i = 1; i < n; i++) {
            Client key = arr[i];
            int j = i - 1;

            // DESC riskScore, if equal → higher balance first
            while (j >= 0 && compare(arr[j], key) < 0) {
                arr[j + 1] = arr[j];
                j--;
            }

            arr[j + 1] = key;
        }

        System.out.println("\nInsertion Sorted (DESC): " + Arrays.toString(arr));
    }

    // 🔹 Comparator for DESC riskScore + accountBalance
    private static int compare(Client c1, Client c2) {
        if (c1.riskScore != c2.riskScore) {
            return Integer.compare(c1.riskScore, c2.riskScore);
        }
        return Double.compare(c1.accountBalance, c2.accountBalance);
    }

    // 🔹 Top 10 highest risk clients
    public static void topRiskClients(Client[] arr) {
        System.out.println("\nTop High Risk Clients:");

        int limit = Math.min(10, arr.length);

        for (int i = 0; i < limit; i++) {
            System.out.println(arr[i].id + " (Risk: " + arr[i].riskScore + ")");
        }
    }

    // 🔹 Main
    public static void main(String[] args) {

        Client[] clients = {
                new Client("clientC", 80, 5000),
                new Client("clientA", 20, 10000),
                new Client("clientB", 50, 7000)
        };

        System.out.println("Original: " + Arrays.toString(clients));

        // Bubble Sort (ASC)
        bubbleSort(clients);

        // Insertion Sort (DESC)
        insertionSort(clients);

        // Top Risks
        topRiskClients(clients);
    }
}