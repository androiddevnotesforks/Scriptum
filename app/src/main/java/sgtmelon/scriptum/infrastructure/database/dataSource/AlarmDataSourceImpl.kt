package sgtmelon.scriptum.infrastructure.database.dataSource

import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.infrastructure.database.dao.AlarmDao
import sgtmelon.scriptum.infrastructure.database.dao.safe.AlarmDaoSafe

class AlarmDataSourceImpl(private val dao: AlarmDao) : AlarmDaoSafe {


    suspend fun get(noteIdList: List<Long>): List<AlarmEntity> {
        return dao.getSafe(noteIdList)
    }
}