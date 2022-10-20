package sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference

import androidx.annotation.IntRange
import kotlinx.coroutines.flow.Flow
import sgtmelon.scriptum.infrastructure.model.item.MelodyItem
import sgtmelon.scriptum.infrastructure.model.key.Repeat
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.MelodyState
import sgtmelon.scriptum.infrastructure.utils.SingleShootLiveData

interface IAlarmPreferenceViewModel {

    val repeat: Repeat

    val repeatSummary: SingleShootLiveData<String>

    fun updateRepeat(value: Int)

    val signalTypeCheck: BooleanArray

    val signalSummary: SingleShootLiveData<String>

    fun updateSignal(value: BooleanArray)

    val volumePercent: Int

    val volumeSummary: SingleShootLiveData<String>

    fun updateVolume(@IntRange(from = 10, to = 100) value: Int)


    val melodyState: SingleShootLiveData<MelodyState>

    val selectMelodyData: Flow<Pair<Array<String>, Int>>

    fun getMelody(p: Int): Flow<MelodyItem>

    fun updateMelody(title: String): Flow<String>
}