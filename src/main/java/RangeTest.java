import java.util.ArrayList;
import java.util.List;

public class RangeTest {


    public static void main(String[] args) {
        String test = "[1,3] | [4,6  ] | [30,-1]";

        List<NumberRange> ranges = NumberRange.parse(test);

        System.out.println(ranges);

        System.out.println(NumberRange.check(100, ranges));
    }
}
