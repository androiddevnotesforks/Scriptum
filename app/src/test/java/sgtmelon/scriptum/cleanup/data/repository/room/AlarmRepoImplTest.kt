package sgtmelon.scriptum.cleanup.data.repository.room

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import java.util.Calendar
import kotlin.random.Random
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.common.utils.getText
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.cleanup.data.room.converter.model.AlarmConverter
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.parent.ParentRepoTest
import sgtmelon.test.common.nextString

/**
 * Test for [AlarmRepoImpl].
 */
class AlarmRepoImplTest : ParentRepoTest() {

    @MockK lateinit var converter: AlarmConverter

    private val repo by lazy { AlarmRepoImpl(alarmDataSource, converter) }
    private val spyRepo by lazy { spyk(repo) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(converter)
    }

    @Test fun `insertOrUpdate via calendar`() {
        val item = mockk<NoteItem>()
        val calendar = mockk<Calendar>()
        val date = nextString()

        FastMock.timeExtension()
        every { calendar.getText() } returns date
        coEvery { spyRepo.insertOrUpdate(item, date) } returns Unit

        runBlocking {
            spyRepo.insertOrUpdate(item, calendar)
        }

        coVerifySequence {
            spyRepo.insertOrUpdate(item, calendar)

            calendar.getText()
            spyRepo.insertOrUpdate(item, date)
        }
    }

    @Test fun insertOrUpdate() {
        val item = mockk<NoteItem>()
        val entity = mockk<AlarmEntity>()

        val date = nextString()
        val insertId = Random.nextLong()

        every { item.alarmDate = date } returns Unit
        every { item.alarmId = insertId } returns Unit

        every { converter.toEntity(item) } returns entity

        every { item.haveAlarm() } returns false
        coEvery { alarmDataSource.insert(entity) } returns null

        runBlocking {
            repo.insertOrUpdate(item, date)
        }

        coEvery { alarmDataSource.insert(entity) } returns insertId

        runBlocking {
            repo.insertOrUpdate(item, date)
        }

        every { item.haveAlarm() } returns true

        runBlocking {
            repo.insertOrUpdate(item, date)
        }

        coVerifySequence {
            item.alarmDate = date
            converter.toEntity(item)
            item.haveAlarm()
            alarmDataSource.insert(entity)

            item.alarmDate = date
            converter.toEntity(item)
            item.haveAlarm()
            alarmDataSource.insert(entity)
            item.alarmId = insertId

            item.alarmDate = date
            converter.toEntity(item)
            item.haveAlarm()
            alarmDataSource.update(entity)
        }
    }

    @Test fun delete() {
        val id = Random.nextLong()

        runBlocking {
            repo.delete(id)
        }

        coVerifySequence {
            alarmDataSource.delete(id)
        }
    }


    @Test fun getItem() {
        val id = Random.nextLong()
        val item = mockk<NotificationItem>()

        coEvery { alarmDataSource.getItem(id) } returns null

        runBlocking {
            assertNull(repo.getItem(id))
        }

        coEvery { alarmDataSource.getItem(id) } returns item

        runBlocking {
            assertEquals(item, repo.getItem(id))
        }

        coVerifySequence {
            alarmDataSource.getItem(id)
            alarmDataSource.getItem(id)
        }
    }

    @Test fun getList() {
        val itemList = mockk<List<NotificationItem>>()

        coEvery { alarmDataSource.getItemList() } returns itemList

        runBlocking {
            assertEquals(repo.getList(), itemList)
        }

        coVerifySequence {
            alarmDataSource.getItemList()
        }
    }

    @Test fun getDateList() {
        val dateList = mockk<List<String>>()

        coEvery { alarmDataSource.getDateList() } returns dateList

        runBlocking {
            assertEquals(repo.getList(), dateList)
        }

        coVerifySequence {
            alarmDataSource.getDateList()
        }
    }
}