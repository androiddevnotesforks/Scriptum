@file:JvmName("NoteDaoSafeExt")
@file:Suppress("DEPRECATION")

package sgtmelon.scriptum.infrastructure.database.dao.safe

import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.infrastructure.database.dao.NoteDao

/**
 * Help interfaces for communicate with [NoteDao] via safe way, preventing errors.
 */

suspend fun NoteDao.insertSafe(entity: NoteEntity): Long? = insert(entity).checkConflictSafe()

suspend fun NoteDao.getBindCountSafe(idList: List<Long>): Int {
    var count = 0
    safeOverflow(idList) { count += getBindCount(it) }
    return count
}

suspend fun NoteDao.getListSafe(idList: List<Long>): List<NoteEntity> {
    return getSafeOverflowList(idList) { getList(it) }
}