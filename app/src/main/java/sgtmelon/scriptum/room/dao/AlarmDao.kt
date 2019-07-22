package sgtmelon.scriptum.room.dao

import androidx.room.*
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.entity.AlarmEntity

/**
 * Класс для общения Dao предупреждений [RoomDb]
 *
 * @author SerjantArbuz
 */
@Dao
interface AlarmDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(alarmEntity: AlarmEntity): Long

    @Query(value = "DELETE FROM ALARM_TABLE WHERE AL_ID = :id")
    fun delete(id: Long)

    @Update fun update(alarmEntity: AlarmEntity)

    @Transaction
    @Query(value = """SELECT NT_ID, NT_NAME, NT_COLOR, NT_TYPE, AL_ID, AL_DATE
        FROM NOTE_TABLE, ALARM_TABLE 
        WHERE NT_ID = AL_NOTE_ID
        ORDER BY DATE(AL_DATE) ASC, TIME(AL_DATE) ASC""")
    fun get(): MutableList<NotificationItem>

    @Query(value = "SELECT * FROM ALARM_TABLE WHERE AL_NOTE_ID = :noteId")
    operator fun get(noteId: Long): AlarmEntity?

}