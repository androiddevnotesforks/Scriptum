package sgtmelon.scriptum.cleanup.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import sgtmelon.scriptum.infrastructure.database.DbData.Alarm
import sgtmelon.scriptum.infrastructure.database.DbData.Alarm.Default
import sgtmelon.scriptum.infrastructure.database.DbData.Alarm.Room
import sgtmelon.scriptum.infrastructure.database.DbData.Note

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