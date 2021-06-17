package sgtmelon.scriptum.data.room.extension

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.data.room.RoomDb
import sgtmelon.scriptum.parent.ParentRoomRepoTest
import kotlin.random.Random

/**
 * Tests for [DaoHelpExtension].
 */
@ExperimentalCoroutinesApi
class DaoHelpExtensionTest : ParentRoomRepoTest() {

    @Test fun checkSafe() {
        val long = Random.nextLong()

        assertEquals(long, long.checkSafe())
        assertNull(RoomDb.UNIQUE_ERROR_ID.checkSafe())
    }

    @Test fun safeOverflow() {
        TODO()
    }
}