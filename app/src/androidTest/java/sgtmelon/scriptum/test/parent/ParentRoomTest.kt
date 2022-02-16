package sgtmelon.scriptum.test.parent

import org.junit.Before
import sgtmelon.scriptum.data.provider.RoomProvider
import sgtmelon.scriptum.data.room.IRoomWork
import sgtmelon.scriptum.data.room.extension.inRoomTest

/**
 * Parent class for Integration tests
 */
abstract class ParentRoomTest : ParentTest(), IRoomWork {

    override val roomProvider = RoomProvider(context)

    protected val crowdList get() = List(QUESTION_LIMIT) { it.toLong() }

    @Before override fun setup() {
        super.setup()
        inRoomTest { clearAllTables() }
    }

    companion object {
        const val QUESTION_LIMIT = 1000
        const val CROWD_SIZE = 5000
    }
}