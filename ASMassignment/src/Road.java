import java.util.concurrent.ArrayBlockingQueue;

public class Road {
    private String name;
    private ArrayBlockingQueue<Vehicle> vehicles;
    private int capacity;

    public Road(int capacity, String name) {
        this.name = name;
        this.capacity = capacity;
        this.vehicles = new ArrayBlockingQueue<>(capacity);
    }

    public boolean addVehicle(Vehicle vehicle) {
        return vehicles.offer(vehicle); // Adds the vehicle to the road if space is available
    }

    public Vehicle removeVehicle() {
        return vehicles.poll(); // Removes and returns the first vehicle from the road, or null if the road is empty
    }

    public boolean hasSpace() {
        return vehicles.size() < capacity; // Checks if there is space available on the road
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }
}
