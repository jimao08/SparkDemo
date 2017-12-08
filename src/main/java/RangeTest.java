
import java.util.List;

public class RangeTest {


    public static void main(String[] args) {
        String test = "[3,40]|";

        List<NumberRange> ranges = NumberRange.parse(test);

        System.out.println(ranges);

        if (ranges == null || ranges.isEmpty()) {
            System.out.println("wrong params");
            return;
        }
        for (int i = 0; i < 100; i++) {
            System.out.println("i=" +i + ">>" + NumberRange.check(i, ranges));
        }


    }
}
