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

//endregion

//region Overflow

suspend fun IRollDao.safeDelete(noteId: Long, idSaveList: List<Long>) {
    if (idSaveList.size <= RoomDb.OVERFLOW_COUNT) {
        delete(noteId, idSaveList)
    } else {
        safeOverflow(idSaveList) { delete(noteId, it) }
    }
}

//endregion