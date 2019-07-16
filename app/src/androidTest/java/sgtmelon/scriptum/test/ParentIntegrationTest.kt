package sgtmelon.scriptum.test

import sgtmelon.scriptum.room.IRoomWork

/**
 * Родительский класс для Integration тестов
 *
 * @author SerjantArbuz
 */
abstract class ParentIntegrationTest : ParentTest(), IRoomWork {

    override fun setUp() {
        super.setUp()
        inTheRoom { clearAllTables() }
    }

    protected companion object {
        const val DATE_1 = "1234-01-02 03:04:05"
        const val DATE_2 = "1345-02-03 04:05:06"
        const val DATE_3 = "1456-03-04 05:06:07"
    }

}