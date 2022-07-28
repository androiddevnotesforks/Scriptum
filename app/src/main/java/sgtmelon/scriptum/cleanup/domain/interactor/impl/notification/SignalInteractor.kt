package sgtmelon.scriptum.cleanup.domain.interactor.impl.notification

import android.media.RingtoneManager
import sgtmelon.common.test.annotation.RunPrivate
import sgtmelon.scriptum.cleanup.domain.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.cleanup.presentation.control.system.callback.IRingtoneControl
import sgtmelon.scriptum.infrastructure.model.MelodyItem

/**
 * Interactor for work with alarm signal.
 */
// TODO rename it and use like useCase for providing singletone melodyList
class SignalInteractor(
    private val ringtoneControl: IRingtoneControl
) : ParentInteractor(),
    ISignalInteractor {

    // TODO move from here android. - package (move it inside ringtoneControl)
    @RunPrivate val typeList = listOf(RingtoneManager.TYPE_ALARM, RingtoneManager.TYPE_RINGTONE)

    // TODO move into useCase (getMelodyList)
    @RunPrivate var melodyList: List<MelodyItem>? = null

    override suspend fun getMelodyList(): List<MelodyItem> {
        return melodyList ?: ringtoneControl.getByType(typeList).also { melodyList = it }
    }

    override fun resetMelodyList() {
        melodyList = null
    }
}