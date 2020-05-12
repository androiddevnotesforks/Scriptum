package sgtmelon.scriptum.data.repository.room

import io.mockk.coEvery
import io.mockk.coVerifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.ParentRoomRepoTest
import sgtmelon.scriptum.TestData
import kotlin.random.Random

/**
 * Test for [AlarmRepo].
 */
@ExperimentalCoroutinesApi
class AlarmRepoTest : ParentRoomRepoTest() {

    private val goodAlarmRepo by lazy { AlarmRepo(goodRoomProvider) }
    private val badAlarmRepo by lazy { AlarmRepo(badRoomProvider) }

    private val data = TestData.Notification

    @Test fun insertOrUpdate() {
        TODO()
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
        val item = data.itemList.random()

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
        val itemList = data.itemList

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