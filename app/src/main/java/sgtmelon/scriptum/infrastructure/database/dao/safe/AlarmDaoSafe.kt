@file:JvmName("AlarmDaoSafeExt")
@file:Suppress("DEPRECATION")

package sgtmelon.scriptum.infrastructure.database.dao.safe

import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.infrastructure.database.dao.AlarmDao
import sgtmelon.scriptum.infrastructure.utils.record

/**
 * Help interfaces for communicate with [AlarmDao] via safe way, preventing errors.
 */

suspend fun AlarmDao.insertSafe(entity: AlarmEntity): Long? {
    try {
        return insert(entity)
    } catch (e: Throwable) {
        e.record()
    }

    return null
}

suspend fun AlarmDao.getListSafe(noteIdList: List<Long>): List<AlarmEntity> {
    val list = mutableListOf<AlarmEntity>()
    safeOverflow(noteIdList) { list.addAll(getList(it)) }
    return list
}

suspend fun AlarmDao.getCountSafe(noteIdList: List<Long>): Int {
    var count = 0
    safeOverflow(noteIdList) { count += getCount(it) }
    return count
}