package sgtmelon.scriptum.test

import sgtmelon.scriptum.room.RoomDb

/**
 * Родительский класс для Integration тестов
 *
 * @author SerjantArbuz
 */
abstract class ParentIntegrationTest: ParentTest() {

    protected fun openRoom() = RoomDb.getInstance(context)

    override fun setUp() {
        super.setUp()
        openRoom().apply { clearAllTables() }.close()
    }

    protected companion object {
        const val DATE_1 = "1234-01-02 03:04:05"
        const val DATE_2 = "1345-02-03 04:05:06"
    }

}