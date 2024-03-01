//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.PrintWriter;
//
//public class Junction implements Runnable {
//    private final String name;
//    private final int[] entryRoutes;
//    private final int[] exitRoutes;
//    private final int greenDuration;
//    private int currentRouteIndex;
//    private long lastChangeTime;
//    private final Road[] roads;
//    private final boolean[] greenLights;
//    private PrintWriter logWriter;
//    private static int[][] destinationRoutes;
//
//    public Junction(String name, int[] entryRoutes, int[] exitRoutes, int greenDuration, Road[] roads, int[][] destinationRoutes) {
//        this.name = name;
//        this.entryRoutes = entryRoutes;
//        this.exitRoutes = exitRoutes;
//        this.greenDuration = greenDuration;
//        this.currentRouteIndex = 0;
//        this.lastChangeTime = System.currentTimeMillis();
//        this.roads = roads;
//        this.greenLights = new boolean[entryRoutes.length];
//        this.greenLights[0] = true; // Set the initial green light to the first entry route
//        this.destinationRoutes = destinationRoutes;
//
//       // try {
//         //   this.logWriter = new PrintWriter(new FileWriter(logFileName, true)); // Append mode
//        //} catch (IOException e) {
//          //  e.printStackTrace();
//        //}
//    }
//
//    @Override
//    public void run() {
//        while (true) {
//            try {
//                Thread.sleep(1000); // Adjust sleep time as needed
//                manageTraffic();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private void manageTraffic() {
//        long currentTime = System.currentTimeMillis();
//        int elapsedTime = (int) ((currentTime - lastChangeTime) / 1000); // Elapsed time in seconds
//
//        if (elapsedTime >= greenDuration) {
//            // Change the green light to the next route
//            greenLights[currentRouteIndex] = false;
//            currentRouteIndex = (currentRouteIndex + 1) % entryRoutes.length;
//            greenLights[currentRouteIndex] = true;
//            lastChangeTime = currentTime;
//        }
//
//        // Check if cars can move on the current green route
//        if (greenLights[currentRouteIndex]) {
//            Road entryRoad = roads[entryRoutes[currentRouteIndex]];
//            Road exitRoad = roads[exitRoutes[currentRouteIndex]];
//            synchronized (entryRoad) {
//                synchronized (exitRoad) {
//                    // Check if there is a car available and space on the exit road
//                    if (!entryRoad.isEmpty() && exitRoad.hasSpace()) {
//                        Vehicle vehicle = entryRoad.removeVehicle();
//                        exitRoad.addVehicle(vehicle);
//                        logActivity("Time: " + formatTime(currentTime) + " - Junction " + name + ": 1 car through from " +
//                                getDirection(entryRoutes[currentRouteIndex]) + ", " + entryRoad.getSize() + " cars waiting.");
//                    }
//                }
//            }
//        } else {
//            Road entryRoad = roads[entryRoutes[currentRouteIndex]];
//            if (!entryRoad.isEmpty()) {
//                logActivity("Time: " + formatTime(currentTime) + " - Junction " + name + ": 0 cars through from " +
//                        getDirection(entryRoutes[currentRouteIndex]) + ", " + entryRoad.getSize() + " cars waiting. GRIDLOCK");
//            }
//        }
//    }
//
//    private String formatTime(long millis) {
//        long minutes = millis / 60000;
//        long seconds = (millis / 1000) % 60;
//        return minutes + "m" + seconds + "s";
//    }
//
//    private String getDirection(int routeIndex) {
//        // Dummy implementation for illustration
//        switch (routeIndex) {
//            case 0:
//                return "North";
//            case 1:
//                return "South";
//            case 2:
//                return "East";
//            case 3:
//                return "West";
//            default:
//                return "Unknown";
//        }
//    }
//
//    private void logActivity(String activity) {
//        if (logWriter != null) {
//            logWriter.println(activity);
//            logWriter.flush(); // Flush the writer to ensure the message is written immediately
//        }
//    }
//
//    public static Junction createJunctionFromConfig(String configFileName, Road[] roads, String logFileName) throws IOException {
//        BufferedReader reader = new BufferedReader(new FileReader(configFileName));
//        String line;
//        while ((line = reader.readLine()) != null) {
//            // Parse configuration and create Junction object
//            // Example line: Junction A: 1 2 3 4 5, 6 7 8 9 10, 30
//            String[] parts = line.split(": ");
//            String name = parts[0];
//            String[] entryRoutesStr = parts[1].split(", ")[0].split(" ");
//            String[] exitRoutesStr = parts[1].split(", ")[1].split(" ");
//            int[] entryRoutes = new int[entryRoutesStr.length];
//            int[] exitRoutes = new int[exitRoutesStr.length];
//            for (int i = 0; i < entryRoutesStr.length; i++) {
//                entryRoutes[i] = Integer.parseInt(entryRoutesStr[i]);
//                exitRoutes[i] = Integer.parseInt(exitRoutesStr[i]);
//            }
//            int greenDuration = Integer.parseInt(parts[2]);
//            return new Junction(name, entryRoutes, exitRoutes, greenDuration, roads, destinationRoutes);
//        }
//        return null;
//    }
//    public void setDestinationRoutes(int[][] destinationRoutes) {
//        this.destinationRoutes = destinationRoutes;
//    }
//
//}
// below is new code

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.time.LocalTime;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;







