package sgtmelon.scriptum.test

import sgtmelon.scriptum.room.IRoomWork

/**
 * Parent class for Integration tests
 */
abstract class ParentIntegrationTest : ParentTest(), IRoomWork {

    override fun setUp() {
        super.setUp()
        inRoom { clearAllTables() }
    }

}