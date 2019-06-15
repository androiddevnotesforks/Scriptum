package sgtmelon.scriptum.model.item

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import sgtmelon.scriptum.adapter.NotificationAdapter
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.data.DbData.Alarm
import sgtmelon.scriptum.room.converter.StringConverter

/**
 * Элемент списка для [NotificationAdapter] и информации о заметке [NoteModel]
 *
 * @author SerjantArbuz
 */
@Entity(tableName = Alarm.TABLE)
@TypeConverters(StringConverter::class)
class AlarmItem(
        @ColumnInfo(name = Alarm.ID) @PrimaryKey(autoGenerate = true) var id: Long = 0,
        @ColumnInfo(name = Alarm.NOTE_ID) var noteId: Long = 0,
        @ColumnInfo(name = Alarm.DATE) var date: String = ""
)