@file:JvmName("DaoHelpExtensionUtils")

package sgtmelon.scriptum.cleanup.data.room.extension

import sgtmelon.common.test.annotation.RunPrivate
import sgtmelon.scriptum.infrastructure.database.annotation.DaoConst

@Deprecated("Use ParentSafeDao")
@RunPrivate fun Long.checkSafe(): Long? = this.takeIf { it != DaoConst.UNIQUE_ERROR_ID }

@Deprecated("Use ParentSafeDao")
@RunPrivate inline fun <T> safeOverflow(list: List<T>, func: (subList: List<T>) -> Unit) {
    /**
     * Start index include for subList.
     */
    var startIndex = 0

    while (startIndex < list.size) {
        /**
         * Last index exclude for subList.
         */
        val lastIndex = if (startIndex + DaoConst.OVERFLOW_COUNT < list.size) {
            startIndex + DaoConst.OVERFLOW_COUNT
        } else {
            list.size
        }

        func(list.subList(startIndex, lastIndex))

        startIndex = lastIndex
    }
}