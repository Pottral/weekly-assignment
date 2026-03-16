import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TrafficAnalytics {
    // 1. Data Structures for different dimensions
    private final Map<String, Integer> pageViews = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> uniqueVisitors = new ConcurrentHashMap<>();
    private final Map<String, Integer> trafficSources = new ConcurrentHashMap<>();

    // 2. Process incoming events (High Throughput)
    public void processEvent(String url, String userId, String source) {
        // Increment total views: O(1)
        pageViews.merge(url, 1, Integer::sum);

        // Track unique visitors: O(1) average
        uniqueVisitors.computeIfAbsent(url, k -> ConcurrentHashMap.newKeySet()).add(userId);

        // Track traffic sources: O(1)
        trafficSources.merge(source, 1, Integer::sum);
    }

    // 3. Generate Dashboard (Computed every 5 seconds)
    public void getDashboard() {
        System.out.println("\n--- REAL-TIME DASHBOARD (Updated: " + new Date() + ") ---");

        // Calculate Top 10 Pages: O(N log K) where N is total pages, K is 10
        List<Map.Entry<String, Integer>> topPages = pageViews.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(10)
                .collect(Collectors.toList());

        System.out.println("Top Pages:");
        int rank = 1;
        for (Map.Entry<String, Integer> entry : topPages) {
            String url = entry.getKey();
            int views = entry.getValue();
            int uniques = uniqueVisitors.get(url).size();
            System.out.printf("%d. %s - %d views (%d unique)\n", rank++, url, views, uniques);
        }

        System.out.println("\nTraffic Sources:");
        trafficSources.forEach((src, count) -> System.out.printf("- %s: %d\n", src, count));
        System.out.println("------------------------------------------------------");
    }

    public static void main(String[] args) throws InterruptedException {
        TrafficAnalytics analytics = new TrafficAnalytics();
        Random random = new Random();
        String[] sources = {"Google", "Facebook", "Direct", "Twitter"};
        String[] pages = {"/news/breaking", "/sports/final", "/tech/iphone", "/weather/local", "/finance/stocks"};

        // Simulate a stream of events in a separate thread
        Thread producer = new Thread(() -> {
            while (true) {
                String url = pages[random.nextInt(pages.length)];
                String user = "user_" + random.nextInt(1000);
                String src = sources[random.nextInt(sources.length)];
                analytics.processEvent(url, user, src);
                try { Thread.sleep(10); } catch (InterruptedException e) {}
            }
        });
        producer.setDaemon(true);
        producer.start();

        // Dashboard refresh loop
        for (int i = 0; i < 3; i++) {
            Thread.sleep(5000); // 5-second interval
            analytics.getDashboard();
        }
    }
}
