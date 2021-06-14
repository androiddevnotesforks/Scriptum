@file:Suppress("DEPRECATION")
@file:JvmName("DaoExtensionUtils")

package sgtmelon.scriptum.data.room.extension

import sgtmelon.scriptum.data.room.RoomDb
import sgtmelon.scriptum.data.room.dao.IRankDao
import sgtmelon.scriptum.data.room.dao.IRollDao
import sgtmelon.scriptum.data.room.entity.RankEntity
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate

//region Insert

suspend fun IRankDao.safeInsert(entity: RankEntity): Long? = insert(entity).checkSafe()

@RunPrivate fun Long.checkSafe(): Long? = this.takeIf { it != RoomDb.UNIQUE_ERROR_ID }

//endregion

//region Overflow

suspend fun IRollDao.safeDelete(noteId: Long, idSaveList: List<Long>) {
    if (idSaveList.size <= RoomDb.OVERFLOW_COUNT) {
        delete(noteId, idSaveList)
    } else {
        safeOverflow(idSaveList) { delete(noteId, it) }
    }
}

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

//endregion