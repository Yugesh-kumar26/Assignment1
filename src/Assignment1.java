import java.util.*;

public class Assignment1 {

    class Vehicle {
        String licensePlate;
        long entryTime;

        Vehicle(String licensePlate) {
            this.licensePlate = licensePlate;
            this.entryTime = System.currentTimeMillis();
        }
    }

    private class Spot {
        Vehicle vehicle;
        boolean occupied;

        Spot() {
            vehicle = null;
            occupied = false;
        }
    }

    private Spot[] parkingLot;
    private int capacity = 500;
    private int totalProbes = 0;
    private int totalParked = 0;

    public Assignment1() {
        parkingLot = new Spot[capacity];
        for (int i = 0; i < capacity; i++) {
            parkingLot[i] = new Spot();
        }
    }

    private int hash(String licensePlate) {
        return Math.abs(licensePlate.hashCode()) % capacity;
    }

    public String parkVehicle(String licensePlate) {
        int idx = hash(licensePlate);
        int probes = 0;

        while (parkingLot[idx].occupied) {
            idx = (idx + 1) % capacity;
            probes++;
        }

        parkingLot[idx].vehicle = new Vehicle(licensePlate);
        parkingLot[idx].occupied = true;
        totalProbes += probes;
        totalParked++;

        return "Assigned spot #" + idx + " (" + probes + " probes)";
    }

    public String exitVehicle(String licensePlate) {
        int idx = hash(licensePlate);
        int probes = 0;

        while (probes < capacity) {
            if (parkingLot[idx].occupied && parkingLot[idx].vehicle.licensePlate.equals(licensePlate)) {
                long durationMs = System.currentTimeMillis() - parkingLot[idx].vehicle.entryTime;
                double hours = durationMs / 3600000.0;
                double fee = Math.round(hours * 5 * 100.0) / 100.0;

                parkingLot[idx].vehicle = null;
                parkingLot[idx].occupied = false;
                totalParked--;

                return "Spot #" + idx + " freed, Duration: " + String.format("%.2f", hours) + "h, Fee: $" + fee;
            }

            idx = (idx + 1) % capacity;
            probes++;
        }

        return "Vehicle not found";
    }

    public String getStatistics() {
        double occupancy = (totalParked * 100.0) / capacity;
        double avgProbes = totalParked == 0 ? 0 : totalProbes * 1.0 / totalParked;

        return "Occupancy: " + String.format("%.1f", occupancy) + "%, Avg Probes: " + String.format("%.2f", avgProbes);
    }
}