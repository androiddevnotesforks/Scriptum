package sgtmelon.scriptum.presentation.screen.vm.impl

import android.os.Bundle
import kotlinx.coroutines.*
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.interactor.callback.IBackupInteractor
import sgtmelon.scriptum.domain.interactor.callback.IPreferenceInteractor
import sgtmelon.scriptum.domain.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.key.PermissionResult
import sgtmelon.scriptum.domain.model.result.ImportResult
import sgtmelon.scriptum.presentation.screen.ui.callback.IPreferenceFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PreferenceFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.IPreferenceViewModel

/**
 * ViewModel for [PreferenceFragment].
 */
class PreferenceViewModel(
        private val interactor: IPreferenceInteractor,
        private val signalInteractor: ISignalInteractor,
        private val backupInteractor: IBackupInteractor,
        @RunPrivate var callback: IPreferenceFragment?
) : IPreferenceViewModel {

    private val viewModelScope by lazy {
        CoroutineScope(context = SupervisorJob() + Dispatchers.Main.immediate)
    }

    override fun onSetup(bundle: Bundle?) {
        callback?.apply {
            setupApp()
            setupBackup()
            setupNote()
            setupNotification()
            setupSave()
            setupOther()

            updateThemeSummary(interactor.getThemeSummary())

            /**
             * Make import permission not enabled before [setupBackup] load data.
             */
            updateImportEnabled(isEnabled = false)

            updateSortSummary(interactor.getSortSummary())
            updateColorSummary(interactor.getDefaultColorSummary())
            updateSavePeriodSummary(interactor.getSavePeriodSummary())

            updateRepeatSummary(interactor.getRepeatSummary())
            updateSignalSummary(interactor.getSignalSummary(signalInteractor.typeCheck))

            /**
             * Make melody permissions not enabled before [setupMelody] load data.
             */
            updateMelodyGroupEnabled(isEnabled = false)
            updateVolumeSummary(interactor.getVolumeSummary())
        }

        viewModelScope.launch {
            setupMelody()
            setupBackup()
        }
    }

    @RunPrivate suspend fun setupMelody() {
        val state = signalInteractor.state ?: return

        fun onEmptyError() {
            if (!state.isMelody) return

            callback?.showToast(R.string.pref_toast_melody_empty)
        }

        val melodyCheck = signalInteractor.getMelodyCheck() ?: return onEmptyError()
        val melodyList = signalInteractor.getMelodyList()
        val melodyItem = melodyList.getOrNull(melodyCheck) ?: return onEmptyError()

        callback?.updateMelodyGroupEnabled(state.isMelody)
        callback?.updateMelodySummary(melodyItem.title)
    }

    @RunPrivate suspend fun setupBackup() {
        val fileList = backupInteractor.getFileList()

        if (fileList.isEmpty()) return

        callback?.updateImportEnabled(isEnabled = true)
    }

    override fun onDestroy(func: () -> Unit) {
        callback = null

        viewModelScope.cancel()
    }

    /**
     * Need reset lists, because user can change permission or
     * delete some files or remove sd card.
     *
     * It calls even after permission dialog.
     */
    override fun onPause() {
        signalInteractor.resetMelodyList()
        backupInteractor.resetFileList()
    }


    override fun onClickTheme() = takeTrue { callback?.showThemeDialog(interactor.theme) }

    override fun onResultTheme(@Theme value: Int) {
        callback?.updateThemeSummary(interactor.updateTheme(value))
    }


    /**
     * TODO Add progress dialog
     */
    override fun onClickExport() = takeTrue {
        viewModelScope.launch {
            val path = backupInteractor.export()

            if (path != null) {
                callback?.showExportPathToast(path)
                callback?.updateImportEnabled(isEnabled = true)
            } else {
                callback?.showToast(R.string.pref_toast_export_error)
            }
        }
    }

    /**
     * Show permission only on [PermissionResult.ALLOWED] because we
     * can display files which not located on SD card.
     */
    override fun onClickImport(result: PermissionResult) = takeTrue {
        when (result) {
            PermissionResult.ALLOWED -> callback?.showImportPermissionDialog()
            else -> viewModelScope.launch { prepareImportDialog() }
        }
    }

    @RunPrivate suspend fun prepareImportDialog() {
        val titleArray = backupInteractor.getFileList().map { it.name }.toTypedArray()

        if (titleArray.isEmpty()) {
            callback?.updateImportEnabled(isEnabled = false)
        } else {
            callback?.showImportDialog(titleArray)
        }
    }

    /**
     * TODO Add progress dialog.
     */
    override fun onResultImport(name: String) {
        viewModelScope.launch {
            val result = backupInteractor.import(name)

            if (result.isSuccess) when (result) {
                is ImportResult.Simple -> callback?.showToast(R.string.pref_toast_import_result)
                is ImportResult.Skip -> callback?.showImportSkipToast(result.skipCount)
            } else {
                callback?.showToast(R.string.pref_toast_import_error)
            }
        }
    }


    override fun onClickSort() = takeTrue { callback?.showSortDialog(interactor.sort) }

    override fun onResultNoteSort(@Sort value: Int) {
        callback?.updateSortSummary(interactor.updateSort(value))
    }

    override fun onClickNoteColor() = takeTrue {
        callback?.showColorDialog(interactor.defaultColor, interactor.theme)
    }

    override fun onResultNoteColor(@Color value: Int) {
        callback?.updateColorSummary(interactor.updateDefaultColor(value))
    }

    override fun onClickSaveTime() = takeTrue {
        callback?.showSaveTimeDialog(interactor.savePeriod)
    }

    override fun onResultSaveTime(value: Int) {
        callback?.updateSavePeriodSummary(interactor.updateSavePeriod(value))
    }


    override fun onClickRepeat() = takeTrue { callback?.showRepeatDialog(interactor.repeat) }

    override fun onResultRepeat(@Repeat value: Int) {
        callback?.updateRepeatSummary(interactor.updateRepeat(value))
    }

    override fun onClickSignal() = takeTrue {
        callback?.showSignalDialog(signalInteractor.typeCheck)
    }

    override fun onResultSignal(valueArray: BooleanArray) {
        callback?.updateSignalSummary(interactor.updateSignal(valueArray))

        val state = signalInteractor.state ?: return

        if (!state.isMelody) {
            callback?.updateMelodyGroupEnabled(isEnabled = false)
        } else {
            viewModelScope.launch {
                if (signalInteractor.getMelodyList().isEmpty()) {
                    callback?.showToast(R.string.pref_toast_melody_empty)
                } else {
                    callback?.updateMelodyGroupEnabled(isEnabled = true)
                }
            }
        }
    }


    /**
     * Show permission only on [PermissionResult.ALLOWED] because we
     * can display melodies which not located on SD card.
     */
    override fun onClickMelody(result: PermissionResult) = takeTrue {
        when (result) {
            PermissionResult.ALLOWED -> callback?.showMelodyPermissionDialog()
            else -> viewModelScope.launch { prepareMelodyDialog() }
        }
    }

    @RunPrivate suspend fun prepareMelodyDialog() {
        val melodyList = signalInteractor.getMelodyList()
        val titleArray = melodyList.map { it.title }.toTypedArray()
        val melodyCheck = signalInteractor.getMelodyCheck()

        if (titleArray.isEmpty() || melodyCheck == null) {
            callback?.updateMelodyGroupEnabled(isEnabled = false)
        } else {
            callback?.showMelodyDialog(titleArray, melodyCheck)
        }
    }

    override fun onSelectMelody(value: Int) {
        viewModelScope.launch {
            val item = signalInteractor.getMelodyList().getOrNull(value) ?: return@launch
            callback?.playMelody(item.uri)
        }
    }

    override fun onResultMelody(title: String) {
        viewModelScope.launch {
            val resultTitle = signalInteractor.setMelodyUri(title)

            when {
                title == resultTitle -> callback?.updateMelodySummary(title)
                resultTitle != null -> {
                    callback?.updateMelodySummary(resultTitle)
                    callback?.showToast(R.string.pref_toast_melody_replace)
                }
                else -> callback?.showToast(R.string.pref_toast_melody_empty)
            }
        }
    }

    override fun onClickVolume() = takeTrue { callback?.showVolumeDialog(interactor.volume) }

    override fun onResultVolume(value: Int) {
        callback?.updateVolumeSummary(interactor.updateVolume(value))
    }


    private fun takeTrue(func: () -> Unit): Boolean {
        func()
        return true
    }

}