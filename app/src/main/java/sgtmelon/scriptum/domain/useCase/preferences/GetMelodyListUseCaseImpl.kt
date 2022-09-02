package sgtmelon.scriptum.domain.useCase.preferences

import sgtmelon.scriptum.data.dataSource.system.RingtoneDataSource
import sgtmelon.scriptum.infrastructure.model.item.MelodyItem

class GetMelodyListUseCaseImpl(
    private val dataSource: RingtoneDataSource
) : GetMelodyListUseCase {

    private var list: List<MelodyItem>? = null

    override suspend operator fun invoke(): List<MelodyItem> {
        return list ?: dataSource.getAlarmList().also { list = it }
    }

    override fun reset() {
        list = null
    }
}