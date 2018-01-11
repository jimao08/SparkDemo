import java.io.*;
import java.util.ArrayList;

public class Demo00001 {


    public static void main(String[] args) throws Exception{
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");

        SerializerOjbect object = new SerializerOjbect(1, list, 0.01);

        SerializerOjbectB b = new SerializerOjbectB(object, 100);
        object.setB(b);


        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(bos);
        objectOutputStream.writeObject(b);

        System.out.println(new String(bos.toByteArray()));


        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        SerializerOjbectB read = ((SerializerOjbectB) ois.readObject());

        System.out.println(read.getObj().getB().getObj().getB());


    }
}
