public class Main {
    public static void main(String[] args) {
        // Initialize and configure your simulation here
        // This includes setting up EntryPoints, Roads, Junctions, and CarParks

        // Define and initialize Roads
        Road roadSouthToA = new Road(60, "Road South to Junction A");
        Road roadEastToB = new Road(30, "Road East to Junction B");
        Road roadNorthToC = new Road(50, "Road North to Junction C");
        Road roadAtoIndustrialCP = new Road(15, "Road Junction A to Industrial Park car park");
        Road roadAtoB = new Road(7, "Road Junction A to Junction B");
        Road roadBtoA = new Road(7, "Road Junction B to Junction A");
        Road roadBtoC = new Road(10, "Road Junction B to Junction C");
        Road roadCtoB = new Road(10, "Road Junction C to Junction B");
        Road roadCtoShoppingCentreCP = new Road(7, "Road Junction C to Shopping Centre car park");
        Road roadCtoD = new Road(10, "Road Junction C to Junction D");
        Road roadDtoUniCP = new Road(15, "Road Junction D to University car park");
        Road roadDtoStationCP = new Road(15, "Road Junction D to Station car park");

        // Define and initialize EntryPoints
        Road[] roadsForSouthEntry = {roadSouthToA};
        EntryPoint entryPointSouth = new EntryPoint("Entry Point South", 550, roadsForSouthEntry);
        new Thread(entryPointSouth).start();

        Road[] roadsForEastEntry = {roadEastToB};
        EntryPoint entryPointEast = new EntryPoint("Entry Point East", 300, roadsForEastEntry);
        new Thread(entryPointEast).start();

        Road[] roadsForNorthEntry = {roadNorthToC};
        EntryPoint entryPointNorth = new EntryPoint("Entry Point North", 550, roadsForNorthEntry);
        new Thread(entryPointNorth).start();
        

        // Define and initialize CarParks
        CarPark industrialParkCarPark = new CarPark("Industrial Park Car Park", new Road[]{roadAtoIndustrialCP}, 1000, 12);
        CarPark shoppingCentreCarPark = new CarPark("Shopping Centre Car Park", new Road[]{roadCtoShoppingCentreCP}, 400, 12);
        CarPark universityCarPark = new CarPark("University Car Park", new Road[]{roadDtoUniCP}, 100, 12);
        CarPark stationCarPark = new CarPark("Station Car Park", new Road[]{roadDtoStationCP}, 150, 12);

        industrialParkCarPark.start();
        shoppingCentreCarPark.start();
        universityCarPark.start();
        stationCarPark.start();

        // Initialize Junctions
        int[] entryRoutesA = {0, 1}; // Example entry routes for Junction A
        int[] exitRoutesA = {8, 9}; // Example exit routes for Junction A
        int[][] destinationRoutesA = {
                // Exit Route 8   Exit Route 9
                /* Entry Route 0 (South) */ {Destination.INDUSTRIAL_PARK, Destination.JUNCTION_B},
                /* Entry Route 1 (From B) */ {Destination.INDUSTRIAL_PARK, Destination.JUNCTION_B}
        };
        Junction junctionA = new Junction("Junction A", entryRoutesA, exitRoutesA, 2, new Road[]{roadAtoB, roadAtoIndustrialCP}, destinationRoutesA);
        junctionA.start();

        // Define and initialize Junction B
        int[] entryRoutesB = {2, 3}; // Entry routes for Junction B
        int[] exitRoutesB = {10, 11}; // Exit routes for Junction B
        int[][] destinationRoutesB = {
                // Exit Route 10   Exit Route 11
                /* Entry Route 2 (From A) */ {Destination.JUNCTION_A, Destination.JUNCTION_C},
                /* Entry Route 3 (From C) */ {Destination.JUNCTION_A, Destination.JUNCTION_C}
        };
        Junction junctionB = new Junction("Junction B", entryRoutesB, exitRoutesB, 2, new Road[]{roadBtoA, roadBtoC}, destinationRoutesB);
        junctionB.start();

        // Define and initialize Junction C
        int[] entryRoutesC = {4, 5}; // Entry routes for Junction C
        int[] exitRoutesC = {12, 13}; // Exit routes for Junction C
        int[][] destinationRoutesC = {
                // Exit Route 12   Exit Route 13
                /* Entry Route 4 (From B) */ {Destination.JUNCTION_B, Destination.SHOPPING_CENTRE},
                /* Entry Route 5 (From D) */ {Destination.JUNCTION_B, Destination.UNIVERSITY}
        };
        Junction junctionC = new Junction("Junction C", entryRoutesC, exitRoutesC, 2, new Road[]{roadCtoB, roadCtoD}, destinationRoutesC);
        junctionC.start();

        // Define and initialize Junction D
        int[] entryRoutesD = {6, 7}; // Entry routes for Junction D
        int[] exitRoutesD = {14, 15}; // Exit routes for Junction D
        int[][] destinationRoutesD = {
                // Exit Route 14   Exit Route 15
                /* Entry Route 6 (From C) */ {Destination.JUNCTION_C, Destination.UNIVERSITY},
                /* Entry Route 7 (From C) */ {Destination.JUNCTION_C, Destination.STATION}
        };
        Junction junctionD = new Junction("Junction D", entryRoutesD, exitRoutesD, 2, new Road[]{roadDtoUniCP, roadDtoStationCP}, destinationRoutesD);
        junctionD.start();

        // Simulation runs for a specified duration
        try {
            Thread.sleep(60 * 60 * 1000); // Simulate running for 1 hour
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final reports or cleanup can be added here
        System.out.println("Simulation ended.");
    }
}
