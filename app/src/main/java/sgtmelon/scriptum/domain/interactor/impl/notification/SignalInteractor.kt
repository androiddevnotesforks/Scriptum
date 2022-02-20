package sgtmelon.scriptum.domain.interactor.impl.notification

import android.media.RingtoneManager
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.room.converter.type.IntConverter
import sgtmelon.scriptum.domain.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.domain.model.annotation.Signal
import sgtmelon.common.test.annotation.RunPrivate
import sgtmelon.scriptum.domain.model.item.MelodyItem
import sgtmelon.scriptum.domain.model.state.SignalState
import sgtmelon.scriptum.extension.validIndexOfFirst
import sgtmelon.scriptum.presentation.control.system.callback.IRingtoneControl

/**
 * Interactor for work with alarm signal.
 */
class SignalInteractor(
    private val ringtoneControl: IRingtoneControl,
    private val preferenceRepo: IPreferenceRepo,
    private val intConverter: IntConverter
) : ParentInteractor(),
    ISignalInteractor {

    @RunPrivate val typeList = listOf(RingtoneManager.TYPE_ALARM, RingtoneManager.TYPE_RINGTONE)


    override val typeCheck: BooleanArray
        get() = intConverter.toArray(preferenceRepo.signal, Signal.digitCount)

    override val state: SignalState? get() = SignalState[typeCheck]


    @RunPrivate var melodyList: List<MelodyItem>? = null

    override suspend fun getMelodyList(): List<MelodyItem> {
        return melodyList ?: ringtoneControl.getByType(typeList).also { melodyList = it }
    }

    override fun resetMelodyList() {
        melodyList = null
    }


    /**
     * If melody not init or was delete - set first melody uri from list.
     *
     * return null if [getMelodyList] is empty.
     */
    override suspend fun getMelodyUri(): String? {
        val list = getMelodyList()

        var value = preferenceRepo.melodyUri

        /**
         * Check uri exist.
         */
        if (value.isEmpty() || !list.any { it.uri == value }) {
            value = list.firstOrNull()?.uri ?: return null
            preferenceRepo.melodyUri = value
        }

        return value
    }

    /**
     * If melody not init or was delete - set first melody uri from list.
     *
     * return null if [getMelodyList] is empty.
     */
    override suspend fun setMelodyUri(title: String): String? {
        val list = getMelodyList()

        /**
         * Check uri exist.
         */
        val item = list.firstOrNull { it.title == title } ?: list.firstOrNull()
        preferenceRepo.melodyUri = item?.uri ?: return null

        return item.title
    }

    /**
     * Index of current melody uri inside [melodyList].
     *
     * return null if [getMelodyList] is empty.
     */
    override suspend fun getMelodyCheck(): Int? {
        val list = getMelodyList()
        val uri = getMelodyUri()

        return list.validIndexOfFirst { it.uri == uri }
    }

}