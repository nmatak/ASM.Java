import java.util.Random;
//public class EntryPoint implements Runnable {
//    private final String name;
//    private final int carsPerHour;
//    private final Road[] roads;
//    private final Random random;
//
//    public EntryPoint(String name, int carsPerHour, Road[] roads) {
//        this.name = name;
//        this.carsPerHour = carsPerHour;
//        this.roads = roads;
//        this.random = new Random();
//    }
//
//    @Override
//    public void run() {
//        while (true) {
//            try {
//                Thread.sleep(calculateInterval());
//                generateCar();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private long calculateInterval() {
//        return (long) (3600 * 1000.0 / carsPerHour);
//    }
//
//    private void generateCar() {
//        int destination = selectDestination();
//        long timestamp = System.currentTimeMillis();
//        Vehicle vehicle = new Vehicle(name, destination, timestamp);
//        boolean added = false;
//        for (Road road : roads) {
//            if (road.addVehicle(vehicle)) {
//                added = true;
//                break;
//            }
//        }
//        if (!added) {
//            // No space available on any road, handle accordingly
//        }
//    }
//
//    private int selectDestination() {
//        int randomNumber = random.nextInt(100);
//        if (randomNumber < 10) {
//            return Destination.UNIVERSITY;
//        } else if (randomNumber < 30) {
//            return Destination.STATION;
//        } else if (randomNumber < 60) {
//            return Destination.SHOPPING_CENTRE;
//        } else {
//            return Destination.INDUSTRIAL_PARK;
//        }
//    }
//}
// below is new code

import java.time.LocalTime;

public class EntryPoint implements Runnable {
    private final String name;
    private final int interval; // Interval in seconds between vehicle arrivals
    private final Road[] roads;

    public EntryPoint(String name, int interval, Road[] roads) {
        this.name = name;
        this.interval = interval;
        this.roads = roads;
    }

    @Override
    public void run() {
        Random random = new Random();
        while (true) {
            try {
                Thread.sleep(interval * 1000L); // Convert seconds to milliseconds
                LocalTime entryTime = LocalTime.now();
                int destination = generateRandomDestination(random);
                Vehicle vehicle = new Vehicle(destination, entryTime);
                feedVehicleToRoad(vehicle);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private int generateRandomDestination(Random random) {
        int randomNumber = random.nextInt(100); // Generate a random number between 0 and 99
        if (randomNumber < 10) {
            return Destination.UNIVERSITY;
        } else if (randomNumber < 30) {
            return Destination.STATION;
        } else if (randomNumber < 60) {
            return Destination.SHOPPING_CENTRE;
        } else {
            return Destination.INDUSTRIAL_PARK;
        }
    }

    private void feedVehicleToRoad(Vehicle vehicle) {
        for (Road road : roads) {
            if (road.hasSpace()) {
                road.addVehicle(vehicle);
                System.out.println("Vehicle added to road: " + vehicle.getDestination() + " -> " + road.getName());
                return;
            }
        }
        System.out.println("No space available on any road for vehicle: " + vehicle.getDestination());
    }
}
