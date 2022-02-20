@file:JvmName("DaoHelpExtensionUtils")

package sgtmelon.scriptum.data.room.extension

import sgtmelon.scriptum.data.room.RoomDb
import sgtmelon.common.test.annotation.RunPrivate

@RunPrivate fun Long.checkSafe(): Long? = this.takeIf { it != RoomDb.UNIQUE_ERROR_ID }

@RunPrivate inline fun <T> safeOverflow(list: List<T>, func: (subList: List<T>) -> Unit) {
    /**
     * Start index include for subList.
     */
    var startIndex = 0

    while (startIndex < list.size) {
        /**
         * Last index exclude for subList.
         */
        val lastIndex = if (startIndex + RoomDb.OVERFLOW_COUNT < list.size) {
            startIndex + RoomDb.OVERFLOW_COUNT
        } else {
            list.size
        }

        func(list.subList(startIndex, lastIndex))

        startIndex = lastIndex
    }
}