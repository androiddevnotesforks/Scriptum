package sgtmelon.scriptum.cleanup.data.repository.room

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.cleanup.data.room.converter.model.AlarmConverter
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.parent.ParentRoomRepoTest
import kotlin.random.Random

/**
 * Test for [AlarmRepo].
 */
@ExperimentalCoroutinesApi
class AlarmRepoTest : ParentRoomRepoTest() {

    @MockK lateinit var converter: AlarmConverter

    private val alarmRepo by lazy { AlarmRepo(roomProvider, converter) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(converter)
    }

    @Test fun insertOrUpdate() = startCoTest {
        val noteItem = mockk<NoteItem>()
        val alarmEntity = mockk<AlarmEntity>()

        val date = nextString()
        val insertId = Random.nextLong()

        every { noteItem.alarmDate = date } returns Unit
        every { noteItem.alarmId = insertId } returns Unit

        every { converter.toEntity(noteItem) } returns alarmEntity
        coEvery { alarmDao.insert(alarmEntity) } returns insertId

        every { noteItem.haveAlarm() } returns false
        alarmRepo.insertOrUpdate(noteItem, date)

        every { noteItem.haveAlarm() } returns true
        alarmRepo.insertOrUpdate(noteItem, date)

        coVerifySequence {
            roomProvider.openRoom()
            noteItem.alarmDate = date
            converter.toEntity(noteItem)
            noteItem.haveAlarm()
            alarmDao.insert(alarmEntity)
            noteItem.alarmId = insertId

            roomProvider.openRoom()
            noteItem.alarmDate = date
            converter.toEntity(noteItem)
            noteItem.haveAlarm()
            alarmDao.update(alarmEntity)
        }
    }

    @Test fun delete() = startCoTest {
        val id = Random.nextLong()

        alarmRepo.delete(id)

        coVerifySequence {
            roomProvider.openRoom()
            alarmDao.delete(id)
        }
    }


    @Test fun getItem() = startCoTest {
        val id = Random.nextLong()
        val item = mockk<NotificationItem>()

        coEvery { alarmDao.getItem(id) } returns null
        assertNull(alarmRepo.getItem(id))

        coEvery { alarmDao.getItem(id) } returns item
        assertEquals(item, alarmRepo.getItem(id))

        coVerifySequence {
            roomProvider.openRoom()
            alarmDao.getItem(id)

            roomProvider.openRoom()
            alarmDao.getItem(id)
        }
    }

    @Test fun getList() = startCoTest {
        val itemList = mockk<MutableList<NotificationItem>>()

        coEvery { alarmDao.getList() } returns itemList
        assertEquals(itemList, alarmRepo.getList())

        coVerifySequence {
            roomProvider.openRoom()
            alarmDao.getList()
        }
    }


    @Test fun getAlarmBackup() = startCoTest {
        val alarmList = mockk<List<AlarmEntity>>()
        val noteIdList = mockk<List<Long>>()

        coEvery { alarmDao.get(noteIdList) } returns alarmList

        assertEquals(alarmList, alarmRepo.getAlarmBackup(noteIdList))

        coVerifySequence {
            roomProvider.openRoom()
            alarmDao.get(noteIdList)
        }
    }
}