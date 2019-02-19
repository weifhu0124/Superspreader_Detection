import java.util.*;

/* abstraction for Packets in a network that helps retrieve 
   the key fields from the packet*/
public class Packet {
    private String srcip;
    private String dstip;
    private String srcPort;
    private String dstPort;
    private String protocol;

    public Packet(String srcip, String dstip, String srcPort, String dstPort, String protocol) {
        this.srcip = srcip;
        this.dstip = dstip;
        this.srcPort = srcPort;
        this.dstPort = dstPort;
        this.protocol = protocol;
    }

    // get source ip of a packet
    public String getSrcIp(){
        return this.srcip;
    }

    // get destination ip of a packet
    public String getDestIp(){
        return this.dstip;
    }
}