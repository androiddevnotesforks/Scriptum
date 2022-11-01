package sgtmelon.scriptum.infrastructure.database.dao.safe

import kotlin.math.min
import sgtmelon.scriptum.infrastructure.database.model.DaoConst
import sgtmelon.scriptum.infrastructure.database.model.DaoDeprecated
import sgtmelon.scriptum.infrastructure.model.exception.dao.DaoConflictIdException
import sgtmelon.scriptum.infrastructure.model.exception.dao.DaoForeignException
import sgtmelon.scriptum.infrastructure.utils.record

/**
 * See description of error in:
 * - [DaoConflictIdException]
 * - [DaoConst.UNIQUE_ERROR_ID]
 * - [DaoDeprecated.INSERT_IGNORE]
 */
fun Long.checkConflictSafe(): Long? {
    val id = this.takeIf { it != DaoConst.UNIQUE_ERROR_ID }

    if (id == null) {
        DaoConflictIdException().record()
    }

    return id
}

/**
 * See description of error in:
 * - [DaoForeignException]
 * - [DaoDeprecated.INSERT_FOREIGN_KEY]
 */
inline fun insertForeignSafe(func: () -> Long?): Long? {
    try {
        return func()
    } catch (e: Throwable) {
        DaoForeignException(e).record()
    }

    return null
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
        val subList = list.subList(startIndex, lastIndex)

        func(subList)
        startIndex = lastIndex
    }
}

inline fun <T, E> getSafeOverflowList(
    list: List<T>,
    func: (subList: List<T>) -> List<E>
): List<E> {
    val resultList = mutableListOf<E>()
    safeOverflow(list) { resultList.addAll(func(it)) }
    return resultList
}