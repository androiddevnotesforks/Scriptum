package sgtmelon.scriptum.integrational.testing

import org.junit.Before
import org.junit.Rule
import org.junit.rules.ExpectedException
import sgtmelon.scriptum.infrastructure.database.model.DaoConst
import sgtmelon.scriptum.parent.ParentTest
import sgtmelon.scriptum.parent.RoomWorker
import sgtmelon.scriptum.parent.di.ParentInjector
import sgtmelon.test.common.OverflowDelegator

/**
 * Parent class for Integration database tests.
 */
abstract class ParentRoomTest : ParentTest(),
    RoomWorker {

    @get:Rule val exceptionRule: ExpectedException = ExpectedException.none()

    override val database = ParentInjector.provideDatabase()

    protected val overflowDelegator = OverflowDelegator(DaoConst.OVERFLOW_COUNT)

    @Before override fun setUp() {
        super.setUp()
        inRoomTest { clearAllTables() }
    }
}