package sgtmelon.handynotes.office.annot;

import androidx.annotation.StringDef;

/**
 * Ключи для нахождения диалогов после поворота экрана
 */
@StringDef({
        Dlg.RENAME, Dlg.SHEET_ADD,
        Dlg.OPTIONS, Dlg.CLEAR_BIN,

        Dlg.CONVERT, Dlg.RANK, Dlg.COLOR,

        Dlg.SORT, Dlg.SAVE_TIME,
        Dlg.THEME, Dlg.INFO
})
public @interface Dlg {

    //Main
    String RENAME = "DLG_RENAME",
            SHEET_ADD = "DLG_SHEET_ADD",
            OPTIONS = "DLG_OPTIONS",
            CLEAR_BIN = "DLG_CLEAR_BIN";

    //Note
    String CONVERT = "DLG_CONVERT",
            RANK = "DLG_RANK",
            COLOR = "DLG_COLOR";

    //Settings
    String SORT = "DLG_SORT",
            SAVE_TIME = "DLG_SAVE_TIME",
            THEME = "DLG_THEME",
            INFO = "DLG_INFO";


    //Значение которое сохраняем в классе диалога
    String VALUE = "VALUE";

}
