package sgtmelon.scriptum.room.dao

import androidx.room.*
import sgtmelon.scriptum.model.item.AlarmItem
import sgtmelon.scriptum.room.RoomDb

/**
 * Класс для общения Dao предупреждений [RoomDb]
 *
 * @author SerjantArbuz
 */
@Dao
interface AlarmDao {

    @Insert fun insert(item: AlarmItem): Long

    @Delete fun delete(item: AlarmItem)

    @Update fun update(item: AlarmItem)

    @Transaction
    @Query(value = "SELECT * FROM NOTE_TABLE, ALARM_TABLE ORDER BY DATE(AL_DATE) DESC, TIME(AL_DATE) DESC")
    fun getTest() : MutableList<NoteModel>

}