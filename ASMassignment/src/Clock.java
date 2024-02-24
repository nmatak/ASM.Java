//public class Clock implements Runnable {
//    private int currentTime; // Current simulated time in seconds
//    private boolean running;
//    private final int tickIntervalMillis; // Time interval for each tick in milliseconds
//
//    public Clock(int tickIntervalMillis) {
//        this.currentTime = 0;
//        this.running = false;
//        this.tickIntervalMillis = tickIntervalMillis;
//    }
//
//    @Override
//    public void run() {
//        running = true;
//        while (running) {
//            try {
//                Thread.sleep(tickIntervalMillis); // Wait for the tick interval
//                currentTime += 10; // Increment current time by 10 seconds for each tick
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public int getCurrentTime() {
//        return currentTime;
//    }
//
//    public void stop() {
//        running = false;
//    }
//}
// below is new code
import java.util.concurrent.TimeUnit;

public class Clock extends Thread {
    private static final int TICK_DURATION = 1000; // 1 second
    private static final int SIMULATED_TICK_DURATION = 10000; // 10 seconds
    private int currentTime;

    public Clock() {
        this.currentTime = 0;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    @Override
    public void run() {
        while (currentTime < 360) { // Run the simulation for 1 hour (360 ticks)
            try {
                TimeUnit.MILLISECONDS.sleep(TICK_DURATION);
                currentTime++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Clock clock = new Clock();
        clock.start();

        // Other parts of the simulation can access the current time by calling clock.getCurrentTime()
    }
}
