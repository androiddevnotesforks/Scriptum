package sgtmelon.scriptum.infrastructure.database.model

import androidx.room.OnConflictStrategy

object DaoConst {
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