package sgtmelon.scriptum.model.data

/**
 * Константы для БД
 * Наименование столов и столбцов базы данных
 * Постоянные и строки для формирования сортировки заметок
 *
 * @author SerjantArbuz
 */
object DbData {

    object Note {
        const val TABLE = "NOTE_TABLE"
        const val ID = "NT_ID"
        const val CREATE = "NT_CREATE"
        const val CHANGE = "NT_CHANGE"
        const val NAME = "NT_NAME"
        const val TEXT = "NT_TEXT"
        const val COLOR = "NT_COLOR"
        const val TYPE = "NT_TYPE"
        const val RANK_ID = "NT_RANK_ID"
        const val RANK_PS = "NT_RANK_PS"
        const val BIN = "NT_BIN"
        const val STATUS = "NT_STATUS"

        // TODO #RELEASE добавить пост сортировку (после сортировки по категории и цвету)
        val orders = arrayOf(
                "DATE($CHANGE) DESC, TIME($CHANGE) DESC",
                "DATE($CREATE) DESC, TIME($CREATE) DESC",
                "$RANK_PS ASC",
                "$COLOR ASC"
        )
    }

    object Roll {
        const val TABLE = "ROLL_TABLE"
        const val ID = "RL_ID"
        const val NOTE_ID = "RL_NOTE_ID"
        const val POSITION = "RL_POSITION"
        const val CHECK = "RL_CHECK"
        const val TEXT = "RL_TEXT"
    }

    object Rank {
        const val TABLE = "RANK_TABLE"
        const val ID = "RK_ID"
        const val NOTE_ID = "RK_NOTE_ID"
        const val POSITION = "RK_POSITION"
        const val NAME = "RK_NAME"
        const val VISIBLE = "RK_VISIBLE"
    }

    object Value {
        const val NONE = "NONE"
        const val DIVIDER = "," // TODO: 16.12.2018 Вынести в отдельный класс аннотации
    }

}