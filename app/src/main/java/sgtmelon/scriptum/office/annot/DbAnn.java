package sgtmelon.scriptum.office.annot;

/**
 * Аннотация для БД
 * Наименование столов и столбцов базы данных
 * Постоянные и строки для формирования сортировки заметок
 */
public @interface DbAnn {

    @interface Note {
        String TABLE = "NOTE_TABLE",
                ID = "NT_ID",
                CREATE = "NT_CREATE",
                CHANGE = "NT_CHANGE",
                NAME = "NT_NAME",
                TEXT = "NT_TEXT",
                COLOR = "NT_COLOR",
                TYPE = "NT_TYPE",
                RANK_ID = "NT_RANK_ID",
                RANK_PS = "NT_RANK_PS",
                BIN = "NT_BIN",
                STATUS = "NT_STATUS";

        String[] orders = new String[]{
                "DATE(" + CREATE + ") DESC, TIME(" + CREATE + ") DESC",
                "DATE(" + CHANGE + ") DESC, TIME(" + CHANGE + ") DESC",
                RANK_PS + " ASC",
                COLOR + " ASC"
        };
    }

    @interface Roll {
        String TABLE = "ROLL_TABLE",
                ID = "RL_ID",
                NOTE_ID = "RL_NOTE_ID",
                POSITION = "RL_POSITION",
                CHECK = "RL_CHECK",
                TEXT = "RL_TEXT";
    }

    @interface Rank {
        String TABLE = "RANK_TABLE",
                ID = "RK_ID",
                NOTE_ID = "RK_NOTE_ID",
                POSITION = "RK_POSITION",
                NAME = "RK_NAME",
                VISIBLE = "RK_VISIBLE";
    }

    @interface Value {
        String NONE = "NONE";
        String DIVIDER = ","; // TODO: 16.12.2018 Вынести в отдельный класс аннотации
    }

}