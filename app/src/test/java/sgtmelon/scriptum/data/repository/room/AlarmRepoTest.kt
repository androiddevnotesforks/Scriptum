package sgtmelon.scriptum.data.repository.room

import io.mockk.coEvery
import io.mockk.coVerifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.scriptum.ParentRoomRepoTest
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.data.room.converter.model.AlarmConverter
import kotlin.random.Random

/**
 * Test for [AlarmRepo].
 */
@ExperimentalCoroutinesApi
class AlarmRepoTest : ParentRoomRepoTest() {

    private val badAlarmRepo by lazy { AlarmRepo(badRoomProvider) }
    private val goodAlarmRepo by lazy { AlarmRepo(goodRoomProvider) }

    @Test fun insertOrUpdate() = startCoTest {
        val updateItem = TestData.Note.firstNote
        val insertItem = TestData.Note.secondNote

        val date = TestData.uniqueString
        val insertId = Random.nextLong()

        val converter = AlarmConverter()
        val updateEntity = converter.toEntity(updateItem.deepCopy(alarmDate = date))
        val insertEntity = converter.toEntity(insertItem.deepCopy(alarmDate = date))

        badAlarmRepo.insertOrUpdate(updateItem, date)
        assertNotEquals(updateItem.alarmDate, date)

        badAlarmRepo.insertOrUpdate(insertItem, date)
        assertNotEquals(insertItem.alarmId, insertId)
        assertNotEquals(insertItem.alarmDate, date)

        goodAlarmRepo.insertOrUpdate(updateItem, date)
        assertEquals(updateItem.alarmDate, date)

        coEvery { alarmDao.insert(any()) } returns insertId

        goodAlarmRepo.insertOrUpdate(insertItem, date)
        assertEquals(insertItem.alarmId, insertId)
        assertEquals(insertItem.alarmDate, date)

        coVerifySequence {
            badRoomProvider.openRoom()
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
            alarmDao.update(updateEntity)

            goodRoomProvider.openRoom()
            alarmDao.insert(insertEntity)
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
        val item = TestData.Notification.itemList.random()

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
        val itemList = TestData.Notification.itemList

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