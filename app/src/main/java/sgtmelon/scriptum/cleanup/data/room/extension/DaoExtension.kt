@file:JvmName("DaoExtensionUtils")

package sgtmelon.scriptum.cleanup.data.room.extension

import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.infrastructure.database.annotation.DaoConst
import sgtmelon.scriptum.infrastructure.database.dao.RankDao
import sgtmelon.scriptum.infrastructure.database.dao.RollDao

//region Insert

@Deprecated("Use dataSource")
suspend fun RankDao.safeInsert(entity: RankEntity): Long? = insert(entity).checkSafe()

//endregion

//region Overflow

/**
 * [excludeIdList] - list of roll id's which need save in db when delete others.
 */
@Deprecated("Use dataSource")
suspend fun RollDao.safeDelete(noteId: Long, excludeIdList: List<Long>) {
    if (excludeIdList.size <= DaoConst.OVERFLOW_COUNT) {
        delete(noteId, excludeIdList)
    } else {
        /**
         * Need replace [excludeIdList] with list which contains only delete id's. Because if we will
         * use [excludeIdList] in circle it cause situation, when we delete needed items.
         */
        val deleteIdList = getList(noteId).mapNotNull { it.id }.toMutableList()
        deleteIdList.removeAll(excludeIdList)

        safeDelete(deleteIdList)
    }
}

/**
 * [idList] - list of roll id's which need delete from db.
 */
@Deprecated("Use dataSource")
suspend fun RollDao.safeDelete(idList: List<Long>) {
    if (idList.size <= DaoConst.OVERFLOW_COUNT) {
        delete(idList)
    } else {
        safeOverflow(idList) { delete(it) }
    }
}

@Deprecated("Use dataSource")
suspend fun RollDao.safeGet(noteIdList: List<Long>): List<RollEntity> {
    return if (noteIdList.size <= DaoConst.OVERFLOW_COUNT) {
        getList(noteIdList)
    } else {
        val resultList = mutableListOf<RollEntity>()
        safeOverflow(noteIdList) { resultList.addAll(getList(it)) }
        resultList
    }
}

//endregion