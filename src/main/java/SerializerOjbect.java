import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;


public class SerializerOjbect implements Serializable{
    private static final long serialVersionUID = 1L;

    private int ival;
    private List<String> list;
    private double dval;
    private SerializerOjbectB b;

    public SerializerOjbect(int ival, List<String> list, double dval) {
        this.ival = ival;
        this.list = list;
        this.dval = dval;
        b = null;
    }

    public SerializerOjbect(int ival, List<String> list, double dval, CondType condType, SerializerOjbectB b) {
        this.ival = ival;
        this.list = list;
        this.dval = dval;
        this.b = b;
    }

    public void setB(SerializerOjbectB b) {
        this.b = b;
    }

    public int getIval() {
        return ival;
    }

    public List<String> getList() {
        return list;
    }

    public double getDval() {
        return dval;
    }

    public SerializerOjbectB getB() {
        return b;
    }

    @Override
    public String toString() {
        return "SerializerOjbect{" +
                "ival=" + ival +
                ", list=" + list +
                ", dval=" + dval +
                ", b=" +
                '}';
    }

    public enum CondType {
        none,
        not,
        and,
        or
    }


//    private void writeObject(ObjectOutputStream out) throws IOException {
////        DataOutputStream output = new DataOutputStream(out);
////
////        out.writeInt(list.size());
////        if (list != null && !list.isEmpty()) {
////            for (String s : list) {
////                output.writeUTF(s);
////            }
////        }
//        System.out.println("write object");
//
//        out.defaultWriteObject();
//    }
//
//    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
////        DataInputStream input = new DataInputStream(in);
////
////        int size = input.readInt();
////
////        if (size != 0) {
////            list = new ArrayList<>();
////            for (int i = 0; i < size; i++) {
////                String s =  input.readUTF();
////                System.out.println("read object:" + s);
////                list.add(s);
////            }
////        }
//
//        in.defaultReadObject();
//    }

}
