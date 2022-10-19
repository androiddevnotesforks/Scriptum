package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference

import androidx.annotation.IntRange
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import sgtmelon.extensions.flowOnBack
import sgtmelon.extensions.launchBack
import sgtmelon.extensions.runBack
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.IAlarmPreferenceViewModel
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.preferences.GetMelodyListUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSignalSummaryUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSummaryUseCase
import sgtmelon.scriptum.infrastructure.model.item.MelodyItem
import sgtmelon.scriptum.infrastructure.model.key.Repeat
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.MelodyState
import sgtmelon.scriptum.infrastructure.utils.SingleShootLiveData

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

        repeatSummary.postValue(getRepeatSummary())
        signalSummary.postValue(getSignalSummary())
        volumeSummary.postValue(getVolumeSummary())

        viewModelScope.launchBack { loadMelody() }
    }

    /**
     * Need reset list, because user can change permission or delete some files or
     * remove sd card. It   calls even after permission dialog.
     */
    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        getMelodyList.reset()
        melodyState.postValue(MelodyState.Enabled(isGroupEnabled = false))
    }

    override val repeat: Repeat get() = preferencesRepo.repeat

    override val repeatSummary by lazy { SingleShootLiveData(getRepeatSummary()) }

    override fun updateRepeat(value: Int) {
        repeatSummary.postValue(getRepeatSummary(value))
    }

    override val signalTypeCheck: BooleanArray get() = preferencesRepo.signalTypeCheck

    override val signalSummary by lazy { SingleShootLiveData(getSignalSummary()) }

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

    override val volumeSummary by lazy { SingleShootLiveData(getVolumeSummary()) }

    override fun updateVolume(@IntRange(from = 10, to = 100) value: Int) {
        volumeSummary.postValue(getVolumeSummary(value))
    }

    override val melodyState = SingleShootLiveData<MelodyState>()

    private suspend fun loadMelody() {
        val state = preferencesRepo.signalState

        melodyState.postValue(MelodyState.Loading(state.isMelody))

        val list = getMelodyList()
        val check = preferencesRepo.getMelodyCheck(list)
        val item = if (check != null) list.getOrNull(check) else null

        if (item != null) {
            melodyState.postValue(MelodyState.Finish(state.isMelody, item))
        } else {
            melodyState.postValue(MelodyState.Empty)
        }
    }

    override val melodyTitlesCheckPair: Flow<Pair<Array<String>, Int>>
        get() = flow {
            val list = getMelodyList()
            val titleArray = list.map { it.title }.toTypedArray()
            val check = preferencesRepo.getMelodyCheck(list)

            if (titleArray.isNotEmpty() && check != null) {
                emit(value = titleArray to check)
            } else {
                melodyState.postValue(MelodyState.Enabled(isGroupEnabled = false))
            }
        }.flowOnBack()

    override fun getMelody(value: Int): Flow<MelodyItem?> = flow {
        emit(getMelodyList().getOrNull(value))
    }.flowOnBack()

    //region Remove

    //    override fun onSetup(bundle: Bundle?) {
    //        callback?.setup()
    //
    //        callback?.updateRepeatSummary()
    //        callback?.updateSignalSummary()
    //        callback?.updateVolumeSummary()
    //
    //        viewModelScope.launch { setupBackground() }
    //    }
    //
    //    @RunPrivate suspend fun setupBackground() {
    //        val state = preferencesRepo.signalState
    //
    //        /** Make melody preference not enabled in every way, before we load data. */
    //        callback?.updateMelodyGroupEnabled(state.isMelody)
    //        callback?.updateMelodyEnabled(isEnabled = false)
    //        callback?.startMelodySummarySearch()
    //
    //        val melodyItem = runBack {
    //            val list = getMelodyList()
    //            val check = preferencesRepo.getMelodyCheck(list) ?: return@runBack null
    //
    //            return@runBack list.getOrNull(check)
    //        }
    //
    //        callback?.stopMelodySummarySearch()
    //
    //        if (melodyItem != null) {
    //            callback?.updateMelodyEnabled(state.isMelody)
    //            callback?.updateMelodySummary(melodyItem.title)
    //        } else {
    //            /**
    //             * Melody preference not enabled in that case.
    //             */
    //            callback?.updateMelodySummary(R.string.pref_summary_alarm_melody_empty)
    //        }
    //    }


    //    override fun onClickMelody(result: PermissionResult?) {
    //        if (result == null) return
    //
    //        when (result) {
    //            PermissionResult.ASK -> callback?.showMelodyPermissionDialog()
    //            else -> viewModelScope.launch { prepareMelodyDialog() }
    //        }
    //    }
    //
    //    @RunPrivate suspend fun prepareMelodyDialog() {
    //        val melodyList = runBack { getMelodyList() }
    //        val melodyCheck = runBack { preferencesRepo.getMelodyCheck(melodyList) }
    //
    //        val titleArray = melodyList.map { it.title }.toTypedArray()
    //
    //        if (titleArray.isEmpty() || melodyCheck == null) {
    //            melodyState.postValue(MelodyState.Enabled(isGroupEnabled = false))
    //        } else {
    //            callback?.showMelodyDialog(titleArray, melodyCheck)
    //        }
    //    }

    override fun onResultMelody(title: String) {
        viewModelScope.launch {
            val resultTitle = runBack { preferencesRepo.setMelodyUri(getMelodyList(), title) }

            when {
                title == resultTitle -> callback?.updateMelodySummary(title)
                resultTitle != null -> {
                    callback?.updateMelodySummary(resultTitle)
                    callback?.showToast(R.string.pref_toast_melody_replace)
                }
                else -> {
                    callback?.updateMelodyEnabled(isEnabled = false)
                    callback?.updateMelodySummary(R.string.pref_summary_alarm_melody_empty)
                }
            }
        }
    }

    //endregion
}