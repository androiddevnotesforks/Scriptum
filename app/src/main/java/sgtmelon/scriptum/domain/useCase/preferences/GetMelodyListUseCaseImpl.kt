package sgtmelon.scriptum.domain.useCase.preferences

import sgtmelon.scriptum.cleanup.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.data.dataSource.system.RingtoneDataSource
import sgtmelon.scriptum.infrastructure.model.MelodyItem

/**
 * Interactor for work with alarm signal.
 */
class GetMelodyListUseCaseImpl(
    private val dataSource: RingtoneDataSource
) : ParentInteractor(),
    GetMelodyListUseCase {

    private var list: List<MelodyItem>? = null

    override suspend fun invoke(): List<MelodyItem> {
        return list ?: dataSource.getAlarmList().also { list = it }
    }

    override fun reset() {
        list = null
    }
}