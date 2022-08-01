@file:JvmName("DaoExtensionUtils")

package sgtmelon.scriptum.cleanup.data.room.extension

import sgtmelon.scriptum.cleanup.data.room.dao.IRankDao
import sgtmelon.scriptum.cleanup.data.room.dao.IRollDao
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.infrastructure.database.annotation.DaoConst

//region Insert

suspend fun IRankDao.safeInsert(entity: RankEntity): Long? = insert(entity).checkSafe()

//endregion

//region Overflow

/**
 * [saveList] - list of roll id's which need save in db when delete others.
 */
suspend fun IRollDao.safeDelete(noteId: Long, saveList: List<Long>) {
    if (saveList.size <= DaoConst.OVERFLOW_COUNT) {
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
    if (deleteList.size <= DaoConst.OVERFLOW_COUNT) {
        deleteByList(noteId, deleteList)
    } else {
        safeOverflow(deleteList) { deleteByList(noteId, it) }
    }
}

suspend fun IRollDao.safeGet(noteIdList: List<Long>): List<RollEntity> {
    return if (noteIdList.size <= DaoConst.OVERFLOW_COUNT) {
        get(noteIdList)
    } else {
        val resultList = mutableListOf<RollEntity>()
        safeOverflow(noteIdList) { resultList.addAll(get(it)) }
        resultList
    }
}

//endregion