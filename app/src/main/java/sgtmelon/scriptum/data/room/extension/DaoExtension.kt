@file:Suppress("DEPRECATION")
@file:JvmName("DaoExtensionUtils")

package sgtmelon.scriptum.data.room.extension

import sgtmelon.scriptum.data.room.RoomDb
import sgtmelon.scriptum.data.room.dao.IRankDao
import sgtmelon.scriptum.data.room.dao.IRollDao
import sgtmelon.scriptum.data.room.entity.RankEntity

//region Insert

suspend fun IRankDao.safeInsert(entity: RankEntity): Long? = insert(entity).checkSafe()

//endregion

//region Overflow

/**
 * [saveList] - list of roll id's which need save in db when delete others.
 */
suspend fun IRollDao.safeDelete(noteId: Long, saveList: List<Long>) {
    if (saveList.size <= RoomDb.OVERFLOW_COUNT) {
        delete(noteId, saveList)
    } else {
        /**
         * Need replace [saveList] with list which contains only delete id's. Because if we will
         * use [saveList] in circle it cause situation, when delete needed items.
         */
        val deleteList = get(noteId).mapNotNull { it.id }.toMutableList()
        deleteList.removeAll(saveList)

        safeDeleteByList(noteId, deleteList)
    }
}

/**
 * [deleteList] - list of roll id's which need delete from db.
 */
suspend fun IRollDao.safeDeleteByList(noteId: Long, deleteList: List<Long>) {
    if (deleteList.size <= RoomDb.OVERFLOW_COUNT) {
        deleteByList(noteId, deleteList)
    } else {
        safeOverflow(deleteList) { deleteByList(noteId, it) }
    }
}

//endregion