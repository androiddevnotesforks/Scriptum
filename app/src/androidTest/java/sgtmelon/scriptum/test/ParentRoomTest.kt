package sgtmelon.scriptum.test

import sgtmelon.scriptum.data.provider.RoomProvider
import sgtmelon.scriptum.data.room.IRoomWork

/**
 * Parent class for Integration tests
 */
abstract class ParentRoomTest : ParentTest(), IRoomWork {

    override val roomProvider = RoomProvider(context)

    override fun setUp() {
        super.setUp()
        inRoomTest { clearAllTables() }
    }

}