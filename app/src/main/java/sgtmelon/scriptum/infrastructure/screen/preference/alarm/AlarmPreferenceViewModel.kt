package sgtmelon.scriptum.infrastructure.screen.preference.alarm

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import sgtmelon.scriptum.infrastructure.model.item.MelodyItem
import sgtmelon.scriptum.infrastructure.model.key.preference.Repeat
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.state.MelodySummaryState
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.state.UpdateMelodyState

interface AlarmPreferenceViewModel {

    val signalTypeCheck: BooleanArray

    val signalSummary: LiveData<String>

    fun updateSignal(value: BooleanArray)

    val repeat: Repeat

    val repeatSummary: LiveData<String>

    fun updateRepeat(value: Int)

    val volumePercent: Int

    val volumeSummary: LiveData<String>

    fun updateVolume(value: Int)

    val melodySummaryState: LiveData<MelodySummaryState>

    val melodyGroupEnabled: LiveData<Boolean>

    val selectMelodyData: Flow<Pair<Array<String>, Int>>

    fun getMelody(p: Int): Flow<MelodyItem>

    fun updateMelody(title: String): Flow<UpdateMelodyState>
}