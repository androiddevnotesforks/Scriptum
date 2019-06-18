package sgtmelon.scriptum.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.TypeConverters
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.converter.BoolConverter
import sgtmelon.scriptum.room.entity.RollEntity

/**
 * Класс для общения Dao списка [RoomDb]
 *
 * @author SerjantArbuz
 */
@Dao
@TypeConverters(BoolConverter::class)
interface RollDao {

    @Insert fun insert(rollEntity: RollEntity): Long

    @Query(value = "SELECT * FROM ROLL_TABLE ORDER BY RL_NOTE_ID ASC, RL_POSITION ASC")
    fun get(): List<RollEntity>

    @Query(value = "SELECT * FROM ROLL_TABLE WHERE RL_NOTE_ID = :noteId ORDER BY RL_POSITION")
    operator fun get(noteId: Long): MutableList<RollEntity>

    /**
     * Получение списка всех пунктов с позиции 0 по 3 (4 пунка)
     */
    @Query(value = """SELECT * FROM ROLL_TABLE
            WHERE RL_NOTE_ID = :noteId AND RL_POSITION BETWEEN 0 AND 3
            ORDER BY RL_POSITION ASC""")
    fun getView(noteId: Long): MutableList<RollEntity>

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
     * @param noteId - Id заметки
     * @param idSaveList - Id, которые остались в заметке
     */
    @Query(value = "DELETE FROM ROLL_TABLE WHERE RL_NOTE_ID = :noteId AND RL_ID NOT IN (:idSaveList)")
    fun delete(noteId: Long, idSaveList: List<Long>)

    /**
     * @param noteId - Id удаляемой заметки
     */
    @Query(value = "DELETE FROM ROLL_TABLE WHERE RL_NOTE_ID = :noteId")
    fun delete(noteId: Long)

}