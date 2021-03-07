package sgtmelon.scriptum.domain.model.data

import sgtmelon.scriptum.data.room.converter.type.StringConverter
import sgtmelon.scriptum.domain.model.item.RankItem
import sgtmelon.scriptum.domain.model.key.NoteType

/**
 * Constants for DataBase
 * Naming of tables and columns for DataBase
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

        object Default {
            const val ID = 0L
            const val CREATE = ""
            const val CHANGE = ""
            const val NAME = ""
            const val TEXT = ""
            const val COLOR = 0
            val TYPE = NoteType.TEXT
            const val RANK_ID = -1L
            const val RANK_PS = -1
            const val BIN = false
            const val STATUS = false
        }

        object Room {
            const val ID = "0"
            const val CREATE = ""
            const val CHANGE = ""
            const val NAME = ""
            const val TEXT = ""
            const val COLOR = "0"
            const val TYPE = "0"
            const val RANK_ID = "-1"
            const val RANK_PS = "-1"
            const val BIN = "0"
            const val STATUS = "0"
        }
    }

    object Roll {
        const val TABLE = "ROLL_TABLE"

        private const val PREFIX = "RL"

        const val ID = "${PREFIX}_ID"
        const val NOTE_ID = "${PREFIX}_NOTE_ID"
        const val POSITION = "${PREFIX}_POSITION"
        const val CHECK = "${PREFIX}_CHECK"
        const val TEXT = "${PREFIX}_TEXT"

        const val INDEX_NOTE_ID = "${TABLE}_NOTE_ID_INDEX"

        object Default {
            val ID = null
            const val NOTE_ID = 0L
            const val POSITION = 0
            const val CHECK = false
            const val TEXT = ""
        }

        object Room {
            const val ID = "0"
            const val NOTE_ID = "0"
            const val POSITION = "0"
            const val CHECK = "0"
            const val TEXT = ""
        }
    }

    object RollVisible {
        const val TABLE = "ROLL_VISIBLE_TABLE"

        private const val PREFIX = "RL_VS"

        const val ID = "${PREFIX}_ID"
        const val NOTE_ID = "${PREFIX}_NOTE_ID"
        const val VALUE = "${PREFIX}_VALUE"

        const val INDEX_NOTE_ID = "${TABLE}_NOTE_ID_INDEX"

        object Default {
            const val ID = 0L
            const val NOTE_ID = 0L
            const val VALUE = true
        }

        object Room {
            const val ID = "0"
            const val NOTE_ID = "0"
            const val VALUE = "1"
        }
    }

    object Rank {
        const val TABLE = "RANK_TABLE"

        private const val PREFIX = "RK"

        const val ID = "${PREFIX}_ID"
        const val NOTE_ID = "${PREFIX}_NOTE_ID"
        const val POSITION = "${PREFIX}_POSITION"
        const val NAME = "${PREFIX}_NAME"
        const val VISIBLE = "${PREFIX}_VISIBLE"

        const val INDEX_NAME = "${TABLE}_NAME_INDEX"

        /**
         * This values not for dataBase. Only for [RankItem].
         */
        const val BIND_COUNT = "${PREFIX}_BIND_COUNT"
        const val NOTIFICATION_COUNT = "${PREFIX}_NOTIFICATION_COUNT"

        object Default {
            const val ID = 0L
            val NOTE_ID = ArrayList<Long>()
            const val POSITION = 0
            const val NAME = ""
            const val VISIBLE = true

            /**
             * This values not for dataBase. Only for [RankItem].
             */
            const val BIND_COUNT = 0
            const val NOTIFICATION_COUNT = 0
        }

        object Room {
            const val ID = "0"
            const val NOTE_ID = StringConverter.NONE
            const val POSITION = "0"
            const val NAME = ""
            const val VISIBLE = "1"
        }
    }

    object Alarm {
        const val TABLE = "ALARM_TABLE"

        private const val PREFIX = "AL"

        const val ID = "${PREFIX}_ID"
        const val NOTE_ID = "${PREFIX}_NOTE_ID"
        const val DATE = "${PREFIX}_DATE"

        const val INDEX_NOTE_ID = "${TABLE}_NOTE_ID_INDEX"

        object Default {
            const val ID = 0L
            const val NOTE_ID = 0L
            const val DATE = ""
        }

        object Room {
            const val ID = "0"
            const val NOTE_ID = "0"
            const val DATE = ""
        }
    }

}