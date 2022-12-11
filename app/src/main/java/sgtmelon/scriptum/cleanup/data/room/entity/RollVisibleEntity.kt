package sgtmelon.scriptum.cleanup.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import sgtmelon.scriptum.infrastructure.converter.types.BoolConverter
import sgtmelon.scriptum.infrastructure.database.DbData.Note
import sgtmelon.scriptum.infrastructure.database.DbData.RollVisible
import sgtmelon.scriptum.infrastructure.database.DbData.RollVisible.Default
import sgtmelon.scriptum.infrastructure.database.DbData.RollVisible.Room

/**
 * Modifier for hide done items in roll list.
 */
@Entity(
    tableName = RollVisible.TABLE,
    foreignKeys = [ForeignKey(
        entity = NoteEntity::class,
        parentColumns = arrayOf(Note.ID),
        childColumns = arrayOf(RollVisible.NOTE_ID),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(
        value = [RollVisible.NOTE_ID],
        name = RollVisible.INDEX_NOTE_ID,
        unique = true
    )]
)
@TypeConverters(BoolConverter::class)
data class RollVisibleEntity(
    @ColumnInfo(name = RollVisible.ID, defaultValue = Room.ID)
    @PrimaryKey(autoGenerate = true)
    var id: Long = Default.ID,

    @ColumnInfo(name = RollVisible.NOTE_ID, defaultValue = Room.NOTE_ID)
    var noteId: Long = Default.NOTE_ID,

    @ColumnInfo(name = RollVisible.VALUE, defaultValue = Room.VALUE)
    val value: Boolean = Default.VALUE
)