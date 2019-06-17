package sgtmelon.scriptum.room.dao

import androidx.room.*
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.entity.AlarmEntity

/**
 * Класс для общения Dao предупреждений [RoomDb]
 *
 * @author SerjantArbuz
 */
@Dao
interface AlarmDao {

    @Insert fun insert(alarmEntity: AlarmEntity): Long

    @Delete fun delete(alarmEntity: AlarmEntity)

    @Update fun update(alarmEntity: AlarmEntity)

    @Transaction
    @Query(value = "SELECT * FROM NOTE_TABLE, ALARM_TABLE ORDER BY DATE(AL_DATE) DESC, TIME(AL_DATE) DESC")
    fun getTest() : MutableList<NoteModel>

}