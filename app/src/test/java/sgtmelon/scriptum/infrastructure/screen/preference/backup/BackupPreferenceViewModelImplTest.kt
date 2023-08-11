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
import sgtmelon.scriptum.infrastructure.model.key.permission.PermissionResult
import sgtmelon.scriptum.testing.parent.ParentLiveDataTest
import kotlin.random.Random

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
            Random.nextBoolean(), TODO(), getBackupFileList, startBackupExport, startBackupImport
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

    @Test fun todo() {
        TODO()
    }
}