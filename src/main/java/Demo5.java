import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.broadcast.Broadcast;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Demo5 {

    public static void main(String[] args) {
        SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("My app");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);

        JavaRDD<String> lines = sc.parallelize(Arrays.asList("aaa1", "panda2", "i like panda3","ddd","aaa4"));


        JavaRDD<String> lines2 = sc.parallelize(Arrays.asList("aaa1", "panda", "i like panda","ddd","aaa"));


        JavaPairRDD<String, String> pairRDD1 = lines.mapToPair(new PairFunction<String, String, String>() {
            @Override
            public Tuple2<String, String> call(String s) throws Exception {
                return new Tuple2<>(s, "ss");
            }
        });


        List<Integer> list = new ArrayList<>();
        list.add(124);
        list.add(0);
        Broadcast<List<Integer>> broadcast = sc.broadcast(list);

        JavaPairRDD<String, String> pairRDD2 = lines2.mapToPair(new PairFunction<String, String, String>() {
            @Override
            public Tuple2<String, String> call(String s) throws Exception {
                return new Tuple2<>(s, "ddd");
            }
        });

        System.out.println(pairRDD1.union(pairRDD2)
                .groupByKey()
                .collect());

        System.out.println(list);
    }
}
