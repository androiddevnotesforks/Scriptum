package sgtmelon.scriptum.domain.interactor.impl.notification

import android.content.Context
import android.media.RingtoneManager
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.room.converter.type.IntConverter
import sgtmelon.scriptum.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.domain.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.domain.model.annotation.Signal
import sgtmelon.scriptum.domain.model.item.MelodyItem
import sgtmelon.scriptum.domain.model.state.SignalState
import java.util.*

/**
 * Interactor for work with alarm signal.
 */
class SignalInteractor(
        private val context: Context,
        private val preferenceRepo: IPreferenceRepo
) : ParentInteractor(),
        ISignalInteractor {

    private val ringtoneManager get() = RingtoneManager(context)

    override val signalCheck: BooleanArray
        get() = IntConverter().toArray(preferenceRepo.signal, Signal.digitCount)

    override val signalState: SignalState get() = SignalState(signalCheck)

    override fun getSignalSummary(summaryArray: Array<String>) = StringBuilder().apply {
        val array = signalCheck

        if (summaryArray.size < array.size) return@apply

        var firstAppend = true
        array.forEachIndexed { i, bool ->
            if (bool) {
                append(if (firstAppend) {
                    firstAppend = false
                    summaryArray[i]
                } else {
                    (", ").plus(summaryArray[i].toLowerCase(Locale.getDefault()))
                })
            }
        }
    }.toString()

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
            /**
             * Func which fill list with all [MelodyItem] for current [RingtoneManager] type
             */
            val fillListByType = { type: Int ->
                ringtoneManager.apply {
                    setType(type)
                    cursor.apply {
                        while (moveToNext()) {
                            val title = getString(RingtoneManager.TITLE_COLUMN_INDEX) ?: continue
                            val uri = getString(RingtoneManager.URI_COLUMN_INDEX) ?: continue
                            val id = getString(RingtoneManager.ID_COLUMN_INDEX) ?: continue

                            add(MelodyItem(title, uri, id))
                        }
                    }.close()
                }
            }

            fillListByType(RingtoneManager.TYPE_ALARM)
            fillListByType(RingtoneManager.TYPE_RINGTONE)
        }.sortedBy { it.title }

}