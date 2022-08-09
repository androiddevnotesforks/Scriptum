package sgtmelon.scriptum.cleanup.parent

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import sgtmelon.scriptum.cleanup.data.provider.RoomProvider
import sgtmelon.scriptum.cleanup.data.room.RoomDb
import sgtmelon.scriptum.cleanup.data.room.dao.INoteDao
import sgtmelon.scriptum.cleanup.data.room.dao.IRankDao
import sgtmelon.scriptum.cleanup.data.room.dao.IRollDao
import sgtmelon.scriptum.infrastructure.database.dao.AlarmDao
import sgtmelon.scriptum.infrastructure.database.dao.IRollVisibleDao

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
    @MockK lateinit var alarmDao: AlarmDao

    @Before override fun setUp() {
        super.setUp()

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