package sgtmelon.handynotes.office.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConvList {

    public static Long[] fromList(List<Long> list) {
        return list.toArray(new Long[list.size()]);
    }

    public static List<Long> toList(Long[] array) {
        return new ArrayList<>(Arrays.asList(array));
    }

}
