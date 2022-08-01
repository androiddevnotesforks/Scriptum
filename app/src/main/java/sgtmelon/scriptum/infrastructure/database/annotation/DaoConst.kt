package sgtmelon.scriptum.infrastructure.database.annotation

import androidx.room.OnConflictStrategy

annotation class DaoConst {
    companion object {
        /**
         * Dao return this id when insert error happen (when use [OnConflictStrategy.IGNORE]).
         */
        const val UNIQUE_ERROR_ID = -1L

        /**
         * Overflow error may happen, when dao function contains "IN (:someList)" and "someList"
         * size bigger than 999 value.
         */
        const val OVERFLOW_COUNT = 900
    }
}