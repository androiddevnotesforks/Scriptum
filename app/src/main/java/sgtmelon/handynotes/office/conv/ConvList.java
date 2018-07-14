package sgtmelon.handynotes.office.conv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConvList {

    public static Long[] fromList(List<Long> list) {
        return list.toArray(new Long[0]);
    }

    public static List<Long> toList(Long[] array) {
        return new ArrayList<>(Arrays.asList(array));
    }


}
