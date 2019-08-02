package sgtmelon.scriptum.model

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RollEntity

/**
 * Тест для [NoteModel]
 *
 * @author SerjantArbuz
 */
class NoteModelTest {

    @Test fun updateCheck() = with(rollNoteModel) {
        rollList.addAll(rollList)

        repeat(times = rollList.size / 2) { rollList.random().isCheck = true }
        updateCheck(check = false)

        rollList.forEach { assertFalse(it.isCheck) }

        repeat(times = rollList.size / 2) { rollList.random().isCheck = true }
        updateCheck(check = true)

        rollList.forEach { assertTrue(it.isCheck) }
    }

    @Test fun isSaveEnabled() {
        with(textNoteModel) {
            assertFalse(isSaveEnabled())
            noteEntity.text = TEST_TEXT
            assertTrue(isSaveEnabled())
        }

        with(rollNoteModel) {
            assertFalse(isSaveEnabled())
            rollList.addAll(list)
            assertFalse(isSaveEnabled())
            rollList.random().text = TEST_TEXT
            assertTrue(isSaveEnabled())
        }
    }

    private companion object {
        const val TEST_TEXT = "TEXT FOR TEST"

        val textNoteModel = NoteModel(NoteEntity(type = NoteType.TEXT))
        val rollNoteModel = NoteModel(NoteEntity(type = NoteType.ROLL))

        val list = arrayListOf(
                RollEntity(), RollEntity(), RollEntity(), RollEntity(), RollEntity()
        )
    }

}