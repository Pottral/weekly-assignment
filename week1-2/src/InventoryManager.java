import java.util.*;

public class InventoryManager {

    private HashMap<String, Integer> stock = new HashMap<>();
    private LinkedHashMap<Integer, String> waitingList = new LinkedHashMap<>();

    public void addProduct(String productId, int quantity) {
        stock.put(productId, quantity);
    }

    public void checkStock(String productId) {
        if (stock.containsKey(productId)) {
            System.out.println(productId + " -> " + stock.get(productId) + " units available");
        } else {
            System.out.println("Product not found");
        }
    }

    public synchronized void purchaseItem(String productId, int userId) {

        int quantity = stock.getOrDefault(productId, 0);

        if (quantity > 0) {
            stock.put(productId, quantity - 1);

            System.out.println("Success for user " + userId +
                    ", " + (quantity - 1) + " units remaining");
        } else {

            waitingList.put(userId, productId);

            System.out.println("Added to waiting list -> user " + userId +
                    ", position " + waitingList.size());
        }
    }
}