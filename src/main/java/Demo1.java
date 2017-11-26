import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Demo1 {

    public static void main(String[] args) {
        SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("My app");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);


        JavaRDD<String> lines = sc.parallelize(Lists.newArrayList("aaa", "panda", "i like panda","ddd","aaa"));

        JavaRDD<String> testLines = lines.mapPartitions(new FlatMapFunction<Iterator<String>, String>() {
            private static final long serialVersionUID = 1L;

            public Iterable<String> call(Iterator<String> it) throws Exception {
                List<String> list = new ArrayList<String>();
                String exposure = null;
                while (it.hasNext()) {
                    exposure = it.next();

                    list.add(exposure);
                }
                return list;
            }
        }).mapToPair(new PairFunction<String, String, Integer>() {
            private static final long serialVersionUID = 1L;

            public Tuple2<String, Integer> call(String s) {
                return new Tuple2<String, Integer>(s, 1);
            }
        }).reduceByKey(new Function2<Integer, Integer, Integer>() {
            private static final long serialVersionUID = 1L;

            public Integer call(Integer i1, Integer i2) {
                return i1 + i2;
            }
        }, 8).mapPartitions(new FlatMapFunction<Iterator<Tuple2<String, Integer>>, String>() {
            private static final long serialVersionUID = 1L;

            public Iterable<String> call(Iterator<Tuple2<String, Integer>> it) throws Exception {
                List<String> list = new ArrayList<String>();
                Tuple2<String, Integer> exposure = null;
                while (it.hasNext()) {
                    exposure = it.next();
                    if (exposure == null || StringUtils.isBlank(exposure._1)) continue;
                    list.add(exposure._1 + "\t" + exposure._2);
                }
                return list;
            }
        });

//        lines.saveAsTextFile("E:\\abc");
        List<String> stringList = testLines.collect();

        System.out.println(stringList);

        try {
            TimeUnit.MINUTES.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
