package sgtmelon.handynotes.db.converter;

import java.util.ArrayList;
import java.util.List;

//TODO переделай без использования статического упоминания, а к результату возвращения
public class ConverterInt {

    public static List<String> fromInteger(List<Integer> integerList) {
        List<String> stringList = new ArrayList<>();
        for (int i : integerList) stringList.add(Integer.toString(i));
        return stringList;
    }

    public static String[] fromInteger(Integer[] integerArr) {
        int size = integerArr.length;

        String[] stringArr = new String[size];
        for (int i = 0; i < size; i++) {
            stringArr[i] = Integer.toString(integerArr[i]);
        }
        return stringArr;
    }

}
