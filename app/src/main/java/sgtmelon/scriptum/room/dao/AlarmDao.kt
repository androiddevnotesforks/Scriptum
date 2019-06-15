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

    // TODO #RELEASE NoteModel
    @Query(value = "SELECT * FROM ALARM_TABLE ORDER BY DATE(AL_DATE) DESC, TIME(AL_DATE) DESC")
    fun get(): MutableList<AlarmItem>

}