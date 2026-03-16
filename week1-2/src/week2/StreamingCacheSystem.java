import java.util.*;
import java.util.concurrent.*;

public class StreamingCacheSystem {

    // Mock Video Data
    static class Video {
        String id;
        String title;
        Video(String id, String title) { this.id = id; this.title = title; }
    }

    // L1 Cache: In-Memory LRU (Capacity: 10,000)
    private final Map<String, Video> l1Cache;
    private final int L1_CAPACITY = 10000;

    // L2 Cache: SSD-Backed (Capacity: 100,000)
    private final Map<String, Video> l2Cache = new ConcurrentHashMap<>();
    private final int L2_CAPACITY = 100000;

    // Stats Tracking
    private double l1Hits = 0, l2Hits = 0, l3Hits = 0, totalRequests = 0;

    public StreamingCacheSystem() {
        // LinkedHashMap with accessOrder = true handles LRU automatically
        this.l1Cache = Collections.synchronizedMap(new LinkedHashMap<String, Video>(L1_CAPACITY, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Video> eldest) {
                return size() > L1_CAPACITY;
            }
        });
    }

    public Video getVideo(String videoId) {
        totalRequests++;
        long startTime = System.currentTimeMillis();

        // 1. Check L1 (Memory)
        if (l1Cache.containsKey(videoId)) {
            l1Hits++;
            System.out.println("-> L1 HIT (0.5ms)");
            return l1Cache.get(videoId);
        }

        // 2. Check L2 (SSD Simulation)
        if (l2Cache.containsKey(videoId)) {
            l2Hits++;
            Video v = l2Cache.get(videoId);
            System.out.println("-> L1 MISS, L2 HIT (5ms)");

            // Promote to L1
            l1Cache.put(videoId, v);
            return v;
        }

        // 3. Check L3 (Database Simulation)
        l3Hits++;
        Video v = fetchFromDatabase(videoId);
        System.out.println("-> L1 MISS, L2 MISS, L3 HIT (150ms)");

        // Add to L2 (Promotion logic could be more complex here)
        if (l2Cache.size() >= L2_CAPACITY) {
            // Simple eviction for L2 demo
            String firstKey = l2Cache.keySet().iterator().next();
            l2Cache.remove(firstKey);
        }
        l2Cache.put(videoId, v);

        return v;
    }

    private Video fetchFromDatabase(String id) {
        try { Thread.sleep(150); } catch (InterruptedException e) {} // Latency
        return new Video(id, "Title of " + id);
    }

    public void getStatistics() {
        System.out.println("\n--- Cache Performance Stats ---");
        System.out.printf("L1 Hit Rate: %.1f%%\n", (l1Hits / totalRequests) * 100);
        System.out.printf("L2 Hit Rate: %.1f%%\n", (l2Hits / totalRequests) * 100);
        System.out.printf("L3 Hit Rate: %.1f%%\n", (l3Hits / totalRequests) * 100);
        double avgTime = (l1Hits * 0.5 + l2Hits * 5.5 + l3Hits * 155.5) / totalRequests;
        System.out.printf("Overall Avg Latency: %.2fms\n", avgTime);
    }

    public static void main(String[] args) {
        StreamingCacheSystem netflix = new StreamingCacheSystem();

        // Request 1: Total Miss
        netflix.getVideo("vid_1");

        // Request 2: L1 Hit
        netflix.getVideo("vid_1");

        // Request 3: Simulate promotion from L2
        // (In a real app, vid_1 would stay in L1, but if evicted it falls to L2)
        netflix.getStatistics();
    }
}