import java.util.*;
import java.util.concurrent.*;

public class AutocompleteSystem {

    // 1. Trie Node Structure
    class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        // Cache top 10 suggestions at each node for O(1) prefix lookup
        List<String> topSuggestions = new ArrayList<>();
    }

    private final TrieNode root = new TrieNode();
    private final Map<String, Integer> queryFrequency = new ConcurrentHashMap<>();
    private final int TOP_K = 10;

    /**
     * Updates the frequency of a query and refreshes the Trie cache.
     */
    public void updateFrequency(String query) {
        queryFrequency.merge(query, 1, Integer::sum);
        insertIntoTrie(query);
    }

    private void insertIntoTrie(String query) {
        TrieNode curr = root;
        for (char c : query.toCharArray()) {
            curr.children.putIfAbsent(c, new TrieNode());
            curr = curr.children.get(c);
            updateNodeCache(curr, query);
        }
    }

    /**
     * Ensures the node's top 10 list includes this query if it's popular enough.
     */
    private void updateNodeCache(TrieNode node, String query) {
        if (!node.topSuggestions.contains(query)) {
            node.topSuggestions.add(query);
        }

        // Sort suggestions by frequency (descending)
        node.topSuggestions.sort((a, b) -> {
            int freqA = queryFrequency.getOrDefault(a, 0);
            int freqB = queryFrequency.getOrDefault(b, 0);
            return freqB - freqA;
        });

        // Keep only top 10
        if (node.topSuggestions.size() > TOP_K) {
            node.topSuggestions.remove(TOP_K);
        }
    }

    /**
     * Returns top 10 suggestions for a prefix.
     * Time Complexity: O(L) where L is prefix length. (Extremely fast)
     */
    public List<String> search(String prefix) {
        TrieNode curr = root;
        for (char c : prefix.toCharArray()) {
            if (!curr.children.containsKey(c)) {
                return Collections.emptyList();
            }
            curr = curr.children.get(c);
        }
        return curr.topSuggestions;
    }

    public static void main(String[] args) {
        AutocompleteSystem engine = new AutocompleteSystem();

        // Simulate historical data
        engine.updateFrequency("java tutorial");
        engine.updateFrequency("java tutorial"); // Higher frequency
        engine.updateFrequency("javascript");
        engine.updateFrequency("java download");
        engine.updateFrequency("java 21 features");

        // Test search
        System.out.println("Suggestions for 'jav':");
        List<String> results = engine.search("jav");
        for (int i = 0; i < results.size(); i++) {
            String q = results.get(i);
            System.out.println((i + 1) + ". " + q + " (" + engine.queryFrequency.get(q) + " searches)");
        }
    }
}