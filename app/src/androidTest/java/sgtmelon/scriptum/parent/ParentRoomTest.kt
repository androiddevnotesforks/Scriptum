package sgtmelon.scriptum.parent

import org.junit.Before
import org.junit.Rule
import org.junit.rules.ExpectedException
import sgtmelon.scriptum.cleanup.data.room.IRoomWork
import sgtmelon.scriptum.cleanup.data.room.extension.inRoomTest
import sgtmelon.scriptum.infrastructure.database.annotation.DaoConst
import sgtmelon.scriptum.parent.di.ParentInjector
import sgtmelon.test.common.OverflowDelegator

/**
 * Parent class for Integration tests.
 */
abstract class ParentRoomTest : ParentTest(),
    IRoomWork {

    @get:Rule val exceptionRule: ExpectedException = ExpectedException.none()

    override val roomProvider = ParentInjector.provideRoomProvider()

    protected val overflowDelegator = OverflowDelegator(DaoConst.OVERFLOW_COUNT)

    @Deprecated("Use overflowDelegator for this")
    protected val overflowLongList
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