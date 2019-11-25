package sgtmelon.scriptum.model.item

import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.getTime
import sgtmelon.scriptum.model.item.NoteItem.Companion.getCheck
import sgtmelon.scriptum.model.key.Complete
import sgtmelon.scriptum.model.key.NoteType

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

    @Test fun getCheck() = assertEquals(CHECK_COUNT, rollList.getCheck())


    @Test fun delete() {
        val item = noteItem.deepCopy(change = "TIME", isBin = false, isStatus = true)

        with(item.delete()) {
            assertCurrentTime(change)
            assertEquals(true, isBin)
            assertEquals(false, isStatus)
        }
    }

    @Test fun restore() {
        val item = noteItem.deepCopy(change = "TIME", isBin = true)

        with(item.restore()) {
            assertCurrentTime(change)
            assertEquals(false, isBin)
        }
    }

    @Test fun convertText() {
        val item = noteItem.deepCopy(change = "TIME", type = NoteType.TEXT)

        with(item.convert()) {
            assertCurrentTime(change)
            assertEquals(NoteType.ROLL, type)
        }
    }

    @Test fun convertRoll() {
        val item = noteItem.deepCopy(change = "TIME", type = NoteType.ROLL)

        with(item.convert()) {
            assertCurrentTime(change)
            assertEquals(NoteType.TEXT, type)
        }
    }

    @Test fun textToList() {
        assertEquals(listOf("1", "2", "34"), noteItem.deepCopy(text = "1\n2\n34").textToList())
    }

    @Test fun haveRank() {
        val item = noteItem.deepCopy()
        assertFalse(item.haveRank())

        item.rankId = 0
        item.rankPs = 0
        assertTrue(item.haveRank())
    }

    @Test fun haveAlarm() {
        val item = noteItem.deepCopy()
        assertFalse(item.haveAlarm())

        item.alarmId = 1
        item.alarmDate = "123"
        assertTrue(item.haveAlarm())
    }

    @Test fun clearRank() {
        val item = noteItem.deepCopy(rankId = 0, rankPs = 0)

        assertTrue(item.haveRank())
        assertFalse(item.clearRank().haveRank())
    }

    @Test fun clearAlarm() {
        val item = noteItem.deepCopy(alarmId = 1, alarmDate = "123")

        assertTrue(item.haveAlarm())
        assertFalse(item.clearAlarm().haveAlarm())
    }

    @Test fun `isSaveEnabled for textNote`() {
        val item = noteItem.deepCopy(type = NoteType.TEXT)
        assertFalse(item.isSaveEnabled())

        item.text = "123"
        assertTrue(item.isSaveEnabled())
    }

    @Test fun `isSaveEnabled for rollNote`() {
        val item = noteItem.deepCopy()
        assertTrue(item.isSaveEnabled())

        item.rollList.forEach { it.text = "" }
        assertFalse(item.isSaveEnabled())
    }

    @Test fun isVisible() {
        val item = noteItem.copy()

        assertTrue(item.isVisible(rankVisibleList))

        item.rankId = 1
        item.rankPs = 1

        assertTrue(item.isVisible(rankVisibleList))

        item.rankId = 4
        item.rankPs = 4

        assertFalse(item.isVisible(rankVisibleList))
    }

    @Test fun isNotVisible() {
        val item = noteItem.copy()

        assertFalse(item.isNotVisible(rankVisibleList))

        item.rankId = 1
        item.rankPs = 1

        assertFalse(item.isNotVisible(rankVisibleList))

        item.rankId = 4
        item.rankPs = 4

        assertTrue(item.isNotVisible(rankVisibleList))
    }


    fun assertCurrentTime(time: String) = assertEquals(getTime(), time)

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

        val rankVisibleList = listOf<Long>(1, 2, 3)
    }

}