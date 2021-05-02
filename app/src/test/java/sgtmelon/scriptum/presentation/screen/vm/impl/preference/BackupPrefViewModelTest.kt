package sgtmelon.scriptum.presentation.screen.vm.impl.preference

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.R
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.domain.interactor.callback.preference.IBackupPrefInteractor
import sgtmelon.scriptum.domain.model.key.PermissionResult
import sgtmelon.scriptum.domain.model.result.ExportResult
import sgtmelon.scriptum.domain.model.result.ImportResult
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
        coEvery { spyViewModel.setupBackground() } returns Unit

        spyViewModel.onSetup()

        coVerifyOrder {
            spyViewModel.onSetup()

            spyViewModel.callback
            callback.setup()
            spyViewModel.callback
            callback.updateExportEnabled(isEnabled = false)
            spyViewModel.callback
            callback.updateImportEnabled(isEnabled = false)

            spyViewModel.setupBackground()
        }
    }

    @Test fun setupBackground() = startCoTest {
        coEvery { interactor.getFileList() } returns emptyList()

        viewModel.setupBackground()

        coEvery { interactor.getFileList() } returns fileList

        viewModel.setupBackground()

        coVerifySequence {
            interactor.getFileList()
            callback.updateExportEnabled(isEnabled = true)

            interactor.getFileList()
            callback.updateExportEnabled(isEnabled = true)
            callback.updateImportEnabled(isEnabled = true)
        }
    }

    @Test fun onPause() {
        viewModel.onPause()

        verifySequence {
            interactor.resetFileList()
        }
    }


    @Test fun onClickExport() = startCoTest {
        coEvery { spyViewModel.startExport() } returns Unit

        for (it in PermissionResult.values()) {
            spyViewModel.onClickExport(it)
        }

        spyViewModel.onClickExport(result = null)

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

            spyViewModel.onClickExport(result = null)
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
            spyViewModel.callback
            callback.updateImportEnabled(isEnabled = false)
            interactor.resetFileList()
            spyViewModel.setupBackground()
        }
    }

    @Test fun onClickImport() = startCoTest {
        coEvery { spyViewModel.prepareImportDialog() } returns Unit

        for (it in PermissionResult.values()) {
            spyViewModel.onClickImport(it)
        }

        spyViewModel.onClickImport(result = null)

        coVerifyOrder {
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

        coEvery { interactor.getFileList() } returns emptyList()

        viewModel.prepareImportDialog()

        coEvery { interactor.getFileList() } returns fileList

        viewModel.prepareImportDialog()

        coVerifySequence {
            interactor.getFileList()
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
            callback.sendNotifyNotesBroadcast()
            callback.sendNotifyInfoBroadcast()

            callback.showImportLoadingDialog()
            interactor.import(name)
            callback.hideImportLoadingDialog()
            callback.showImportSkipToast(skipCount)
            callback.sendNotifyNotesBroadcast()
            callback.sendNotifyInfoBroadcast()

            callback.showImportLoadingDialog()
            interactor.import(name)
            callback.hideImportLoadingDialog()
            callback.showToast(R.string.pref_toast_import_error)
        }
    }
}