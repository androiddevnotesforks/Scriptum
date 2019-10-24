package sgtmelon.scriptum.room.entity

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.data.DbData.Alarm
import sgtmelon.scriptum.model.data.DbData.Note

/**
 * Element of alarm for [NoteModel]
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
        @ColumnInfo(name = Alarm.ID) @PrimaryKey(autoGenerate = true) var id: Long = ND_ID,
        @ColumnInfo(name = Alarm.NOTE_ID) var noteId: Long = ND_NOTE_ID,
        @ColumnInfo(name = Alarm.DATE) var date: String = ND_DATE
) {

    fun needInsert() = id == ND_ID

    fun clear() = apply {
        id = ND_ID
        date = ND_DATE
    }

    companion object {
        const val ND_ID = 0L
        const val ND_NOTE_ID = 0L
        const val ND_DATE = ""
    }

}