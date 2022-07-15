package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.R
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.IBackupPreferenceInteractor
import sgtmelon.scriptum.cleanup.domain.model.item.FileItem
import sgtmelon.scriptum.cleanup.domain.model.key.PermissionResult
import sgtmelon.scriptum.cleanup.domain.model.result.ExportResult
import sgtmelon.scriptum.cleanup.domain.model.result.ImportResult
import sgtmelon.scriptum.getRandomSize
import sgtmelon.scriptum.parent.ParentViewModelTest
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.IBackupPreferenceFragment
import kotlin.random.Random

/**
 * Test for [BackupPreferenceViewModel].
 */
@ExperimentalCoroutinesApi
class BackupPreferenceViewModelTest : ParentViewModelTest() {

    @MockK lateinit var interactor: IBackupPreferenceInteractor
    @MockK lateinit var callback: IBackupPreferenceFragment

    private val fileList = TestData.Backup.fileList

    private val viewModel by lazy { BackupPreferenceViewModel(application) }
    private val spyViewModel by lazy { spyk(viewModel) }

    @Before override fun setup() {
        super.setup()

        viewModel.setCallback(callback)
        viewModel.setInteractor(interactor)
    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(callback, interactor)
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)
        viewModel.onDestroy()
        assertNull(viewModel.callback)
    }


    @Test fun onSetup() {
        every { callback.getStoragePermissionResult() } returns null
        spyViewModel.onSetup()

        every { callback.getStoragePermissionResult() } returns PermissionResult.LOW_API
        coEvery { spyViewModel.setupBackground() } returns Unit
        spyViewModel.onSetup()

        every { callback.getStoragePermissionResult() } returns PermissionResult.ALLOWED
        spyViewModel.onSetup()

        every { callback.getStoragePermissionResult() } returns PermissionResult.FORBIDDEN
        spyViewModel.onSetup()

        every { callback.getStoragePermissionResult() } returns PermissionResult.GRANTED
        spyViewModel.onSetup()

        coVerifyOrder {
            spyViewModel.onSetup()
            spyViewModel.callback
            callback.setup()
            spyViewModel.callback
            callback.getStoragePermissionResult()

            spyViewModel.onSetup()
            spyViewModel.callback
            callback.setup()
            spyViewModel.callback
            callback.getStoragePermissionResult()
            spyViewModel.setupBackground()

            repeat(times = 2) {
                spyViewModel.onSetup()
                spyViewModel.callback
                callback.setup()
                spyViewModel.callback
                callback.getStoragePermissionResult()
                spyViewModel.callback
                callback.updateExportEnabled(isEnabled = true)
                spyViewModel.callback
                callback.updateExportSummary(R.string.pref_summary_permission_need)
                spyViewModel.callback
                callback.updateImportEnabled(isEnabled = true)
                spyViewModel.callback
                callback.updateImportSummary(R.string.pref_summary_permission_need)
            }

            spyViewModel.onSetup()
            spyViewModel.callback
            callback.setup()
            spyViewModel.callback
            callback.getStoragePermissionResult()
            spyViewModel.setupBackground()
        }
    }

    @Test fun setupBackground() = startCoTest {
        val list = mockk<List<FileItem>>()
        val size = getRandomSize()

        coEvery { interactor.getFileList() } returns list
        every { list.isEmpty() } returns true

        viewModel.setupBackground()

        every { list.isEmpty() } returns false
        every { list.size } returns size

        viewModel.setupBackground()

        coVerifySequence {
            callback.updateExportEnabled(isEnabled = false)
            callback.resetExportSummary()
            callback.updateImportEnabled(isEnabled = false)
            callback.startImportSummarySearch()
            interactor.getFileList()
            callback.stopImportSummarySearch()
            list.isEmpty()
            callback.updateImportSummary(R.string.pref_summary_backup_import_empty)
            callback.updateExportEnabled(isEnabled = true)

            callback.updateExportEnabled(isEnabled = false)
            callback.resetExportSummary()
            callback.updateImportEnabled(isEnabled = false)
            callback.startImportSummarySearch()
            interactor.getFileList()
            callback.stopImportSummarySearch()
            list.isEmpty()
            list.size
            callback.updateImportSummaryFound(size)
            callback.updateImportEnabled(isEnabled = true)
            callback.updateExportEnabled(isEnabled = true)
        }
    }

    @Test fun onPause() {
        viewModel.onPause()

        verifySequence {
            interactor.resetFileList()
        }
    }


    @Test fun onClickExport() {
        val resultList = mutableListOf<PermissionResult?>(null)
        resultList.addAll(PermissionResult.values())

        every { spyViewModel.onClickExport(any()) } returns Unit

        for (item in resultList) {
            every { callback.getStoragePermissionResult() } returns item
            spyViewModel.onClickExport()
        }

        verifySequence {
            for (item in resultList) {
                spyViewModel.onClickExport()
                spyViewModel.callback
                callback.getStoragePermissionResult()

                if (item == null) continue

                if (item == PermissionResult.FORBIDDEN) {
                    spyViewModel.onClickExport(PermissionResult.FORBIDDEN)
                } else {
                    spyViewModel.onClickExport(item)
                }
            }
        }
    }

    @Test fun onClickExport_withResult() {
        coEvery { spyViewModel.startExport() } returns Unit

        for (it in PermissionResult.values()) {
            spyViewModel.onClickExport(it)
        }

        coVerifyOrder {
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
        }
    }

    @Test fun startExport() = startCoTest {
        val path = nextString()

        coEvery { interactor.export() } returns ExportResult.Error

        spyViewModel.startExport()

        coEvery { interactor.export() } returns ExportResult.Success(path)
        coEvery { spyViewModel.setupBackground() } returns Unit

        spyViewModel.startExport()

        coVerifySequence {
            spyViewModel.startExport()
            spyViewModel.callback
            callback.showExportLoadingDialog()
            interactor.export()
            spyViewModel.callback
            callback.hideExportLoadingDialog()
            spyViewModel.callback
            callback.showToast(R.string.pref_toast_export_error)

            spyViewModel.startExport()
            spyViewModel.callback
            callback.showExportLoadingDialog()
            interactor.export()
            spyViewModel.callback
            callback.hideExportLoadingDialog()
            spyViewModel.callback
            callback.showExportPathToast(path)
            interactor.resetFileList()
            spyViewModel.setupBackground()
        }
    }


    @Test fun onClickImport() {
        val resultList = mutableListOf<PermissionResult?>(null)
        resultList.addAll(PermissionResult.values())

        every { spyViewModel.onClickImport(any()) } returns Unit

        for (item in resultList) {
            every { callback.getStoragePermissionResult() } returns item
            spyViewModel.onClickImport()
        }

        verifySequence {
            for (item in resultList) {
                spyViewModel.onClickImport()
                spyViewModel.callback
                callback.getStoragePermissionResult()

                if (item == null) continue

                if (item == PermissionResult.FORBIDDEN) {
                    spyViewModel.onClickImport(PermissionResult.FORBIDDEN)
                } else {
                    spyViewModel.onClickImport(item)
                }
            }
        }
    }

    @Test fun onClickImport_withResult() {
        coEvery { spyViewModel.prepareImportDialog() } returns Unit

        for (it in PermissionResult.values()) {
            spyViewModel.onClickImport(it)
        }

        coVerifyOrder {
            spyViewModel.onClickImport(PermissionResult.LOW_API)
            spyViewModel.prepareImportDialog()

            spyViewModel.onClickImport(PermissionResult.ALLOWED)
            spyViewModel.callback
            callback.showImportPermissionDialog()

            spyViewModel.onClickImport(PermissionResult.FORBIDDEN)
            spyViewModel.callback
            callback.showImportDenyDialog()

            spyViewModel.onClickImport(PermissionResult.GRANTED)
            spyViewModel.prepareImportDialog()
        }
    }

    @Test fun prepareImportDialog() = startCoTest {
        val titleArray = fileList.map { it.name }.toTypedArray()

        coEvery { interactor.getFileList() } returns emptyList()

        viewModel.prepareImportDialog()

        coEvery { interactor.getFileList() } returns fileList

        viewModel.prepareImportDialog()

        coVerifySequence {
            interactor.getFileList()
            callback.updateImportSummary(R.string.pref_summary_backup_import_empty)
            callback.updateImportEnabled(isEnabled = false)

            interactor.getFileList()
            callback.showImportDialog(titleArray)
        }
    }

    @Test fun onResultImport() {
        val name = nextString()
        val skipCount = Random.nextInt()

        coEvery { interactor.import(name) } returns ImportResult.Simple

        viewModel.onResultImport(name)

        coEvery { interactor.import(name) } returns ImportResult.Skip(skipCount)

        viewModel.onResultImport(name)

        coEvery { interactor.import(name) } returns ImportResult.Error

        viewModel.onResultImport(name)

        coVerifySequence {
            callback.showImportLoadingDialog()
            interactor.import(name)
            callback.hideImportLoadingDialog()
            callback.showToast(R.string.pref_toast_import_result)
            callback.sendTidyUpAlarmBroadcast()
            callback.sendNotifyNotesBroadcast()
            callback.sendNotifyInfoBroadcast()

            callback.showImportLoadingDialog()
            interactor.import(name)
            callback.hideImportLoadingDialog()
            callback.showImportSkipToast(skipCount)
            callback.sendTidyUpAlarmBroadcast()
            callback.sendNotifyNotesBroadcast()
            callback.sendNotifyInfoBroadcast()

            callback.showImportLoadingDialog()
            interactor.import(name)
            callback.hideImportLoadingDialog()
            callback.showToast(R.string.pref_toast_import_error)
        }
    }
}