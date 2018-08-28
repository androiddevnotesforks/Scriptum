package sgtmelon.handynotes.office.annot;

import androidx.annotation.StringDef;

/**
 * Аннотация для БД
 * Наименование столов и столбцов базы данных
 * Постоянные и строки для формирования сортировки заметок
 */
@StringDef({
        Db.NT_TB,
        Db.NT_ID,
        Db.NT_CR, Db.NT_CH,
        Db.NT_NM, Db.NT_TX,
        Db.NT_CL, Db.NT_TP,
        Db.NT_RK_ID, Db.NT_RK_PS,
        Db.NT_BN, Db.NT_ST,

        Db.RL_TB,
        Db.RL_ID, Db.RL_ID_NT,
        Db.RL_PS, Db.RL_CH, Db.RL_TX,

        Db.RK_TB,
        Db.RK_ID, Db.RK_ID_NT,
        Db.RK_PS, Db.RK_NM, Db.RK_VS
})
public @interface Db {

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
