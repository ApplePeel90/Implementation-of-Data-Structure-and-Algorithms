/*
Name: Hemali Amitkumar Patel
Netid: hap170002

Nmae: Qi Wang
NetId: qxw170003
*/

package qxw170003;

public class Timer {
    long startTime, endTime, elapsedTime, memAvailable, memUsed;
    boolean ready;

    public Timer() {
        startTime = System.currentTimeMillis();
        ready = false;
    }

    public void start() {
        startTime = System.currentTimeMillis();
        ready = false;
    }

    public Timer end() {
        endTime = System.currentTimeMillis();
        elapsedTime = endTime-startTime;
        memAvailable = Runtime.getRuntime().totalMemory();
        memUsed = memAvailable - Runtime.getRuntime().freeMemory();
        ready = true;
        return this;
    }

    public long duration() { if(!ready) { end(); }  return elapsedTime; }

    public long memory()   { if(!ready) { end(); }  return memUsed; }

    public String toString() {
        if(!ready) { end(); }
        return "Time: " + elapsedTime + " msec.\n" + "Memory: " + (memUsed/1048576) + " MB / " + (memAvailable/1048576) + " MB.";
    }
}
