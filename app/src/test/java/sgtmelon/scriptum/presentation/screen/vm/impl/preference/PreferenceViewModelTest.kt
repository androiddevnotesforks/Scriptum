package sgtmelon.scriptum.presentation.screen.vm.impl.preference

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.R
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.domain.interactor.callback.IBackupInteractor
import sgtmelon.scriptum.domain.interactor.callback.IBindInteractor
import sgtmelon.scriptum.domain.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.domain.interactor.callback.preference.IPreferenceInteractor
import sgtmelon.scriptum.domain.model.key.PermissionResult
import sgtmelon.scriptum.domain.model.result.ExportResult
import sgtmelon.scriptum.domain.model.result.ImportResult
import sgtmelon.scriptum.domain.model.state.SignalState
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.IPreferenceFragment
import kotlin.random.Random

/**
 * Test for [PreferenceViewModel].
 */
@ExperimentalCoroutinesApi
class PreferenceViewModelTest : ParentViewModelTest() {

    @MockK lateinit var interactor: IPreferenceInteractor
    @MockK lateinit var signalInteractor: ISignalInteractor
    @MockK lateinit var backupInteractor: IBackupInteractor
    @MockK lateinit var bindInteractor: IBindInteractor
    @MockK lateinit var callback: IPreferenceFragment

    private val melodyList = TestData.Melody.melodyList
    private val fileList = TestData.Backup.fileList

    private val viewModel by lazy { PreferenceViewModel(application) }
    private val spyViewModel by lazy { spyk(viewModel) }

    override fun setUp() {
        super.setUp()

        viewModel.setCallback(callback)
        viewModel.setInteractor(interactor, signalInteractor, backupInteractor, bindInteractor)
    }

