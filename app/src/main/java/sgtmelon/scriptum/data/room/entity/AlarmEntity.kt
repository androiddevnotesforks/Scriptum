package sgtmelon.scriptum.data.room.entity

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import sgtmelon.scriptum.domain.model.data.DbData.Alarm
import sgtmelon.scriptum.domain.model.data.DbData.Alarm.Default
import sgtmelon.scriptum.domain.model.data.DbData.Alarm.Room
import sgtmelon.scriptum.domain.model.data.DbData.Note

/**
 * Entity of note alarm.
 */
@Entity(
    tableName = Alarm.TABLE,
    foreignKeys = [ForeignKey(
        entity = NoteEntity::class,
        parentColumns = arrayOf(Note.ID),
        childColumns = arrayOf(Alarm.NOTE_ID),
        onUpdate = CASCADE,
        onDelete = CASCADE
    )],
    indices = [Index(value = [Alarm.NOTE_ID], name = Alarm.INDEX_NOTE_ID, unique = true)]
)
data class AlarmEntity(
    @ColumnInfo(name = Alarm.ID, defaultValue = Room.ID)
    @PrimaryKey(autoGenerate = true)
    var id: Long = Default.ID,

    @ColumnInfo(name = Alarm.NOTE_ID, defaultValue = Room.NOTE_ID)
    var noteId: Long = Default.NOTE_ID,

    @ColumnInfo(name = Alarm.DATE, defaultValue = Room.DATE)
    var date: String = Default.DATE
)