import java.util.ArrayList;

public class TableEntry {
    private long sourceIP;
    private boolean[] bitmap;
    private int counter;

    // constructor
    public TableEntry(long sourceIP, int bitmap_len) {
        this.sourceIP = sourceIP;
        this.bitmap = new boolean[bitmap_len];
        this.counter = 0;
    }

    /* Getters for the internal variables */
    public long getSourceIP() {
        return sourceIP;
    }

    public boolean[] getBitmap() {
        return bitmap;
    }

    public int getCounter() {
        return counter;
    }

    /* Setters for the bitmap and counter */
    public void setBitmap(boolean[] bitmap) {
        this.bitmap = bitmap;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
