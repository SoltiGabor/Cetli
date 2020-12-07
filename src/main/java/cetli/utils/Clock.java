package cetli.utils;

public class Clock {
    private long t = 0;
    
    public void start() {
        start("Starting clock");
    }
    
    public void start(String msg) {
        t = System.currentTimeMillis();
        System.out.println(msg);
    }
    
    public void time() {
        time("clock");
    }
    
    public void time(String msg) {
        System.out.println(msg + ": " + (System.currentTimeMillis() - t));
    }
}
