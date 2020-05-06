package sgtmelon.scriptum.domain.interactor.impl.notification

import android.media.RingtoneManager
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.room.converter.type.IntConverter
import sgtmelon.scriptum.domain.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.domain.model.annotation.Signal
import sgtmelon.scriptum.domain.model.item.MelodyItem
import sgtmelon.scriptum.domain.model.state.SignalState
import sgtmelon.scriptum.presentation.control.system.callback.IRingtoneControl
import java.util.*

/**
 * Interactor for work with alarm signal.
 */
class SignalInteractor(
        private val ringtoneControl: IRingtoneControl,
        private val preferenceRepo: IPreferenceRepo
) : ParentInteractor(),
        ISignalInteractor {

    override val check: BooleanArray
        get() = IntConverter().toArray(preferenceRepo.signal, Signal.digitCount)

    override val state: SignalState get() = SignalState(check)


    /**
     * If melody not init or was delete - set first melody uri from list
     */
    override var melodyUri: String = preferenceRepo.melodyUri
        get() = melodyList.let {
            var value = preferenceRepo.melodyUri

            /**
             * Check uri exist
             */
            if (value.isEmpty() || !it.map { item -> item.uri }.contains(value)) {
                value = it.first().uri
                preferenceRepo.melodyUri = value
            }

            return value
        }
        set(value) = melodyList.map { it.uri }.let {
            /**
             * Check uri exist
             */
            field = if (it.contains(value)) {
                value
            } else {
                it.first()
            }

            preferenceRepo.melodyUri = field
        }

    /**
     * Index of melody uri in [melodyList]
     */
    override val melodyCheck: Int get() = melodyList.map { it.uri }.indexOf(melodyUri)

    /**
     * TODO #RELEASE add suspend
     */
    override val melodyList: List<MelodyItem>
        get() = ArrayList<MelodyItem>().apply {
            ringtoneControl.getByType(RingtoneManager.TYPE_ALARM)
            ringtoneControl.getByType(RingtoneManager.TYPE_RINGTONE)
        }.sortedBy { it.title }

}