package sgtmelon.scriptum.data.dataSource.system

import sgtmelon.scriptum.infrastructure.model.item.MelodyItem

interface RingtoneDataSource {

    suspend fun getAlarmList(): List<MelodyItem>
}