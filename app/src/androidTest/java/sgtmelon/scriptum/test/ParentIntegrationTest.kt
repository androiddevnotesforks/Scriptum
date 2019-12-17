package sgtmelon.scriptum.test

import org.junit.Assert.assertEquals
import sgtmelon.extension.getTime
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.room.IRoomWork

/**
 * Parent class for Integration tests
 */
abstract class ParentIntegrationTest : ParentTest(), IRoomWork {

    override fun setUp() {
        super.setUp()
        inRoomTest { clearAllTables() }
    }

    protected fun assertChangeTime(item: NoteItem) = assertEquals(getTime(), item.change)

    companion object {
        const val UNIQUE_ERROR_ID = -1L
    }

}