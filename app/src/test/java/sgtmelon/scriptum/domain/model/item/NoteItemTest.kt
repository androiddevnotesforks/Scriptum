package sgtmelon.scriptum.domain.model.item

import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.getTime
import sgtmelon.scriptum.ParentTest
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.data.DbData.Alarm
import sgtmelon.scriptum.domain.model.data.DbData.Note
import sgtmelon.scriptum.domain.model.key.Complete
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.extension.getText
import sgtmelon.scriptum.domain.model.item.NoteItem.Roll.Companion.INDICATOR_MAX_COUNT as MAX_COUNT

/**
 * Test for [NoteItem].
 */
class NoteItemTest : ParentTest() {

    @Test fun noteType() {
        assertEquals(textItem.type, NoteType.TEXT)
        assertEquals(rollItem.type, NoteType.ROLL)
    }

    @Test fun switchStatus() {
        assertTrue(textItem.deepCopy(isStatus = false).switchStatus().isStatus)
        assertFalse(textItem.deepCopy(isStatus = true).switchStatus().isStatus)
    }

    @Test fun updateTime() = assertChangeTime(rollItem.deepCopy(change = CHANGE_TEXT).updateTime())

    @Test fun haveRank() {
        assertFalse(rollItem.deepCopy().haveRank())
        assertFalse(rollItem.deepCopy(rankId = 0).haveRank())
        assertFalse(rollItem.deepCopy(rankPs = 0).haveRank())
        assertTrue(rollItem.deepCopy(rankId = 0, rankPs = 0).haveRank())
    }

