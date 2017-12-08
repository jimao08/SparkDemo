import java.util.ArrayList;
import java.util.List;

public class NumberRange {
    private int start;
    private int end;

    public NumberRange(int start, int end) {
        this.start = start;
        this.end = end;
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

        for (int i = 0; i < ranges.size() && !pass; i++) {
            NumberRange range = ranges.get(i);
            if (range.end == -1) {
                pass = number >= range.start;
            } else if (range.start <= range.end) {
                pass = number <= range.end && number >= range.start;
            }
        }
        return pass;
    }

    public static List<NumberRange> parse(String expression) {
        List<NumberRange> ranges = new ArrayList<>();

        for (String str : expression.split("\\|")) {
            if (!str.contains("[") || !str.contains("]")) {
                return null;
            }

            String[] split = str.split(",");

            if (split.length != 2) {
                return null;
            }

            String s0 = split[0];
            int index1 = s0.indexOf("[");
            int num1 = Integer.valueOf(s0.substring(index1 + 1).trim());

            String s1 = split[1];
            int index2 = s1.indexOf("]");
            int num2 = Integer.valueOf(s1.substring(0, index2).trim());

            if ((num2 != -1 && (num1 > num2 || num2 <= 0))
                    || num1 <= 0) {
                return null;
            }

            NumberRange range = new NumberRange(num1, num2);
            ranges.add(range);
        }

        return ranges;
    }
}