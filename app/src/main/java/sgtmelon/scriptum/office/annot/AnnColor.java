package sgtmelon.scriptum.office.annot;

import sgtmelon.scriptum.R;

/**
 * Описание цветов приложения
 */
public @interface AnnColor {

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
            R.color.note_red_light, R.color.note_purple_light,
            R.color.note_indigo_light, R.color.note_blue_light,
            R.color.note_teal_light, R.color.note_green_light,
            R.color.note_yellow_light, R.color.note_orange_light,
            R.color.note_brown_light, R.color.note_blue_grey_light,
            R.color.note_white_light};

    int[] cl_dark = new int[]{
            R.color.note_red_dark, R.color.note_purple_dark,
            R.color.note_indigo_dark, R.color.note_blue_dark,
            R.color.note_teal_dark, R.color.note_green_dark,
            R.color.note_yellow_dark, R.color.note_orange_dark,
            R.color.note_brown_dark, R.color.note_blue_grey_dark,
            R.color.note_white_dark};

}
