package sgtmelon.scriptum.cleanup.domain.interactor.impl.notification

import android.media.RingtoneManager
import sgtmelon.common.test.annotation.RunPrivate
import sgtmelon.scriptum.cleanup.domain.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.cleanup.domain.model.item.MelodyItem
import sgtmelon.scriptum.cleanup.extension.validIndexOfFirst
import sgtmelon.scriptum.cleanup.presentation.control.system.callback.IRingtoneControl
import sgtmelon.scriptum.infrastructure.preferences.Preferences
import sgtmelon.scriptum.infrastructure.preferences.converter.SignalConverter
import sgtmelon.scriptum.infrastructure.preferences.model.state.SignalState

/**
 * Interactor for work with alarm signal.
 */
// TODO #PREF move staff from here into preferenceRepo
class SignalInteractor(
    private val ringtoneControl: IRingtoneControl,
    private val preferences: Preferences,
    private val signalConverter: SignalConverter
) : ParentInteractor(),
    ISignalInteractor {

    @RunPrivate val typeList = listOf(RingtoneManager.TYPE_ALARM, RingtoneManager.TYPE_RINGTONE)


    override val typeCheck: BooleanArray get() = signalConverter.toArray(preferences.signal)

    override val state: SignalState? get() = signalConverter.toState(preferences.signal)


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

        var value = preferences.melodyUri

        /**
         * Check uri exist.
         */
        if (value.isEmpty() || !list.any { it.uri == value }) {
            value = list.firstOrNull()?.uri ?: return null
            preferences.melodyUri = value
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
        preferences.melodyUri = item?.uri ?: return null

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