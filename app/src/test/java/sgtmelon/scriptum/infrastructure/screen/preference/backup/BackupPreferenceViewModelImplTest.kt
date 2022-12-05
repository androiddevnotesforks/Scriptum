package sgtmelon.scriptum.infrastructure.screen.preference.backup

import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import org.junit.After
import org.junit.Test
import sgtmelon.scriptum.cleanup.TestData
import sgtmelon.scriptum.domain.useCase.backup.GetBackupFileListUseCase
import sgtmelon.scriptum.domain.useCase.backup.StartBackupExportUseCase
import sgtmelon.scriptum.domain.useCase.backup.StartBackupImportUseCase
import sgtmelon.scriptum.infrastructure.model.key.PermissionResult
import sgtmelon.scriptum.testing.parent.ParentLiveDataTest

/**
 * Test for [BackupPreferenceViewModelImpl].
 */
class BackupPreferenceViewModelImplTest : ParentLiveDataTest() {

    //region Setup

    @MockK lateinit var initPermissionResult: PermissionResult
    @MockK lateinit var getBackupFileList: GetBackupFileListUseCase
    @MockK lateinit var startBackupExport: StartBackupExportUseCase
    @MockK lateinit var startBackupImport: StartBackupImportUseCase

    private val fileList = TestData.Backup.fileList

    private val viewModel by lazy {
        BackupPreferenceViewModelImpl(
            initPermissionResult, getBackupFileList, startBackupExport, startBackupImport
        )
    }
    private val spyViewModel by lazy { spyk(viewModel) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(
            initPermissionResult, getBackupFileList, startBackupExport, startBackupImport
        )
    }

    //endregion

    @Test fun getExportSummary() {
        TODO()
    }

    @Test fun getExportEnabled() {
        TODO()
    }

    @Test fun getImportSummary() {
        TODO()
    }

    @Test fun getImportEnabled() {
        TODO()
    }

    @Test fun startExport() {
        TODO()
    }

    @Test fun getImportData() {
        TODO()
    }

    @Test fun startImport() {
        TODO()
    }
}