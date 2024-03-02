public class ParkingSpace {
    private boolean isOccupied;

    public ParkingSpace() {
        this.isOccupied = false;
    }

    public synchronized boolean park(Vehicle vehicle) {
        if (!isOccupied) {
            isOccupied = true;
            return true;
        }
        return false;
    }

    public synchronized void leave() {
        isOccupied = false;
    }

    public boolean isOccupied() {
        return isOccupied;
    }
}
