package sgtmelon.scriptum.test

import org.junit.Assert.assertEquals
import sgtmelon.extension.getTime
import sgtmelon.scriptum.room.IRoomWork

/**
 * Parent class for Integration tests
 */
abstract class ParentIntegrationTest : ParentTest(), IRoomWork {

    override fun setUp() {
        super.setUp()
        inRoom { clearAllTables() }
    }

    fun assertCurrentTime(time: String) = assertEquals(getTime(), time)

    companion object {
        const val UNIQUE_ERROR_ID = -1L
    }

}