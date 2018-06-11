package sgtmelon.handynotes.database.converter;

import java.util.ArrayList;
import java.util.List;

//TODO переделай без использования статического упоминания, а к результату возвращения
public class ConverterInt {

    public static List<String> fromInteger(List<Integer> integerList) {
        List<String> stringList = new ArrayList<>();
        for (int i : integerList) stringList.add(Integer.toString(i));
        return stringList;
    }


    public static List<Integer> fromString(List<String> stringList) {
        List<Integer> integerList = new ArrayList<>();
        for (String s : stringList) integerList.add(Integer.parseInt(s));
        return integerList;
    }

}
