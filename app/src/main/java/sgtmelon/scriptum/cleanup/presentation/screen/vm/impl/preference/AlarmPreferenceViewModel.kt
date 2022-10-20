package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference

import android.util.Log
import androidx.annotation.IntRange
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import sgtmelon.extensions.launchBack
import sgtmelon.extensions.onBack
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.IAlarmPreferenceViewModel
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.preferences.GetMelodyListUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSignalSummaryUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSummaryUseCase
import sgtmelon.scriptum.infrastructure.model.item.MelodyItem
import sgtmelon.scriptum.infrastructure.model.key.Repeat
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.MelodyState

class AlarmPreferenceViewModel(
    lifecycle: Lifecycle,
    private val preferencesRepo: PreferencesRepo,
    private val getRepeatSummary: GetSummaryUseCase,
    private val getSignalSummary: GetSignalSummaryUseCase,
    private val getVolumeSummary: GetSummaryUseCase,
    private val getMelodyList: GetMelodyListUseCase
) : ViewModel(),
    IAlarmPreferenceViewModel,
    DefaultLifecycleObserver {

    init {
        lifecycle.addObserver(this)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        viewModelScope.launchBack { loadMelody() }
    }

    /**
     * Need reset list, because user can change permission or delete some files or
     * remove sd card. It calls even after permission dialog.
     */
    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        getMelodyList.reset()
    }

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
            val state = preferencesRepo.signalState

            melodyState.postValue(MelodyState.Enabled(state.isMelody))

            if (state.isMelody && getMelodyList().isEmpty()) {
                melodyState.postValue(MelodyState.Empty)
            }
        }
    }

    override val volumePercent: Int get() = preferencesRepo.volumePercent

    override val volumeSummary = MutableLiveData(getVolumeSummary())

    override fun updateVolume(@IntRange(from = 10, to = 100) value: Int) {
        volumeSummary.postValue(getVolumeSummary(value))
    }

    override val melodyState = MutableLiveData<MelodyState>()

    private suspend fun loadMelody() {
        Log.i("HERE", "loadMelody: 0")
        val state = preferencesRepo.signalState

        Log.i("HERE", "loadMelody: 1 | state=$state")
        melodyState.postValue(MelodyState.Loading(state.isMelody))

        val list = getMelodyList()
        val check = preferencesRepo.getMelodyCheck(list)
        val item = if (check != null) list.getOrNull(check) else null

        Log.i("HERE", "loadMelody: 2 | item=$item")
        if (item != null) {
            Log.i("HERE", "loadMelody: 3")
            melodyState.postValue(MelodyState.Finish(state.isMelody, item))
        } else {
            Log.i("HERE", "loadMelody: 4")
            melodyState.postValue(MelodyState.Empty)
        }
    }

    /** Information about melodies naming and chosen one position. */
    override val selectMelodyData: Flow<Pair<Array<String>, Int>>
        get() = flow {
            val list = getMelodyList()
            val titleArray = list.map { it.title }.toTypedArray()
            val check = preferencesRepo.getMelodyCheck(list)

            if (titleArray.isNotEmpty() && check != null) {
                emit(value = titleArray to check)
            } else {
                melodyState.postValue(MelodyState.Enabled(isGroupEnabled = false))
            }
        }.onBack()

    override fun getMelody(p: Int): Flow<MelodyItem> = flow {
        getMelodyList().getOrNull(p)?.let { emit(it) }
    }.onBack()

    override fun updateMelody(title: String): Flow<String> = flow {
        val resultTitle = preferencesRepo.setMelodyUri(getMelodyList(), title)
        if (resultTitle != null) {
            emit(resultTitle)
        } else {
            melodyState.postValue(MelodyState.Empty)
        }
    }.onBack()
}