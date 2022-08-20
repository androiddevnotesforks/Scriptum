@file:JvmName("RankDaoSafeExt")
@file:Suppress("DEPRECATION")

package sgtmelon.scriptum.infrastructure.database.dao.safe

import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.infrastructure.database.dao.RankDao

/**
 * Help interfaces for communicate with [RankDao] via safe way, preventing errors.
 */

suspend fun RankDao.insertSafe(entity: RankEntity): Long? = insert(entity).checkSafe()