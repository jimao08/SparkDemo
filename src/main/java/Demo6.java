import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Demo6 {


    public static void main(String[] args) throws Exception{
        List<String> pvLines = FileUtils.readLines(new File("testPvFile"));

        //key:uid_cid,value：pv计数
        Map<String, Integer> pvMap = new HashMap<>();

        for (String pvLine : pvLines) {
            String[] split = pvLine.split(",");

            String key = split[0] + "_" + split[1];

            pvMap.put(key, pvMap.getOrDefault(key, 0) + Integer.valueOf(split[5]));
        }


        //key:uid_cid,value:cv计数
        List<String> clickLines = FileUtils.readLines(new File("testClickFile"));
        Map<String, Integer> clickMap = new HashMap<>();

        for (String clickLine : clickLines) {
            String[] split = clickLine.split(",");

            String key = split[0] + "_" + split[3];

            //点击没有num，每次+1
            clickMap.put(key, clickMap.getOrDefault(key, 0) + 1);
        }

        //key:uid_cid,value：pv,cv计数
        Map<String, Freq> freqMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : pvMap.entrySet()) {
            String key = entry.getKey();
            Integer pv = entry.getValue();

            Integer ck = clickMap.getOrDefault(key,0);

            Freq freq = freqMap.get(key);
            if (freq == null) {
                freqMap.put(key, new Freq(pv, ck));
            }
        }

        System.out.println(freqMap);


        //key:cid_f ,value:fs
        Map<String, FreqStatistics> freqStatisticsMap = new HashMap<>();
        for (Map.Entry<String, Freq> entry : freqMap.entrySet()) {
            String key = entry.getKey();

            String[] keySplit = key.split("_");

            Freq freq = entry.getValue();

            int pv = freq.getPv();
            String fsKey = keySplit[1] + "_" + Math.min(pv, 20);

            FreqStatistics statistics = freqStatisticsMap.get(fsKey);
            if (statistics == null) {
                statistics = new FreqStatistics( 0, 0, 0, 0);

                freqStatisticsMap.put(fsKey, statistics);
            }

            statistics.setPv(statistics.getPv() + freq.getPv());
            statistics.setCv(statistics.getCv() + freq.getCv());

            statistics.setPuv(statistics.getPuv() + 1);
            statistics.setCuv(statistics.getCuv() + 1);
        }

        for (Map.Entry<String, FreqStatistics> entry : freqStatisticsMap.entrySet()) {
            System.out.println(entry.getKey() + ">>" + entry.getValue());
        }

    }
}



class Freq {
    private int pv;
    private int cv;

    @Override
    public String toString() {
        return "{" +
                "pv=" + pv +
                ", cv=" + cv +
                '}';
    }

    public Freq(int pv, int cv) {
        this.pv = pv;
        this.cv = cv;
    }

    public int getPv() {
        return pv;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    public int getCv() {
        return cv;
    }

    public void setCv(int cv) {
        this.cv = cv;
    }
}

class FreqStatistics {
    private int pv;
    private int puv;
    private int cv;
    private int cuv;

    public FreqStatistics(int pv, int puv, int cv, int cuv) {
        this.pv = pv;
        this.puv = puv;
        this.cv = cv;
        this.cuv = cuv;
    }


    @Override
    public String toString() {
        return "{" +
                "pv=" + pv +
                ", puv=" + puv +
                ", cv=" + cv +
                ", cuv=" + cuv +
                '}';
    }

    public int getPv() {
        return pv;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    public int getPuv() {
        return puv;
    }

    public void setPuv(int puv) {
        this.puv = puv;
    }

    public int getCv() {
        return cv;
    }

    public void setCv(int cv) {
        this.cv = cv;
    }

    public int getCuv() {
        return cuv;
    }

    public void setCuv(int cuv) {
        this.cuv = cuv;
    }
}
