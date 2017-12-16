import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.broadcast.Broadcast;
import scala.Tuple2;

import java.util.List;
import java.util.TreeSet;

/**
 *
 */
public class Demo7 {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setMaster("local").setAppName("my app");
        conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
        conf.set("spark.kryoserializer.buffer.max", "1024m");

        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaPairRDD<LongWritable, Text> textJavaPairRDD = sc.hadoopFile("testFile", TextInputFormat.class,
                LongWritable.class, Text.class);

        Broadcast<TreeSet<Long>> treeSetBroadcast = sc.broadcast(new TreeSet<>());

        JavaRDD<String> stringJavaRDD = textJavaPairRDD.map(new Function<Tuple2<LongWritable, Text>, String>() {
            @Override
            public String call(Tuple2<LongWritable, Text> v1) throws Exception {
                TreeSet<Long> treeSet = treeSetBroadcast.value();

                LongWritable longWritable = v1._1();
                treeSet.add(longWritable.get());

                return v1._2().toString();
            }
        });

        stringJavaRDD.saveAsTextFile("aa");

        System.out.println(treeSetBroadcast.value());
        List<Tuple2<LongWritable, Text>> list = textJavaPairRDD.collect();

//        System.out.println(stringJavaRDD.collect());



//        for (Tuple2<LongWritable, Text> tuple2 : list) {
//            System.out.println(tuple2._2().toString());
//        }


        System.out.println(list);

        for (String s : stringJavaRDD.collect()) {
            System.out.println(s + ">>" + s.length());
        }
        System.out.println(treeSetBroadcast.value());

    }
}
