package sgtmelon.scriptum.cleanup.domain.model.item

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import sgtmelon.extensions.getCalendarText
import sgtmelon.scriptum.infrastructure.database.DbData.Alarm
import sgtmelon.scriptum.infrastructure.database.DbData.Note
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.utils.extensions.MAX_COUNT
import sgtmelon.scriptum.infrastructure.utils.extensions.note.clearAlarm
import sgtmelon.scriptum.infrastructure.utils.extensions.note.clearRank
import sgtmelon.scriptum.infrastructure.utils.extensions.note.copy
import sgtmelon.scriptum.infrastructure.utils.extensions.note.getCheckCount
import sgtmelon.scriptum.infrastructure.utils.extensions.note.haveAlarm
import sgtmelon.scriptum.infrastructure.utils.extensions.note.haveRank
import sgtmelon.scriptum.infrastructure.utils.extensions.note.isSaveEnabled
import sgtmelon.scriptum.infrastructure.utils.extensions.note.joinToText
import sgtmelon.scriptum.infrastructure.utils.extensions.note.onConvert
import sgtmelon.scriptum.infrastructure.utils.extensions.note.onDelete
import sgtmelon.scriptum.infrastructure.utils.extensions.note.onItemCheck
import sgtmelon.scriptum.infrastructure.utils.extensions.note.onRestore
import sgtmelon.scriptum.infrastructure.utils.extensions.note.onSave
import sgtmelon.scriptum.infrastructure.utils.extensions.note.type
import sgtmelon.scriptum.infrastructure.utils.extensions.note.updateComplete
import sgtmelon.scriptum.infrastructure.utils.extensions.note.updateTime
import sgtmelon.scriptum.testing.parent.ParentTest

/**
 * Test for [NoteItem].
 */
class NoteItemTest : ParentTest() {

    // TODO create tests for Extensions

    //region Data

    private val rollList = arrayListOf(
        RollItem(position = 0, text = "1"),
        RollItem(position = 1, text = "2", isCheck = true),
        RollItem(position = 2, text = "3", isCheck = true)
    )

    private val textItem = NoteItem.Text(create = "12345", color = Color.WHITE)
    private val rollItem = NoteItem.Roll(create = "12345", color = Color.WHITE, list = rollList)

    private val splitList = listOf("1", "2", "34")

    private val nameSpace = "1  2  3   "
    private val nameClear = "1 2 3"

    private val splitText = "1\n\n2\n34\n"

    private val changeText = "TIME"

    private val copyText = "COPY_TEXT"
    private val realText = "REAL_TEXT"
    private val copyPosition = 9
    private val realPosition = 0

    private val checkCount = 2

    //endregion

    private fun assertChangeTime(noteItem: NoteItem) =
        assertEquals(getCalendarText(), noteItem.change)

    @Test fun noteType() {
        assertEquals(textItem.type, NoteType.TEXT)
        assertEquals(rollItem.type, NoteType.ROLL)
    }

    @Test fun switchStatus() {
        TODO()
        //        assertTrue(textItem.deepCopy(isStatus = false).switchStatus().isStatus)
        //        assertFalse(textItem.deepCopy(isStatus = true).switchStatus().isStatus)
    }

    @Test fun updateTime() {
        val item = rollItem.copy(change = changeText)
        item.updateTime()
        assertChangeTime(item)
    }

    @Test fun haveRank() {
        assertFalse(rollItem.copy().haveRank)
        assertFalse(rollItem.copy(rank = NoteRank(id = 0)).haveRank)
        assertFalse(rollItem.copy(rank = NoteRank(position = 0)).haveRank)
        assertTrue(rollItem.copy(rank = NoteRank(id = 0, position = 0)).haveRank)
    }

    @Test fun haveAlarm() {
        assertFalse(rollItem.copy().haveAlarm)
        assertFalse(rollItem.copy(alarm = NoteAlarm(id = 1)).haveAlarm)
        assertFalse(rollItem.copy(alarm = NoteAlarm(date = "DATE")).haveAlarm)
        assertTrue(rollItem.copy(alarm = NoteAlarm(id = 1, date = "DATE")).haveAlarm)
    }

    @Test fun clearRank() {
        val item = rollItem.copy(rank = NoteRank(id = 0, position = 0))

        assertTrue(item.haveRank)
        assertFalse(item.clearRank().haveRank)
    }

    @Test fun clearAlarm() {
        val item = rollItem.copy(alarm = NoteAlarm(id = 1, date = "123"))

        assertTrue(item.haveAlarm)
        assertFalse(item.clearAlarm().haveAlarm)
    }

    @Test fun onDelete() {
        rollItem.copy(change = changeText, isBin = false, isStatus = true).onDelete().let {
            assertChangeTime(it)
            assertTrue(it.isBin)
            assertFalse(it.isStatus)
        }
    }

    @Test fun onRestore() {
        rollItem.copy(change = changeText, isBin = true).onRestore().let {
            assertChangeTime(it)
            assertFalse(it.isBin)
        }
    }

    //region TextNote

    @Test fun `default values for text`() {
        NoteItem.Text(color = Color.BLUE).apply {
            assertEquals(Note.Default.ID, id)
            assertEquals(getCalendarText(), create)
            assertEquals(Note.Default.CHANGE, change)
            assertEquals(Note.Default.NAME, name)
            assertEquals(Note.Default.TEXT, text)
            assertEquals(Note.Default.RANK_ID, rank.id)
            assertEquals(Note.Default.RANK_PS, rank.position)
            assertEquals(Note.Default.BIN, isBin)
            assertEquals(Note.Default.STATUS, isStatus)
            assertEquals(Alarm.Default.ID, alarm.id)
            assertEquals(Alarm.Default.DATE, alarm.date)
        }
    }

