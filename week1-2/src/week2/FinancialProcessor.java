package week2;

import java.util.*;
import java.util.concurrent.*;

public class FinancialProcessor {

    static class Transaction {
        int id;
        int amount;
        String merchant;
        long timestamp; // epoch ms
        String accountId;

        Transaction(int id, int amount, String merchant, long timestamp, String accountId) {
            this.id = id;
            this.amount = amount;
            this.merchant = merchant;
            this.timestamp = timestamp;
            this.accountId = accountId;
        }
    }

    private final List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    /**
     * Classic Two-Sum: Finds pairs summing to target.
     * Time Complexity: O(N)
     */
    public List<String> findTwoSum(int target) {
        Map<Integer, Transaction> seen = new HashMap<>();
        List<String> results = new ArrayList<>();

        for (Transaction t : transactions) {
            int complement = target - t.amount;
            if (seen.containsKey(complement)) {
                results.add("Pair found: ID " + seen.get(complement).id + " & ID " + t.id);
            }
            seen.put(t.amount, t);
        }
        return results;
    }

    /**
     * Duplicate Detection: Same amount, same merchant, different accounts.
     * Using a composite key for O(1) detection.
     */
    public void detectDuplicates() {
        // Key: "Amount:MerchantName"
        Map<String, List<Transaction>> activityMap = new HashMap<>();

        for (Transaction t : transactions) {
            String key = t.amount + ":" + t.merchant;
            activityMap.computeIfAbsent(key, k -> new ArrayList<>()).add(t);
        }

        System.out.println("\n--- Duplicate/Suspicious Activity ---");
        for (List<Transaction> group : activityMap.values()) {
            if (group.size() > 1) {
                Set<String> accounts = new HashSet<>();
                for (Transaction t : group) accounts.add(t.accountId);

                if (accounts.size() > 1) {
                    System.out.println("Suspicious: " + group.size() + " txns for $" +
                            group.get(0).amount + " at " + group.get(0).merchant +
                            " across " + accounts.size() + " accounts.");
                }
            }
        }
    }

    /**
     * K-Sum (Recursive with memoization hint)
     * Simplified 3-Sum variant for demonstration.
     */
    public List<List<Integer>> findThreeSum(int target) {
        List<List<Integer>> result = new ArrayList<>();
        transactions.sort(Comparator.comparingInt(a -> a.amount));

        for (int i = 0; i < transactions.size() - 2; i++) {
            int left = i + 1;
            int right = transactions.size() - 1;
            while (left < right) {
                int sum = transactions.get(i).amount + transactions.get(left).amount + transactions.get(right).amount;
                if (sum == target) {
                    result.add(Arrays.asList(transactions.get(i).id, transactions.get(left).id, transactions.get(right).id));
                    left++; right--;
                } else if (sum < target) left++;
                else right--;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        FinancialProcessor processor = new FinancialProcessor();

        processor.addTransaction(new Transaction(1, 500, "Store A", 1000, "Acc_1"));
        processor.addTransaction(new Transaction(2, 300, "Store B", 1015, "Acc_2"));
        processor.addTransaction(new Transaction(3, 200, "Store C", 1030, "Acc_3"));
        processor.addTransaction(new Transaction(4, 500, "Store A", 1045, "Acc_4")); // Duplicate hint

        System.out.println(processor.findTwoSum(500)); // Should find 300 + 200
        processor.detectDuplicates();
        System.out.println("Three Sum (1000): " + processor.findThreeSum(1000));
    }
}