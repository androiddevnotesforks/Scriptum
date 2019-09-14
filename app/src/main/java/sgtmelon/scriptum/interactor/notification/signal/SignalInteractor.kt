package sgtmelon.scriptum.interactor.notification.signal

import android.content.Context
import android.media.RingtoneManager
import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.model.annotation.Signal
import sgtmelon.scriptum.model.item.MelodyItem
import sgtmelon.scriptum.model.state.SignalState
import sgtmelon.scriptum.room.converter.IntConverter
import java.util.*

/**
 * Interactor for work with alarm signal
 */
class SignalInteractor(context: Context) : ParentInteractor(context), ISignalInteractor {

    private val ringtoneManager = RingtoneManager(context)

    override val signalCheck: BooleanArray
        get() = IntConverter().toArray(iPreferenceRepo.signal, Signal.digitCount)

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
    override var melodyUri: String = iPreferenceRepo.melodyUri
        get() = melodyList.let {
            var value = iPreferenceRepo.melodyUri

            /**
             * Check uri exist
             */
            if (value.isEmpty() || !it.map { item -> item.uri }.contains(value)) {
                value = it.first().uri
                iPreferenceRepo.melodyUri = value
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

            iPreferenceRepo.melodyUri = field
        }

    /**
     * Index of melody uri in [melodyList]
     */
    override val melodyCheck: Int get() = melodyList.map { it.uri }.indexOf(melodyUri)

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