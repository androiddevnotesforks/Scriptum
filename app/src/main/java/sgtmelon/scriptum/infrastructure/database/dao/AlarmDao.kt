package sgtmelon.scriptum.infrastructure.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import sgtmelon.scriptum.cleanup.data.room.RoomDb
import sgtmelon.scriptum.cleanup.data.room.annotation.DaoDeprecated
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.domain.model.data.DbData
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem

/**
 * Interface for communication [DbData.Alarm.TABLE] with [RoomDb].
 */
@Dao
interface AlarmDao {

    @Deprecated(DaoDeprecated.INSERT)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(alarmEntity: AlarmEntity): Long

    @Query(value = "DELETE FROM ALARM_TABLE WHERE AL_NOTE_ID = :noteId")
    suspend fun delete(noteId: Long)

    @Update
    suspend fun update(alarmEntity: AlarmEntity)

    @Transaction
    @Query(value = """SELECT NT_ID, NT_NAME, NT_COLOR, NT_TYPE, AL_ID, AL_DATE
        FROM NOTE_TABLE, ALARM_TABLE 
        WHERE NT_ID = AL_NOTE_ID AND NT_ID = :noteId
        ORDER BY DATE(AL_DATE) ASC, TIME(AL_DATE) ASC""")
    suspend fun getItem(noteId: Long): NotificationItem?

    @Transaction
    @Query(value = """SELECT NT_ID, NT_NAME, NT_COLOR, NT_TYPE, AL_ID, AL_DATE
        FROM NOTE_TABLE, ALARM_TABLE 
        WHERE NT_ID = AL_NOTE_ID
        ORDER BY DATE(AL_DATE) ASC, TIME(AL_DATE) ASC""")
    suspend fun getList(): MutableList<NotificationItem>

    @Query(value = "SELECT * FROM ALARM_TABLE ORDER BY AL_NOTE_ID")
    suspend fun get(): List<AlarmEntity>

    @Query(value = "SELECT * FROM ALARM_TABLE WHERE AL_NOTE_ID = :noteId")
    suspend fun get(noteId: Long): AlarmEntity?

    @Deprecated(DaoDeprecated.LIST)
    @Query(value = "SELECT * FROM ALARM_TABLE WHERE AL_NOTE_ID IN (:noteIdList)")
    suspend fun get(noteIdList: List<Long>): List<AlarmEntity>

    @Query(value = """SELECT COUNT(AL_ID) FROM ALARM_TABLE""")
    suspend fun getCount(): Int

    @Deprecated(DaoDeprecated.LIST)
    @Query(value = "SELECT COUNT(AL_ID) FROM ALARM_TABLE WHERE AL_NOTE_ID IN (:noteIdList)")
    suspend fun getCount(noteIdList: List<Long>): Int
}