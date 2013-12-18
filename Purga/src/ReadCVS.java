/**
 * Created with IntelliJ IDEA.
 * User: XPTA491
 * Date: 27-11-2013
 * Time: 15:15
 *
 * Edited by:
 * DATE:           USER:               ChangeLog
 * Original        Oracle              Original Class
 * 28/11/2013      PT-SI\XPTA491       Added ReadCVS class and introduced batch alterations.
 *
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ReadCVS {
    /*
    public static void main(String[] args) {

        ReadCVS obj = new ReadCVS();
        obj.run();

    }   */

    public Map<String, String> run() {

        //String csvFile = "/opt/oracle/middleware/AIA/extensions/Purge/export.csv";
        String csvFile = "C:\\Users\\xpta491\\export.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        Map<String, String> table = new HashMap<String, String>();

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] entry = line.split(cvsSplitBy);
                int spaceIndex = entry[1].indexOf("*");
                if (spaceIndex != -1)
                {
                    entry[1] = entry[1].substring(0, spaceIndex);
                }
                entry[1]=entry[1].trim();
                entry[0]=entry[0].trim();
                if (entry[0].equals("ID")){
                    System.out.println("First Line");
                } else {
                    table.put(entry[0], entry[1]);
                    System.out.println("Instance Stored [id=" + entry[0] +
                            " , composite=" + entry[1] + "]");
                }
            }
            System.out.println("CSV to Map Conversion --> Done");
            return table;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e){
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return table;
    }

}
