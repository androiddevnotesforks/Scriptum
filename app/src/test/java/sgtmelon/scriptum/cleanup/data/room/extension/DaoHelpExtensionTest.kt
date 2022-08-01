package sgtmelon.scriptum.cleanup.data.room.extension

import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.infrastructure.database.annotation.DaoConst
import sgtmelon.scriptum.parent.ParentRoomRepoTest

/**
 * Tests for [DaoHelpExtension].
 */
@ExperimentalCoroutinesApi
class DaoHelpExtensionTest : ParentRoomRepoTest() {

    @Test fun checkSafe() {
        val long = Random.nextLong()

        assertEquals(long, long.checkSafe())
        assertNull(DaoConst.UNIQUE_ERROR_ID.checkSafe())
    }

    @Test fun safeOverflow() {
        TODO()
    }
}