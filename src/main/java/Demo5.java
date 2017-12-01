import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

public class Demo5 {

    public static void main(String[] args) {
        SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("My app");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);

        JavaRDD<String> lines = sc.parallelize(Arrays.asList("aaa1", "panda2", "i like panda3","ddd","aaa4"));


        JavaRDD<String> lines2 = sc.parallelize(Arrays.asList("aaa1", "panda", "i like panda","ddd","aaa"));

        JavaPairRDD<String, String> pairRDD = lines.union(lines2).mapToPair(new PairFunction<String, String, String>() {
            @Override
            public Tuple2<String, String> call(String s) throws Exception {

                try {
                    if (s.equals("aaa4")) {
                        throw new Exception("sss");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return new Tuple2<>(s, s.charAt(0) + "");
            }
        }).reduceByKey(new Function2<String, String, String>() {
            @Override
            public String call(String v1, String v2) throws Exception {
                return v1 + v2;
            }
        },10);


        List<Tuple2<String, String>> sample = pairRDD.takeSample(true, 2);


        System.out.println(sample);

    }
}
