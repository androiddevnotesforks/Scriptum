package sgtmelon.scriptum.data.room

import android.content.Context
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.ParentTest

/**
 * Test for [RoomDb].
 */
class RoomDbTest : ParentTest() {

    @MockK lateinit var context: Context

    private val roomDb by lazy { RoomDb[context] }

    @Test fun checkInsertIgnore() {
        assertNull(roomDb.checkInsertIgnore(RoomDb.UNIQUE_ERROR_ID))

        for (it in listOf(-2L, 10L, 15L, 23L)) {
            assertEquals(it, roomDb.checkInsertIgnore(it))
        }
    }
}