package sgtmelon.handynotes.db.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConverterList {

    public static String[] fromList(List<String> list) {
        return list.toArray(new String[list.size()]);
    }

    public static List<String> toList(String[] array) {
        return new ArrayList<>(Arrays.asList(array));
    }

}
