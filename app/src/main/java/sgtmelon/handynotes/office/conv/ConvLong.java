package sgtmelon.handynotes.office.conv;

import java.util.ArrayList;
import java.util.List;

public class ConvLong {

    public static List<String> fromLong(List<Long> longList) {
        List<String> stringList = new ArrayList<>();
        for (long i : longList) stringList.add(Long.toString(i));
        return stringList;
    }

    public static String[] fromLong(long[] longArr) {
        int size = longArr.length;

        String[] stringArr = new String[size];
        for (int i = 0; i < size; i++) {
            stringArr[i] = Long.toString(longArr[i]);
        }
        return stringArr;
    }

}