public class Junction extends Thread {
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

    public Junction(String name, int[] entryRoutes, int[] exitRoutes, int numEntryRoutes, Road[] roads, int[][] destinationRoutes) {
        this.name = name;
        this.entryRoutes = entryRoutes;
        this.exitRoutes = exitRoutes;
        // this.numEntryRoutes = numEntryRoutes;
        this.numEntryRoutes = roads.length;
        this.numExitRoutes = exitRoutes.length;
        this.roads = roads;
        this.destinationRoutes = destinationRoutes;
        this.greenLights = new int[numEntryRoutes];
        this.greenLightDuration = 10; // Default green light duration (in seconds)
        this.lock = new ReentrantLock();
        this.carsWaiting = new int[numEntryRoutes]; // Initialize the array with the size of the number of entry routes
        initializeDirections(); // Call the method to initialize entryRouteDirections
    }

    public enum Direction {
        North,
        South,
        East,
        Unknown,
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
                    System.out.println(direction);
                    moveCars(i, direction); // Pass direction to moveCars method
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    // private void moveCars(int entryRouteIndex, Direction direction) {
    //     // Logic to move cars across the junction for the specified entry route
    //     // This method needs to be implemented based on the requirements
    //     int roadCapacity=roads[entryRouteIndex].getCapacity();
    //     int carsThrough= roadCapacity; //determineCarsPassingThrough(entryRouteIndex);
    //     int carsWaiting=0;
    //    // carsWaiting[entryRouteIndex]=0;
    //     String directionString= " ";

    //     //carsThrough+= simulateCarsPassingThrough;

