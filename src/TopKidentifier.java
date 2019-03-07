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


//        ArrayList<Packet> temp;
//        temp = new ArrayList<Packet>(inputPacketStream.subList(0,10000));
//        inputPacketStream = temp;


        //parameters used in this experiment.
        int wholetablesize = 100;
        int d = 4;
        int bitmaplen = 512;
        int recirculate_delay = 10;


        // ground truth
        // get the list of Source Ip and its number of destinations. K indicates the number of its destinations are larger than K;
        HashMap<Long, HashSet<Long>> spreaders = SourceWithCount.getSpreaders(inputPacketStream);

        SourceCount = SourceWithCount.topKSuperspreader(spreaders, 1);
        Collections.sort(SourceCount, new SourceWithCountCompare());

        ArrayList<SourceWithCount> SourceCounttemp;
        SourceCounttemp = new ArrayList<SourceWithCount>(SourceCount.subList(0,wholetablesize));
        SourceCount = SourceCounttemp;





        // initialize the whole table in switch (supposed to be divided into d tables in reality)
        ArrayList<TableEntry> table = new ArrayList<TableEntry>(wholetablesize);
        for (int i = 0; i < wholetablesize; i++) {
            table.add(null);
        }
        HashFunction hash = new HashFunction(d,wholetablesize);

        // loop through all incoming packets
        while(inputPacketStream.size()!=0){
            Packet incoming = inputPacketStream.get(0);
            // check recirculate bit in packet's metadata
            long ip = incoming.getSrcIp();
            int[] position = hash.index(ip);

            if(incoming.recirculated_min){
                int stage_number = incoming.min_stage;
                int position_sub = position[stage_number];
//                TableEntry tmp = table.get(position_sub);
//                tmp.setSourceIP(incoming.getSrcIp());
                TableEntry tmp = new TableEntry(incoming.getSrcIp(),bitmaplen);
                tmp.bitmapSet(incoming.getDestIp());
                table.set(position_sub,tmp);
                inputPacketStream.remove(0);
                //test
//                System.out.println("recirculation for substitution");
                continue;

            }

            if(incoming.recirculated_dup){
                for(int i=0;i<d;i++){
                    if(table.get(position[i]).getSourceIP() == incoming.getSrcIp()){
                        TableEntry tmp = table.get(position[i]);
                        boolean[] bitmap_tmp = tmp.getBitmap();
                        for(int k =0;k<bitmaplen;k++){
                            bitmap_tmp[k] = bitmap_tmp[k] || incoming.bitmap[k];
                        }
                        tmp.setBitmap(bitmap_tmp);
                        tmp.bitmapTocounter();
                        table.set(position[i],tmp);
                        inputPacketStream.remove(0);
                        break;
                    }
                }
                //test
                System.out.println("recirculation for duplication");
                continue;
            }

            // flag to indicate whether entry having same source ip has been found;
            boolean matched = false;
            for(int j =0; j<d; j++){
                if(table.get(position[j]) == null){
                    if(!matched){
                        TableEntry entry = new TableEntry(ip,bitmaplen);
                        // set bitmap and counter if necessary
                        entry.bitmapSet(incoming.getDestIp());
                        // insert into the table
                        table.set(position[j], entry);
                        matched = true;
                    }
                }

                else if(table.get(position[j]).getSourceIP()==ip){
                    if(!matched){
                        // set bitmap and counter if necessary
                        TableEntry entry = table.get(position[j]);
                        entry.bitmapSet(incoming.getDestIp());
                        table.set(position[j],entry);
                        matched = true;
                    }
                    else{
                        //find duplicate entry
                        // copy bitmap to the metadata of this packet, and reset this entry to null;
                       incoming.recirculated_dup = true;
                       for(int k =0;k<bitmaplen;k++){
                           incoming.bitmap[k] = incoming.bitmap[k] || table.get(position[j]).getBitmap()[k];
                       }
                       table.set(position[j],null);
                    }
                }

                else if(table.get(position[j]).getSourceIP()!=ip){
                    // fail to find an entry in which the source ip is the same as the source ip of the packet, record this entry's
                    // count, bitmap and stage number.
                    if(!matched){
                        if(incoming.carry_min>table.get(position[j]).getCounter()){
                            incoming.carry_min = table.get(position[j]).getCounter();
                            for(int k =0;k<bitmaplen;k++){
                                incoming.bitmap[k] = incoming.bitmap[k] || table.get(position[j]).getBitmap()[k];
                            }
                            incoming.min_stage = j;
                        }
                    }

                }
            }
            if(!matched){
                // generate an integer in [0,carry_min-1];
                Random random = new Random();
                int R = random.nextInt(incoming.carry_min*100);
                if(R == 0 || incoming.carry_min == 1){
                    incoming.recirculated_min = true;
                    // add packet to a particular position in the input packet stream
                    // to simulate the recircualte delay.
                    if(inputPacketStream.size()<recirculate_delay){
                        inputPacketStream.add(inputPacketStream.size()-1,incoming);
                    }
                    else {
                        inputPacketStream.add(recirculate_delay-1,incoming);
                    }
                }
            }
            inputPacketStream.remove(0);

            //test
//            System.out.println("number of remaining packets"+inputPacketStream.size());
        }

        for(int l = 0; l<table.size(); l++){
            System.out.println("IP: "+table.get(l).getSourceIP()+" counter: "+table.get(l).getCounter());
        }

        ArrayList<SourceWithCount> output = new ArrayList<SourceWithCount>(wholetablesize);
        for(int i = 0; i<wholetablesize; i++){
            long ip = table.get(i).getSourceIP();
            int count = table.get(i).getCounter();
            SourceWithCount tmp = new SourceWithCount(ip,count);
            output.add(tmp);
        }

        Evaluate evaluate = new Evaluate(SourceCount,output);
        System.out.println("accuracy= "+evaluate.accuracy());
//        System.out.println("precise= "+evaluate.precision());
//        System.out.println("recall= "+evaluate.recall());

    }





}
