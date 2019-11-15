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

    internal companion object {
        const val UNIQUE_ERROR_ID = -1L
    }

}