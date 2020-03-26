package sgtmelon.scriptum.data.room.entity

import androidx.room.*
import sgtmelon.scriptum.data.room.converter.type.BoolConverter
import sgtmelon.scriptum.domain.model.data.DbData.Note
import sgtmelon.scriptum.domain.model.data.DbData.RollVisible
import sgtmelon.scriptum.domain.model.data.DbData.RollVisible.Default
import sgtmelon.scriptum.domain.model.data.DbData.RollVisible.Room

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
        indices = [Index(value = [RollVisible.NOTE_ID], name = RollVisible.INDEX_NOTE_ID, unique = true)]
)
@TypeConverters(BoolConverter::class)
class RollVisibleEntity(
        @ColumnInfo(name = RollVisible.ID, defaultValue = Room.ID) @PrimaryKey(autoGenerate = true) val id: Long = Default.ID,
        @ColumnInfo(name = RollVisible.NOTE_ID, defaultValue = Room.NOTE_ID) val noteId: Long = Default.NOTE_ID,
        @ColumnInfo(name = RollVisible.VALUE, defaultValue = Room.VALUE) val value: Boolean = Default.VALUE
)