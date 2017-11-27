import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaHadoopRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Demo3 {


    private static JavaSparkContext ctx = null;

    public static void main(String[] args) throws Exception {

        SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("My app");
        sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
        sparkConf.set("spark.kryoserializer.buffer.max", "1024m");

        ctx = new JavaSparkContext(sparkConf);


        JavaPairRDD<String, Integer> rdd1 = getRdd("testFile");
        System.out.println(rdd1.collect().size());


        JavaPairRDD<String, Integer> rdd2 = getRdd("testFile2");

        JavaPairRDD<String, Integer> newRdd = ctx.union(rdd1, rdd2).groupByKey(10).mapToPair(new PairFunction<Tuple2<String, Iterable<Integer>>, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(Tuple2<String, Iterable<Integer>> stringIterableTuple2) throws Exception {
                int sum = 0;
                for (Integer ival : stringIterableTuple2._2()) {
                    sum += ival;
                }

                return new Tuple2<>(stringIterableTuple2._1(), sum);
            }
        });

        System.out.println(newRdd.collect());

    }

    private static JavaPairRDD<String, Integer> getRdd(String fileName) {
        JavaHadoopRDD<LongWritable, Text> testFile = (JavaHadoopRDD<LongWritable, Text>) ctx.hadoopFile(fileName,
                TextInputFormat.class, LongWritable.class, Text.class, 10);


        JavaPairRDD<String, Integer> rdd = testFile.mapPartitionsWithInputSplit(new Function2<InputSplit, Iterator<Tuple2<LongWritable, Text>>, Iterator<String>>() {
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
        });

        return rdd;
    }
}
