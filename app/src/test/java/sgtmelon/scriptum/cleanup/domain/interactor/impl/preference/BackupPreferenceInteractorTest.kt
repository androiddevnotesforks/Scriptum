package sgtmelon.scriptum.cleanup.domain.interactor.impl.preference

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.spyk
import kotlin.random.Random
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import sgtmelon.scriptum.cleanup.data.repository.room.BackupRepoImpl
import sgtmelon.scriptum.cleanup.data.repository.room.callback.BackupRepo
import sgtmelon.scriptum.cleanup.data.room.backup.IBackupParser
import sgtmelon.scriptum.cleanup.domain.model.annotation.FileType
import sgtmelon.scriptum.cleanup.domain.model.item.FileItem
import sgtmelon.scriptum.cleanup.domain.model.result.ExportResult
import sgtmelon.scriptum.cleanup.domain.model.result.ImportResult
import sgtmelon.scriptum.cleanup.domain.model.result.ParserResult
import sgtmelon.scriptum.cleanup.parent.ParentTest
import sgtmelon.scriptum.data.dataSource.system.CipherDataSource
import sgtmelon.scriptum.data.dataSource.system.FileDataSource
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.test.common.nextShortString
import sgtmelon.test.common.nextString

/**
 * Test for [BackupPreferenceInteractor].
 */
class BackupPreferenceInteractorTest : ParentTest() {

    @MockK lateinit var preferencesRepo: PreferencesRepo
    @MockK lateinit var backupRepo: BackupRepo

    @MockK lateinit var backupParser: IBackupParser
    @MockK lateinit var fileDataSource: FileDataSource
    @MockK lateinit var cipherDataSource: CipherDataSource

    private val interactor by lazy {
        BackupPreferenceInteractor(
            preferencesRepo, backupRepo, backupParser, fileDataSource, cipherDataSource
        )
    }
    private val spyInteractor by lazy { spyk(interactor) }

    @Before override fun setUp() {
        super.setUp()
        assertNull(interactor.fileList)
    }

    @After override fun tearDown() {
        super.tearDown()

        confirmVerified(
            preferencesRepo, backupRepo, backupParser, fileDataSource, cipherDataSource
        )
    }


    @Test fun getFileList() {
        val list = mockk<List<FileItem>>()

        coEvery { fileDataSource.getFileList(FileType.BACKUP) } returns list

        runBlocking {
            assertEquals(list, interactor.getFileList())
        }

        assertEquals(list, interactor.fileList)

        coEvery { fileDataSource.getFileList(FileType.BACKUP) } returns emptyList()

        runBlocking {
            assertEquals(list, interactor.getFileList())
        }

        coVerifySequence {
            fileDataSource.getFileList(FileType.BACKUP)
        }
    }

    @Test fun resetFileList() {
        interactor.fileList = mockk()

        assertNotNull(interactor.fileList)
        interactor.resetFileList()
        assertNull(interactor.fileList)
    }

    @Test fun export() {
        val parserResult = mockk<ParserResult>()

        val data = nextString()
        val encryptData = nextString()
        val timeName = nextString()
        val path = nextString()

        coEvery { backupRepo.getData() } returns parserResult
        every { backupParser.collect(parserResult) } returns data
        every { cipherDataSource.encrypt(data) } returns encryptData
        every { fileDataSource.getTimeName(FileType.BACKUP) } returns timeName
        every { fileDataSource.writeFile(timeName, encryptData) } returns null

        runBlocking {
            assertEquals(interactor.export(), ExportResult.Error)
        }

        every { fileDataSource.writeFile(timeName, encryptData) } returns path

        runBlocking {
            assertEquals(interactor.export(), ExportResult.Success(path))
        }

        coVerifySequence {
            repeat(times = 2) {
                backupRepo.getData()
                backupParser.collect(parserResult)
                cipherDataSource.encrypt(data)
                fileDataSource.getTimeName(FileType.BACKUP)
                fileDataSource.writeFile(timeName, encryptData)
            }
        }
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

        coEvery { spyInteractor.getFileList() } returns fileList

        runBlocking {
            assertEquals(spyInteractor.import(wrongName), ImportResult.Error)
        }

        every { fileDataSource.readFile(item.path) } returns null

        runBlocking {
            assertEquals(spyInteractor.import(item.name), ImportResult.Error)
        }

        every { fileDataSource.readFile(item.path) } returns encryptData
        every { cipherDataSource.decrypt(encryptData) } returns data
        every { backupParser.parse(data) } returns null

        runBlocking {
            assertEquals(spyInteractor.import(item.name), ImportResult.Error)
        }

        mockkObject(BackupRepoImpl.Model)
        every { BackupRepoImpl.Model[parserResult] } returns backupModel

        every { backupParser.parse(data) } returns parserResult
        every { preferencesRepo.isBackupSkipImports } returns isSkipImports
        coEvery { backupRepo.insertData(backupModel, isSkipImports) } returns ImportResult.Simple

        runBlocking {
            assertEquals(spyInteractor.import(item.name), ImportResult.Simple)
        }

        coEvery { backupRepo.insertData(backupModel, isSkipImports) } returns skipResult

        runBlocking {
            assertEquals(spyInteractor.import(item.name), skipResult)
        }

        coVerifySequence {
            spyInteractor.import(wrongName)
            spyInteractor.getFileList()

            spyInteractor.import(item.name)
            spyInteractor.getFileList()
            fileDataSource.readFile(item.path)

            spyInteractor.import(item.name)
            spyInteractor.getFileList()
            fileDataSource.readFile(item.path)
            cipherDataSource.decrypt(encryptData)
            backupParser.parse(data)

            repeat(times = 2) {
                spyInteractor.import(item.name)
                spyInteractor.getFileList()
                fileDataSource.readFile(item.path)
                cipherDataSource.decrypt(encryptData)
                backupParser.parse(data)
                preferencesRepo.isBackupSkipImports
                BackupRepoImpl.Model[parserResult]
                backupRepo.insertData(backupModel, isSkipImports)
            }
        }
    }
}