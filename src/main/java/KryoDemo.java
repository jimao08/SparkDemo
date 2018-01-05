import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class KryoDemo {

    public static void main(String[] args) throws Exception {
        Kryo kryo = new Kryo();

        ArrayList<String> list = new ArrayList<>(Arrays.asList("1", "2", "3"));
        kryo.setReferences(false);
        kryo.setRegistrationRequired(false);
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
        kryo.register(ArrayList.class);

         Output output = new Output(new FileOutputStream("kryodemo"));
        kryo.writeObject(output, list);

        output.flush();
        output.close();
        ArrayList readObject = kryo.readObject(new Input(new FileInputStream("kryodemo")), ArrayList.class);

        System.out.println(readObject);

    }
}
