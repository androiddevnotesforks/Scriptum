package sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference

import androidx.annotation.IntRange
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import sgtmelon.scriptum.infrastructure.model.item.MelodyItem
import sgtmelon.scriptum.infrastructure.model.key.Repeat
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.MelodyState

interface IAlarmPreferenceViewModel {

    val repeat: Repeat

    val repeatSummary: LiveData<String>

    fun updateRepeat(value: Int)

    val signalTypeCheck: BooleanArray

    val signalSummary: LiveData<String>

    fun updateSignal(value: BooleanArray)

    val volumePercent: Int

    val volumeSummary: LiveData<String>

    fun updateVolume(@IntRange(from = 10, to = 100) value: Int)


    val melodyState: LiveData<MelodyState>

    val selectMelodyData: Flow<Pair<Array<String>, Int>>

    fun getMelody(p: Int): Flow<MelodyItem>

    fun updateMelody(title: String): Flow<String>
}