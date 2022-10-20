package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference

import androidx.annotation.IntRange
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import sgtmelon.extensions.launchBack
import sgtmelon.extensions.onBack
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.AlarmPreferenceViewModel
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.preferences.GetMelodyListUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSignalSummaryUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSummaryUseCase
import sgtmelon.scriptum.infrastructure.model.item.MelodyItem
import sgtmelon.scriptum.infrastructure.model.key.Repeat
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.MelodySummaryState

class AlarmPreferenceViewModelImpl(
    private val preferencesRepo: PreferencesRepo,
    private val getRepeatSummary: GetSummaryUseCase,
    private val getSignalSummary: GetSignalSummaryUseCase,
    private val getVolumeSummary: GetSummaryUseCase,
    private val getMelodyList: GetMelodyListUseCase
) : ViewModel(),
    AlarmPreferenceViewModel {

    override val repeat: Repeat get() = preferencesRepo.repeat

    override val repeatSummary = MutableLiveData(getRepeatSummary())

    override fun updateRepeat(value: Int) {
        repeatSummary.postValue(getRepeatSummary(value))
    }

    override val signalTypeCheck: BooleanArray get() = preferencesRepo.signalTypeCheck

    override val signalSummary = MutableLiveData(getSignalSummary())

    override fun updateSignal(value: BooleanArray) {
        signalSummary.postValue(getSignalSummary(value))

        viewModelScope.launchBack {
            if (getMelodyList().isEmpty()) {
                melodyGroupEnabled.postValue(false)
                melodySummaryState.postValue(MelodySummaryState.Empty)
            } else {
                melodyGroupEnabled.postValue(preferencesRepo.signalState.isMelody)
            }
        }
    }

    override val volumePercent: Int get() = preferencesRepo.volumePercent

    override val volumeSummary = MutableLiveData(getVolumeSummary())

    override fun updateVolume(@IntRange(from = 10, to = 100) value: Int) {
        volumeSummary.postValue(getVolumeSummary(value))
    }

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
        get() = flow {
            val list = getMelodyList()
            val titleArray = list.map { it.title }.toTypedArray()
            val check = preferencesRepo.getMelodyCheck(list)

            if (titleArray.isEmpty() || check == null) {
                melodyGroupEnabled.postValue(false)
                melodySummaryState.postValue(MelodySummaryState.Empty)
            } else {
                emit(value = titleArray to check)
            }
        }.onBack()

    override fun getMelody(p: Int): Flow<MelodyItem> = flow {
        getMelodyList().getOrNull(p)?.let { emit(it) }
    }.onBack()

    /**
     * Return: success set melody or chosen another one by app.
     */
    override fun updateMelody(title: String): Flow<Boolean> = flow {
        val resultTitle = preferencesRepo.setMelodyUri(getMelodyList(), title)
        if (resultTitle != null) {
            melodyGroupEnabled.postValue(preferencesRepo.signalState.isMelody)
            melodySummaryState.postValue(MelodySummaryState.Finish(resultTitle))
            emit(value = title == resultTitle)
        } else {
            melodyGroupEnabled.postValue(false)
            melodySummaryState.postValue(MelodySummaryState.Empty)
        }
    }.onBack()
}