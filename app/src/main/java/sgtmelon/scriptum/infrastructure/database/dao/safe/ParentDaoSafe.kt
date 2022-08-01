package sgtmelon.scriptum.infrastructure.database.dao.safe

import kotlin.math.min
import sgtmelon.scriptum.infrastructure.database.annotation.DaoConst

interface ParentDaoSafe {

    /**
     * See description of error in [DaoConst.UNIQUE_ERROR_ID].
     */
    suspend fun Long.checkSafe(): Long? = this.takeIf { it != DaoConst.UNIQUE_ERROR_ID }

    /**
     * See description of error in [DaoConst.OVERFLOW_COUNT].
     */
    suspend fun <T> safeOverflow(list: List<T>, func: suspend (subList: List<T>) -> Unit) {
        if (list.size <= DaoConst.OVERFLOW_COUNT) {
            func(list)
            return
        }

        /** Start index INCLUDE for subList. */
        var startIndex = 0

        while (startIndex < list.size) {
            /** Last index EXCLUDE for subList. */
            val lastIndex = min(a = startIndex + DaoConst.OVERFLOW_COUNT, list.size)

            func(list.subList(startIndex, lastIndex))

            startIndex = lastIndex
        }
    }
}