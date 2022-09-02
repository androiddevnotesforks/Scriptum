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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import sgtmelon.scriptum.cleanup.data.repository.room.BackupRepoImpl
import sgtmelon.scriptum.cleanup.data.repository.room.callback.AlarmRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.BackupRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.RankRepo
import sgtmelon.scriptum.cleanup.data.room.backup.IBackupParser
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.cleanup.domain.model.annotation.FileType
import sgtmelon.scriptum.cleanup.domain.model.item.FileItem
import sgtmelon.scriptum.cleanup.domain.model.key.NoteType
import sgtmelon.scriptum.cleanup.domain.model.result.ExportResult
import sgtmelon.scriptum.cleanup.domain.model.result.ImportResult
import sgtmelon.scriptum.cleanup.domain.model.result.ParserResult
import sgtmelon.scriptum.cleanup.parent.ParentInteractorTest
import sgtmelon.scriptum.cleanup.presentation.control.cipher.CipherDataSource
import sgtmelon.scriptum.data.dataSource.system.FileDataSource
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.test.common.nextShortString
import sgtmelon.test.common.nextString

/**
 * Test for [BackupPreferenceInteractor].
 */
@ExperimentalCoroutinesApi
class BackupPreferenceInteractorTest : ParentInteractorTest() {

    @MockK lateinit var preferencesRepo: PreferencesRepo
    @MockK lateinit var alarmRepo: AlarmRepo
    @MockK lateinit var rankRepo: RankRepo
    @MockK lateinit var noteRepo: NoteRepo
    @MockK lateinit var backupRepo: BackupRepo

    @MockK lateinit var backupParser: IBackupParser
    @MockK lateinit var fileDataSource: FileDataSource
    @MockK lateinit var cipherControl: CipherDataSource

    private val interactor by lazy {
        BackupPreferenceInteractor(
            preferencesRepo, alarmRepo, rankRepo, noteRepo, backupRepo,
            backupParser, fileDataSource, cipherControl
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
            preferencesRepo, alarmRepo, rankRepo, noteRepo, backupRepo,
            backupParser, fileDataSource, cipherControl
        )
    }


    @Test fun getFileList() = startCoTest {
        val list = mockk<List<FileItem>>()

        coEvery { fileDataSource.getFileList(FileType.BACKUP) } returns list

        assertEquals(list, interactor.getFileList())
        assertEquals(list, interactor.fileList)

        coEvery { fileDataSource.getFileList(FileType.BACKUP) } returns emptyList()

        assertEquals(list, interactor.getFileList())

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

    @Test fun export() = startCoTest {
        val noteList = listOf(
            NoteEntity(id = Random.nextLong(), type = NoteType.TEXT),
            NoteEntity(id = Random.nextLong(), type = NoteType.ROLL),
            NoteEntity(id = Random.nextLong(), type = NoteType.ROLL),
            NoteEntity(id = Random.nextLong(), type = NoteType.TEXT)
        )

        val noteIdList = noteList.filter { it.type == NoteType.ROLL }.map { it.id }

        val rollList = mockk<List<RollEntity>>()
        val rollVisibleList = mockk<List<RollVisibleEntity>>()
        val rankList = mockk<List<RankEntity>>()
        val alarmList = mockk<List<AlarmEntity>>()

        val parserResult = ParserResult(noteList, rollList, rollVisibleList, rankList, alarmList)

        val data = nextString()
        val encryptData = nextString()
        val timeName = nextString()
        val path = nextString()

        coEvery { noteRepo.getNoteBackupList() } returns noteList
        coEvery { noteRepo.getRollBackupList(noteIdList) } returns rollList
        coEvery { noteRepo.getRollVisibleBackupList(noteIdList) } returns rollVisibleList
        coEvery { rankRepo.getRankBackup() } returns rankList
        coEvery { alarmRepo.getBackupList(noteIdList) } returns alarmList

        every { backupParser.collect(parserResult) } returns data
        every { cipherControl.encrypt(data) } returns encryptData
        every { fileDataSource.getTimeName(FileType.BACKUP) } returns timeName
        every { fileDataSource.writeFile(timeName, encryptData) } returns null

        assertEquals(ExportResult.Error, interactor.export())

        every { fileDataSource.writeFile(timeName, encryptData) } returns path

        assertEquals(ExportResult.Success(path), interactor.export())

        coVerifySequence {
            repeat(times = 2) {
                noteRepo.getNoteBackupList()
                noteRepo.getRollBackupList(noteIdList)
                noteRepo.getRollVisibleBackupList(noteIdList)
                rankRepo.getRankBackup()
                alarmRepo.getBackupList(noteIdList)

                backupParser.collect(parserResult)
                cipherControl.encrypt(data)
                fileDataSource.getTimeName(FileType.BACKUP)
                fileDataSource.writeFile(timeName, encryptData)
            }
        }
    }

    @Test fun import() = startCoTest {
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

        assertEquals(ImportResult.Error, spyInteractor.import(wrongName))

        every { fileDataSource.readFile(item.path) } returns null

        assertEquals(ImportResult.Error, spyInteractor.import(item.name))

        every { fileDataSource.readFile(item.path) } returns encryptData
        every { cipherControl.decrypt(encryptData) } returns data
        every { backupParser.parse(data) } returns null

        assertEquals(ImportResult.Error, spyInteractor.import(item.name))

        mockkObject(BackupRepoImpl.Model)
        every { BackupRepoImpl.Model[parserResult] } returns backupModel

        every { backupParser.parse(data) } returns parserResult
        every { preferencesRepo.isBackupSkipImports } returns isSkipImports
        coEvery { backupRepo.insertData(backupModel, isSkipImports) } returns ImportResult.Simple

        assertEquals(ImportResult.Simple, spyInteractor.import(item.name))

        coEvery { backupRepo.insertData(backupModel, isSkipImports) } returns skipResult

        assertEquals(skipResult, spyInteractor.import(item.name))

        coVerifySequence {
            spyInteractor.import(wrongName)
            spyInteractor.getFileList()

            spyInteractor.import(item.name)
            spyInteractor.getFileList()
            fileDataSource.readFile(item.path)

            spyInteractor.import(item.name)
            spyInteractor.getFileList()
            fileDataSource.readFile(item.path)
            cipherControl.decrypt(encryptData)
            backupParser.parse(data)

            repeat(times = 2) {
                spyInteractor.import(item.name)
                spyInteractor.getFileList()
                fileDataSource.readFile(item.path)
                cipherControl.decrypt(encryptData)
                backupParser.parse(data)
                preferencesRepo.isBackupSkipImports
                BackupRepoImpl.Model[parserResult]
                backupRepo.insertData(backupModel, isSkipImports)
            }
        }
    }
}