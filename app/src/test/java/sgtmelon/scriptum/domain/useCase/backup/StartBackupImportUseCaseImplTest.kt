package sgtmelon.scriptum.domain.useCase.backup

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkObject
import kotlin.random.Random
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.cleanup.data.repository.room.BackupRepoImpl
import sgtmelon.scriptum.cleanup.data.repository.room.callback.BackupRepo
import sgtmelon.scriptum.cleanup.data.room.backup.BackupParser
import sgtmelon.scriptum.cleanup.domain.model.result.ParserResult
import sgtmelon.scriptum.cleanup.parent.ParentTest
import sgtmelon.scriptum.data.dataSource.system.CipherDataSource
import sgtmelon.scriptum.data.dataSource.system.FileDataSource
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.model.result.ImportResult
import sgtmelon.scriptum.infrastructure.model.item.FileItem
import sgtmelon.test.common.nextShortString
import sgtmelon.test.common.nextString

/**
 * Test for [StartBackupImportUseCaseImpl].
 */
class StartBackupImportUseCaseImplTest : ParentTest() {

    @MockK lateinit var preferencesRepo: PreferencesRepo
    @MockK lateinit var backupRepo: BackupRepo

    @MockK lateinit var backupParser: BackupParser
    @MockK lateinit var fileDataSource: FileDataSource
    @MockK lateinit var cipherDataSource: CipherDataSource

    private val startBackupImport by lazy {
        StartBackupImportUseCaseImpl(
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
        val fileList = List(size = 5) { FileItem(nextShortString(), nextString()) }
        val wrongName = nextString()
        val item = fileList.random()
        val encryptData = nextString()
        val data = nextString()
        val parserResult = mockk<ParserResult>()
        val isSkipImports = Random.nextBoolean()
        val backupModel = mockk<BackupRepoImpl.Model>()

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

        mockkObject(BackupRepoImpl.Model)
        every { BackupRepoImpl.Model[parserResult] } returns backupModel

        every { backupParser.convert(data) } returns parserResult
        every { preferencesRepo.isBackupSkipImports } returns isSkipImports
        coEvery { backupRepo.insertData(backupModel, isSkipImports) } returns ImportResult.Simple

        runBlocking {
            assertEquals(startBackupImport(item.name, fileList), ImportResult.Simple)
        }

        coEvery { backupRepo.insertData(backupModel, isSkipImports) } returns skipResult

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
                preferencesRepo.isBackupSkipImports
                BackupRepoImpl.Model[parserResult]
                backupRepo.insertData(backupModel, isSkipImports)
            }
        }
    }
}