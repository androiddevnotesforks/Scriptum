package sgtmelon.scriptum.test.parent

import org.junit.Before
import sgtmelon.scriptum.cleanup.data.provider.RoomProvider
import sgtmelon.scriptum.cleanup.data.room.IRoomWork
import sgtmelon.scriptum.cleanup.data.room.extension.inRoomTest

/**
 * Parent class for Integration tests
 */
abstract class ParentRoomTest : ParentTest(), IRoomWork {

    override val roomProvider = RoomProvider(context)

    protected val crowdList get() = List(QUESTION_LIMIT * (10..50).random()) { it.toLong() }

    @Before override fun setUp() {
        super.setUp()
        inRoomTest { clearAllTables() }
    }

    companion object {
        const val QUESTION_LIMIT = 1000
        const val CROWD_SIZE = 5000
    }
}