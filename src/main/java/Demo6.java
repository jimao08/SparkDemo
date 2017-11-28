import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Demo6 {


    public static void main(String[] args) throws Exception{
        List<String> pvLines = FileUtils.readLines(new File("testPvFile"));

        List<String> clickLines = FileUtils.readLines(new File("testClickFile"));


        Map<String, Integer> pvMap = new HashMap<>();

        for (String pvLine : pvLines) {
            String[] split = pvLine.split(",");

            String key = split[0] + "_" + split[1];

            pvMap.put(key, pvMap.getOrDefault(key, 0) + Integer.valueOf(split[5]));
        }


        Map<String, Integer> clickMap = new HashMap<>();

        for (String clickLine : clickLines) {
            String[] split = clickLine.split(",");

            String key = split[0] + "_" + split[3];

            clickMap.put(key, pvMap.getOrDefault(key, 0) + 1);
        }

        System.out.println(clickMap);





    }
}
