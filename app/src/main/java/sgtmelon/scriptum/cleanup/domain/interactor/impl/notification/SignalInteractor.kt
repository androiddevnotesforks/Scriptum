package sgtmelon.scriptum.cleanup.domain.interactor.impl.notification

import android.media.RingtoneManager
import sgtmelon.common.test.annotation.RunPrivate
import sgtmelon.scriptum.cleanup.domain.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.cleanup.extension.validIndexOfFirst
import sgtmelon.scriptum.cleanup.presentation.control.system.callback.IRingtoneControl
import sgtmelon.scriptum.infrastructure.converter.SignalConverter
import sgtmelon.scriptum.infrastructure.model.MelodyItem
import sgtmelon.scriptum.infrastructure.model.state.SignalState
import sgtmelon.scriptum.infrastructure.preferences.Preferences

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


    // TODO rename: signalTypeCheck
    // TODO move into repo
    override val typeCheck: BooleanArray get() = signalConverter.toArray(preferences.signal)

    // TODO rename: signalState
    // TODO move into repo
    override val state: SignalState? get() = signalConverter.toState(preferences.signal)


    // TODO move into useCase (getMelodyList)
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
    // TODO move into repo
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
    // TODO move into repo
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
    // TODO move into repo
    override suspend fun getMelodyCheck(): Int? {
        val list = getMelodyList()
        val uri = getMelodyUri()

        return list.validIndexOfFirst { it.uri == uri }
    }

}