import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFlatMapFunction;
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

        Broadcast<List<String>> cast = sc.broadcast(testList);

        JavaRDD<String> resultLines = lines.mapPartitions(new FlatMapFunction<Iterator<String>, String>() {
                    @Override
                    public Iterable<String> call(Iterator<String> stringIterator) throws Exception {

                        List<String> wordList = new ArrayList<>();
                        while (stringIterator.hasNext()) {
                            String line = stringIterator.next();

                            if (StringUtils.isBlank(line)) {
                                continue;
                            }

                            String[] words = line.split(" ");

                            wordList.addAll(Arrays.asList(words));

                            System.out.println(cast.getValue());
                        }
                        return wordList;
                    }
                }).mapPartitionsToPair(new PairFlatMapFunction<Iterator<String>, String, Integer>() {
                    @Override
                    public Iterable<Tuple2<String, Integer>> call(Iterator<String> stringIterator) throws Exception {
                        List<Tuple2<String, Integer>> result = new ArrayList<>();


                        while (stringIterator.hasNext()) {
                    String word = stringIterator.next();
                    result.add(new Tuple2<>(word, 1));
                }

                return result;
            }
        }).reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1 + v2;
            }
        }).mapPartitions(new FlatMapFunction<Iterator<Tuple2<String, Integer>>, String>() {
            @Override
            public Iterable<String> call(Iterator<Tuple2<String, Integer>> it) throws Exception {
                List<String> result = new ArrayList<>();

                while (it.hasNext()) {
                    Tuple2<String, Integer> next = it.next();
                    result.add(next._1 + ">" + next._2);
                }

                return result;
            }
        });





        System.out.println(resultLines.collect());

        System.out.println();



    }
}
