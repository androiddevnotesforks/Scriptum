package sgtmelon.scriptum.data.room

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.ParentTest

/**
 * Test for [RoomMigrate].
 */
class RoomMigrateTest : ParentTest() {

    @Test fun `FROM 4 TO 5 remove multiply noteId`() {
        val noteIdExistSet: MutableSet<Long> = mutableSetOf()

        val noteIdList: MutableList<MutableList<Long>> = arrayListOf(
                arrayListOf(1L, 2L, 3L),
                arrayListOf(2L, 3L, 4L),
                arrayListOf(3L, 4L, 5L)
        )

        val noteIdListExpected: MutableList<MutableList<Long>> = arrayListOf(
                arrayListOf(1L, 2L, 3L),
                arrayListOf(4L),
                arrayListOf(5L)
        )

        for (i in 0 until noteIdList.size) {
            for (it in noteIdExistSet) {
                if (noteIdList[i].contains(it)) noteIdList[i].remove(it)
            }

            /**
             * Add not deleted id's for next FOR run.
             */
            noteIdExistSet.addAll(noteIdList[i])
        }

        assertEquals(noteIdListExpected, noteIdList)
    }
}