    @Test fun `isSaveEnabled for text`() {
        textItem.copy().apply {
            assertFalse(isSaveEnabled)
            text = "123"
            assertTrue(isSaveEnabled)
        }
    }


    @Test fun `deepCopy for text`() {
        val firstItem = textItem.copy(text = realText)
        val secondItem = firstItem.copy()

        assertEquals(firstItem, secondItem)

        firstItem.text = copyText
        assertEquals(realText, secondItem.text)
    }

    @Test fun splitText() {
        TODO("Move this test into rollExtensions test")
        //        assertEquals(splitList, textItem.deepCopy(text = splitText).splitText())
    }


    @Test fun `onSave for text`() {
        textItem.copy(change = changeText, name = nameSpace).apply {
            onSave()

            assertEquals(nameClear, name)
            assertChangeTime(this)
        }
    }

    @Test fun `onConvert for text`() {
        textItem.copy(change = changeText, text = splitText).onConvert().apply {
            assertEquals(NoteType.ROLL, type)

            assertEquals(splitList.size, list.size)

            for ((i, text) in splitList.withIndex()) {
                assertEquals(i, list[i].position)
                assertEquals(text, list[i].text)
            }

            assertChangeTime(this)
            assertEquals("0/${splitList.size}", text)
        }
    }

    //endregion

    //region RollNote

    @Test fun `defaultValues for roll`() {
        NoteItem.Roll(color = Color.BLUE).apply {
            assertEquals(Note.Default.ID, id)
            assertEquals(getCalendarText(), create)
            assertEquals(Note.Default.CHANGE, change)
            assertEquals(Note.Default.NAME, name)
            assertEquals(Note.Default.TEXT, text)
            assertEquals(Note.Default.RANK_ID, rank.id)
            assertEquals(Note.Default.RANK_PS, rank.position)
            assertEquals(Note.Default.BIN, isBin)
            assertEquals(Note.Default.STATUS, isStatus)
            assertEquals(Alarm.Default.ID, alarm.id)
            assertEquals(Alarm.Default.DATE, alarm.date)
            assertEquals(0, list.size)
        }
    }

    @Test fun `isSaveEnabled for roll`() {
        rollItem.copy().apply {
            assertTrue(isSaveEnabled)
            list.forEach { it.text = "" }
            assertFalse(isSaveEnabled)
        }
    }

    @Test fun `deepCopy for roll`() {
        val firstItem = rollItem.copy(name = realText)
        val secondItem = firstItem.copy()

        assertEquals(firstItem, secondItem)

        firstItem.name = copyText
        firstItem.list.first().position = copyPosition

        assertEquals(realText, secondItem.name)
        assertEquals(realPosition, secondItem.list.first().position)
    }


    @Test fun updateComplete() {
        // TODO
        //        rollItem.deepCopy().apply {
        //            assertEquals("0/${list.size}", updateComplete().text)
        //        }
        //
        //        rollItem.deepCopy().apply {
        //            assertEquals("${list.size}/${list.size}", updateComplete().text)
        //        }

        rollItem.copy().apply {
            assertEquals("${checkCount}/${list.size}", updateComplete().text)
        }

        rollItem.copy().apply {
            with(list) { while (size != MAX_COUNT) add(first()) }
            assertEquals("${checkCount}/${MAX_COUNT}", updateComplete().text)

            with(list) { add(first()) }
            assertEquals("${checkCount}/${MAX_COUNT}", updateComplete().text)
        }

        rollItem.copy().apply {
            with(list) {
                for (it in this) it.isCheck = true
                while (size != MAX_COUNT) add(random().copy(isCheck = true))
            }
            assertEquals("${MAX_COUNT}/${MAX_COUNT}", updateComplete().text)

            with(list) { add(random().copy(isCheck = true)) }
            assertEquals("${MAX_COUNT}/${MAX_COUNT}", updateComplete().text)
        }
    }

    @Test fun getCheck() = assertEquals(checkCount, rollItem.list.getCheckCount())


    @Test fun onItemCheck() {
        rollItem.copy(change = changeText).apply {
            onItemCheck(list.indices.first)

            assertTrue(list.first().isCheck)
            assertChangeTime(this)
            assertEquals("3/3", text)
        }

        rollItem.copy(change = changeText).apply {
            onItemCheck(p = 4)

            assertEquals(changeText, change)
        }
    }


    @Test fun `onSave for roll`() {
        rollItem.copy(change = changeText, name = nameSpace).apply {
            list.add(RollItem(position = 6, text = "   "))
            list.add(RollItem(position = 10, text = "   4  "))

            updateComplete()

            assertEquals(5, list.size)
            assertEquals("2/5", text)

            onSave()

            assertEquals(4, list.size)
            assertEquals("2/4", text)

            for ((i, item) in list.withIndex()) {
                assertEquals(i, item.position)
            }

            assertEquals(nameClear, name)
            assertChangeTime(this)
        }
    }

    @Test fun `onConvert for roll`() {
        rollItem.copy(change = changeText).onConvert().apply {
            assertEquals(NoteType.TEXT, type)

            assertChangeTime(this)
            assertEquals(rollList.joinToText(), text)
        }

        val list = rollList.subList(0, 2)
        rollItem.copy(change = changeText, list = list).onConvert().apply {
            assertEquals(NoteType.TEXT, type)

            assertChangeTime(this)
            assertEquals(list.joinToText(), text)
        }
    }

    //endregion

}