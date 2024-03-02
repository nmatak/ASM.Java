import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Junction extends Thread {
    private static long simulationTime = 0; 
    private String name;
    private int[] entryRoutes;
    private int[] exitRoutes;
    private int numEntryRoutes;
    private int numExitRoutes;
    private Road[] roads;
    private int[][] destinationRoutes;
    private int[] greenLights;
    private int greenLightDuration; // Duration of green light for each road (in seconds)
    private Lock lock;
    private int[] carsWaiting;
    private Direction[] entryRouteDirections; // Directions for entry routes
    private Direction[] exitRouteDirections; // Directions for exit routes
    private PrintWriter logWriter;

    public Junction(String name, int[] entryRoutes, int[] exitRoutes, int numEntryRoutes, Road[] roads, int[][] destinationRoutes) {
        this.name = name;
        this.entryRoutes = entryRoutes;
        this.exitRoutes = exitRoutes;
        this.numEntryRoutes = numEntryRoutes;
        this.numExitRoutes = exitRoutes.length;
        this.roads = roads;
        this.destinationRoutes = destinationRoutes;
        this.greenLights = new int[numEntryRoutes];
        this.greenLightDuration = 10; // Default green light duration (in seconds)
        this.lock = new ReentrantLock();
        this.carsWaiting = new int[numEntryRoutes]; // Initialize the array with the size of the number of entry routes
        initializeDirections(); // Call the method to initialize entryRouteDirections
        try {
            logWriter = new PrintWriter(new FileWriter("junction_" + name + "_log.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public enum Direction {
        North,
        South,
        East,
    }

    public void setGreenLightDuration(int duration) {
        this.greenLightDuration = duration;
    }

    @Override
    public void run() {
        while (true) {
            for (int i = 0; i < numEntryRoutes; i++) {
                try {
                    // Simulate green light for current entry route
                    lock.lock();
                    greenLights[i] = 1; // Set green light for current route
                    TimeUnit.SECONDS.sleep(greenLightDuration); // Green light duration
                    greenLights[i] = 0; // Reset green light for current route
                    lock.unlock();
                    // Move cars across the junction for the current entry route
                    Direction direction = entryRouteDirections[i]; // Get direction for current entry route
                    moveCars(i, direction); // Pass direction to moveCars method
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void moveCars(int entryRouteIndex, Direction direction) {
        // Correctly reference carsWaiting as an array
        int roadCapacity = roads[entryRouteIndex].getCapacity();
        int carsPassed = 0;

        // Check if the road is full
        if (carsWaiting[entryRouteIndex] >= roadCapacity) {
            logActivity(direction, carsPassed, carsWaiting[entryRouteIndex], entryRouteIndex, "GRIDLOCK");
            return; // Exit method if the road is full
        }

        // Calculate the number of cars to pass through based on the road capacity and waiting cars
        int carsToPass = Math.min(roadCapacity - carsWaiting[entryRouteIndex], 60); // Adjust the number of cars to pass through

        // Simulate cars moving through the junction
        for (int i = 0; i < carsToPass; i++) {
            carsWaiting[entryRouteIndex]--; // Decrease the waiting cars
            carsPassed++; // Increase the cars passed
            simulationTime += 100;
            // Log the activity
            logActivity(direction, carsPassed, carsToPass-carsPassed, entryRouteIndex, ""); // Ensure the gridlock status is an empty string when not gridlocked

            // Simulate car movement (you can adjust this based on your requirements)
            try {
                TimeUnit.MILLISECONDS.sleep(100); // Simulate car movement time
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    private void logActivity(Direction direction, int carsPassed, int carsWaiting, int entryRouteIndex, String gridlockStatus) {
        String directionString = direction.toString();
        String logMessage = "Time: " + System.currentTimeMillis() / simulationTime + " - Junction " + name + ": " + carsPassed + " cars through from " + directionString + ", " + carsWaiting + " cars waiting. " + gridlockStatus;
        System.out.println(logMessage);
        logWriter.println(logMessage);
        logWriter.flush();
    }


    private void initializeDirections() {
    // Initialize entryRouteDirections and exitRouteDirections based on the configuration
        entryRouteDirections = new Direction[numEntryRoutes];
        exitRouteDirections = new Direction[numExitRoutes];

        // Example logic to assign directions
        // This is a placeholder. You need to implement the logic based on your configuration.
        for (int i = 0; i < numEntryRoutes; i++) {
            // Example logic to assign different directions
            // This could be based on the layout of the junction, the roads leading to it, etc.
            switch (i) {
                case 0:
                    entryRouteDirections[i] = Direction.North;
                    break;
                case 1:
                    entryRouteDirections[i] = Direction.South;
                    break;
                case 2:
                    entryRouteDirections[i] = Direction.East;
                    break;
                // Add more cases as needed based on the number of entry routes
            }
        }

        // Similar logic for exit routes if needed
        for (int i = 0; i < numExitRoutes; i++) {
            // Example logic to assign different directions
            // This could be based on the layout of the junction, the roads leading from it, etc.
            switch (i) {
                case 0:
                    exitRouteDirections[i] = Direction.North;
                    break;
                case 1:
                    exitRouteDirections[i] = Direction.South;
                    break;
                case 2:
                    exitRouteDirections[i] = Direction.East;
                    break;
                // Add more cases as needed based on the number of exit routes
            }
        }
    }

}
