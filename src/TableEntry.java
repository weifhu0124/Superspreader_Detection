public class TableEntry {
    private long sourceIP;
    private boolean[] bitmap;
    private int counter;
    private int bitmaplen;

    // constructor
    public TableEntry(long sourceIP,int bitmaplen) {
        this.sourceIP = sourceIP;
        this.bitmap = new boolean[bitmaplen];
        this.bitmaplen = bitmaplen;
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
    public void setSourceIP(long sourceIP){
        this.sourceIP = sourceIP;
    }
    public void setBitmap(boolean[] bitmap) {
        this.bitmap = bitmap;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    private void increCounter(){
        this.counter += 1;
    }

    public void bitmapTocounter(){
        this.counter = 0;
        for(int k =0;k<bitmaplen;k++){
            if(this.bitmap[k]){
                this.counter ++;
            }
        }
    }

    public void bitmapSet(long destip){
        int position = HashFunction.Hash_Bitmap(destip,bitmap.length);
        if (!bitmap[position]){
            bitmap[position] = true;
            increCounter();
        }

    }
}
