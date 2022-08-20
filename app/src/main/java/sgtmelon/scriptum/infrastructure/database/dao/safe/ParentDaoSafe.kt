package sgtmelon.scriptum.infrastructure.database.dao.safe

import kotlin.math.min
import sgtmelon.scriptum.infrastructure.database.annotation.DaoConst
import sgtmelon.scriptum.infrastructure.model.exception.DaoIdConflictException
import sgtmelon.scriptum.infrastructure.utils.record

/**
 * See description of error in [DaoConst.UNIQUE_ERROR_ID].
 */
fun Long.checkSafe(): Long? {
    val id = this.takeIf { it != DaoConst.UNIQUE_ERROR_ID }

    if (id == null) {
        DaoIdConflictException().record()
    }

    return id
}

/**
 * See description of error in [DaoConst.OVERFLOW_COUNT].
 */
inline fun <T> safeOverflow(list: List<T>, func: (subList: List<T>) -> Unit) {
    if (list.size <= DaoConst.OVERFLOW_COUNT) {
        func(list)
        return
    }

    /** Start index INCLUDED for subList. */
    var startIndex = 0

    while (startIndex < list.size) {
        /** Last index EXCLUDED for subList. */
        val lastIndex = min(a = startIndex + DaoConst.OVERFLOW_COUNT, list.size)

        func(list.subList(startIndex, lastIndex))

        startIndex = lastIndex
    }
}