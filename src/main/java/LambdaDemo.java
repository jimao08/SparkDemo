import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LambdaDemo {

    public static void main(String[] args) {

        List<String> list = Arrays.asList("abc test", "abc", "nba");

        List<String> newList = list.stream().flatMap(s -> Arrays.stream(s.split(" "))).collect(Collectors.toList());

        System.out.println(newList);

    }
}
