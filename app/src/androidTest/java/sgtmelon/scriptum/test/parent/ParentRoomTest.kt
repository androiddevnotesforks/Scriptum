package sgtmelon.scriptum.test.parent

import org.junit.Before
import sgtmelon.scriptum.data.provider.RoomProvider
import sgtmelon.scriptum.data.room.IRoomWork

/**
 * Parent class for Integration tests
 */
abstract class ParentRoomTest : ParentTest(), IRoomWork {

    override val roomProvider = RoomProvider(context)

    @Before override fun setup() {
        super.setup()
        inRoomTest { clearAllTables() }
    }
}