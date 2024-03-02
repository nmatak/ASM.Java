import java.util.Random;
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
