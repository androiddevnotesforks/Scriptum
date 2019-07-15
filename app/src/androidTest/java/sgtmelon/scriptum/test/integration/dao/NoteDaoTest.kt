package sgtmelon.scriptum.test.integration.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.dao.NoteDao
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.test.ParentIntegrationTest
import kotlin.random.Random

/**
 * Интеграционный тест для [NoteDao]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class NoteDaoTest : ParentIntegrationTest() {

    // TODO написать тесты
    // TODO # Тест на проверку отсутствия заметки в базе (не должно вылетить)
    // TODO # Проверка сортировки (как минимум 3 заметки) + проверка постсортировки у rank/color
    // TODO #
    // TODO #
    // TODO #
    // TODO #

    @Test fun getOnWrongId() = inTheRoom { assertNull(getNoteDao()[Random.nextLong()]) }

    @Test fun getOnCorrectId() = inTheRoom {
        with(getNoteDao()) {
            insert(noteFirst)
            insert(noteSecond)
            insert(noteThird)

            assertEquals(noteFirst, get(noteFirst.id))
            assertEquals(noteSecond, get(noteSecond.id))
            assertEquals(noteThird, get(noteThird.id))
        }
    }

    @Test fun getFromBin() = inTheRoom {
        with(getNoteDao()) {
            assertEquals(arrayListOf<NoteEntity>(), get(bin = false))
            assertEquals(arrayListOf<NoteEntity>(), get(bin = true))

            insert(noteFirst)
            insert(noteSecond)
            assertEquals(arrayListOf(noteFirst, noteSecond), get(bin = false))
        }
    }

    private companion object {
        val noteFirst = NoteEntity(
                id = 1, create = DATE_1, change = DATE_1, text = "123", name = "456",
                color = 1, type = NoteType.TEXT
        )

        val noteSecond = NoteEntity(
                id = 2, create = DATE_2, change = DATE_2, text = "654", name = "321",
                color = 2, type = NoteType.TEXT, isBin = true
        )

        val noteThird = NoteEntity(
                id = 3, create = DATE_3, change = DATE_3, text = "123", name = "",
                color = 3, type = NoteType.TEXT
        )


    }

}