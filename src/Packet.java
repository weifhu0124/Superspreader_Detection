import java.util.*;

/* abstraction for Packets in a network that helps retrieve 
   the key fields from the packet*/
public class Packet {
    private long srcip;
    private long dstip;

    //metadata
    public int min_stage;
    public int carry_min;
    public boolean recirculated;

//    private String srcPort;
//    private String dstPort;
//    private String protocol;

    public Packet(long srcip, long dstip) {
        this.srcip = srcip;
        this.dstip = dstip;
//        this.srcPort = srcPort;
//        this.dstPort = dstPort;
//        this.protocol = protocol;
    }

    // get source ip of a packet
    public long getSrcIp(){
        return this.srcip;
    }

    // get destination ip of a packet
    public long getDestIp(){
        return this.dstip;
    }
}