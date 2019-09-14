package sgtmelon.scriptum.test

import sgtmelon.scriptum.room.IRoomWork

/**
 * Родительский класс для Integration тестов
 */
abstract class ParentIntegrationTest : ParentTest(), IRoomWork {

    override fun setUp() {
        super.setUp()
        inRoom { clearAllTables() }
    }

}