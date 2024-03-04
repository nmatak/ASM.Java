import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;

public class Junction extends Thread {
    private Map<String, Integer> locationCapacities;
    private Map<String, Integer> locationIndices;
    private ScheduledExecutorService scheduler;
    private static long simulationTime = 0;
    private String name;
    private int[] entryRoutes;
    private int[] exitRoutes;
    private int numEntryRoutes;
    private int numExitRoutes;
    private Road[] roads;
    private int[][] destinationRoutes;
    private int[] greenLights;
    private int greenLightDuration;
    private Lock lock;
    private int[] carsWaiting;
    private Direction[] entryRouteDirections;
    private Direction[] exitRouteDirections;
    private PrintWriter logWriter;
    private Map<String, Integer> carsParked;
    private Map<String, Long> totalJourneyTime;
    private int totalCarsCreated = 0;
    private int totalCarsParked = 0;
    private int totalCarsQueued = 0;

   public Junction(String name, int[] entryRoutes, int[] exitRoutes, int numEntryRoutes, Road[] roads, int[][] destinationRoutes) {
    this.name = name;
    this.entryRoutes = entryRoutes;
    this.exitRoutes = exitRoutes;
    this.numEntryRoutes = numEntryRoutes;
    this.numExitRoutes = exitRoutes.length;
    this.roads = roads;
    this.destinationRoutes = destinationRoutes;
    this.greenLights = new int[numEntryRoutes];
    this.greenLightDuration = 10;
    this.lock = new ReentrantLock();
    this.carsWaiting = new int[numEntryRoutes];

    locationCapacities = new HashMap<>();
    locationCapacities.put("University", 56);
    locationCapacities.put("Station", 99);
    locationCapacities.put("Shopping centre", 123);
    locationCapacities.put("Industrial car park", 762);

    locationIndices = new HashMap<>();
    locationIndices.put("University", 0);
    locationIndices.put("Station", 1);
    locationIndices.put("Shopping centre", 2);
    locationIndices.put("Industrial car park", 3);

    carsParked = new HashMap<>();
    totalJourneyTime = new HashMap<>();
    for (String location : locationCapacities.keySet()) {
        carsParked.put(location, 0);
        totalJourneyTime.put(location, 0L);
    }

    initializeDirections(); // Call initializeDirections() to ensure entryRouteDirections is initialized

    scheduler = Executors.newScheduledThreadPool(1);
    scheduler.scheduleAtFixedRate(this::outputTotalCounts, 0, 10, TimeUnit.MINUTES);

    try {
        logWriter = new PrintWriter(new FileWriter("junction_" + name + "_log.txt"));
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    public void logSpaces() {
        int minutes = (int) (simulationTime / 60000);
        int seconds = (int) ((simulationTime % 60000) / 1000);
        String timeString = String.format("%dm%ds", minutes, seconds);

        StringBuilder logMessage = new StringBuilder("Time: " + timeString + " ");
        for (Map.Entry<String, Integer> entry : locationCapacities.entrySet()) {
            String location = entry.getKey();
            int capacity = entry.getValue();
            int parkedCars = carsParked.getOrDefault(location, 0);
            int spacesAvailable = capacity - parkedCars;

            logMessage.append(location).append(": ").append(String.format("%03d", spacesAvailable)).append(" Spaces ");
        }
        System.out.println(logMessage.toString());
        logWriter.println(logMessage.toString());
        logWriter.flush();
    }

    public void parkCar(String location, long journeyTime) {
        int index = locationIndices.get(location);
        int currentParked = carsParked.getOrDefault(location, 0);
        carsParked.put(location, currentParked + 1);
        totalJourneyTime.put(location, totalJourneyTime.getOrDefault(location, 0L) + journeyTime);
        totalCarsParked++;
    }

    public void reportCarParkStatistics() {
        for (String location : carsParked.keySet()) {
            int cars = carsParked.get(location);
            long totalTime = totalJourneyTime.get(location);
            if (cars > 0) {
                long averageTime = totalTime / cars;
                String timeString = String.format("%dm%ds", TimeUnit.MILLISECONDS.toMinutes(averageTime), TimeUnit.MILLISECONDS.toSeconds(averageTime) % 60);
                System.out.println(location + ": " + cars + " Cars parked, average journey time " + timeString);
            }
        }
    }

    public void shutdownScheduler() {
        ScheduledExecutorService outputService = Executors.newScheduledThreadPool(1);
        outputService.scheduleAtFixedRate(this::logSpaces, 1, 1, TimeUnit.MINUTES);

        ScheduledExecutorService reportService = Executors.newScheduledThreadPool(1);
        reportService.schedule(() -> {
            reportCarParkStatistics();
            outputService.shutdown();
            reportService.shutdown();
        }, 6, TimeUnit.MINUTES);
    }

    public void outputTotalCounts() {
        int minutes = (int) (simulationTime / 60000);
        String timeString = String.format("%dm", minutes);

        System.out.println("Time: " + timeString + " University: " + carsParked.get("University") + " Spaces" +
                " Station: " + carsParked.get("Station") + " Spaces" +
                " Shopping Centre: " + carsParked.get("Shopping centre") + " Spaces" +
                " Industrial Park: " + carsParked.get("Industrial car park") + " Spaces");
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
                    lock.lock();
                    greenLights[i] = 1;
                    TimeUnit.SECONDS.sleep(greenLightDuration);
                    greenLights[i] = 0;
                    lock.unlock();
                    Direction direction = entryRouteDirections[i];
                    moveCars(i, direction);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void moveCars(int entryRouteIndex, Direction direction) {
        int roadCapacity = roads[entryRouteIndex].getCapacity();
        int carsPassed = 0;

        if (carsWaiting[entryRouteIndex] >= roadCapacity) {
            logActivity(direction, carsPassed, carsWaiting[entryRouteIndex], entryRouteIndex, "GRIDLOCK");
            return;
        }

        int carsToPass = Math.min(roadCapacity - carsWaiting[entryRouteIndex], 60);

        for (int i = 0; i < carsToPass; i++) {
            carsWaiting[entryRouteIndex]--;
            carsPassed++;
            simulationTime += 100;
            logActivity(direction, carsPassed, carsToPass-carsPassed, entryRouteIndex, "");

            // Simulate car movement (you can adjust this based on your requirements)
            try {
                TimeUnit.MILLISECONDS.sleep(100); // Simulate car movement time
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Simulate parking
            parkCar("Shopping centre", 5000); // Example parking, adjust as needed
        }
    }

    private void logActivity(Direction direction, int carsPassed, int carsWaiting, int entryRouteIndex, String gridlockStatus) {
        int minutes = (int) (simulationTime / 60000);
        int seconds = (int) ((simulationTime % 60000) / 1000);
        String timeString = String.format("%dm%ds", minutes, seconds);

        String directionString = direction.toString();
        String logMessage = "Time: " + timeString + " - Junction " + name + ": " + carsPassed + " cars through from " + directionString + ", " + carsWaiting + " cars waiting. " + gridlockStatus;
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