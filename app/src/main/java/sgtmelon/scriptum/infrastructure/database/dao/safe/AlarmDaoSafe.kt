@file:JvmName("AlarmDaoSafeExt")

package sgtmelon.scriptum.infrastructure.database.dao.safe

import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.infrastructure.database.dao.AlarmDao

/**
 * Help interfaces for communicate with [AlarmDao] via safe way, preventing errors.
 */
suspend fun AlarmDao.getSafe(noteIdList: List<Long>): List<AlarmEntity> {
    val list = mutableListOf<AlarmEntity>()
    safeOverflow(noteIdList) { list.addAll(get(noteIdList)) }
    return list
}

suspend fun AlarmDao.getCountSafe(noteIdList: List<Long>): Int {
    var count = 0
    safeOverflow(noteIdList) { count += getCount(it) }
    return count
}