package sgtmelon.scriptum.data.repository.room

import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.ParentRoomRepoTest
import sgtmelon.scriptum.data.room.converter.model.AlarmConverter
import sgtmelon.scriptum.data.room.entity.AlarmEntity
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.NotificationItem
import kotlin.random.Random

/**
 * Test for [AlarmRepo].
 */
@ExperimentalCoroutinesApi
class AlarmRepoTest : ParentRoomRepoTest() {

    private val alarmConverter = mockkClass(AlarmConverter::class)

    private val badAlarmRepo by lazy { AlarmRepo(badRoomProvider, alarmConverter) }
    private val goodAlarmRepo by lazy { AlarmRepo(goodRoomProvider, alarmConverter) }

    @Test fun insertOrUpdate() = startCoTest {
        val noteItem = mockk<NoteItem>()
        val alarmEntity = mockk<AlarmEntity>()

        val date = Random.nextString()
        val insertId = Random.nextLong()

        every { noteItem.alarmDate = date } returns Unit
        every { noteItem.alarmId = insertId } returns Unit

        every { alarmConverter.toEntity(noteItem) } returns alarmEntity
        coEvery { alarmDao.insert(alarmEntity) } returns insertId

        badAlarmRepo.insertOrUpdate(noteItem, date)

        every { noteItem.haveAlarm() } returns false
        goodAlarmRepo.insertOrUpdate(noteItem, date)

        every { noteItem.haveAlarm() } returns true
        goodAlarmRepo.insertOrUpdate(noteItem, date)

        coVerifySequence {
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
            noteItem.alarmDate = date
            alarmConverter.toEntity(noteItem)
            noteItem.haveAlarm()
            alarmDao.insert(alarmEntity)
            noteItem.alarmId = insertId

            goodRoomProvider.openRoom()
            noteItem.alarmDate = date
            alarmConverter.toEntity(noteItem)
            noteItem.haveAlarm()
            alarmDao.update(alarmEntity)
        }
    }

    @Test fun delete() = startCoTest {
        val id = Random.nextLong()

        badAlarmRepo.delete(Random.nextLong())
        goodAlarmRepo.delete(id)

        coVerifySequence {
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
            alarmDao.delete(id)
        }
    }

    @Test fun getItem() = startCoTest {
        val id = Random.nextLong()
        val item = mockk<NotificationItem>()

        assertNull(badAlarmRepo.getItem(Random.nextLong()))

        coEvery { alarmDao.getItem(id) } returns null
        assertEquals(null, goodAlarmRepo.getItem(id))

        coEvery { alarmDao.getItem(id) } returns item
        assertEquals(item, goodAlarmRepo.getItem(id))

        coVerifySequence {
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
            alarmDao.getItem(id)

            goodRoomProvider.openRoom()
            alarmDao.getItem(id)
        }
    }

    @Test fun getList() = startCoTest {
        val itemList = mockk<MutableList<NotificationItem>>()

        assertNull(badAlarmRepo.getList())

        coEvery { alarmDao.getList() } returns itemList
        assertEquals(itemList, goodAlarmRepo.getList())

        coVerifySequence {
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
            alarmDao.getList()
        }
    }

}