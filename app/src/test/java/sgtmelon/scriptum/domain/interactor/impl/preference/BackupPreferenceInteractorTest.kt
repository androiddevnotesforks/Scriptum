package sgtmelon.scriptum.domain.interactor.impl.preference

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import sgtmelon.common.nextShortString
import sgtmelon.common.nextString
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.BackupRepo
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.repository.room.callback.IBackupRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.data.room.backup.IBackupParser
import sgtmelon.scriptum.data.room.entity.*
import sgtmelon.scriptum.domain.model.annotation.FileType
import sgtmelon.scriptum.domain.model.item.FileItem
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.domain.model.result.ExportResult
import sgtmelon.scriptum.domain.model.result.ImportResult
import sgtmelon.scriptum.domain.model.result.ParserResult
import sgtmelon.scriptum.parent.ParentInteractorTest
import sgtmelon.scriptum.presentation.control.cipher.ICipherControl
import sgtmelon.scriptum.presentation.control.file.IFileControl
import kotlin.random.Random

/**
 * Test for [BackupPreferenceInteractor].
 */
@ExperimentalCoroutinesApi
class BackupPreferenceInteractorTest : ParentInteractorTest() {

    @MockK lateinit var preferenceRepo: IPreferenceRepo
    @MockK lateinit var alarmRepo: IAlarmRepo
    @MockK lateinit var rankRepo: IRankRepo
    @MockK lateinit var noteRepo: INoteRepo
    @MockK lateinit var backupRepo: IBackupRepo

    @MockK lateinit var backupParser: IBackupParser
    @MockK lateinit var fileControl: IFileControl
    @MockK lateinit var cipherControl: ICipherControl

    private val interactor by lazy {
        BackupPreferenceInteractor(
            preferenceRepo, alarmRepo, rankRepo, noteRepo, backupRepo,
            backupParser, fileControl, cipherControl
        )
    }
    private val spyInteractor by lazy { spyk(interactor) }

    @Before override fun setup() {
        super.setup()
        assertNull(interactor.fileList)
    }

    @After override fun tearDown() {
        super.tearDown()

        confirmVerified(
            preferenceRepo, alarmRepo, rankRepo, noteRepo, backupRepo,
            backupParser, fileControl, cipherControl
        )
    }


    @Test fun getFileList() = startCoTest {
        val list = mockk<List<FileItem>>()

        coEvery { fileControl.getFileList(FileType.BACKUP) } returns list

        assertEquals(list, interactor.getFileList())
        assertEquals(list, interactor.fileList)

        coEvery { fileControl.getFileList(FileType.BACKUP) } returns emptyList()

        assertEquals(list, interactor.getFileList())

        coVerifySequence {
            fileControl.getFileList(FileType.BACKUP)
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

        coEvery { noteRepo.getNoteBackup() } returns noteList
        coEvery { noteRepo.getRollBackup(noteIdList) } returns rollList
        coEvery { noteRepo.getRollVisibleBackup(noteIdList) } returns rollVisibleList
        coEvery { rankRepo.getRankBackup() } returns rankList
        coEvery { alarmRepo.getAlarmBackup(noteIdList) } returns alarmList

        every { backupParser.collect(parserResult) } returns data
        every { cipherControl.encrypt(data) } returns encryptData
        every { fileControl.getTimeName(FileType.BACKUP) } returns timeName
        every { fileControl.writeFile(timeName, encryptData) } returns null

        assertEquals(ExportResult.Error, interactor.export())

        every { fileControl.writeFile(timeName, encryptData) } returns path

        assertEquals(ExportResult.Success(path), interactor.export())

        coVerifySequence {
            repeat(times = 2) {
                noteRepo.getNoteBackup()
                noteRepo.getRollBackup(noteIdList)
                noteRepo.getRollVisibleBackup(noteIdList)
                rankRepo.getRankBackup()
                alarmRepo.getAlarmBackup(noteIdList)

                backupParser.collect(parserResult)
                cipherControl.encrypt(data)
                fileControl.getTimeName(FileType.BACKUP)
                fileControl.writeFile(timeName, encryptData)
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
        val importSkip = Random.nextBoolean()
        val backupModel = mockk<BackupRepo.Model>()

        val skipResult = ImportResult.Skip(Random.nextInt())

        coEvery { spyInteractor.getFileList() } returns fileList

        assertEquals(ImportResult.Error, spyInteractor.import(wrongName))

        every { fileControl.readFile(item.path) } returns null

        assertEquals(ImportResult.Error, spyInteractor.import(item.name))

        every { fileControl.readFile(item.path) } returns encryptData
        every { cipherControl.decrypt(encryptData) } returns data
        every { backupParser.parse(data) } returns null

        assertEquals(ImportResult.Error, spyInteractor.import(item.name))

        mockkObject(BackupRepo.Model)
        every { BackupRepo.Model[parserResult] } returns backupModel

        every { backupParser.parse(data) } returns parserResult
        every { preferenceRepo.importSkip } returns importSkip
        coEvery { backupRepo.insertData(backupModel, importSkip) } returns ImportResult.Simple

        assertEquals(ImportResult.Simple, spyInteractor.import(item.name))

        coEvery { backupRepo.insertData(backupModel, importSkip) } returns skipResult

        assertEquals(skipResult, spyInteractor.import(item.name))

        coVerifySequence {
            spyInteractor.import(wrongName)
            spyInteractor.getFileList()

            spyInteractor.import(item.name)
            spyInteractor.getFileList()
            fileControl.readFile(item.path)

            spyInteractor.import(item.name)
            spyInteractor.getFileList()
            fileControl.readFile(item.path)
            cipherControl.decrypt(encryptData)
            backupParser.parse(data)

            repeat(times = 2) {
                spyInteractor.import(item.name)
                spyInteractor.getFileList()
                fileControl.readFile(item.path)
                cipherControl.decrypt(encryptData)
                backupParser.parse(data)
                preferenceRepo.importSkip
                BackupRepo.Model[parserResult]
                backupRepo.insertData(backupModel, importSkip)
            }
        }
    }
}