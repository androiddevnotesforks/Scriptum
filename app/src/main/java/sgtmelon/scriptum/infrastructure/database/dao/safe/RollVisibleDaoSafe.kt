@file:JvmName("RollVisibleDaoSafeExt")
@file:Suppress("DEPRECATION")

package sgtmelon.scriptum.infrastructure.database.dao.safe

import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.infrastructure.database.dao.RollVisibleDao
import sgtmelon.scriptum.infrastructure.utils.record

/**
 * Help interfaces for communicate with [RollVisibleDao] via safe way, preventing errors.
 */

suspend fun RollVisibleDao.insertSafe(entity: RollVisibleEntity): Long? {
    try {
        return insert(entity).checkSafe()
    } catch (e: Throwable) {
        e.record()
    }

    return null
}

suspend fun RollVisibleDao.getListSafe(noteIdList: List<Long>): List<RollVisibleEntity> {
    val list = mutableListOf<RollVisibleEntity>()
    safeOverflow(noteIdList) { list.addAll(getList(it)) }
    return list
}