import java.util.*;

public class SmartParking {
    enum Status { EMPTY, OCCUPIED, DELETED }

    static class Spot {
        String licensePlate;
        long entryTime;
        Status status = Status.EMPTY;

        void park(String plate) {
            this.licensePlate = plate;
            this.entryTime = System.currentTimeMillis();
            this.status = Status.OCCUPIED;
        }

        void clear() {
            this.status = Status.DELETED;
            this.licensePlate = null;
        }
    }

    private final int capacity;
    private final Spot[] lot;
    private int occupiedCount = 0;
    private int totalProbes = 0;
    private int totalParkings = 0;

    public SmartParking(int capacity) {
        this.capacity = capacity;
        this.lot = new Spot[capacity];
        for (int i = 0; i < capacity; i++) lot[i] = new Spot();
    }

    // Custom Hash Function
    private int hash(String plate) {
        return Math.abs(plate.hashCode()) % capacity;
    }

    /**
     * Parks a vehicle using Linear Probing.
     * Complexity: O(1) average, O(N) worst case.
     */
    public String parkVehicle(String plate) {
        if (occupiedCount >= capacity) return "Error: Lot Full";

        int preferredSpot = hash(plate);
        int currentSpot = preferredSpot;
        int probes = 0;

        // Linear Probing: Find next OCCUPIED or DELETED spot
        while (lot[currentSpot].status == Status.OCCUPIED) {
            currentSpot = (currentSpot + 1) % capacity;
            probes++;
        }

        lot[currentSpot].park(plate);
        occupiedCount++;
        totalProbes += probes;
        totalParkings++;

        return String.format("Assigned spot #%d (%d probes)", currentSpot, probes);
    }

    /**
     * Frees a spot and calculates fee.
     */
    public String exitVehicle(String plate) {
        int preferredSpot = hash(plate);
        int currentSpot = preferredSpot;
        int checked = 0;

        while (checked < capacity) {
            if (lot[currentSpot].status == Status.EMPTY) break; // Not found

            if (lot[currentSpot].status == Status.OCCUPIED &&
                    lot[currentSpot].licensePlate.equals(plate)) {

                long durationMs = System.currentTimeMillis() - lot[currentSpot].entryTime;
                double fee = Math.max(5.0, (durationMs / 1000.0) * 2.0); // Simple fee logic

                lot[currentSpot].clear();
                occupiedCount--;
                return String.format("Spot #%d freed. Fee: $%.2f", currentSpot, fee);
            }
            currentSpot = (currentSpot + 1) % capacity;
            checked++;
        }
        return "Vehicle not found.";
    }

    public void getStatistics() {
        double occupancy = ((double) occupiedCount / capacity) * 100;
        double avgProbes = totalParkings == 0 ? 0 : (double) totalProbes / totalParkings;

        System.out.println("\n--- Parking Stats ---");
        System.out.printf("Occupancy: %.1f%%\n", occupancy);
        System.out.printf("Avg Probes per Parking: %.2f\n", avgProbes);
        System.out.println("----------------------\n");
    }

    public static void main(String[] args) {
        SmartParking mallParking = new SmartParking(500);

        // Simulate collisions
        System.out.println(mallParking.parkVehicle("ABC-1234"));
        System.out.println(mallParking.parkVehicle("ABC-1235")); // Likely collision
        System.out.println(mallParking.parkVehicle("XYZ-9999"));

        mallParking.getStatistics();
        System.out.println(mallParking.exitVehicle("ABC-1234"));
    }
}