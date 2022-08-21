@file:JvmName("RollDaoSafeExt")
@file:Suppress("DEPRECATION")

package sgtmelon.scriptum.infrastructure.database.dao.safe

import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.infrastructure.database.annotation.DaoConst
import sgtmelon.scriptum.infrastructure.database.dao.RollDao
import sgtmelon.scriptum.infrastructure.utils.record


/**
 * Help interfaces for communicate with [RollDao] via safe way, preventing errors.
 */

suspend fun RollDao.insertSafe(entity: RollEntity): Long? {
    try {
        return insert(entity).checkSafe()
    } catch (e: Throwable) {
        e.record()
    }

    return null
}

suspend fun RollDao.deleteSafe(noteId: Long, excludeIdList: List<Long>) {
    if (excludeIdList.size <= DaoConst.OVERFLOW_COUNT) {
        delete(noteId, excludeIdList)
    } else {
        /**
         * Need replace [excludeIdList] with list which contains only delete id's. Because if we will
         * use [excludeIdList] in circle it cause situation, when we delete needed items.
         */
        val includeIdList = getIdList(noteId).toMutableList()
        includeIdList.removeAll(excludeIdList)
        safeOverflow(includeIdList) { delete(it) }
    }
}

suspend fun RollDao.getListSafe(noteIdList: List<Long>): List<RollEntity> {
    return getSafeOverflowList(noteIdList) { getList(it) }
}