import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

/* Simulation of Ground Truth for Superspreader Detection */
public class SSGroundTruth {
    // read packet input from source file
    private static ArrayList<Packet> read_csv_file(String filepath){
        ArrayList<Packet> inputPackets = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(filepath));
            while (scanner.hasNext()){
                String packet_info = scanner.next();
                String[] field = packet_info.split(",");
                inputPackets.add(new Packet(field[0], field[1], field[2], field[3], field[4]));
            }
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return inputPackets;
    }

    // construct spreader hashtable from input array
    private static HashMap<String, Integer> getSpreaders(ArrayList<Packet>inputPackets){
        HashMap<String, Integer> spreaders = new HashMap<>();
        return spreaders;
    }

    // get top K superspreader
    private static ArrayList<String> topKSuperspreader(HashMap<String, Integer> spreaders, int K){
        ArrayList<String> topk = new ArrayList<>();
        return topk;
    }

    public static void main(String[] args){
        //ArrayList<Packet> input = read_csv_file("/Users/weifenghu/Desktop/MSCS/W19/CSE222A/superspreader/src/test.csv");
        System.out.print("Done");
    }
}
