import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class TopKidentifier {

    public static void main(String[] args){

        //start_time records the time when main function is called;
        long start_time = System.currentTimeMillis();

        //inputPacketStream records the source ip and dest ip addresses of the incoming packets in sequence;
        ArrayList<Packet> inputPacketStream = new ArrayList<Packet>();

        //SourceCount contains the number of different destinations for each source ip;
        ArrayList<SourceWithCount> SourceCount = new ArrayList<SourceWithCount>();

        String file_path = args[0];
        File folder = new File(file_path);
        File[] listOfFiles = folder.listFiles();
        for(int i=0;i<listOfFiles.length;i++){
            inputPacketStream = DataParser.parsedata_5("/Users/yangrui/data-130000/"+listOfFiles[i].getName(),inputPacketStream);
        }

        // ground truth
        // get the list of Source Ip and its number of destinations. K indicates the number of its destinations are larger than K;
        HashMap<Long, HashSet<Long>> spreaders = SourceWithCount.getSpreaders(inputPacketStream);
        SourceCount = SourceWithCount.topKSuperspreader(spreaders, 1);

        


    }





}
