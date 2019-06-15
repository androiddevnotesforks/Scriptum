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

        private const val PREFIX = "NT"

        const val ID = "${PREFIX}_ID"
        const val CREATE = "${PREFIX}_CREATE"
        const val CHANGE = "${PREFIX}_CHANGE"
        const val NAME = "${PREFIX}_NAME"
        const val TEXT = "${PREFIX}_TEXT"
        const val COLOR = "${PREFIX}_COLOR"
        const val TYPE = "${PREFIX}_TYPE"
        const val RANK_ID = "${PREFIX}_RANK_ID"
        const val RANK_PS = "${PREFIX}_RANK_PS"
        const val BIN = "${PREFIX}_BIN"
        const val STATUS = "${PREFIX}_STATUS"

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

        private const val PREFIX = "RL"

        const val ID = "${PREFIX}_ID"
        const val NOTE_ID = "${PREFIX}_NOTE_ID"
        const val POSITION = "${PREFIX}_POSITION"
        const val CHECK = "${PREFIX}_CHECK"
        const val TEXT = "${PREFIX}_TEXT"
    }

    object Rank {
        const val TABLE = "RANK_TABLE"

        private const val PREFIX = "RK"

        const val ID = "${PREFIX}_ID"
        const val NOTE_ID = "${PREFIX}_NOTE_ID"
        const val POSITION = "${PREFIX}_POSITION"
        const val NAME = "${PREFIX}_NAME"
        const val VISIBLE = "${PREFIX}_VISIBLE"
    }

    object Alarm {
        const val TABLE = "ALARM_TABLE"

        private const val PREFIX = "AL"

        const val ID = "${PREFIX}_ID"
        const val NOTE_ID = "${PREFIX}_NOTE_ID"
        const val DATE = "${PREFIX}_DATE"
    }

}