import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

public class Demo4 {

    public static void main(String[] args) {
        SparkConf sparkConf = new SparkConf().setAppName("My app");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);

        JavaRDD<String> lines = sc.textFile("testPvFile", 5);

        List<String> collect = lines.mapPartitions((stringIterator)-> {
            {
                ArrayList<String> list = new ArrayList<>();

                while (stringIterator.hasNext()) {

                    String next = stringIterator.next();

                    if (StringUtils.isBlank(next)) {
                        continue;
                    }

                    String[] split = next.split(",");

                    if (split.length != 6) {
                        continue;
                    }

                    String s = split[0] + "_" + split[1];

                    list.add(s);
                }

                return list;
            }
        }).mapToPair(s -> new Tuple2<>(s, 1))
                .reduceByKey((v1, v2) -> v1 + v2)
                .map(tuple -> tuple._1 + ">" + tuple._2())
                .collect();
        System.out.println(collect);

    }
}
