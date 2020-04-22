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

    @Test fun noteType() {
        assertEquals(textItem.type, NoteType.TEXT)
        assertEquals(rollItem.type, NoteType.ROLL)
    }

    @Test fun switchStatus() {
        val itemFirst = textItem.deepCopy(isStatus = false)
        val itemSecond = textItem.deepCopy(isStatus = true)

        itemFirst.switchStatus()
        itemSecond.switchStatus()

        assertTrue(itemFirst.isStatus)
        assertFalse(itemSecond.isStatus)
    }

    //region start

    @Test fun deepCopy() {
        val itemFirst = rollItem.deepCopy()
        val itemSecond = itemFirst.deepCopy()

        itemFirst.rollList.first().position = COPY_POSITION

        assertEquals(REAL_POSITION, itemSecond.rollList.first().position)
    }


    @Test fun updateComplete() {
        val size = rollList.size

        var item = rollItem.deepCopy()
        assertEquals("0/${size}", item.updateComplete(Complete.EMPTY).text)

        item = rollItem.deepCopy()
        assertEquals("${size}/${size}", item.updateComplete(Complete.FULL).text)

        item = rollItem.deepCopy()
        assertEquals("${CHECK_COUNT}/${size}", item.updateComplete().text)

        item = rollItem.deepCopy().apply {
            while (rollList.size != 99) rollList.add(rollList.first())
        }
        assertEquals("${CHECK_COUNT}/99", item.updateComplete().text)

        item = rollItem.deepCopy().apply {
            while (rollList.size != 100) rollList.add(rollList.first())
        }
        assertEquals("${CHECK_COUNT}/99", item.updateComplete().text)

        item = rollItem.deepCopy().apply {
            while (rollList.size != 99) rollList.add(rollList.first())
            rollList.forEach { it.isCheck = true }
        }
        assertEquals("99/99", item.updateComplete().text)

        item = rollItem.deepCopy().apply {
            while (rollList.size != 100) rollList.add(rollList.first())
            rollList.forEach { it.isCheck = true }
        }
        assertEquals("99/99", item.updateComplete().text)
    }

    @Test fun updateCheck() {
        var item = rollItem.deepCopy()
        item.updateCheck(isCheck = true)

        assertFalse(item.rollList.any { !it.isCheck })

        item = rollItem.deepCopy()
        item.updateCheck(isCheck = false)

        assertFalse(item.rollList.any { it.isCheck })
    }

    @Test fun getCheck() = assertEquals(CHECK_COUNT, rollItem.getCheck())


    @Test fun updateTime() = assertChangeTime(rollItem.deepCopy(change = "TIME").updateTime())

    @Test fun delete() {
        val item = rollItem.deepCopy(change = "TIME", isBin = false, isStatus = true)

        item.onDelete().let {
            assertChangeTime(it)
            assertEquals(true, it.isBin)
            assertEquals(false, it.isStatus)
        }
    }

    @Test fun restore() {
        val item = rollItem.deepCopy(change = "TIME", isBin = true)

        item.onRestore().let {
            assertChangeTime(it)
            assertEquals(false, it.isBin)
        }
    }

    @Test fun convertText() {
        val item = textItem.deepCopy(change = "TIME")

        item.onConvert().let {
            assertChangeTime(it)
            assertEquals(NoteType.ROLL, it.type)
        }
    }

    @Test fun convertRoll() {
        val item = rollItem.deepCopy(change = "TIME")

        item.onConvert().let {
            assertChangeTime(it)
            assertEquals(NoteType.TEXT, it.type)
        }
    }

    @Test fun splitText() {
        assertEquals(listOf("1", "2", "34"), textItem.deepCopy(text = "1\n2\n34").splitText())
    }

    @Test fun haveRank() {
        val item = rollItem.deepCopy()
        assertFalse(item.haveRank())

        item.rankId = 0
        item.rankPs = 0
        assertTrue(item.haveRank())
    }

    @Test fun haveAlarm() {
        val item = rollItem.deepCopy()
        assertFalse(item.haveAlarm())

        item.alarmId = 1
        item.alarmDate = "123"
        assertTrue(item.haveAlarm())
    }

    @Test fun clearRank() {
        val item = rollItem.deepCopy(rankId = 0, rankPs = 0)

        assertTrue(item.haveRank())
        assertFalse(item.clearRank().haveRank())
    }

    @Test fun clearAlarm() {
        val item = rollItem.deepCopy(alarmId = 1, alarmDate = "123")

        assertTrue(item.haveAlarm())
        assertFalse(item.clearAlarm().haveAlarm())
    }

    @Test fun `isSaveEnabled for textNote`() {
        val item = textItem.deepCopy()
        assertFalse(item.isSaveEnabled())

        item.text = "123"
        assertTrue(item.isSaveEnabled())
    }

    @Test fun `isSaveEnabled for rollNote`() {
        val item = rollItem.deepCopy()
        assertTrue(item.isSaveEnabled())

        item.rollList.forEach { it.text = "" }
        assertFalse(item.isSaveEnabled())
    }

    @Test fun isVisible() {
        val item = rollItem.deepCopy()

        assertTrue(item.isVisible(rankVisibleList))

        item.rankId = 1
        item.rankPs = 1

        assertTrue(item.isVisible(rankVisibleList))

        item.rankId = 4
        item.rankPs = 4

        assertFalse(item.isVisible(rankVisibleList))
    }


    private fun assertChangeTime(noteItem: NoteItem) = assertEquals(getTime(), noteItem.change)

    //endregion

    private companion object {
        const val COPY_POSITION = 9
        const val REAL_POSITION = 0

        const val CHECK_COUNT = 2

        val rollList = arrayListOf(
                RollItem(position = 0, text = "1"),
                RollItem(position = 1, text = "2", isCheck = true),
                RollItem(position = 2, text = "3", isCheck = true)
        )

        val textItem = NoteItem.Text(create = "12345", color = 0)
        val rollItem = NoteItem.Roll(create = "12345", color = 0, rollList = rollList)

        val rankVisibleList = listOf<Long>(1, 2, 3)
    }

}