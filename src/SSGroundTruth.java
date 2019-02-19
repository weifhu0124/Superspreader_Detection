import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

/* Simulation of Ground Truth for Superspreader Detection */
public class SSGroundTruth {
    // read packet input from source file
    private static ArrayList<Packet> read_csv_file(String filepath){
        ArrayList<Packet> inputPackets = new ArrayList<Packet>();
        Converter convert = new Converter();
        try {
            Scanner scanner = new Scanner(new File(filepath));
            while (scanner.hasNext()){
                String packet_info = scanner.nextLine();
                packet_info = packet_info.trim();
                String[] field = packet_info.split("\\s+");
                long srcip = convert.convertAddressToLong(field[0]);
                long dstip = convert.convertAddressToLong(field[2]);
                inputPackets.add(new Packet(srcip, dstip, field[3], field[4], field[5]));
            }
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return inputPackets;
    }

    // construct spreader hashtable from input array
    private static HashMap<Long, HashSet<Long>> getSpreaders(ArrayList<Packet>inputPackets){
        HashMap<Long, HashSet<Long>> spreaders = new HashMap<Long, HashSet<Long>>();
        for (Packet p: inputPackets){
            if (!spreaders.containsKey(p.getSrcIp())){
                spreaders.put(p.getSrcIp(), new HashSet<Long>());
            }
            spreaders.get(p.getSrcIp()).add(p.getDestIp());
        }
        return spreaders;
    }

    // get top K superspreader
    private static ArrayList<Long> topKSuperspreader(HashMap<Long, HashSet<Long>> spreaders, int K){
        ArrayList<Long> topk = new ArrayList<Long>();
        for (Long src_ip : spreaders.keySet()){
            if (spreaders.get(src_ip).size() >= K){
                topk.add(src_ip);
            }
        }
        Collections.sort(topk);
        Collections.reverse(topk);
        return topk;
    }

    public static void main(String[] args){
        ArrayList<Packet> input = read_csv_file("/Users/weifenghu/Desktop/MSCS/W19/CSE222A/superspreader/src/test1.csv");
        HashMap<Long, HashSet<Long>> spreaders = getSpreaders(input);
        ArrayList<Long> topk = topKSuperspreader(spreaders, 3);
        System.out.print("Done");
    }
}
