import java.util.ArrayList;
import java.util.List;

public class NumberRange {
    private int start;
    private int end;

    public NumberRange(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "NumberRange{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }

    public static boolean check(int number, List<NumberRange> ranges) {
        boolean pass = false;
        for (NumberRange range : ranges) {
            if (range.start == range.end) {
                pass = number == range.start;
            }
            if (pass) {
                break;
            }

            if (range.end == -1) {
                pass = number > range.start;
            }

            if (pass) {
                break;
            }

            if (range.start < range.getEnd()) {
                pass = number <= range.end && number >= range.start;
            }
            if (pass) {
                break;
            }
        }
        return pass;
    }

    public static List<NumberRange> parse(String expression) {

        List<NumberRange> ranges = new ArrayList<>();

        for (String str : expression.split("\\|")) {
            String[] split = str.split(",");

            if (split.length != 2) {
                continue;
            }

            String s0 = split[0];
            int index1 = s0.indexOf("[");
            int num1 = Integer.valueOf(s0.substring(index1 + 1).trim());

            String s1 = split[1];
            int index2 = s1.indexOf("]");
            int num2 = Integer.valueOf(s1.substring(0, index2).trim());


            if (num2 != -1 && num1 > num2) {
                continue;
            }

            NumberRange range = new NumberRange(num1, num2);
            ranges.add(range);
        }

        return ranges;
    }
}