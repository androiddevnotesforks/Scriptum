package sgtmelon.scriptum.data.dataSource.system

import sgtmelon.scriptum.infrastructure.model.MelodyItem

/**
 * Interface for [RingtoneDataSourceImpl].
 */
interface RingtoneDataSource {

    suspend fun getAlarmList(): List<MelodyItem>
}