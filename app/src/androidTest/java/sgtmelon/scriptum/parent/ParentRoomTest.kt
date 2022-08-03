package sgtmelon.scriptum.parent

import org.junit.Before
import sgtmelon.scriptum.cleanup.data.room.IRoomWork
import sgtmelon.scriptum.cleanup.data.room.extension.inRoomTest
import sgtmelon.scriptum.parent.di.ParentInjector

/**
 * Parent class for Integration tests.
 */
abstract class ParentRoomTest : ParentTest(),
    IRoomWork {

    override val roomProvider = ParentInjector.provideRoomProvider()

    protected val crowdLongList
        get() = List(OVERFLOW_LIMIT * (10..50).random()) { it.toLong() }

    @Before override fun setUp() {
        super.setUp()
        inRoomTest { clearAllTables() }
    }

    companion object {
        const val OVERFLOW_LIMIT = 1000
        const val CROWD_SIZE = 5000
    }
}