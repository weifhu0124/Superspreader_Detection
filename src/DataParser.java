import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class DataParser {


    public static ArrayList<Packet> parsedata_5(String filepath,ArrayList<Packet> packetStream){
        Converter convert = new Converter();
        int valid_packet = 0;
        try {
            Scanner scanner = new Scanner(new File(filepath));
            while (scanner.hasNext()){
                String packet_info = scanner.nextLine();
                packet_info = packet_info.trim();
                String[] field = packet_info.split("\\s+");
                if (field.length < 6){
                    continue;
                }
                if(field[3].equals("TCP") || field[3].equals("UDP")) {
                    long srcip = convert.convertAddressToLong(field[0]);
                    long dstip = convert.convertAddressToLong(field[2]);
                    packetStream.add(new Packet(srcip, dstip));
                    valid_packet += 1;
                }
            }
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
//        System.out.println(valid_packet);
        return packetStream;
    }


}
