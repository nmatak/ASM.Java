public class Main {
    public static void main(String[] args) {

        int[] entryRoutesA = {0, 1};// 0 from south and 1 from B
        int[] entryRoutesB = {2,3,4}; //2 from east and 3 from A and 4 from C
        int[] entryRoutesC = {5,6};// 5 from north and 6 from B
        int[] entryRoutesD = {7}; // 7 from C

        int[] exitRoutesA = {8,9}; // 8 to ICP and to B
        int[] exitRoutesB = {10,11}; // 10 from B to a and 11 from b to c
        int[] exitRoutesC = {12,13,14}; // 12 from c to b, and 13 to shoppingCP. 14 to D
        int[] exitRoutesD = {15,16}; //15 to station and 16 to Uni

        int[][] destinationRoutesA = {
                // Exit Route 8   Exit Route 9
                /* Entry Route 0 (South) */ {Destination.INDUSTRIAL_PARK, Destination.JUNCTION_B},
                /* Entry Route 1 (From B) */ {Destination.INDUSTRIAL_PARK, Destination.JUNCTION_B}
        };

        int[][] destinationRoutesB = {
                // Exit Route 10   Exit Route 11
                /* Entry Route 2 (East) */ {Destination.JUNCTION_A, Destination.JUNCTION_C},
                /* Entry Route 3 (From A) */ {Destination.JUNCTION_A, Destination.JUNCTION_C},
                /* Entry Route 4 (From C) */ {Destination.JUNCTION_A, Destination.JUNCTION_C}
        };

        int[][] destinationRoutesC = {
                // Exit Route 12   Exit Route 13   Exit Route 14
                /* Entry Route 5 (North) */ {Destination.JUNCTION_B, Destination.SHOPPING_CENTRE, Destination.JUNCTION_D},
                /* Entry Route 6 (From B) */ {Destination.JUNCTION_B, Destination.SHOPPING_CENTRE, Destination.JUNCTION_D}
        };

        int[][] destinationRoutesD = {
                // Exit Route 15   Exit Route 16
                /* Entry Route 7 (From C) */ {Destination.STATION, Destination.UNIVERSITY}
        };



        // Define and initialize EntryPoints
        EntryPoint entryPointSouth = new EntryPoint("Entry Point South", 550, new Road[]{});
        EntryPoint entryPointEast = new EntryPoint("Entry Point East", 300,new Road[]{} );
        EntryPoint entryPointNorth = new EntryPoint("Entry Point North", 550, new Road[]{});

        // Define and initialize CarParks
        CarPark industrialParkCarPark = new CarPark("Industrial Park Car Park", new Road[]{}, 1000,12 );
        CarPark shoppingCentreCarPark = new CarPark("Shopping Centre Car Park", new Road[]{},400,12);
        CarPark universityCarPark = new CarPark("University Car Park", new Road[]{},100,12);
        CarPark stationCarPark = new CarPark("Station Car Park", new Road[]{},150,12);

        // Initialize Roads
        Road roadSouthToA = new Road( 60, "Road South to Junction A");
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


        // Define and initialize Junctions
        Junction junctionA = new Junction("Junction A", entryRoutesA, exitRoutesA, 2,new Road[]{roadAtoB, roadAtoIndustrialCP},destinationRoutesA);
        Junction junctionB = new Junction("Junction B", entryRoutesB, exitRoutesB,3, new Road[]{roadBtoC, roadBtoA},destinationRoutesB);
        Junction junctionC = new Junction("Junction C",entryRoutesC, exitRoutesC,2, new Road[]{roadCtoShoppingCentreCP, roadCtoD},destinationRoutesC);
        Junction junctionD = new Junction("Junction D",entryRoutesD,exitRoutesD,1,new Road[]{roadDtoStationCP, roadDtoUniCP},destinationRoutesD);




        // Set up destination routes for Junctions
        junctionA.setDestinationRoutes(new int[][]{{1, 2}}); // Industrial Park: 1, Junction B: 2
        junctionB.setDestinationRoutes(new int[][]{{0, 2}}); // Junction A: 0, Junction C: 2
        junctionC.setDestinationRoutes(new int[][]{{3, 7}}); // Junction D: 3, Shopping Centre Car Park: 7
        junctionD.setDestinationRoutes(new int[][]{{8, 9}}); // University Car Park: 8, Station Car Park: 9

        // Start the simulation
        // Proceed with the rest of your simulation setup and start the simulation...

        junctionA.start();
        junctionB.start();
        junctionC.start();
        junctionD.start();


        String logContent = junctionA.readLogFile();
        System.out.println("Junction Log Content:");
        System.out.println(logContent);

        String logContentB = junctionB.readLogFile();
        System.out.println("Junction B Log Content:");
        System.out.println(logContentB);

        String logContentC = junctionC.readLogFile();
        System.out.println("Junction C Log Content:");
        System.out.println(logContentC);

        String logContentD = junctionD.readLogFile();
        System.out.println("Junction D Log Content:");
        System.out.println(logContentD);


    }
}