    override fun tearDown() {
        super.tearDown()
        confirmVerified(callback, interactor, signalInteractor, backupInteractor, bindInteractor)
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)
        viewModel.onDestroy()
        assertNull(viewModel.callback)
    }


    @Test fun onSetup_forUser() = startCoTest {
        val themeSummary = nextString()
        val sortSummary = nextString()
        val defaultColorSummary = nextString()
        val savePeriodSummary = nextString()
        val repeatSummary = nextString()
        val typeCheck = BooleanArray(size = 3) { Random.nextBoolean() }
        val signalSummary = nextString()
        val volumeSummary = nextString()

        coEvery { spyViewModel.setupMelody() } returns Unit
        coEvery { spyViewModel.setupBackup() } returns Unit

        every { interactor.isDeveloper } returns false

        every { interactor.getThemeSummary() } returns themeSummary
        every { interactor.getSortSummary() } returns sortSummary
        every { interactor.getDefaultColorSummary() } returns defaultColorSummary
        every { interactor.getSavePeriodSummary() } returns savePeriodSummary
        every { interactor.getRepeatSummary() } returns repeatSummary
        every { signalInteractor.typeCheck } returns typeCheck
        every { interactor.getSignalSummary(typeCheck) } returns signalSummary
        every { interactor.getVolumeSummary() } returns volumeSummary

        spyViewModel.onSetup()

        coVerify {
            spyViewModel.onSetup()

            spyViewModel.callback
            callback.apply {
                setupApp()
                setupBackup()
                setupNote()
                setupSave()
                setupNotification()
                setupOther()

                interactor.isDeveloper

                interactor.getThemeSummary()
                updateThemeSummary(themeSummary)

                updateExportEnabled(isEnabled = false)
                updateImportEnabled(isEnabled = false)

                interactor.getSortSummary()
                updateSortSummary(sortSummary)
                interactor.getDefaultColorSummary()
                updateColorSummary(defaultColorSummary)
                interactor.getSavePeriodSummary()
                updateSavePeriodSummary(savePeriodSummary)

                interactor.getRepeatSummary()
                updateRepeatSummary(repeatSummary)
                signalInteractor.typeCheck
                interactor.getSignalSummary(typeCheck)
                updateSignalSummary(signalSummary)

                updateMelodyGroupEnabled(isEnabled = false)
                interactor.getVolumeSummary()
                updateVolumeSummary(volumeSummary)
            }

            spyViewModel.setupBackup()
            spyViewModel.setupMelody()
        }
    }

    @Test fun onSetup_forDeveloper() = startCoTest {
        val themeSummary = nextString()
        val sortSummary = nextString()
        val defaultColorSummary = nextString()
        val savePeriodSummary = nextString()
        val repeatSummary = nextString()
        val typeCheck = BooleanArray(size = 3) { Random.nextBoolean() }
        val signalSummary = nextString()
        val volumeSummary = nextString()

        coEvery { spyViewModel.setupMelody() } returns Unit
        coEvery { spyViewModel.setupBackup() } returns Unit

        every { interactor.isDeveloper } returns true

        every { interactor.getThemeSummary() } returns themeSummary
        every { interactor.getSortSummary() } returns sortSummary
        every { interactor.getDefaultColorSummary() } returns defaultColorSummary
        every { interactor.getSavePeriodSummary() } returns savePeriodSummary
        every { interactor.getRepeatSummary() } returns repeatSummary
        every { signalInteractor.typeCheck } returns typeCheck
        every { interactor.getSignalSummary(typeCheck) } returns signalSummary
        every { interactor.getVolumeSummary() } returns volumeSummary

        spyViewModel.onSetup()

        coVerify {
            spyViewModel.onSetup()

            spyViewModel.callback
            callback.apply {
                setupApp()
                setupBackup()
                setupNote()
                setupSave()
                setupNotification()
                setupOther()

                interactor.isDeveloper
                setupDeveloper()

                interactor.getThemeSummary()
                updateThemeSummary(themeSummary)

                updateExportEnabled(isEnabled = false)
                updateImportEnabled(isEnabled = false)

                interactor.getSortSummary()
                updateSortSummary(sortSummary)
                interactor.getDefaultColorSummary()
                updateColorSummary(defaultColorSummary)
                interactor.getSavePeriodSummary()
                updateSavePeriodSummary(savePeriodSummary)

                interactor.getRepeatSummary()
                updateRepeatSummary(repeatSummary)
                signalInteractor.typeCheck
                interactor.getSignalSummary(typeCheck)
                updateSignalSummary(signalSummary)

                updateMelodyGroupEnabled(isEnabled = false)
                interactor.getVolumeSummary()
                updateVolumeSummary(volumeSummary)
            }

            spyViewModel.setupBackup()
            spyViewModel.setupMelody()
        }
    }

    @Test fun setupBackup() = startCoTest {
        coEvery { backupInteractor.getFileList() } returns emptyList()

        viewModel.setupBackup()

        coEvery { backupInteractor.getFileList() } returns fileList

        viewModel.setupBackup()

        coVerifySequence {
            backupInteractor.getFileList()
            callback.updateExportEnabled(isEnabled = true)

            backupInteractor.getFileList()
            callback.updateExportEnabled(isEnabled = true)
            callback.updateImportEnabled(isEnabled = true)
        }
    }

    @Test fun setupMelody_notMelody() = startCoTest {
        val state = SignalState(isMelody = false, isVibration = Random.nextBoolean())
        val item = melodyList.random()
        val index = melodyList.indexOf(item)

        every { signalInteractor.state } returns null

        viewModel.setupMelody()

        every { signalInteractor.state } returns state
        coEvery { signalInteractor.getMelodyCheck() } returns null

        viewModel.setupMelody()

        coEvery { signalInteractor.getMelodyCheck() } returns index
        coEvery { signalInteractor.getMelodyList() } returns emptyList()

        viewModel.setupMelody()

        coEvery { signalInteractor.getMelodyList() } returns melodyList

        viewModel.setupMelody()

        coVerifySequence {
            signalInteractor.state

            signalInteractor.state
            signalInteractor.getMelodyCheck()

            signalInteractor.state
            signalInteractor.getMelodyCheck()
            signalInteractor.getMelodyList()

            signalInteractor.state
            signalInteractor.getMelodyCheck()
            signalInteractor.getMelodyList()
            callback.updateMelodyGroupEnabled(state.isMelody)
            callback.updateMelodySummary(item.title)
        }
    }

    @Test fun setupMelody_isMelody() = startCoTest {
        val state = SignalState(isMelody = true, isVibration = Random.nextBoolean())
        val item = melodyList.random()
        val index = melodyList.indexOf(item)

        every { signalInteractor.state } returns null

        viewModel.setupMelody()

        every { signalInteractor.state } returns state
        coEvery { signalInteractor.getMelodyCheck() } returns null

        viewModel.setupMelody()

        coEvery { signalInteractor.getMelodyCheck() } returns index
        coEvery { signalInteractor.getMelodyList() } returns emptyList()

        viewModel.setupMelody()

        coEvery { signalInteractor.getMelodyList() } returns melodyList

        viewModel.setupMelody()

        coVerifySequence {
            signalInteractor.state

            signalInteractor.state
            signalInteractor.getMelodyCheck()
            callback.showToast(R.string.pref_toast_melody_empty)

            signalInteractor.state
            signalInteractor.getMelodyCheck()
            signalInteractor.getMelodyList()
            callback.showToast(R.string.pref_toast_melody_empty)

            signalInteractor.state
            signalInteractor.getMelodyCheck()
            signalInteractor.getMelodyList()
            callback.updateMelodyGroupEnabled(state.isMelody)
            callback.updateMelodySummary(item.title)
        }
    }

    @Test fun onPause() {
        viewModel.onPause()

        verifySequence {
            signalInteractor.resetMelodyList()
            backupInteractor.resetFileList()
        }
    }


    @Test fun onClickTheme() {
        val value = Random.nextInt()

        every { interactor.theme } returns value

        viewModel.onClickTheme()

        verifySequence {
            interactor.theme
            callback.showThemeDialog(value)
        }
    }

    @Test fun onResultTheme() {
        val value = Random.nextInt()
        val summary = nextString()

        every { interactor.updateTheme(value) } returns summary

        viewModel.onResultTheme(value)

        verifySequence {
            interactor.updateTheme(value)
            callback.updateThemeSummary(summary)
        }
    }


    @Test fun onClickExport() = startCoTest {
        coEvery { spyViewModel.startExport() } returns Unit

        for (it in PermissionResult.values()) {
            spyViewModel.onClickExport(it)
        }

        spyViewModel.onClickExport(result = null)

        coVerify {
            spyViewModel.onClickExport(PermissionResult.LOW_API)
            spyViewModel.startExport()

            spyViewModel.onClickExport(PermissionResult.ALLOWED)
            spyViewModel.callback
            callback.showExportPermissionDialog()

            spyViewModel.onClickExport(PermissionResult.FORBIDDEN)
            spyViewModel.callback
            callback.showExportDenyDialog()

            spyViewModel.onClickExport(PermissionResult.GRANTED)
            spyViewModel.startExport()

            spyViewModel.onClickExport(result = null)
        }
    }

    @Test fun startExport() = startCoTest {
        val path = nextString()

        coEvery { backupInteractor.export() } returns ExportResult.Error

        spyViewModel.startExport()

        coEvery { backupInteractor.export() } returns ExportResult.Success(path)
        coEvery { spyViewModel.setupBackup() } returns Unit

        spyViewModel.startExport()

        coVerifySequence {
            spyViewModel.startExport()
            spyViewModel.callback
            callback.showExportLoadingDialog()
            backupInteractor.export()
            spyViewModel.callback
            callback.hideExportLoadingDialog()
            spyViewModel.callback
            callback.showToast(R.string.pref_toast_export_error)

            spyViewModel.startExport()
            spyViewModel.callback
            callback.showExportLoadingDialog()
            backupInteractor.export()
            spyViewModel.callback
            callback.hideExportLoadingDialog()
            spyViewModel.callback
            callback.showExportPathToast(path)
            spyViewModel.callback
            callback.updateImportEnabled(isEnabled = false)
            backupInteractor.resetFileList()
            spyViewModel.setupBackup()
        }
    }

    @Test fun onClickImport() = startCoTest {
        coEvery { spyViewModel.prepareImportDialog() } returns Unit

        for (it in PermissionResult.values()) {
            spyViewModel.onClickImport(it)
        }

        spyViewModel.onClickImport(result = null)

        coVerify {
            spyViewModel.onClickImport(PermissionResult.LOW_API)
            spyViewModel.prepareImportDialog()

            spyViewModel.onClickImport(PermissionResult.ALLOWED)
            spyViewModel.callback
            callback.showImportPermissionDialog()

            spyViewModel.onClickImport(PermissionResult.FORBIDDEN)
            spyViewModel.prepareImportDialog()

            spyViewModel.onClickImport(PermissionResult.GRANTED)
            spyViewModel.prepareImportDialog()

            spyViewModel.onClickImport(result = null)
        }
    }

    @Test fun prepareImportDialog() = startCoTest {
        val titleArray = fileList.map { it.name }.toTypedArray()

        coEvery { backupInteractor.getFileList() } returns emptyList()

        viewModel.prepareImportDialog()

        coEvery { backupInteractor.getFileList() } returns fileList

        viewModel.prepareImportDialog()

        coVerifySequence {
            backupInteractor.getFileList()
            callback.updateImportEnabled(isEnabled = false)

            backupInteractor.getFileList()
            callback.showImportDialog(titleArray)
        }
    }

    @Test fun onResultImport() {
        val name = nextString()
        val skipCount = Random.nextInt()

        coEvery { backupInteractor.import(name) } returns ImportResult.Simple

        viewModel.onResultImport(name)

        coEvery { backupInteractor.import(name) } returns ImportResult.Skip(skipCount)

        viewModel.onResultImport(name)

        coEvery { backupInteractor.import(name) } returns ImportResult.Error

        viewModel.onResultImport(name)

        coVerifySequence {
            callback.showImportLoadingDialog()
            backupInteractor.import(name)
            callback.hideImportLoadingDialog()
            callback.showToast(R.string.pref_toast_import_result)
            bindInteractor.notifyNoteBind(callback)
            bindInteractor.notifyInfoBind(callback)

            callback.showImportLoadingDialog()
            backupInteractor.import(name)
            callback.hideImportLoadingDialog()
            callback.showImportSkipToast(skipCount)
            bindInteractor.notifyNoteBind(callback)
            bindInteractor.notifyInfoBind(callback)

            callback.showImportLoadingDialog()
            backupInteractor.import(name)
            callback.hideImportLoadingDialog()
            callback.showToast(R.string.pref_toast_import_error)
        }
    }


    @Test fun onClickSort() {
        val value = Random.nextInt()

        every { interactor.sort } returns value

        viewModel.onClickSort()

        verifySequence {
            interactor.sort
            callback.showSortDialog(value)
        }
    }

    @Test fun onResultNoteSort() {
        val value = Random.nextInt()
        val summary = nextString()

        every { interactor.updateSort(value) } returns summary

        viewModel.onResultNoteSort(value)

        verifySequence {
            interactor.updateSort(value)
            callback.updateSortSummary(summary)
        }
    }

    @Test fun onClickNoteColor() {
        val color = Random.nextInt()

        every { interactor.defaultColor } returns color

        viewModel.onClickNoteColor()

        verifySequence {
            interactor.defaultColor
            callback.showColorDialog(color)
        }
    }

    @Test fun onResultNoteColor() {
        val value = Random.nextInt()
        val summary = nextString()

        every { interactor.updateDefaultColor(value) } returns summary

        viewModel.onResultNoteColor(value)

        verifySequence {
            interactor.updateDefaultColor(value)
            callback.updateColorSummary(summary)
        }
    }

    @Test fun onClickSaveTime() {
        val value = Random.nextInt()

        every { interactor.savePeriod } returns value

        viewModel.onClickSaveTime()

        verifySequence {
            interactor.savePeriod
            callback.showSaveTimeDialog(value)
        }
    }

    @Test fun onResultSaveTime() {
        val value = Random.nextInt()
        val summary = nextString()

        every { interactor.updateSavePeriod(value) } returns summary

        viewModel.onResultSaveTime(value)

        verifySequence {
            interactor.updateSavePeriod(value)
            callback.updateSavePeriodSummary(summary)
        }
    }


    @Test fun onClickRepeat() {
        val value = Random.nextInt()

        every { interactor.repeat } returns value

        viewModel.onClickRepeat()

        verifySequence {
            interactor.repeat
            callback.showRepeatDialog(value)
        }
    }

    @Test fun onResultRepeat() {
        val value = Random.nextInt()
        val summary = nextString()

        every { interactor.updateRepeat(value) } returns summary

        viewModel.onResultRepeat(value)

        verifySequence {
            interactor.updateRepeat(value)
            callback.updateRepeatSummary(summary)
        }
    }

    @Test fun onClickSignal() {
        val valueArray = BooleanArray(size = 3) { Random.nextBoolean() }

        every { signalInteractor.typeCheck } returns valueArray

        viewModel.onClickSignal()

        verifySequence {
            signalInteractor.typeCheck
            callback.showSignalDialog(valueArray)
        }
    }

    @Test fun onResultSignal_isMelody() {
        val valueArray = BooleanArray(size = 3) { Random.nextBoolean() }
        val state = SignalState(isMelody = true, isVibration = Random.nextBoolean())
        val summary = nextString()

        every { interactor.updateSignal(valueArray) } returns summary

        every { signalInteractor.state } returns null
        viewModel.onResultSignal(valueArray)

        every { signalInteractor.state } returns state
        coEvery { signalInteractor.getMelodyList() } returns emptyList()
        viewModel.onResultSignal(valueArray)

        coEvery { signalInteractor.getMelodyList() } returns melodyList
        viewModel.onResultSignal(valueArray)

        coVerifySequence {
            interactor.updateSignal(valueArray)
            callback.updateSignalSummary(summary)
            signalInteractor.state

            interactor.updateSignal(valueArray)
            callback.updateSignalSummary(summary)
            signalInteractor.state
            signalInteractor.getMelodyList()
            callback.showToast(R.string.pref_toast_melody_empty)

            interactor.updateSignal(valueArray)
            callback.updateSignalSummary(summary)
            signalInteractor.state
            signalInteractor.getMelodyList()
            callback.updateMelodyGroupEnabled(isEnabled = true)
        }
    }

    @Test fun onResultSignal_notMelody() {
        val valueArray = BooleanArray(size = 3) { Random.nextBoolean() }
        val state = SignalState(isMelody = false, isVibration = Random.nextBoolean())
        val summary = nextString()

        every { interactor.updateSignal(valueArray) } returns summary

        every { signalInteractor.state } returns null
        viewModel.onResultSignal(valueArray)

        every { signalInteractor.state } returns state
        viewModel.onResultSignal(valueArray)

        coVerifySequence {
            interactor.updateSignal(valueArray)
            callback.updateSignalSummary(summary)
            signalInteractor.state

            interactor.updateSignal(valueArray)
            callback.updateSignalSummary(summary)
            signalInteractor.state
            callback.updateMelodyGroupEnabled(isEnabled = false)
        }
    }


    @Test fun onClickMelody() = startCoTest {
        coEvery { spyViewModel.prepareMelodyDialog() } returns Unit

        for (it in PermissionResult.values()) {
            spyViewModel.onClickMelody(it)
        }

        spyViewModel.onClickMelody(result = null)

        coVerify {
            spyViewModel.onClickMelody(PermissionResult.LOW_API)
            spyViewModel.prepareMelodyDialog()

            spyViewModel.onClickMelody(PermissionResult.ALLOWED)
            spyViewModel.callback
            callback.showMelodyPermissionDialog()

            spyViewModel.onClickMelody(PermissionResult.FORBIDDEN)
            spyViewModel.prepareMelodyDialog()

            spyViewModel.onClickMelody(PermissionResult.GRANTED)
            spyViewModel.prepareMelodyDialog()

            spyViewModel.onClickMelody(result = null)
        }
    }

    @Test fun prepareMelodyDialog() = startCoTest {
        val index = Random.nextInt()
        val titleArray = melodyList.map { it.title }.toTypedArray()

        coEvery { signalInteractor.getMelodyList() } returns emptyList()
        coEvery { signalInteractor.getMelodyCheck() } returns null

        viewModel.prepareMelodyDialog()

        coEvery { signalInteractor.getMelodyCheck() } returns Random.nextInt()

        viewModel.prepareMelodyDialog()

        coEvery { signalInteractor.getMelodyList() } returns melodyList
        coEvery { signalInteractor.getMelodyCheck() } returns null

        viewModel.prepareMelodyDialog()

        coEvery { signalInteractor.getMelodyCheck() } returns index

        viewModel.prepareMelodyDialog()

        coVerifySequence {
            repeat(times = 3) {
                signalInteractor.getMelodyList()
                signalInteractor.getMelodyCheck()
                callback.updateMelodyGroupEnabled(isEnabled = false)
            }

            signalInteractor.getMelodyList()
            signalInteractor.getMelodyCheck()
            callback.showMelodyDialog(titleArray, index)
        }
    }

    @Test fun onSelectMelody_onNull() {
        val index = -1

        coEvery { signalInteractor.getMelodyList() } returns melodyList

        viewModel.onSelectMelody(index)

        coVerifySequence {
            signalInteractor.getMelodyList()
        }
    }

    @Test fun onSelectMelody() {
        val item = melodyList.random()
        val index = melodyList.indexOf(item)

        coEvery { signalInteractor.getMelodyList() } returns melodyList

        viewModel.onSelectMelody(index)

        coVerifySequence {
            signalInteractor.getMelodyList()

            callback.playMelody(item.uri)
        }
    }

    @Test fun onResultMelody() {
        val item = melodyList.random()

        coEvery { signalInteractor.getMelodyList() } returns melodyList
        coEvery { signalInteractor.setMelodyUri(item.title) } returns item.title

        viewModel.onResultMelody(item.title)

        coVerifySequence {
            signalInteractor.setMelodyUri(item.title)

            callback.updateMelodySummary(item.title)
        }
    }

    @Test fun onResultMelody_onNotEquals() {
        val item = melodyList.random()
        val newTitle = nextString()

        coEvery { signalInteractor.getMelodyList() } returns melodyList
        coEvery { signalInteractor.setMelodyUri(item.title) } returns newTitle

        viewModel.onResultMelody(item.title)

        coVerifySequence {
            signalInteractor.setMelodyUri(item.title)

            callback.updateMelodySummary(newTitle)
            callback.showToast(R.string.pref_toast_melody_replace)
        }
    }

    @Test fun onResultMelody_onNull() {
        val item = melodyList.random()

        coEvery { signalInteractor.getMelodyList() } returns melodyList
        coEvery { signalInteractor.setMelodyUri(item.title) } returns null

        viewModel.onResultMelody(item.title)

        coVerifySequence {
            signalInteractor.setMelodyUri(item.title)

            callback.showToast(R.string.pref_toast_melody_empty)
        }
    }

    @Test fun onClickVolume() {
        val value = Random.nextInt()

        every { interactor.volume } returns value

        viewModel.onClickVolume()

        verifySequence {
            interactor.volume
            callback.showVolumeDialog(value)
        }
    }

    @Test fun onResultVolume() {
        val value = Random.nextInt()
        val summary = nextString()

        every { interactor.updateVolume(value) } returns summary

        viewModel.onResultVolume(value)

        verifySequence {
            interactor.updateVolume(value)
            callback.updateVolumeSummary(summary)
        }
    }


    @Test fun onUnlockDeveloper() {
        every { interactor.isDeveloper } returns false
        every { interactor.isDeveloper = true } returns Unit
        viewModel.onUnlockDeveloper()

        every { interactor.isDeveloper } returns true
        viewModel.onUnlockDeveloper()

        verifySequence {
            interactor.isDeveloper
            interactor.isDeveloper = true
            callback.setupDeveloper()
            callback.showToast(R.string.pref_toast_develop_unlock)

            interactor.isDeveloper
            callback.showToast(R.string.pref_toast_develop_already)
        }
    }
}