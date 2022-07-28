package sgtmelon.scriptum.cleanup.domain.interactor.impl.notification

import sgtmelon.common.test.annotation.RunPrivate
import sgtmelon.scriptum.cleanup.domain.interactor.callback.notification.GetMelodyListUseCase
import sgtmelon.scriptum.cleanup.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.infrastructure.model.MelodyItem
import sgtmelon.scriptum.infrastructure.provider.RingtoneProvider

/**
 * Interactor for work with alarm signal.
 */
// TODO rename it and use like useCase for providing singletone melodyList
class GetMelodyListUseCaseImpl(
    private val ringtoneProvider: RingtoneProvider
) : ParentInteractor(),
    GetMelodyListUseCase {

    // TODO move into useCase (getMelodyList)
    @RunPrivate var melodyList: List<MelodyItem>? = null

    override suspend fun getMelodyList(): List<MelodyItem> {
        return melodyList ?: ringtoneProvider.getAlarmList().also { melodyList = it }
    }

    override fun resetMelodyList() {
        melodyList = null
    }
}