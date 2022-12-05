package sgtmelon.scriptum.cleanup.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import sgtmelon.scriptum.cleanup.data.room.converter.type.BoolConverter
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Note
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Roll
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Roll.Default
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Roll.Room

/**
 * Entity of element in check list note.
 */
@Entity(
    tableName = Roll.TABLE,
    foreignKeys = [ForeignKey(
        entity = NoteEntity::class,
        parentColumns = arrayOf(Note.ID),
        childColumns = arrayOf(Roll.NOTE_ID),
        onUpdate = CASCADE,
        onDelete = CASCADE
    )],
    indices = [Index(value = [Roll.NOTE_ID], name = Roll.INDEX_NOTE_ID)]
)
@TypeConverters(BoolConverter::class)
data class RollEntity(
    @ColumnInfo(name = Roll.ID, defaultValue = Room.ID)
    @PrimaryKey(autoGenerate = true)
    var id: Long? = Default.ID,

    @ColumnInfo(name = Roll.NOTE_ID, defaultValue = Room.NOTE_ID)
    var noteId: Long = Default.NOTE_ID,

    @ColumnInfo(name = Roll.POSITION, defaultValue = Room.POSITION)
    var position: Int = Default.POSITION,

    @ColumnInfo(name = Roll.CHECK, defaultValue = Room.CHECK)
    var isCheck: Boolean = Default.CHECK,

    @ColumnInfo(name = Roll.TEXT, defaultValue = Room.TEXT)
    var text: String = Default.TEXT
)