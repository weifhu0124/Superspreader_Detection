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

        //feed to packet to the table
        int wholetablesize = 100;
        int d = 4;
        int bitmaplen = 20;

        ArrayList<TableEntry> table = new ArrayList<TableEntry>(wholetablesize);
        for (int i = 0; i < wholetablesize; i++) {
            table.add(null);
        }
        HashFunction hash = new HashFunction(d,wholetablesize);
        for(int i =0; i<inputPacketStream.size(); i++) {
            long ip = inputPacketStream.get(i).getSrcIp();
            int[] position = hash.index(ip);
            boolean flag = false;
            for(int j =0; j<d; j++){
                if(table.get(position[j]) == null){
                    TableEntry entry = new TableEntry(ip,bitmaplen);
                    // set bitmap and counter if necessary
                    entry.bitmapSet(inputPacketStream.get(i).getDestIp());
                    // insert into the table
                    table.set(position[j], entry);
                    flag = true; // indicates the packet finds its sourceip in the table;
                    break;
                }
                else if(table.get(position[j]).getSourceIP()==ip){
                    // set bitmap and counter if necessary
                    table.get(position[j]).bitmapSet(inputPacketStream.get(i).getDestIp());

                }
                else if(table.get(position[j]).getSourceIP()!=ip){
                    inputPacketStream.get(i).carry_min = table.get(position[j]).getCounter();
                    inputPacketStream.get(i).min_stage = j;
                }

            }
            if(!flag){
                //set the random bit;
            }

        }






    }





}
