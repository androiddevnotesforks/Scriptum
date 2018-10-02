package sgtmelon.scriptum.office.annot.def;

import androidx.annotation.StringDef;

/**
 * Ключи для нахождения диалогов после поворота экрана
 */
@StringDef({
        DefDlg.RENAME,
        DefDlg.SHEET_ADD,
        DefDlg.OPTIONS,
        DefDlg.CLEAR_BIN,

        DefDlg.CONVERT,
        DefDlg.RANK,
        DefDlg.COLOR,

        DefDlg.SORT,
        DefDlg.SAVE_TIME,
        DefDlg.THEME,
        DefDlg.INFO
})
public @interface DefDlg {

    String RENAME = "DLG_RENAME",
            SHEET_ADD = "DLG_SHEET_ADD",
            OPTIONS = "DLG_OPTIONS",
            CLEAR_BIN = "DLG_MESSAGE";

    String CONVERT = "DLG_CONVERT",
            RANK = "DLG_RANK",
            COLOR = "DLG_COLOR";

    String SORT = "DLG_SORT",
            SAVE_TIME = "DLG_SAVE_TIME",
            THEME = "DLG_THEME",
            INFO = "DLG_INFO";


    /**
     * Значения которое сохраняем в классе диалога
     */
    String INIT = "INIT",
            VALUE = "VALUE";

}
