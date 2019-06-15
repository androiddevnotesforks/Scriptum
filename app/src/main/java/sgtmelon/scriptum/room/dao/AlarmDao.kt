package sgtmelon.scriptum.room.dao

import androidx.room.*
import sgtmelon.scriptum.model.item.AlarmItem
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.converter.NoteTypeConverter

/**
 * Класс для общения Dao предупреждений [RoomDb]
 *
 * @author SerjantArbuz
 */
@Dao
@TypeConverters(NoteTypeConverter::class)
interface AlarmDao {

    @Insert fun insert(item: AlarmItem): Long

    @Delete fun delete(item: AlarmItem)

    @Update fun update(item: AlarmItem)

}