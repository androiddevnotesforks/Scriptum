package sgtmelon.scriptum.domain.model.item

import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.getTime
import sgtmelon.scriptum.ParentTest
import sgtmelon.scriptum.domain.model.key.Complete
import sgtmelon.scriptum.domain.model.key.NoteType

/**
 * Test for [NoteItem].
 */
class NoteItemTest : ParentTest() {

    @Test fun deepCopy() {
        val itemFirst = noteItem.deepCopy()
        val itemSecond = itemFirst.deepCopy()

        itemFirst.rollList.first().position = COPY_POSITION

        assertEquals(REAL_POSITION, itemSecond.rollList.first().position)
    }

    @Test fun switchCopy() {
        val itemFirst = noteItem.deepCopy(isStatus = false)
        val itemSecond = noteItem.deepCopy(isStatus = true)

        itemFirst.switchStatus()
        itemSecond.switchStatus()

        assertTrue(itemFirst.isStatus)
        assertFalse(itemSecond.isStatus)
    }


    @Test fun updateComplete() {
        val size = rollList.size

        var item = noteItem.deepCopy()
        assertEquals("0/${size}", item.updateComplete(Complete.EMPTY).text)

        item = noteItem.deepCopy()
        assertEquals("${size}/${size}", item.updateComplete(Complete.FULL).text)

        item = noteItem.deepCopy()
        assertEquals("${CHECK_COUNT}/${size}", item.updateComplete().text)

        item = noteItem.deepCopy().apply {
            while (rollList.size != 99) rollList.add(rollList.first())
        }
        assertEquals("${CHECK_COUNT}/99", item.updateComplete().text)

        item = noteItem.deepCopy().apply {
            while (rollList.size != 100) rollList.add(rollList.first())
        }
        assertEquals("${CHECK_COUNT}/99", item.updateComplete().text)

        item = noteItem.deepCopy().apply {
            while (rollList.size != 99) rollList.add(rollList.first())
            rollList.forEach { it.isCheck = true }
        }
        assertEquals("99/99", item.updateComplete().text)

        item = noteItem.deepCopy().apply {
            while (rollList.size != 100) rollList.add(rollList.first())
            rollList.forEach { it.isCheck = true }
        }
        assertEquals("99/99", item.updateComplete().text)
    }

    @Test fun updateCheck() {
        var item = noteItem.deepCopy()
        item.updateCheck(isCheck = true)

        assertFalse(item.rollList.any { !it.isCheck })

        item = noteItem.deepCopy()
        item.updateCheck(isCheck = false)

        assertFalse(item.rollList.any { it.isCheck })
    }

    @Test fun getCheck() = assertEquals(CHECK_COUNT, noteItem.getCheck())


    @Test fun updateTime() = assertChangeTime(noteItem.copy(change = "TIME").updateTime())

    @Test fun delete() {
        val item = noteItem.deepCopy(change = "TIME", isBin = false, isStatus = true)

        item.onDelete().let {
            assertChangeTime(it)
            assertEquals(true, it.isBin)
            assertEquals(false, it.isStatus)
        }
    }

    @Test fun restore() {
        val item = noteItem.deepCopy(change = "TIME", isBin = true)

        item.onRestore().let {
            assertChangeTime(it)
            assertEquals(false, it.isBin)
        }
    }

    @Test fun convertText() {
        val item = noteItem.deepCopy(change = "TIME", type = NoteType.TEXT)

        item.onConvert().let {
            assertChangeTime(it)
            assertEquals(NoteType.ROLL, it.type)
        }
    }

    @Test fun convertRoll() {
        val item = noteItem.deepCopy(change = "TIME", type = NoteType.ROLL)

        item.onConvert().let {
            assertChangeTime(it)
            assertEquals(NoteType.TEXT, it.type)
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


    private fun assertChangeTime(noteItem: NoteItem) = assertEquals(getTime(), noteItem.change)

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