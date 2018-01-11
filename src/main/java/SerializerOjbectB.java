import java.io.Serializable;


public class SerializerOjbectB implements Serializable{
    private SerializerOjbect obj;
    private Integer ival;

    public SerializerOjbectB(SerializerOjbect obj, Integer ival) {
        this.ival = ival;
        this.obj = obj;
    }

    @Override
    public String toString() {
        return "SerializerOjbectB{" +
                "obj=" + obj +
                ", ival=" + ival +
                '}';
    }

    public SerializerOjbect getObj() {
        return obj;
    }

    public Integer getIval() {
        return ival;
    }
}
