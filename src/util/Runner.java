package util;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class Runner {
    static boolean runSamples = true;
    static String[] sampleAnswers1 = new String[]        {"11", "2", "161", "18", "143", "41", "3749", "14", "1928", "36", "55312",          "1930", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1"};
    static String[] sampleAnswers2 = new String[]        {"31", "4", "48" , "9",  "123", "6", "11387", "34", "2858", "81", "65601038650482", "1206", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1"};
    static boolean[] hasDifferentSamples = new boolean[] {false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};

    public static void main(String[] args) throws Exception {
        var days = new String[] {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        var parts = new boolean[] {true, false};

        for (String day : days) {
            var cls = Class.forName("days.Day" + day);
            var exclude = (boolean) cls.getMethod("exclude").invoke(cls.getDeclaredConstructor().newInstance());
            if (exclude) {
                System.out.println("Excluding day" + day + "\n");
                continue;
            }
            for (boolean part : parts) {
                boolean samplePassed = runSamples(cls, day, part);
                if (runSamples && !samplePassed) {
                    continue;
                }
                var reader = new Reader("src/data/day" + day + ".txt");
                var inputs = reader.readAsStrings();
                var m = cls.getMethod("timeAndLogResult", String.class, boolean.class, ArrayList.class);
                m.invoke(cls.getDeclaredConstructor().newInstance(), day, part, inputs);
                System.out.println();
            }
        }
    }

    private static boolean runSamples(Class<?> cls, String day, boolean part1) throws Exception {
        if (!runSamples) {
            return true;
        }
        System.out.print("Sample ");
        var index = Integer.parseInt(day) - 1;
        var reader = new Reader("src/test/day" + day + "sample" + ((hasDifferentSamples[index] && !part1)? "2" : "") + ".txt");
        var inputs = reader.readAsStrings();
        var m = cls.getMethod("timeAndLogResult", String.class, boolean.class, ArrayList.class);
        var result = m.invoke(cls.getDeclaredConstructor().newInstance(), day, part1, inputs);
        var sampleAnswer = (part1) ? sampleAnswers1[index] : sampleAnswers2[index];
        if (sampleAnswer == null) {
            return true;
        }
        var passed = result.equals(sampleAnswer);
        if (!passed) {
            System.out.println("\nSample failed! Expected: " + sampleAnswer + " Actual: " + result + "\n");
        }
        return passed;
    }
}
