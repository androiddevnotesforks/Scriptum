package sgtmelon.scriptum.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
        inRoom { clearAllTables() }
    }

    protected fun assertChangeTime(item: NoteItem) = assertEquals(getTime(), item.change)

    companion object {
        const val UNIQUE_ERROR_ID = -1L
    }

}