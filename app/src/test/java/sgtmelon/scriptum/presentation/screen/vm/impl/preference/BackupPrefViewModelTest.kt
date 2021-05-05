package sgtmelon.scriptum.presentation.screen.vm.impl.preference

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.R
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.domain.interactor.callback.preference.IBackupPrefInteractor
import sgtmelon.scriptum.domain.model.item.FileItem
import sgtmelon.scriptum.domain.model.key.PermissionResult
import sgtmelon.scriptum.domain.model.result.ExportResult
import sgtmelon.scriptum.domain.model.result.ImportResult
import sgtmelon.scriptum.getRandomSize
import sgtmelon.scriptum.parent.ParentViewModelTest
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.IBackupPrefFragment
import kotlin.random.Random

/**
 * Test for [BackupPrefViewModel].
 */
@ExperimentalCoroutinesApi
class BackupPrefViewModelTest : ParentViewModelTest() {

    @MockK lateinit var interactor: IBackupPrefInteractor
    @MockK lateinit var callback: IBackupPrefFragment

    private val fileList = TestData.Backup.fileList

    private val viewModel by lazy { BackupPrefViewModel(application) }
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
        coEvery { spyViewModel.isExportAllowed() } returns null
        spyViewModel.onSetup()

        coEvery { spyViewModel.isExportAllowed() } returns true
        coEvery { spyViewModel.isImportAllowed() } returns null
        spyViewModel.onSetup()

        coEvery { spyViewModel.isImportAllowed() } returns false
        spyViewModel.onSetup()

        coEvery { spyViewModel.isExportAllowed() } returns false
        spyViewModel.onSetup()

        coEvery { spyViewModel.isImportAllowed() } returns true
        spyViewModel.onSetup()

        coEvery { spyViewModel.isExportAllowed() } returns true
        coEvery { spyViewModel.isImportAllowed() } returns true
        coEvery { spyViewModel.setupBackground() } returns Unit
        spyViewModel.onSetup()

        coVerifyOrder {
            spyViewModel.onSetup()
            spyViewModel.callback
            callback.setup()
            spyViewModel.isExportAllowed()

            spyViewModel.onSetup()
            spyViewModel.callback
            callback.setup()
            spyViewModel.isExportAllowed()
            spyViewModel.isImportAllowed()

            spyViewModel.onSetup()
            spyViewModel.callback
            callback.setup()
            spyViewModel.isExportAllowed()
            spyViewModel.isImportAllowed()

            spyViewModel.onSetup()
            spyViewModel.callback
            callback.setup()
            spyViewModel.isExportAllowed()

            spyViewModel.onSetup()
            spyViewModel.callback
            callback.setup()
            spyViewModel.isExportAllowed()

            spyViewModel.onSetup()
            spyViewModel.callback
            callback.setup()
            spyViewModel.isExportAllowed()
            spyViewModel.isImportAllowed()
            spyViewModel.setupBackground()
        }
    }

    @Test fun isExportAllowed() {
        every { callback.getExportPermissionResult() } returns null
        assertNull(viewModel.isExportAllowed())

        every { callback.getExportPermissionResult() } returns PermissionResult.LOW_API
        assertEquals(true, viewModel.isExportAllowed())

        every { callback.getExportPermissionResult() } returns PermissionResult.ALLOWED
        assertEquals(false, viewModel.isExportAllowed())

        every { callback.getExportPermissionResult() } returns PermissionResult.FORBIDDEN
        assertEquals(false, viewModel.isExportAllowed())

        every { callback.getExportPermissionResult() } returns PermissionResult.GRANTED
        assertEquals(true, viewModel.isExportAllowed())

        verifySequence {
            callback.getExportPermissionResult()

            callback.getExportPermissionResult()

            callback.getExportPermissionResult()
            callback.updateExportEnabled(isEnabled = true)
            callback.updateExportSummary(R.string.pref_summary_permission_need)

            callback.getExportPermissionResult()
            callback.updateExportEnabled(isEnabled = true)
            callback.updateExportSummary(R.string.pref_summary_permission_need)

            callback.getExportPermissionResult()
        }
    }

    @Test fun isImportAllowed() {
        every { callback.getImportPermissionResult() } returns null
        assertNull(viewModel.isImportAllowed())

        every { callback.getImportPermissionResult() } returns PermissionResult.LOW_API
        assertEquals(true, viewModel.isImportAllowed())

        every { callback.getImportPermissionResult() } returns PermissionResult.ALLOWED
        assertEquals(false, viewModel.isImportAllowed())

        every { callback.getImportPermissionResult() } returns PermissionResult.FORBIDDEN
        assertEquals(false, viewModel.isImportAllowed())

        every { callback.getImportPermissionResult() } returns PermissionResult.GRANTED
        assertEquals(true, viewModel.isImportAllowed())

        verifySequence {
            callback.getImportPermissionResult()

            callback.getImportPermissionResult()

            callback.getImportPermissionResult()
            callback.updateImportEnabled(isEnabled = true)
            callback.updateImportSummary(R.string.pref_summary_permission_need)

            callback.getImportPermissionResult()
            callback.updateImportEnabled(isEnabled = true)
            callback.updateImportSummary(R.string.pref_summary_permission_need)

            callback.getImportPermissionResult()
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

        for (exportItem in resultList) {
            every { callback.getExportPermissionResult() } returns exportItem
            for (importItem in resultList) {
                every { callback.getImportPermissionResult() } returns importItem
                spyViewModel.onClickExport()
            }
        }

        verifySequence {
            for (exportItem in resultList) {
                for (importItem in resultList) {
                    spyViewModel.onClickExport()
                    spyViewModel.callback
                    callback.getExportPermissionResult()

                    if (exportItem == null) continue

                    spyViewModel.callback
                    callback.getImportPermissionResult()

                    if (importItem == null) continue

                    if (exportItem == PermissionResult.FORBIDDEN
                            || importItem == PermissionResult.FORBIDDEN) {
                        spyViewModel.onClickExport(PermissionResult.FORBIDDEN)
                    } else {
                        spyViewModel.onClickExport(exportItem)
                    }
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

        for (exportItem in resultList) {
            every { callback.getExportPermissionResult() } returns exportItem
            for (importItem in resultList) {
                every { callback.getImportPermissionResult() } returns importItem
                spyViewModel.onClickImport()
            }
        }

        verifySequence {
            for (exportItem in resultList) {
                for (importItem in resultList) {
                    spyViewModel.onClickImport()
                    spyViewModel.callback
                    callback.getExportPermissionResult()

                    if (exportItem == null) continue

                    spyViewModel.callback
                    callback.getImportPermissionResult()

                    if (importItem == null) continue

                    if (exportItem == PermissionResult.FORBIDDEN
                            || importItem == PermissionResult.FORBIDDEN) {
                        spyViewModel.onClickImport(PermissionResult.FORBIDDEN)
                    } else {
                        spyViewModel.onClickImport(importItem)
                    }
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