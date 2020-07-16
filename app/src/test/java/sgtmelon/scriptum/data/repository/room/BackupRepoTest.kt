package sgtmelon.scriptum.data.repository.room

import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.nextShortString
import sgtmelon.extension.nextString
import sgtmelon.scriptum.ParentRoomRepoTest
import sgtmelon.scriptum.data.room.entity.AlarmEntity
import sgtmelon.scriptum.data.room.entity.NoteEntity
import sgtmelon.scriptum.data.room.entity.RollEntity
import sgtmelon.scriptum.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.domain.model.data.DbData.Alarm
import sgtmelon.scriptum.domain.model.data.DbData.Roll
import sgtmelon.scriptum.domain.model.data.DbData.RollVisible
import sgtmelon.scriptum.domain.model.result.ImportResult
import kotlin.random.Random

/**
 * Test for [BackupRepo].
 */
@ExperimentalCoroutinesApi
class BackupRepoTest : ParentRoomRepoTest() {

    private val backupRepo by lazy { BackupRepo(roomProvider) }
    private val spyBackupRepo by lazy { spyk(backupRepo) }

    @Test fun insertData() = startCoTest {
        val model = mockk<BackupRepo.Model>()
        val noteList = mockk<MutableList<NoteEntity>>()
        val size = Random.nextInt()
        val removeList = mockk<List<NoteEntity>>()

        every { model.noteList } returns noteList
        every { noteList.size } returns size

        coEvery { spyBackupRepo.getRemoveNoteList(model, roomDb) } returns removeList
        coEvery { spyBackupRepo.clearList(removeList, model) } returns Unit
        coEvery { spyBackupRepo.clearRankList(model, roomDb) } returns Unit
        coEvery { spyBackupRepo.clearAlarmList(model, roomDb) } returns Unit
        coEvery { spyBackupRepo.insertNoteList(model, roomDb) } returns Unit
        coEvery { spyBackupRepo.insertRollList(model, roomDb) } returns Unit
        coEvery { spyBackupRepo.insertRollVisibleList(model, roomDb) } returns Unit
        coEvery { spyBackupRepo.insertRankList(model, roomDb) } returns Unit
        coEvery { spyBackupRepo.insertAlarmList(model, roomDb) } returns Unit

        assertEquals(ImportResult.Simple, spyBackupRepo.insertData(model, importSkip = false))

        val skipResult = ImportResult.Skip(skipCount = 0)
        assertEquals(skipResult, spyBackupRepo.insertData(model, importSkip = true))

        coVerify {
            spyBackupRepo.insertData(model, importSkip = false)
            roomProvider.openRoom()
            model.noteList
            noteList.size
            spyBackupRepo.clearRankList(model, roomDb)
            spyBackupRepo.clearAlarmList(model, roomDb)
            spyBackupRepo.insertNoteList(model, roomDb)
            spyBackupRepo.insertRollList(model, roomDb)
            spyBackupRepo.insertRollVisibleList(model, roomDb)
            spyBackupRepo.insertRankList(model, roomDb)
            spyBackupRepo.insertAlarmList(model, roomDb)

            spyBackupRepo.insertData(model, importSkip = true)
            roomProvider.openRoom()
            model.noteList
            noteList.size
            spyBackupRepo.getRemoveNoteList(model, roomDb)
            spyBackupRepo.clearList(removeList, model)
            spyBackupRepo.clearRankList(model, roomDb)
            spyBackupRepo.clearAlarmList(model, roomDb)
            spyBackupRepo.insertNoteList(model, roomDb)
            spyBackupRepo.insertRollList(model, roomDb)
            spyBackupRepo.insertRollVisibleList(model, roomDb)
            spyBackupRepo.insertRankList(model, roomDb)
            spyBackupRepo.insertAlarmList(model, roomDb)
            model.noteList
            noteList.size
        }
    }

    @Test fun getRemoveNoteList() {
        TODO()
    }

    @Test fun needSkipTextNote() {
        val item = mockk<NoteEntity>()
        val name = nextShortString()
        val text = nextString()

        val list = MutableList(size = 5) {
            NoteEntity(name = nextShortString(), text = nextString())
        }

        every { item.name } returns name
        every { item.text } returns text

        assertFalse(backupRepo.needSkipTextNote(item, list))

        list.add(item)

        assertTrue(backupRepo.needSkipTextNote(item, list))
    }

    @Test fun needSkipRollNote() {
        TODO()
    }

    @Test fun clearList() {
        TODO()
    }

    @Test fun clearRankList() {
        TODO()
    }

    @Test fun clearAlarmList() {
        TODO()
    }

    @Test fun insertNoteList() {
        TODO()
    }

    @Test fun insertRollList() = startCoTest {
        val model = mockk<BackupRepo.Model>()
        val item = mockk<RollEntity>(relaxUnitFun = true)
        val newId = Random.nextLong()

        every { model.rollList } returns mutableListOf(item)
        coEvery { rollDao.insert(item) } returns newId

        backupRepo.insertRollList(model, roomDb)

        coVerifySequence {
            model.rollList

            roomDb.rollDao
            item.id = Roll.Default.ID
            rollDao.insert(item)
            item.id = newId
        }
    }

    @Test fun insertRollVisibleList() = startCoTest {
        val model = mockk<BackupRepo.Model>()
        val item = mockk<RollVisibleEntity>(relaxUnitFun = true)
        val newId = Random.nextLong()

        every { model.rollVisibleList } returns mutableListOf(item)
        coEvery { rollVisibleDao.insert(item) } returns newId

        backupRepo.insertRollVisibleList(model, roomDb)

        coVerifySequence {
            model.rollVisibleList

            roomDb.rollVisibleDao
            item.id = RollVisible.Default.ID
            rollVisibleDao.insert(item)
            item.id = newId
        }
    }

    @Test fun insertRankList() {
        TODO()
    }

    @Test fun insertAlarmList() = startCoTest {
        val model = mockk<BackupRepo.Model>()
        val item = mockk<AlarmEntity>(relaxUnitFun = true)
        val newId = Random.nextLong()

        every { model.alarmList } returns mutableListOf(item)
        coEvery { alarmDao.insert(item) } returns newId

        backupRepo.insertAlarmList(model, roomDb)

        coVerifySequence {
            model.alarmList

            roomDb.alarmDao
            item.id = Alarm.Default.ID
            alarmDao.insert(item)
            item.id = newId
        }
    }

}