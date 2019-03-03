import java.util.ArrayList;

public class Table_Entry {
    private long sourceIP;
    private ArrayList<Boolean> bitmap;
    private int counter;

    // constructor
    public Table_Entry(long sourceIP) {
        this.sourceIP = sourceIP;
        this.bitmap = new ArrayList<Boolean>();
        this.counter = 0;
    }

    /* Getters for the internal variables */
    public long getSourceIP() {
        return sourceIP;
    }

    public ArrayList<Boolean> getBitmap() {
        return bitmap;
    }

    public int getCounter() {
        return counter;
    }

    /* Setters for the bitmap and counter */
    public void setBitmap(ArrayList<Boolean> bitmap) {
        this.bitmap = bitmap;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
