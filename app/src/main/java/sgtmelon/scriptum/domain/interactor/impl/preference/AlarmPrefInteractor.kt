package sgtmelon.scriptum.domain.interactor.impl.preference

import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.room.converter.type.IntConverter
import sgtmelon.scriptum.domain.interactor.callback.preference.IAlarmPrefInteractor
import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.presentation.provider.SummaryProvider
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.IAlarmPrefViewModel
import java.util.*

/**
 * Interactor for [IAlarmPrefViewModel].
 */
class AlarmPrefInteractor(
    private val summaryProvider: SummaryProvider,
    private val preferenceRepo: IPreferenceRepo,
    private val intConverter: IntConverter
) : IAlarmPrefInteractor {

    @Repeat override val repeat: Int get() = preferenceRepo.repeat

    override fun getRepeatSummary(): String? = summaryProvider.repeat.getOrNull(repeat)

    override fun updateRepeat(@Repeat value: Int): String? {
        preferenceRepo.repeat = value
        return getRepeatSummary()
    }


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
                        (", ").plus(summaryArray[i].toLowerCase(Locale.getDefault()))
                    })
                }
            }
        }.toString()
    }

    override fun updateSignal(valueArray: BooleanArray): String? {
        preferenceRepo.signal = intConverter.toInt(valueArray)
        return getSignalSummary(valueArray)
    }


    override val volume: Int get() = preferenceRepo.volume

    override fun getVolumeSummary(): String = summaryProvider.getVolume(volume)

    override fun updateVolume(value: Int): String {
        preferenceRepo.volume = value
        return getVolumeSummary()
    }

}