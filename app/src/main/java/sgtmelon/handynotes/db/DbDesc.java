package sgtmelon.handynotes.db;

import android.arch.persistence.room.Ignore;

public class DbDesc {

    @Ignore
    public static final String NT_TB = "NOTE_TABLE",
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

    @Ignore
    public static final String RL_TB = "ROLL_TABLE",
            RL_ID = "RL_ID",
            RL_CR = "RL_CREATE",
            RL_PS = "RL_POSITION",
            RL_CH = "RL_CHECK",
            RL_TX = "RL_TEXT";

    @Ignore
    public static final String RK_TB = "RANK_TABLE",
            RK_ID = "RK_ID",
            RK_PS = "RK_POSITION",
            RK_NM = "RK_NAME",
            RK_CR = "RK_CREATE",
            RK_VS = "RK_VISIBLE";

    public static final String none = "NONE";
    public static final String divider = ",";

    public static final String[] orders = new String[]{
            "DATE(" + NT_CR + ") DESC, TIME(" + NT_CR + ") DESC",
            "DATE(" + NT_CH + ") DESC, TIME(" + NT_CH + ") DESC",
            NT_RK_PS + " ASC",
            NT_CL + " ASC"
    };

    public static final int
            typeText = 0, typeRoll = 1,         //Типы заметок
            binFalse = 0, binTrue = 1,          //Расположение относительно карзины
            checkFalse = 0, checkTrue = 1,      //Выполнение пункта
            visibleFalse = 0, visibleTrue = 1,  //Видимость категории
            statusFalse = 0, statusTrue = 1;    //Закрепление в статус-баре

}
