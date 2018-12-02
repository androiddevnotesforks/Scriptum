package sgtmelon.scriptum.office.annot;

import sgtmelon.scriptum.R;

/**
 * Описание цветов приложения
 */
public @interface ColorAnn {

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
