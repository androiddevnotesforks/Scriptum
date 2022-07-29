package sgtmelon.scriptum.domain.useCase.preferences

import sgtmelon.scriptum.cleanup.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.infrastructure.model.MelodyItem
import sgtmelon.scriptum.infrastructure.provider.RingtoneProvider

/**
 * Interactor for work with alarm signal.
 */
class GetMelodyListUseCaseImpl(
    private val ringtoneProvider: RingtoneProvider
) : ParentInteractor(),
    GetMelodyListUseCase {

    private var list: List<MelodyItem>? = null

    override suspend fun invoke(): List<MelodyItem> {
        return list ?: ringtoneProvider.getAlarmList().also { list = it }
    }

    override fun reset() {
        list = null
    }
}