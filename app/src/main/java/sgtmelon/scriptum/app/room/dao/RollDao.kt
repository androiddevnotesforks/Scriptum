package sgtmelon.scriptum.app.room.dao

import android.text.TextUtils
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import sgtmelon.scriptum.app.model.NoteModel
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.app.room.RoomDb
import java.util.*

/**
 * Класс для общения Dao списка [RoomDb]
 */
@Dao
interface RollDao : BaseDao {

    @Insert
    fun insert(rollItem: RollItem): Long

    /**
     * Запись пунктов после конвертирования из текстовой заметки
     *
     * @param noteId - Id заметки
     * @param text   - Текст потенциальных пунктов
     * @return - Список для [NoteModel]
     */
    fun insert(noteId: Long, text: String): MutableList<RollItem> {
        val textToRoll = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val rollList = ArrayList<RollItem>()

        var p = 0
        for (toRoll in textToRoll) {
            if (TextUtils.isEmpty(toRoll)) continue

            val rollItem = RollItem()
            rollItem.noteId = noteId
            rollItem.position = p++
            rollItem.isCheck = false
            rollItem.text = toRoll

            rollItem.id = insert(rollItem)

            rollList.add(rollItem)
        }

        return rollList
    }

    @Query(value = "SELECT * FROM ROLL_TABLE ORDER BY RL_NOTE_ID ASC, RL_POSITION ASC")
    fun get(): List<RollItem>

    /**
     * Получение текста для текстовой заметки на основе списка
     *
     * @param noteId - Id заметки
     * @return - Строка для текстовой заметки
     */
    fun getText(noteId: Long): String {
        val rollList = getRoll(noteId)

        val text = StringBuilder()
        for (i in rollList.indices) {
            if (i != 0) text.append("\n")
            text.append(rollList[i].text)
        }

        return text.toString()
    }

    /**
     * Получение текста для уведомления на основе списка
     *
     * @param idNote - Id заметки
     * @param check  - Количество отмеченых пунктов в заметке
     * @return - Строка для уведомления
     */
    fun getText(idNote: Long, check: String): String {
        val rollList = getRoll(idNote)

        val text = StringBuilder()
        text.append(check).append(" |")

        for (i in rollList.indices) {
            val rollItem = rollList[i]

            if (rollItem.isCheck)
                text.append(" \u2713 ")
            else
                text.append(" - ")

            text.append(rollItem.text)

            if (i != rollList.size - 1) text.append(" |")
        }

        return text.toString()
    }

    @Query(value = "UPDATE ROLL_TABLE SET RL_POSITION = :position, RL_TEXT = :text WHERE RL_ID = :id")
    fun update(id: Long, position: Int, text: String)

    /**
     * Обновление выполнения конкретного пункта
     *
     * @param id    - Id пункта
     * @param check - Состояние отметки
     */
    @Query(value = "UPDATE ROLL_TABLE SET RL_CHECK = :check WHERE RL_ID = :id")
    fun update(id: Long, check: Boolean)

    /**
     * Обновление выполнения для всех пунктов
     *
     * @param id - Id заметки
     * @param check  - Состояние отметки
     */
    @Query(value = "UPDATE ROLL_TABLE SET RL_CHECK = :check WHERE RL_NOTE_ID = :id")
    fun updateAllCheck(id: Long, check: Boolean)

    /**
     * Удаление пунктов при сохранении после свайпа
     *
     * @param idNote - Id заметки
     * @param idSave - Id, которые остались в заметке
     */
    @Query(value = "DELETE FROM ROLL_TABLE WHERE RL_NOTE_ID = :idNote AND RL_ID NOT IN (:idSave)")
    fun delete(idNote: Long, idSave: List<Long>)

    /**
     * @param idNote - Id удаляемой заметки
     */
    @Query(value = "DELETE FROM ROLL_TABLE WHERE RL_NOTE_ID = :idNote")
    fun delete(idNote: Long)

}