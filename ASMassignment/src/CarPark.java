//import java.time.LocalDateTime;
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.BlockingQueue;
//
//public class CarPark implements Runnable {
//    private String name;
//    private Road[] road;
//    private int capacity;
//    private BlockingQueue<Vehicle> parkedCars;
//    private int availableSpaces;
//
//    // Constructor
//    public CarPark(String name, Road road, int capacity) {
//        this.name = name;
//        this.road = new Road[]{};
//        this.capacity = capacity;
//        this.parkedCars = new ArrayBlockingQueue<>(capacity);
//        this.availableSpaces = capacity;
//    }
//
//    public int getAvailableSpaces() {
//        return availableSpaces;
//    }
//
//    // Method to admit a car into the car park
//    private void admitCar(Vehicle vehicle) throws InterruptedException {
//        // Simulate the car taking a ticket and moving through the barrier
//        Thread.sleep(12000); // 12 simulated seconds
//
//        // Add the car to the car park
//        parkedCars.put(vehicle);
//        System.out.println(vehicle + " parked at " + name + " at " + LocalDateTime.now());
//    }
//
//    // Method to remove a car from the car park
//    private void releaseCar() throws InterruptedException {
//        // Simulate the car leaving the car park
//        Thread.sleep(12000); // 12 simulated seconds
//
//        // Remove the car from the car park
//        Vehicle vehicle = parkedCars.take();
//        System.out.println(vehicle + " released from " + name + " at " + LocalDateTime.now());
//    }
//
//    // Method required by Runnable interface
//    @Override
//    public void run() {
//        while (true) {
//            try {
//                // Remove a car from the road and admit it into the car park
//                Vehicle vehicle = road.removeVehicle();
//                if (vehicle != null) {
//                    admitCar(vehicle);
//                }
//
//                // Wait for the next car to arrive
//                Thread.sleep(1000); // 1 simulated second
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
// below is new code
import java.util.concurrent.ArrayBlockingQueue;

public class CarPark extends Thread {
    private String name;
    private Road[] road;
    private ArrayBlockingQueue<Vehicle> parkedCars;
    private int capacity;
    private int processingTime; // Time taken to admit each car (in simulated seconds)

    public CarPark(String name, Road[] road,int capacity, int processingTime) {
        this.name = name;
        this.road=road;
        this.capacity = capacity;
        this.processingTime = processingTime;
        this.parkedCars = new ArrayBlockingQueue<>(capacity);
    }

    public void admitCar(Vehicle vehicle) {
        try {
            Thread.sleep(processingTime * 1000); // Simulate time taken to admit a car
            parkedCars.put(vehicle); // Add the vehicle to the car park
            System.out.println(vehicle + " admitted to " + name);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // Car park thread logic can be added here if needed
    }
}
