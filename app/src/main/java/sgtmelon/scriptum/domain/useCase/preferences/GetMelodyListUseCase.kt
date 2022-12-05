package sgtmelon.scriptum.domain.useCase.preferences

import sgtmelon.scriptum.data.dataSource.system.RingtoneDataSource
import sgtmelon.scriptum.infrastructure.model.item.MelodyItem

class GetMelodyListUseCase(private val dataSource: RingtoneDataSource) {

    private var list: List<MelodyItem>? = null

    suspend operator fun invoke(): List<MelodyItem> {
        return list ?: dataSource.getAlarmList().also { list = it }
    }

    fun reset() {
        list = null
    }
}