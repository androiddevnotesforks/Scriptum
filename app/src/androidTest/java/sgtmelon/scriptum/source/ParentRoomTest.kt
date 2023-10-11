package sgtmelon.scriptum.source

import org.junit.Before
import sgtmelon.scriptum.infrastructure.database.model.DaoConst
import sgtmelon.test.common.OverflowDelegator

/**
 * Parent class for Integration database tests.
 */
abstract class ParentRoomTest : ParentTest(),
    RoomWorker {

    override val database by lazy { component.database }

    protected val overflowDelegator = OverflowDelegator(DaoConst.OVERFLOW_COUNT)

    @Before override fun setUp() {
        super.setUp()
        inRoomTest { clearAllTables() }
    }
}