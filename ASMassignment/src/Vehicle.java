import java.time.LocalTime;
//public class Vehicle {
//   // private String entryPointName;
//    private int destination;
//    private long entryTime;
//    private long parkTime;
//
//    public Vehicle(String entryPointName, int destination, long entryTime) {
//        //this.entryPointName = entryPointName;
//        this.destination = destination;
//        this.entryTime = entryTime;
//    }
//
//    public int getDestination() {
//        return destination;
//    }
//
//    public void park(long parkTime) {
//        this.parkTime = parkTime;
//    }
//
//    public long getEntryTime() {
//        return entryTime;
//    }
//
//    public long getParkTime() {
//        return parkTime;
//    }
//}
// below is new


public class Vehicle {
    private int destination;
    private LocalTime entryTime;
    private LocalTime parkedTime;

    public Vehicle(int destination, LocalTime entryTime) {
        this.destination = destination;
        this.entryTime = entryTime;
    }

    public int getDestination() {
        return destination;
    }

    public LocalTime getEntryTime() {
        return entryTime;
    }

    public LocalTime getParkedTime() {
        return parkedTime;
    }

    public void setParkedTime(LocalTime parkedTime) {
        this.parkedTime = parkedTime;
    }
}