    //     // Log the activity
    //     logActivity(direction, carsThrough, carsWaiting,entryRouteIndex);
    // }
    private void moveCars(int entryRouteIndex, Direction direction) {
        // Logic to move cars across the junction for the specified entry route
        System.out.println("Enter Rout Index " + entryRouteIndex);
        System.out.println("Array Length " + roads.length);

        int roadCapacity = roads[entryRouteIndex].getCapacity();
        int maxCars = Math.min(60, roadCapacity); // Calculate the maximum cars based on traffic flow rate

        // Simulate cars moving through the junction
        for (int i = 0; i < maxCars; i++) {
            // Update carsWaiting (if needed)
            if (carsWaiting[entryRouteIndex] > 0) {
                carsWaiting[entryRouteIndex]--;
            }

            // Log the activity
            logActivity(direction, 1, carsWaiting[entryRouteIndex], entryRouteIndex);

            // Simulate car movement (you can adjust this based on your requirements)
            try {
                TimeUnit.MILLISECONDS.sleep(100); // Simulate car movement time
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    // Method to simulate cars passing through the junction
    // private int determineCarsPassingThrough(int entryRouteIndex) {
    //     // Initialize the number of cars passing through
    //     int roadCapacity=roads[entryRouteIndex].getCapacity();
    //     int carsThrough = Math.min(roadCapacity, determineCarsPassingThrough(entryRouteIndex));
    //     int maxCars=0;

    //     // Simulate the traffic flow based on the entry route index
    //     switch (entryRouteIndex) {
    //         case 0: // South
    //             // Calculate maximum cars based on traffic flow rate
    //             maxCars = Math.min(60, entryRoutes[entryRouteIndex]); // Maximum 60 cars or road capacity, whichever is lower
    //             break;
    //         case 1: // East
    //             // Calculate maximum cars based on traffic flow rate
    //             maxCars = Math.min(30, entryRoutes[entryRouteIndex]); // Maximum 30 cars or road capacity, whichever is lower
    //             break;
    //         case 2: // North
    //             maxCars = Math.min(50, entryRoutes[entryRouteIndex]); // Maximum 30 cars or road capacity, whichever is lower
    //         // Add cases for other entry routes as needed
    //         default:
    //             // Handle unknown entry routes
    //             break;
    //     }

    //     return maxCars;
    // }
    private int determineCarsPassingThrough(int entryRouteIndex) {
        // Initialize the number of cars passing through
        int roadCapacity = roads[entryRouteIndex].getCapacity();
        int maxCars = 0;

        // Simulate the traffic flow based on the entry route index
        switch (entryRouteIndex) {
            case 0: // South
                // Calculate maximum cars based on traffic flow rate
                maxCars = Math.min(60, roadCapacity); // Maximum 60 cars or road capacity, whichever is lower
                break;
            case 1: // East
                // Calculate maximum cars based on traffic flow rate
                maxCars = Math.min(30, roadCapacity); // Maximum 30 cars or road capacity, whichever is lower
                break;
            case 2: // North
                maxCars = Math.min(50, roadCapacity); // Maximum 50 cars or road capacity, whichever is lower
                break;
            // Add cases for other entry routes as needed
            default:
                // Handle unknown entry routes
                break;
        }

        return maxCars;
    }


    private void updateCarsWaiting(int entryRouteIndex) {
        carsWaiting[entryRouteIndex]++; // Increment the counter for the corresponding entry route
    }

    // Method to calculate total cars waiting at the junction
    private int calculateTotalCarsWaiting() {
        int totalWaiting = 0;
        for (int i = 0; i < numEntryRoutes; i++) {
            totalWaiting += carsWaiting[i]; // Sum up all the counters
        }
        return totalWaiting;
    }

    private void logActivity(Direction direction, int carsThrough, int carsWaiting, int entryRouteIndex) {
        // Implement logic to determine the direction based on the entry route
        // Example logic:
        switch (entryRouteIndex) {
            case 0:
                direction = Direction.South;
                break;
            case 1:
                direction = Direction.East;
                break;
            case 2:
                direction = Direction.North;
                break;
            default:
                direction = Direction.Unknown;
                break;
        }

        String logEntry = String.format("Time: %02d:%02d - %s: %d cars through from %s, %d cars waiting.\n",
                LocalTime.now().getMinute(), LocalTime.now().getSecond(), name, carsThrough, direction, carsWaiting);

        try {
            // Your file I/O code that may throw IOException
            FileWriter fileWriter = new FileWriter("junction_log.txt", true);
            PrintWriter writer = new PrintWriter(fileWriter);
            writer.println(logEntry);
            //writer.close(); // Remember to close the PrintWriter after using it
        } catch (IOException e) {
            // Handle the IOException here
            e.printStackTrace(); // Or handle it in another appropriate way
        }

    }
    public String readLogFile() {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("junction_log.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    public void setDestinationRoutes(int[][] ints) {
    }

    private Direction determineDirection(int entryRouteIndex) {
        switch(entryRouteIndex) {
            case 0:
                return Direction.South;
            case 1:
                return Direction.East;
            case 2:
                return Direction.North;
            // Add cases for other entryRouteIndex values as needed
            default:
                return Direction.Unknown; // Return a default direction if the index is invalid
        }
    }


    private void initializeDirections() {
        entryRouteDirections = new Direction[numEntryRoutes];
        for (int i = 0; i < numEntryRoutes; i++) {
            entryRouteDirections[i] = determineDirection(entryRoutes[i]);
        }
    }
}