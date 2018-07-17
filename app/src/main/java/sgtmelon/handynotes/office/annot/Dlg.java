package sgtmelon.handynotes.office.annot;

public @interface Dlg {

    //Значение которое сохраняем в классе диалога
    String VALUE = "VALUE";

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

}
