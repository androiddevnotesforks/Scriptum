package sgtmelon.scriptum.room.entity

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.data.DbData.Alarm
import sgtmelon.scriptum.model.data.DbData.Note

/**
 * Элемент будильника в [NoteModel]
 *
 * @author SerjantArbuz
 */
@Entity(tableName = Alarm.TABLE,
        foreignKeys = [ForeignKey(
                entity = NoteItem::class,
                parentColumns = arrayOf(Note.ID),
                childColumns = arrayOf(Alarm.NOTE_ID),
                onUpdate = CASCADE,
                onDelete = CASCADE
        )],
        indices = [Index(Alarm.NOTE_ID)]
)
data class AlarmEntity(
        @ColumnInfo(name = Alarm.ID) @PrimaryKey(autoGenerate = true) var id: Long = 0,
        @ColumnInfo(name = Alarm.NOTE_ID) var noteId: Long = 0,
        @ColumnInfo(name = Alarm.DATE) var date: String = ""
)