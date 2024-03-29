package sgtmelon.scriptum.domain.useCase.backup

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.cleanup.data.repository.room.callback.BackupRepo
import sgtmelon.scriptum.data.backup.BackupCollector
import sgtmelon.scriptum.data.dataSource.system.CipherDataSource
import sgtmelon.scriptum.data.dataSource.system.FileDataSource
import sgtmelon.scriptum.domain.model.result.ExportResult
import sgtmelon.scriptum.domain.model.result.ParserResult
import sgtmelon.scriptum.infrastructure.model.key.FileType
import sgtmelon.test.common.nextString
import sgtmelon.tests.uniter.ParentTest
import sgtmelon.scriptum.infrastructure.model.key.AppError.File as FileError

/**
 * Test for [StartBackupImportUseCase].
 */
class StartBackupExportUseCaseTest : ParentTest() {

    @MockK lateinit var backupRepo: BackupRepo

    @MockK lateinit var backupCollector: BackupCollector
    @MockK lateinit var fileDataSource: FileDataSource
    @MockK lateinit var cipherDataSource: CipherDataSource

    private val startBackupExport by lazy {
        StartBackupExportUseCase(backupRepo, backupCollector, fileDataSource, cipherDataSource)
    }

    @After override fun tearDown() {
        super.tearDown()

        confirmVerified(backupRepo, backupCollector, fileDataSource, cipherDataSource)
    }

    @Test fun invoke() {
        val parserResult = mockk<ParserResult.Export>()

        val data = nextString()
        val encryptData = nextString()
        val timeName = nextString()
        val path = nextString()

        coEvery { backupRepo.getData() } returns parserResult
        every { backupCollector.convert(parserResult) } returns null

        runBlocking {
            assertEquals(startBackupExport(), ExportResult.Error(FileError.Data))
        }

        every { backupCollector.convert(parserResult) } returns data
        every { cipherDataSource.encrypt(data) } returns encryptData
        every { fileDataSource.getTimeName(FileType.BACKUP) } returns timeName
        every { fileDataSource.writeFile(timeName, encryptData) } returns null

        runBlocking {
            assertEquals(startBackupExport(), ExportResult.Error(FileError.Create))
        }

        every { fileDataSource.writeFile(timeName, encryptData) } returns path

        runBlocking {
            assertEquals(startBackupExport(), ExportResult.Success(path))
        }

        coVerifySequence {
            backupRepo.getData()
            backupCollector.convert(parserResult)

            repeat(times = 2) {
                backupRepo.getData()
                backupCollector.convert(parserResult)
                cipherDataSource.encrypt(data)
                fileDataSource.getTimeName(FileType.BACKUP)
                fileDataSource.writeFile(timeName, encryptData)
            }
        }
    }
}