package sgtmelon.handynotes.office.conv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConvList {

    public static String[] fromList(List<String> list) {
        return list.toArray(new String[list.size()]);
    }

    public static List<String> toList(String[] array) {
        return new ArrayList<>(Arrays.asList(array));
    }

}
