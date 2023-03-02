package sgtmelon.scriptum.data.repository.database

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlin.random.Random
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.cleanup.data.room.converter.model.AlarmConverter
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.domain.model.item.NoteAlarm
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.parent.ParentRepoTest
import sgtmelon.scriptum.infrastructure.utils.extensions.note.haveAlarm
import sgtmelon.test.common.nextString

/**
 * Test for [AlarmRepoImpl].
 */
class AlarmRepoImplTest : ParentRepoTest() {

    @MockK lateinit var converter: AlarmConverter

    private val repository by lazy { AlarmRepoImpl(alarmDataSource, converter) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(converter)
    }

    @Test fun insertOrUpdate() {
        val item = mockk<NoteItem>()
        val entity = mockk<AlarmEntity>()

        val alarm = mockk<NoteAlarm>()
        val date = nextString()
        val insertId = Random.nextLong()
        val updateId = Random.nextLong()

        FastMock.noteExtensions()
        every { item.haveAlarm } returns false
        every { item.alarm } returns alarm
        every { alarm.date = date } returns Unit
        every { converter.toEntity(item) } returns entity
        coEvery { alarmDataSource.insert(entity) } returns null
        every { alarm.id = insertId } returns Unit

        runBlocking {
            assertNull(repository.insertOrUpdate(item, date))
        }

        coEvery { alarmDataSource.insert(entity) } returns insertId
        every { alarm.id } returns insertId

        runBlocking {
            assertEquals(repository.insertOrUpdate(item, date), insertId)
        }

        every { item.haveAlarm } returns true
        every { alarm.id } returns updateId

        runBlocking {
            assertEquals(repository.insertOrUpdate(item, date), updateId)
        }

        coVerifySequence {
            item.haveAlarm
            item.alarm
            alarm.date = date
            converter.toEntity(item)
            item.alarm
            alarmDataSource.insert(entity)

            item.haveAlarm
            item.alarm
            alarm.date = date
            converter.toEntity(item)
            item.alarm
            alarmDataSource.insert(entity)
            alarm.id = insertId
            item.alarm
            alarm.id

            item.haveAlarm
            item.alarm
            alarm.date = date
            converter.toEntity(item)
            alarmDataSource.update(entity)
            item.alarm
            alarm.id
        }
    }

    @Test fun delete() {
        val id = Random.nextLong()

        runBlocking {
            repository.delete(id)
        }

        coVerifySequence {
            alarmDataSource.delete(id)
        }
    }


    @Test fun getList() {
        val itemList = mockk<List<NotificationItem>>()

        coEvery { alarmDataSource.getItemList() } returns itemList

        runBlocking {
            assertEquals(repository.getList(), itemList)
        }

        coVerifySequence {
            alarmDataSource.getItemList()
        }
    }

    @Test fun getDateList() {
        val dateList = mockk<List<String>>()

        coEvery { alarmDataSource.getDateList() } returns dateList

        runBlocking {
            assertEquals(repository.getDateList(), dateList)
        }

        coVerifySequence {
            alarmDataSource.getDateList()
        }
    }
}