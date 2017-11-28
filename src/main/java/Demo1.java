import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.*;
import org.apache.spark.broadcast.Broadcast;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Demo1 {

    public static void main(String[] args) {
        SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("My app");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);


//        JavaRDD<String> lines = sc.parallelize(Lists.newArrayList("aaa", "panda", "i like panda","ddd","aaa"));
        JavaRDD<String> lines = sc.textFile("testFile", 8);

        List<String> testList = Lists.newArrayList("1", "2", "3", "5");


        JavaPairRDD<String, Integer> pairRDD = lines.mapPartitions(new FlatMapFunction<Iterator<String>, String>() {
            @Override
            public Iterable<String> call(Iterator<String> it) throws Exception {

                ArrayList<String> result = new ArrayList<>();

                while (it.hasNext()) {

                    String next = it.next().trim();

                    if (StringUtils.isBlank(next)) {
                        continue;
                    }

                    result.addAll(Arrays.asList(next.split(" ")));
                }

                return result;
            }
        }).mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String s) throws Exception {
                return new Tuple2<>(s, 1);
            }
        }).reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1 + v2;
            }
        });


        JavaRDD<String> stringJavaRDD = pairRDD.map(new Function<Tuple2<String, Integer>, String>() {
            @Override
            public String call(Tuple2<String, Integer> v1) throws Exception {
                return v1._1() + ">" + v1._2();
            }

        });

        String reduce = stringJavaRDD.reduce(new Function2<String, String, String>() {
            @Override
            public String call(String v1, String v2) throws Exception {
                return v1 + v2;
            }
        });

        JavaRDD<String> aRdd = stringJavaRDD.filter(i -> i.contains("a"));


        System.out.println(reduce);


//        System.out.println(aRdd.count());


//        stringJavaRDD.saveAsTextFile("now");

//        System.out.println(stringJavaRDD.collect());
    }
}
