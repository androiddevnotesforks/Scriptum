package sgtmelon.scriptum.model.item

import org.junit.Assert.*
import org.junit.Test
import sgtmelon.scriptum.model.annotation.Color.Companion.list
import sgtmelon.scriptum.model.key.Complete
import sgtmelon.scriptum.model.key.NoteType
import kotlin.random.Random

/**
 * Test for [NoteItem]
 */
class NoteItemTest {

    @Test fun deepCopy() {
        val itemFirst = noteItem.deepCopy()
        val itemSecond = itemFirst.deepCopy()

        itemFirst.rollList.first().position = COPY_POSITION

        assertEquals(REAL_POSITION, itemSecond.rollList.first().position)
    }

    @Test fun updateComplete() {
        val size = rollList.size

        var item = noteItem.deepCopy()
        assertEquals(0, item.updateComplete(Complete.EMPTY))
        assertEquals("0/${size}", item.text)

        item = noteItem.deepCopy()
        assertEquals(size, item.updateComplete(Complete.FULL))
        assertEquals("${size}/${size}", item.text)

        item = noteItem.deepCopy()
        assertEquals(CHECK_COUNT, item.updateComplete())
        assertEquals("${CHECK_COUNT}/${size}", item.text)
    }

    @Test fun updateCheck() {
        var item = noteItem.deepCopy()
        item.updateCheck(isCheck = true)

        assertFalse(item.rollList.any { !it.isCheck })

        item = noteItem.deepCopy()
        item.updateCheck(isCheck = false)

        assertFalse(item.rollList.any { it.isCheck })
    }


    private companion object {
        const val COPY_POSITION = 9
        const val REAL_POSITION = 0

        const val CHECK_COUNT = 2

        val rollList = arrayListOf(
                RollItem(position = 0, text = "1"),
                RollItem(position = 1, text = "2", isCheck = true),
                RollItem(position = 2, text = "3", isCheck = true)
        )

        val noteItem = NoteItem(
                create = "12345", color = 0, type = NoteType.ROLL, rollList = rollList
        )
    }

}