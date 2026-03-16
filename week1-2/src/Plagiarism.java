import java.util.*;
import java.util.concurrent.*;

class DNSCache {
    // 1. Internal Entry Class
    class DNSEntry {
        String domain;
        String ipAddress;
        long expiryTime;
        DNSEntry prev, next;

        DNSEntry(String domain, String ipAddress, int ttlSeconds) {
            this.domain = domain;
            this.ipAddress = ipAddress;
            // Store expiry as absolute epoch time
            this.expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000L);
        }
    }

    private final int capacity;
    private final Map<String, DNSEntry> map = new ConcurrentHashMap<>();
    private DNSEntry head, tail; // For LRU

    private int hits = 0;
    private int misses = 0;

    public DNSCache(int capacity) {
        this.capacity = capacity;
    }

    // 2. The Resolve Method
    public synchronized String resolve(String domain) {
        long startTime = System.currentTimeMillis();
        DNSEntry entry = map.get(domain);
        long now = System.currentTimeMillis();

        // Check if exists AND is still valid
        if (entry != null && now < entry.expiryTime) {
            moveToHead(entry);
            hits++;
            long elapsed = System.currentTimeMillis() - startTime;
            System.out.println("resolve(\"" + domain + "\") -> Cache HIT -> " + entry.ipAddress + " (retrieved in " + elapsed + "ms)");
            return entry.ipAddress;
        }

        // Handle Expired or Miss
        if (entry != null) {
            System.out.print("resolve(\"" + domain + "\") -> Cache EXPIRED -> ");
            removeNode(entry);
        } else {
            System.out.print("resolve(\"" + domain + "\") -> Cache MISS -> ");
        }

        // Simulate Upstream Query (100ms latency)
        String ip = "172.217.14." + (new Random().nextInt(254) + 1);
        int ttl = 2; // Short TTL for demonstration purposes

        addEntry(domain, ip, ttl);
        misses++;
        System.out.println("Query upstream -> " + ip + " (TTL: " + ttl + "s)");
        return ip;
    }

    // 3. LRU Logic
    private void addEntry(String domain, String ip, int ttl) {
        if (map.size() >= capacity) {
            System.out.println("[Evicting LRU: " + tail.domain + "]");
            map.remove(tail.domain);
            removeNode(tail);
        }
        DNSEntry newNode = new DNSEntry(domain, ip, ttl);
        map.put(domain, newNode);
        setHead(newNode);
    }

    private void setHead(DNSEntry node) {
        node.next = head;
        node.prev = null;
        if (head != null) head.prev = node;
        head = node;
        if (tail == null) tail = node;
    }

    private void removeNode(DNSEntry node) {
        if (node.prev != null) node.prev.next = node.next;
        else head = node.next;

        if (node.next != null) node.next.prev = node.prev;
        else tail = node.prev;
    }

    private void moveToHead(DNSEntry node) {
        removeNode(node);
        setHead(node);
    }

    public void printStats() {
        double total = hits + misses;
        double rate = (total == 0) ? 0 : (hits / total) * 100;
        System.out.println("\n--- Cache Stats ---");
        System.out.printf("Hit Rate: %.1f%%\n", rate);
        System.out.println("-------------------\n");
    }
}

// 4. Test Runner
class DNSManager {
    public static void main(String[] args) throws InterruptedException {
        DNSCache cache = new DNSCache(2); // Small capacity to test LRU

        // First resolve (Miss)
        cache.resolve("google.com");

        // Second resolve (Hit)
        cache.resolve("google.com");

        // Wait for TTL to expire (set to 2s in logic above)
        System.out.println("... waiting 3 seconds for TTL expiration ...");
        Thread.sleep(3000);

        // Third resolve (Expired/Miss)
        cache.resolve("google.com");

        cache.printStats();
    }
}