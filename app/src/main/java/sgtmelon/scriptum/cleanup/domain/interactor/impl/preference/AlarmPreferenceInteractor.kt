package sgtmelon.scriptum.cleanup.domain.interactor.impl.preference

import androidx.annotation.IntRange
import java.util.Locale
import sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.IAlarmPreferenceInteractor
import sgtmelon.scriptum.cleanup.domain.model.annotation.Repeat
import sgtmelon.scriptum.cleanup.presentation.provider.SummaryProvider
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.IAlarmPreferenceViewModel
import sgtmelon.scriptum.infrastructure.converter.SignalConverter
import sgtmelon.scriptum.infrastructure.preferences.Preferences

/**
 * Interactor for [IAlarmPreferenceViewModel].
 */
class AlarmPreferenceInteractor(
    private val summaryProvider: SummaryProvider,
    private val preferences: Preferences,
    private val signalConverter: SignalConverter
) : IAlarmPreferenceInteractor {

    @Repeat override val repeat: Int get() = preferences.repeat

    override fun getRepeatSummary(): String? = summaryProvider.repeat.getOrNull(repeat)

    override fun updateRepeat(@Repeat value: Int): String? {
        preferences.repeat = value
        return getRepeatSummary()
    }


    // TODO move inside summaryProvider
    override fun getSignalSummary(valueArray: BooleanArray): String? {
        val summaryArray = summaryProvider.signal

        if (summaryArray.size != valueArray.size) return null

        return StringBuilder().apply {
            var firstAppend = true

            for ((i, bool) in valueArray.withIndex()) {
                if (bool) {
                    append(if (firstAppend) {
                        firstAppend = false
                        summaryArray[i]
                    } else {
                        (", ").plus(summaryArray[i].lowercase(Locale.getDefault()))
                    })
                }
            }
        }.toString()
    }

    override fun updateSignal(valueArray: BooleanArray): String? {
        preferences.signal = signalConverter.toString(valueArray)
        return getSignalSummary(valueArray)
    }


    override val volume: Int get() = preferences.volume

    override fun getVolumeSummary(): String = summaryProvider.getVolume(volume)

    override fun updateVolume(@IntRange(from = 10, to = 100) value: Int): String {
        preferences.volume = value
        return getVolumeSummary()
    }

}