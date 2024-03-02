import java.time.LocalTime;

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
