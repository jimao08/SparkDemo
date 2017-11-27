import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaHadoopRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Demo2 {

    public static void main(String[] args) throws Exception {

        SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("My app");
        sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
        sparkConf.set("spark.kryoserializer.buffer.max", "1024m");

        JavaSparkContext ctx = new JavaSparkContext(sparkConf);

        JavaHadoopRDD<LongWritable, Text> testFile = (JavaHadoopRDD<LongWritable, Text>) ctx.hadoopFile("testFile",
                TextInputFormat.class, LongWritable.class, Text.class, 10);


        JavaRDD<String> rdd = testFile.mapPartitionsWithInputSplit(new Function2<InputSplit, Iterator<Tuple2<LongWritable, Text>>, Iterator<String>>() {
            @Override
            public Iterator<String> call(InputSplit split, Iterator<Tuple2<LongWritable, Text>> it) throws Exception {

                FileSplit fileSplit = (FileSplit) split;

                ArrayList<String> words = new ArrayList<>();
                while (it.hasNext()) {
                    Tuple2<LongWritable, Text> next = it.next();
                    words.addAll(Arrays.asList(next._2.toString().split(" ")));
                }

                return words.iterator();
            }
        }, true).mapPartitionsToPair(new PairFlatMapFunction<Iterator<String>, String, Integer>() {
            @Override
            public Iterable<Tuple2<String, Integer>> call(Iterator<String> stringIterator) throws Exception {
                List<Tuple2<String, Integer>> list = new ArrayList<>();


                while (stringIterator.hasNext()) {
                    String next = stringIterator.next();
                    list.add(new Tuple2<>(next, 1));
                }
                return list;
            }
        }).reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1 + v2;
            }
        }).mapPartitions(new FlatMapFunction<Iterator<Tuple2<String, Integer>>, String>() {
            @Override
            public Iterable<String> call(Iterator<Tuple2<String, Integer>> it) throws Exception {

                List<String> list = new ArrayList<>();
                while (it.hasNext()) {
                    Tuple2<String, Integer> next = it.next();
                    list.add(next._1 + ">" + next._2);
                }
                return list;
            }
        });


        System.out.println(rdd.collect());
    }
}
