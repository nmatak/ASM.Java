import java.util.ArrayList;
import java.util.List;

public class CarPark extends Thread {
    private String name;
    private Road[] roads;
    private int capacity;
    private int processingTime;
    private List<ParkingSpace> parkingSpaces;

    public CarPark(String name, Road[] roads, int capacity, int processingTime) {
        this.name = name;
        this.roads = roads;
        this.capacity = capacity;
        this.processingTime = processingTime;
        this.parkingSpaces = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            parkingSpaces.add(new ParkingSpace());
        }
    }

    @Override
    public void run() {
        while (true) {
            // Simulate processing of vehicles
            try {
                Thread.sleep(processingTime * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized boolean parkVehicle(Vehicle vehicle) {
        for (ParkingSpace space : parkingSpaces) {
            if (space.park(vehicle)) {
                return true;
            }
        }
        return false; // No available parking space
    }

    public synchronized void vehicleLeaves(Vehicle vehicle) {
        for (ParkingSpace space : parkingSpaces) {
            if (space.isOccupied()) {
                space.leave();
                break;
            }
        }
    }

}
