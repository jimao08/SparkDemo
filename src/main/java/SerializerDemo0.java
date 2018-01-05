import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

import java.io.*;
import java.util.Arrays;

public class SerializerDemo0 {


    public static void main(String[] args) throws Exception {
        SerializerOjbect object = new SerializerOjbect(1, Arrays.asList("1", "2", "3"), 0.01);


        Kryo kryo = new Kryo();

//        Output output = new Output(new FileOutputStream("tt"));
//        kryo.writeObject(output, object);
//        output.flush();
//        output.close();


        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("tt"));
        objectOutputStream.writeObject(object);
//
//
//        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("tt"));
//        Object readObject = inputStream.readObject();
    }
}