    @Test fun haveAlarm() {
        assertFalse(rollItem.deepCopy().haveAlarm())
        assertFalse(rollItem.deepCopy(alarmId = 1).haveAlarm())
        assertFalse(rollItem.deepCopy(alarmDate = "DATE").haveAlarm())
        assertTrue(rollItem.deepCopy(alarmId = 1, alarmDate = "DATE").haveAlarm())
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

    @Test fun isVisible() {
        val idList = listOf<Long>(1, 2, 3)

        assertTrue(rollItem.deepCopy().isVisible(idList))
        assertTrue(rollItem.deepCopy(rankId = 0).isVisible(idList))
        assertTrue(rollItem.deepCopy(rankPs = 0).isVisible(idList))

        assertFalse(rollItem.deepCopy(rankId = 0, rankPs = 0).isVisible(idList))
        assertTrue(rollItem.deepCopy(rankId = 1, rankPs = 1).isVisible(idList))
    }


    @Test fun onDelete() {
        rollItem.deepCopy(change = CHANGE_TEXT, isBin = false, isStatus = true).onDelete().let {
            assertChangeTime(it)
            assertTrue(it.isBin)
            assertFalse(it.isStatus)
        }
    }

    @Test fun onRestore() {
        rollItem.deepCopy(change = CHANGE_TEXT, isBin = true).onRestore().let {
            assertChangeTime(it)
            assertFalse(it.isBin)
        }
    }

    //region TextNote

    @Test fun defaultValues_forText() {
        NoteItem.Text(color = Color.BLUE).apply {
            assertEquals(Note.Default.ID, id)
            assertEquals(getTime(), create)
            assertEquals(Note.Default.CHANGE, change)
            assertEquals(Note.Default.NAME, name)
            assertEquals(Note.Default.TEXT, text)
            assertEquals(Note.Default.RANK_ID, rankId)
            assertEquals(Note.Default.RANK_PS, rankPs)
            assertEquals(Note.Default.BIN, isBin)
            assertEquals(Note.Default.STATUS, isStatus)
            assertEquals(Alarm.Default.ID, alarmId)
            assertEquals(Alarm.Default.DATE, alarmDate)
        }
    }

    @Test fun isSaveEnabled_forText() {
        textItem.deepCopy().apply {
            assertFalse(isSaveEnabled())
            text = "123"
            assertTrue(isSaveEnabled())
        }
    }


    @Test fun deepCopy_forText() {
        val itemFirst = textItem.deepCopy(text = REAL_TEXT)
        val itemSecond = itemFirst.deepCopy()

        itemFirst.text = COPY_TEXT
        assertEquals(REAL_TEXT, itemSecond.text)
    }

    @Test fun splitText() {
        assertEquals(splitList, textItem.deepCopy(text = SPLIT_TEXT).splitText())
    }


    @Test fun onSave_forText() {
        textItem.deepCopy(change = CHANGE_TEXT, name = NAME_SPACE).apply {
            onSave()

            assertEquals(NAME_CLEAR, name)
            assertChangeTime(this)
        }
    }

    @Test fun onConvert_forText() {
        textItem.deepCopy(change = CHANGE_TEXT, text = SPLIT_TEXT).onConvert().apply {
            assertEquals(NoteType.ROLL, type)

            assertEquals(splitList.size, list.size)
            splitList.forEachIndexed { i, text ->
                assertEquals(i, list[i].position)
                assertEquals(text, list[i].text)
            }

            assertChangeTime(this)
            assertEquals("0/${splitList.size}", text)
        }
    }

    //endregion

    //region RollNote

    @Test fun defaultValues_forRoll() {
        NoteItem.Roll(color = Color.BLUE).apply {
            assertEquals(Note.Default.ID, id)
            assertEquals(getTime(), create)
            assertEquals(Note.Default.CHANGE, change)
            assertEquals(Note.Default.NAME, name)
            assertEquals(Note.Default.TEXT, text)
            assertEquals(Note.Default.RANK_ID, rankId)
            assertEquals(Note.Default.RANK_PS, rankPs)
            assertEquals(Note.Default.BIN, isBin)
            assertEquals(Note.Default.STATUS, isStatus)
            assertEquals(Alarm.Default.ID, alarmId)
            assertEquals(Alarm.Default.DATE, alarmDate)
            assertEquals(0, list.size)
        }
    }

    @Test fun isSaveEnabled_forRoll() {
        rollItem.deepCopy().apply {
            assertTrue(isSaveEnabled())
            list.forEach { it.text = "" }
            assertFalse(isSaveEnabled())
        }
    }

    @Test fun deepCopy_forRoll() {
        val itemFirst = rollItem.deepCopy(name = REAL_TEXT)
        val itemSecond = itemFirst.deepCopy()

        itemFirst.name = COPY_TEXT
        itemFirst.list.first().position = COPY_POSITION

        assertEquals(REAL_TEXT, itemSecond.name)
        assertEquals(REAL_POSITION, itemSecond.list.first().position)
    }


    @Test fun updateComplete() {
        rollItem.deepCopy().apply {
            assertEquals("0/${list.size}", updateComplete(Complete.EMPTY).text)
        }

        rollItem.deepCopy().apply {
            assertEquals("${list.size}/${list.size}", updateComplete(Complete.FULL).text)
        }

        rollItem.deepCopy().apply {
            assertEquals("${CHECK_COUNT}/${list.size}", updateComplete().text)
        }

        rollItem.deepCopy().apply {
            with(list) { while (size != MAX_COUNT) add(first()) }
            assertEquals("${CHECK_COUNT}/${MAX_COUNT}", updateComplete().text)

            with(list) { add(first()) }
            assertEquals("${CHECK_COUNT}/${MAX_COUNT}", updateComplete().text)
        }

        rollItem.deepCopy().apply {
            with(list) {
                forEach { it.isCheck = true }
                while (size != MAX_COUNT) add(random().copy(isCheck = true))
            }
            assertEquals("${MAX_COUNT}/${MAX_COUNT}", updateComplete().text)

            with(list) { add(random().copy(isCheck = true)) }
            assertEquals("${MAX_COUNT}/${MAX_COUNT}", updateComplete().text)
        }
    }

    @Test fun updateCheck() {
        rollItem.deepCopy().apply {
            updateCheck(isCheck = true)

            assertFalse(list.any { !it.isCheck })
            assertEquals("${list.size}/${list.size}", text)
        }

        rollItem.deepCopy().apply {
            updateCheck(isCheck = false)

            assertFalse(list.any { it.isCheck })
            assertEquals("0/${list.size}", text)
        }
    }

    @Test fun getCheck() = assertEquals(CHECK_COUNT, rollItem.getCheck())


    @Test fun onItemCheck() {
        rollItem.deepCopy(change = CHANGE_TEXT).apply {
            onItemCheck(list.indices.first)

            assertTrue(list.first().isCheck)
            assertChangeTime(this)
            assertEquals("3/3", text)
        }

        rollItem.deepCopy(change = CHANGE_TEXT).apply {
            onItemCheck(p = 4)

            assertEquals(CHANGE_TEXT, change)
        }
    }

    @Test fun onItemLongCheck() {
        rollItem.deepCopy(change = CHANGE_TEXT).apply {
            assertTrue(onItemLongCheck())
            assertChangeTime(this)
            assertEquals("3/3", text)

            change = CHANGE_TEXT

            assertFalse(onItemLongCheck())
            assertChangeTime(this)
            assertEquals("0/3", text)
        }
    }


    @Test fun onSave_forRoll() {
        rollItem.deepCopy(change = CHANGE_TEXT, name = NAME_SPACE).apply {
            list.add(RollItem(position = 6, text = "   "))
            list.add(RollItem(position = 10, text = "   4  "))

            updateComplete()

            assertEquals(5, list.size)
            assertEquals("2/5", text)

            onSave()

            assertEquals(4, list.size)
            assertEquals("2/4", text)

            list.forEachIndexed { i, item -> assertEquals(i, item.position) }

            assertEquals(NAME_CLEAR, name)
            assertChangeTime(this)
        }
    }

    @Test fun onConvert_forRoll() {
        rollItem.deepCopy(change = CHANGE_TEXT).onConvert().apply {
            assertEquals(NoteType.TEXT, type)

            assertChangeTime(this)
            assertEquals(rollList.getText(), text)
        }

        val list = rollList.subList(0, 2)
        rollItem.deepCopy(change = CHANGE_TEXT, rollList = list).onConvert(list).apply {
            assertEquals(NoteType.TEXT, type)

            assertChangeTime(this)
            assertEquals(list.getText(), text)
        }
    }

    //endregion

    private fun assertChangeTime(noteItem: NoteItem) = assertEquals(getTime(), noteItem.change)

    private val rollList = arrayListOf(
            RollItem(position = 0, text = "1"),
            RollItem(position = 1, text = "2", isCheck = true),
            RollItem(position = 2, text = "3", isCheck = true)
    )

    private val textItem = NoteItem.Text(create = "12345", color = 0)
    private val rollItem = NoteItem.Roll(create = "12345", color = 0, list = rollList)

    private val splitList = listOf("1", "2", "34")

    private companion object {
        const val NAME_SPACE = "1  2  3   "
        const val NAME_CLEAR = "1 2 3"

        const val SPLIT_TEXT = "1\n\n2\n34\n"

        const val CHANGE_TEXT = "TIME"

        const val COPY_TEXT = "COPY_TEXT"
        const val REAL_TEXT = "REAL_TEXT"

        const val COPY_POSITION = 9
        const val REAL_POSITION = 0

        const val CHECK_COUNT = 2
    }

}