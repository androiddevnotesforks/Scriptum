package sgtmelon.scriptum

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import sgtmelon.scriptum.data.provider.RoomProvider
import sgtmelon.scriptum.data.room.RoomDb
import sgtmelon.scriptum.data.room.dao.*

/**
 * Parent class for RoomRepo tests.
 */
@ExperimentalCoroutinesApi
abstract class ParentRoomRepoTest : ParentCoTest() {

    @MockK lateinit var roomProvider: RoomProvider

    @MockK lateinit var roomDb: RoomDb

    @MockK lateinit var noteDao: INoteDao
    @MockK lateinit var rollDao: IRollDao
    @MockK lateinit var rollVisibleDao: IRollVisibleDao
    @MockK lateinit var rankDao: IRankDao
    @MockK lateinit var alarmDao: IAlarmDao

    @Before override fun setup() {
        super.setup()

        every { roomProvider.openRoom() } returns roomDb

        every { roomDb.noteDao } returns noteDao
        every { roomDb.rollDao } returns rollDao
        every { roomDb.rollVisibleDao } returns rollVisibleDao
        every { roomDb.rankDao } returns rankDao
        every { roomDb.alarmDao } returns alarmDao
    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(roomProvider, noteDao, rollDao, rollVisibleDao, rankDao, alarmDao)
    }

}