package sgtmelon.handynotes.office.annot.def;

import sgtmelon.handynotes.R;

public @interface DefColor {

    //Кружки для диалога смены цвета и фильтра
    int[] icon = new int[]{
            R.drawable.ic_color_00, R.drawable.ic_color_01,
            R.drawable.ic_color_02, R.drawable.ic_color_03,
            R.drawable.ic_color_04, R.drawable.ic_color_05,
            R.drawable.ic_color_06, R.drawable.ic_color_07,
            R.drawable.ic_color_08, R.drawable.ic_color_09,
            R.drawable.ic_color_10};

    //Цвета для заметок
    int[] light = new int[]{
            R.color.noteRed, R.color.notePurple,
            R.color.noteIndigo, R.color.noteBlue,
            R.color.noteTeal, R.color.noteGreen,
            R.color.noteYellow, R.color.noteOrange,
            R.color.noteBrown, R.color.noteBlueGrey,
            R.color.noteWhite};

    int[] dark = new int[]{
            R.color.noteRedDark, R.color.notePurpleDark,
            R.color.noteIndigoDark, R.color.noteBlueDark,
            R.color.noteTealDark, R.color.noteGreenDark,
            R.color.noteYellowDark, R.color.noteOrangeDark,
            R.color.noteBrownDark, R.color.noteBlueGreyDark,
            R.color.noteWhiteDark};

}
