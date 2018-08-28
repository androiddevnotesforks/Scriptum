package sgtmelon.handynotes.office.annot.def;

import sgtmelon.handynotes.R;

public @interface DefColor {

    //Кружки для диалога смены цвета и фильтра
    int[] ic_light = new int[]{
            R.drawable.ic_color_00_l, R.drawable.ic_color_01_l,
            R.drawable.ic_color_02_l, R.drawable.ic_color_03_l,
            R.drawable.ic_color_04_l, R.drawable.ic_color_05_l,
            R.drawable.ic_color_06_l, R.drawable.ic_color_07_l,
            R.drawable.ic_color_08_l, R.drawable.ic_color_09_l,
            R.drawable.ic_color_10_l};

    int[] ic_dark = new int[]{
            R.drawable.ic_color_00_d, R.drawable.ic_color_01_d,
            R.drawable.ic_color_02_d, R.drawable.ic_color_03_d,
            R.drawable.ic_color_04_d, R.drawable.ic_color_05_d,
            R.drawable.ic_color_06_d, R.drawable.ic_color_07_d,
            R.drawable.ic_color_08_d, R.drawable.ic_color_09_d,
            R.drawable.ic_color_10_d};

    //Цвета для заметок
    int[] cl_light = new int[]{
            R.color.noteRed, R.color.notePurple,
            R.color.noteIndigo, R.color.noteBlue,
            R.color.noteTeal, R.color.noteGreen,
            R.color.noteYellow, R.color.noteOrange,
            R.color.noteBrown, R.color.noteBlueGrey,
            R.color.noteWhite};

    int[] cl_dark = new int[]{
            R.color.noteRedDark, R.color.notePurpleDark,
            R.color.noteIndigoDark, R.color.noteBlueDark,
            R.color.noteTealDark, R.color.noteGreenDark,
            R.color.noteYellowDark, R.color.noteOrangeDark,
            R.color.noteBrownDark, R.color.noteBlueGreyDark,
            R.color.noteWhiteDark};

}
