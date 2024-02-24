//public class Road {
//    private Vehicle[] vehicles;
//    private final int capacity;
//    private final String name;
//    private int front; // Index of the front of the queue
//    private int rear; // Index of the rear of the queue
//    private int size;
//    private int currentSize; // Current number of vehicles on the road
//
//    public Road(int capacity, String name) {
//        this.capacity = capacity;
//        this.name = name;
//        this.vehicles = new Vehicle[capacity];
//        this.front = 0;
//        this.rear = 0;
//        this.size = 0;
//        this.currentSize =0;
//    }
//
//    public int getSize(){
//        return currentSize;
//    }
//
//    public synchronized boolean addVehicle(Vehicle vehicle) {
//        if (size < capacity) {
//            vehicles[rear] = vehicle;
//            rear = (rear + 1) % capacity; // Circular buffer
//            size++;
//            return true;
//        } else {
//            return false; // Road is full
//        }
//    }
//
//    public synchronized Vehicle removeVehicle() {
//        if (size > 0) {
//            Vehicle vehicle = vehicles[front];
//            front = (front + 1) % capacity; // Circular buffer
//            size--;
//            return vehicle;
//        } else {
//            return null; // Road is empty
//        }
//    }
//
//    public synchronized boolean hasSpace() {
//        return size < capacity;
//    }
//
//    public synchronized boolean isEmpty() {
//        return size == 0;
//    }
//
//    // Other methods as needed
//
//}
// below is new code

import java.util.concurrent.ArrayBlockingQueue;

public class Road {
    private String name;
    private ArrayBlockingQueue<Vehicle> vehicles;
    private int capacity;

    public Road( int capacity, String name) {
        this.name=name;
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

    public boolean hasVehicle() {
        return !vehicles.isEmpty(); // Checks if there is any vehicle on the road
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }
}
