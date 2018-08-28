package sgtmelon.handynotes.office.annot.def.db;

import androidx.annotation.StringDef;

/**
 * Аннотация для БД.
 * Наименование столов и столбцов базы данных.
 * Строковые постоянные.
 * Строки для формирования сортировки заметок.
 */
@StringDef({DefDb.NT_TB,
        DefDb.NT_ID,
        DefDb.NT_CR, DefDb.NT_CH,
        DefDb.NT_NM, DefDb.NT_TX,
        DefDb.NT_CL, DefDb.NT_TP,
        DefDb.NT_RK_ID, DefDb.NT_RK_PS,
        DefDb.NT_BN, DefDb.NT_ST,

        DefDb.RL_TB,
        DefDb.RL_ID, DefDb.RL_ID_NT,
        DefDb.RL_PS, DefDb.RL_CH, DefDb.RL_TX,

        DefDb.RK_TB,
        DefDb.RK_ID, DefDb.RK_ID_NT,
        DefDb.RK_PS, DefDb.RK_NM, DefDb.RK_VS
})
public @interface DefDb {

    String NT_TB = "NOTE_TABLE",
            NT_ID = "NT_ID",
            NT_CR = "NT_CREATE",
            NT_CH = "NT_CHANGE",
            NT_NM = "NT_NAME",
            NT_TX = "NT_TEXT",
            NT_CL = "NT_COLOR",
            NT_TP = "NT_TYPE",
            NT_RK_ID = "NT_RANK_ID",
            NT_RK_PS = "NT_RANK_PS",
            NT_BN = "NT_BIN",
            NT_ST = "NT_STATUS";

    String RL_TB = "ROLL_TABLE",
            RL_ID = "RL_ID",
            RL_ID_NT = "RL_ID_NOTE",
            RL_PS = "RL_POSITION",
            RL_CH = "RL_CHECK",
            RL_TX = "RL_TEXT";

    String RK_TB = "RANK_TABLE",
            RK_ID = "RK_ID",
            RK_ID_NT = "RK_ID_NOTE",
            RK_PS = "RK_POSITION",
            RK_NM = "RK_NAME",
            RK_VS = "RK_VISIBLE";

    String none = "NONE";
    String divider = ",";

    String[] orders = new String[]{
            "DATE(" + NT_CR + ") DESC, TIME(" + NT_CR + ") DESC",
            "DATE(" + NT_CH + ") DESC, TIME(" + NT_CH + ") DESC",
            NT_RK_PS + " ASC",
            NT_CL + " ASC"
    };

}
