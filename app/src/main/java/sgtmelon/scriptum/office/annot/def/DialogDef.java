package sgtmelon.scriptum.office.annot.def;

import androidx.annotation.StringDef;

/**
 * Ключи для нахождения диалогов после поворота экрана
 */
@StringDef({
        DialogDef.RENAME,
        DialogDef.SHEET,
        DialogDef.OPTIONS,
        DialogDef.CLEAR_BIN,

        DialogDef.CONVERT,
        DialogDef.RANK,
        DialogDef.COLOR,

        DialogDef.SORT,
        DialogDef.SAVE_TIME,
        DialogDef.THEME,
        DialogDef.INFO
})
public @interface DialogDef {

    String RENAME = "DLG_RENAME",
            SHEET = "DLG_SHEET",
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
