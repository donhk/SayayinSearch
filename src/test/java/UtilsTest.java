import ninja.donhk.utils.Utils;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class UtilsTest {

    @Test
    public void test1() {
        List<String> input = Arrays.asList(
                "*.log"
        );
        for (String s : input) {
            System.out.println(s + "-> " + Utils.prepareExpression(s));
        }
    }
}
