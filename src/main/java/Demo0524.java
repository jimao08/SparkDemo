import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Demo0524 {

    public static void main(String[] args) throws Exception {

        String path = MyUtils.getDownloadFilePath("test0524.txt");


        List<String> lines = FileUtils.readLines(new File(path));

        long lineCount = 0;
        long emptyCount = 0;
        long bugCount = 0;
        long repeatCount = 0;


        Map<String, Map<String, String>> deviceMap = new HashMap<>();
        Map<Integer, String> indexNameMap = new HashMap<>();

        indexNameMap.put(0, "device");
        indexNameMap.put(1, "name");
        indexNameMap.put(2, "sex");
        indexNameMap.put(3, "age");

        for (String line : lines) {

            String[] split = line.split("\t");

            if (split.length != 4) {
                continue;
            }

            HashMap<String, String> currentMap = new HashMap<>();
            for (int i = 0; i < split.length; i++) {
                String s = split[i];

                if (s.equals("")) {
                    emptyCount++;
                }

                currentMap.put(indexNameMap.get(i), s);
            }

            String device = currentMap.get("device");

            Map<String, String> old = deviceMap.get(device);

            if (old == null) {
                deviceMap.put(device, currentMap);
            } else {
                repeatCount++;

                for (Map.Entry<String, String> entry : old.entrySet()) {
                    String k = entry.getKey();
                    String oldValue = entry.getValue();

                    String newValue = currentMap.get(k);
                    if (!oldValue.equals(newValue)) {
                        bugCount++;
                    }

                }
            }

            System.out.println(currentMap);

            lineCount++;
        }


        final int size = indexNameMap.size();
        System.out.println("emptyCount=" + emptyCount);
        System.out.println("bugCount=" + bugCount);
        System.out.println("repeatCount=" + repeatCount * (size - 1));
        System.out.println("lineCount=" + lineCount);
        System.out.println("total=" + lineCount * (size - 1));
    }
}
