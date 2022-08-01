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
import sgtmelon.common.utils.nextShortString
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.cleanup.data.repository.room.BackupRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.IBackupRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.IRankRepo
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
import sgtmelon.scriptum.cleanup.presentation.control.cipher.ICipherControl
import sgtmelon.scriptum.cleanup.presentation.control.file.IFileControl
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo

/**
 * Test for [BackupPreferenceInteractor].
 */
@ExperimentalCoroutinesApi
class BackupPreferenceInteractorTest : ParentInteractorTest() {

    @MockK lateinit var preferencesRepo: PreferencesRepo
    @MockK lateinit var alarmRepo: IAlarmRepo
    @MockK lateinit var rankRepo: IRankRepo
    @MockK lateinit var noteRepo: INoteRepo
    @MockK lateinit var backupRepo: IBackupRepo

    @MockK lateinit var backupParser: IBackupParser
    @MockK lateinit var fileControl: IFileControl
    @MockK lateinit var cipherControl: ICipherControl

    private val interactor by lazy {
        BackupPreferenceInteractor(
            preferencesRepo, alarmRepo, rankRepo, noteRepo, backupRepo,
            backupParser, fileControl, cipherControl
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
        val isSkipImports = Random.nextBoolean()
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
                preferencesRepo.isBackupSkipImports
                BackupRepo.Model[parserResult]
                backupRepo.insertData(backupModel, isSkipImports)
            }
        }
    }
}