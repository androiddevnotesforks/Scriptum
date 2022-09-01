@file:JvmName("AlarmDaoSafeExt")
@file:Suppress("DEPRECATION")

package sgtmelon.scriptum.infrastructure.database.dao.safe

import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.infrastructure.database.dao.AlarmDao

/**
 * Help interfaces for communicate with [AlarmDao] via safe way, preventing errors.
 */

suspend fun AlarmDao.insertSafe(entity: AlarmEntity): Long? = insertForeignSafe { insert(entity) }

suspend fun AlarmDao.getListSafe(noteIdList: List<Long>): List<AlarmEntity> {
    return getSafeOverflowList(noteIdList) { getList(it) }
}

suspend fun AlarmDao.getCountSafe(noteIdList: List<Long>): Int {
    var count = 0
    safeOverflow(noteIdList) { count += getCount(it) }
    return count
}