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
import sgtmelon.scriptum.data.backup.BackupParser
import sgtmelon.scriptum.data.dataSource.system.CipherDataSource
import sgtmelon.scriptum.data.dataSource.system.FileDataSource
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.model.result.ImportResult
import sgtmelon.scriptum.domain.model.result.ParserResult
import sgtmelon.scriptum.infrastructure.model.item.FileItem
import sgtmelon.scriptum.infrastructure.model.key.FileType
import sgtmelon.scriptum.testing.parent.ParentTest
import sgtmelon.test.common.getRandomSize
import sgtmelon.test.common.nextShortString
import sgtmelon.test.common.nextString
import kotlin.random.Random

/**
 * Test for [StartBackupImportUseCase].
 */
class StartBackupImportUseCaseTest : ParentTest() {

    @MockK lateinit var preferencesRepo: PreferencesRepo
    @MockK lateinit var backupRepo: BackupRepo

    @MockK lateinit var backupParser: BackupParser
    @MockK lateinit var fileDataSource: FileDataSource
    @MockK lateinit var cipherDataSource: CipherDataSource

    private val startBackupImport by lazy {
        StartBackupImportUseCase(
            preferencesRepo, backupRepo, backupParser, fileDataSource, cipherDataSource
        )
    }

    @After override fun tearDown() {
        super.tearDown()

        confirmVerified(
            preferencesRepo, backupRepo, backupParser, fileDataSource, cipherDataSource
        )
    }

    @Test fun import() {
        val fileList = List(getRandomSize()) {
            FileItem(nextShortString(), nextString(), FileType.BACKUP)
        }
        val wrongName = nextString()
        val item = fileList.random()
        val encryptData = nextString()
        val data = nextString()
        val parserResult = mockk<ParserResult.Import>()
        val isSkipImports = Random.nextBoolean()

        val skipResult = ImportResult.Skip(Random.nextInt())

        runBlocking {
            assertEquals(startBackupImport(wrongName, fileList), ImportResult.Error)
        }

        every { fileDataSource.readFile(item.path) } returns null

        runBlocking {
            assertEquals(startBackupImport(item.name, fileList), ImportResult.Error)
        }

        every { fileDataSource.readFile(item.path) } returns encryptData
        every { cipherDataSource.decrypt(encryptData) } returns data
        every { backupParser.convert(data) } returns null

        runBlocking {
            assertEquals(startBackupImport(item.name, fileList), ImportResult.Error)
        }

        every { backupParser.convert(data) } returns parserResult
        every { preferencesRepo.isBackupSkip } returns isSkipImports
        coEvery { backupRepo.insertData(parserResult, isSkipImports) } returns ImportResult.Simple

        runBlocking {
            assertEquals(startBackupImport(item.name, fileList), ImportResult.Simple)
        }

        coEvery { backupRepo.insertData(parserResult, isSkipImports) } returns skipResult

        runBlocking {
            assertEquals(startBackupImport(item.name, fileList), skipResult)
        }

        coVerifySequence {
            fileDataSource.readFile(item.path)

            fileDataSource.readFile(item.path)
            cipherDataSource.decrypt(encryptData)
            backupParser.convert(data)

            repeat(times = 2) {
                fileDataSource.readFile(item.path)
                cipherDataSource.decrypt(encryptData)
                backupParser.convert(data)
                preferencesRepo.isBackupSkip
                backupRepo.insertData(parserResult, isSkipImports)
            }
        }
    }
}