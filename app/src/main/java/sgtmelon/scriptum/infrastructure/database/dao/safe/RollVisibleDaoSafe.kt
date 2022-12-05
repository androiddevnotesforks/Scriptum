@file:JvmName("RollVisibleDaoSafeExt")
@file:Suppress("DEPRECATION")

package sgtmelon.scriptum.infrastructure.database.dao.safe

import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.infrastructure.database.dao.RollVisibleDao

/**
 * Help interfaces for communicate with [RollVisibleDao] via safe way, preventing errors.
 */

suspend fun RollVisibleDao.insertSafe(entity: RollVisibleEntity): Long? {
    return insertForeignSafe { insert(entity).checkConflictSafe() }
}

suspend fun RollVisibleDao.getListSafe(noteIdList: List<Long>): List<RollVisibleEntity> {
    return getSafeOverflowList(noteIdList) { getList(it) }
}