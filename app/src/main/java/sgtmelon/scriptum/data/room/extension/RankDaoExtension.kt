package sgtmelon.scriptum.data.room.extension

import sgtmelon.scriptum.data.room.RoomDb
import sgtmelon.scriptum.data.room.dao.IRankDao
import sgtmelon.scriptum.data.room.entity.RankEntity

suspend fun IRankDao.safeInsert(entity: RankEntity): Long? {
    return insert(entity).takeIf { it != RoomDb.UNIQUE_ERROR_ID }
}