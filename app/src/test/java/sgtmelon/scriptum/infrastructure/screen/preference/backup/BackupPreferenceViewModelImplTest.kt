package sgtmelon.scriptum.infrastructure.screen.preference.backup

import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifySequence
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.TestData
import sgtmelon.scriptum.cleanup.parent.ParentViewModelTest
import sgtmelon.scriptum.domain.model.result.ExportResult
import sgtmelon.scriptum.domain.model.result.ImportResult
import sgtmelon.scriptum.domain.useCase.backup.GetBackupFileListUseCase
import sgtmelon.scriptum.domain.useCase.backup.StartBackupExportUseCase
import sgtmelon.scriptum.domain.useCase.backup.StartBackupImportUseCase
import sgtmelon.scriptum.infrastructure.model.item.FileItem
import sgtmelon.scriptum.infrastructure.model.key.PermissionResult
import sgtmelon.scriptum.testing.getRandomSize
import sgtmelon.test.common.nextString

/**
 * Test for [BackupPreferenceViewModelImpl].
 */
@ExperimentalCoroutinesApi
class BackupPreferenceViewModelImplTest : ParentViewModelTest() {

    //region Setup

    @MockK lateinit var callback: IBackupPreferenceFragment
    @MockK lateinit var getBackupFileList: GetBackupFileListUseCase
    @MockK lateinit var startBackupExport: StartBackupExportUseCase
    @MockK lateinit var startBackupImport: StartBackupImportUseCase

    private val fileList = TestData.Backup.fileList

    private val viewModel by lazy {
        BackupPreferenceViewModelImpl(
            callback,
            getBackupFileList,
            startBackupExport,
            startBackupImport
        )
    }
    private val spyViewModel by lazy { spyk(viewModel) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(callback, getBackupFileList, startBackupExport, startBackupImport)
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)
        viewModel.onDestroy()
        assertNull(viewModel.callback)
    }

    //endregion

    @Test fun onSetup() {
        every { callback.getStoragePermissionResult() } returns null
        spyViewModel.onSetup()

        every { callback.getStoragePermissionResult() } returns PermissionResult.LOW_API
        coEvery { spyViewModel.setupBackground() } returns Unit
        spyViewModel.onSetup()

        every { callback.getStoragePermissionResult() } returns PermissionResult.ASK
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

        coEvery { getBackupFileList() } returns list
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
            getBackupFileList()
            callback.stopImportSummarySearch()
            list.isEmpty()
            callback.updateImportSummary(R.string.pref_summary_backup_import_empty)
            callback.updateExportEnabled(isEnabled = true)

            callback.updateExportEnabled(isEnabled = false)
            callback.resetExportSummary()
            callback.updateImportEnabled(isEnabled = false)
            callback.startImportSummarySearch()
            getBackupFileList()
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
            getBackupFileList.reset()
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

    @Test fun `onClickExport with result`() {
        coEvery { spyViewModel.startExport() } returns Unit

        for (it in PermissionResult.values()) {
            spyViewModel.onClickExport(it)
        }

        coVerifyOrder {
            spyViewModel.onClickExport(PermissionResult.LOW_API)
            spyViewModel.startExport()

            spyViewModel.onClickExport(PermissionResult.ASK)
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

        coEvery { startBackupExport() } returns ExportResult.Error

        spyViewModel.startExport()

        coEvery { startBackupExport() } returns ExportResult.Success(path)
        coEvery { spyViewModel.setupBackground() } returns Unit

        spyViewModel.startExport()

        coVerifySequence {
            spyViewModel.startExport()
            spyViewModel.callback
            callback.showExportLoadingDialog()
            startBackupExport()
            spyViewModel.callback
            callback.hideExportLoadingDialog()
            spyViewModel.callback
            callback.showToast(R.string.pref_toast_export_error)

            spyViewModel.startExport()
            spyViewModel.callback
            callback.showExportLoadingDialog()
            startBackupExport()
            spyViewModel.callback
            callback.hideExportLoadingDialog()
            spyViewModel.callback
            callback.showExportPathToast(path)
            getBackupFileList.reset()
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

    @Test fun `onClickImport with result`() {
        coEvery { spyViewModel.prepareImportDialog() } returns Unit

        for (it in PermissionResult.values()) {
            spyViewModel.onClickImport(it)
        }

        coVerifyOrder {
            spyViewModel.onClickImport(PermissionResult.LOW_API)
            spyViewModel.prepareImportDialog()

            spyViewModel.onClickImport(PermissionResult.ASK)
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

        coEvery { getBackupFileList() } returns emptyList()

        viewModel.prepareImportDialog()

        coEvery { getBackupFileList() } returns fileList

        viewModel.prepareImportDialog()

        coVerifySequence {
            getBackupFileList()
            callback.updateImportSummary(R.string.pref_summary_backup_import_empty)
            callback.updateImportEnabled(isEnabled = false)

            getBackupFileList()
            callback.showImportDialog(titleArray)
        }
    }

    @Test fun onResultImport() {
        val backupFileList = mockk<List<FileItem>>()
        val name = nextString()
        val skipCount = Random.nextInt()

        coEvery { getBackupFileList() } returns backupFileList
        coEvery { startBackupImport(name, backupFileList) } returns ImportResult.Simple

        viewModel.onResultImport(name)

        coEvery { startBackupImport(name, backupFileList) } returns ImportResult.Skip(skipCount)

        viewModel.onResultImport(name)

        coEvery { startBackupImport(name, backupFileList) } returns ImportResult.Error

        viewModel.onResultImport(name)

        coVerifySequence {
            callback.showImportLoadingDialog()
            getBackupFileList()
            startBackupImport(name, backupFileList)
            callback.hideImportLoadingDialog()
            callback.showToast(R.string.pref_toast_import_result)
            callback.sendTidyUpAlarmBroadcast()
            callback.sendNotifyNotesBroadcast()
            callback.sendNotifyInfoBroadcast()

            callback.showImportLoadingDialog()
            getBackupFileList()
            startBackupImport(name, backupFileList)
            callback.hideImportLoadingDialog()
            callback.showImportSkipToast(skipCount)
            callback.sendTidyUpAlarmBroadcast()
            callback.sendNotifyNotesBroadcast()
            callback.sendNotifyInfoBroadcast()

            callback.showImportLoadingDialog()
            getBackupFileList()
            startBackupImport(name, backupFileList)
            callback.hideImportLoadingDialog()
            callback.showToast(R.string.pref_toast_import_error)
        }
    }
}