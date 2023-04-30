package sgtmelon.scriptum.infrastructure.screen.preference.alarm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import sgtmelon.extensions.flowBack
import sgtmelon.extensions.launchBack
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.preferences.GetMelodyListUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSignalSummaryUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSummaryUseCase
import sgtmelon.scriptum.infrastructure.model.item.MelodyItem
import sgtmelon.scriptum.infrastructure.model.key.preference.Repeat
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.state.MelodySummaryState
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.state.UpdateMelodyState

class AlarmPreferenceViewModelImpl(
    private val preferencesRepo: PreferencesRepo,
    private val getSignalSummary: GetSignalSummaryUseCase,
    private val getRepeatSummary: GetSummaryUseCase,
    private val getVolumeSummary: GetSummaryUseCase,
    private val getMelodyList: GetMelodyListUseCase
) : ViewModel(),
    AlarmPreferenceViewModel {

    override val signalTypeCheck: BooleanArray get() = preferencesRepo.signalTypeCheck

    override val signalSummary = MutableLiveData(getSignalSummary())

    override fun updateSignal(value: BooleanArray) {
        signalSummary.postValue(getSignalSummary(value))

        viewModelScope.launchBack {
            if (getMelodyList().isNotEmpty()) {
                melodyGroupEnabled.postValue(preferencesRepo.signalState.isMelody)
            } else {
                melodyGroupEnabled.postValue(false)
                melodySummaryState.postValue(MelodySummaryState.Empty)
            }
        }
    }

    override val repeat: Repeat get() = preferencesRepo.repeat

    override val repeatSummary = MutableLiveData(getRepeatSummary())

    override fun updateRepeat(value: Int) = repeatSummary.postValue(getRepeatSummary(value))

    override val volumePercent: Int get() = preferencesRepo.volumePercent

    override val volumeSummary = MutableLiveData(getVolumeSummary())

    override fun updateVolume(value: Int) = volumeSummary.postValue(getVolumeSummary(value))

    override val melodySummaryState by lazy {
        MutableLiveData<MelodySummaryState>()
            .also { viewModelScope.launchBack { loadMelody() } }
    }

    private suspend fun loadMelody() {
        melodyGroupEnabled.postValue(false)
        melodySummaryState.postValue(MelodySummaryState.Loading)

        val list = getMelodyList()
        val check = preferencesRepo.getMelodyCheck(list)
        val item = if (check != null) list.getOrNull(check) else null

        if (item != null) {
            melodyGroupEnabled.postValue(preferencesRepo.signalState.isMelody)
            melodySummaryState.postValue(MelodySummaryState.Finish(item.title))
        } else {
            melodySummaryState.postValue(MelodySummaryState.Empty)
        }
    }

    override val melodyGroupEnabled by lazy { MutableLiveData<Boolean>() }

    /**
     * Information about melodies naming and chosen current position.
     */
    override val selectMelodyData: Flow<Pair<Array<String>, Int>>
        get() = flowBack {
            val list = getMelodyList()
            val titleArray = list.map { it.title }.toTypedArray()
            val check = preferencesRepo.getMelodyCheck(list)

            if (titleArray.isNotEmpty() && check != null) {
                emit(value = titleArray to check)
            } else {
                melodyGroupEnabled.postValue(false)
                melodySummaryState.postValue(MelodySummaryState.Empty)
            }
        }

    override fun getMelody(p: Int): Flow<MelodyItem> = flowBack {
        getMelodyList().getOrNull(p)?.let { emit(it) }
    }

    /**
     * Return: success set melody or chosen another one by app.
     */
    override fun updateMelody(title: String): Flow<UpdateMelodyState> = flowBack {
        val resultTitle = preferencesRepo.setMelodyUri(getMelodyList(), title)
        if (resultTitle != null) {
            melodyGroupEnabled.postValue(preferencesRepo.signalState.isMelody)
            melodySummaryState.postValue(MelodySummaryState.Finish(resultTitle))

            if (title != resultTitle) {
                emit(UpdateMelodyState.AutoChoose)
            }
        } else {
            melodyGroupEnabled.postValue(false)
            melodySummaryState.postValue(MelodySummaryState.Empty)
        }
    }
}