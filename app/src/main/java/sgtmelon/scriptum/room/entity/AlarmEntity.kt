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
                entity = NoteEntity::class,
                parentColumns = arrayOf(Note.ID),
                childColumns = arrayOf(Alarm.NOTE_ID),
                onUpdate = CASCADE,
                onDelete = CASCADE
        )],
        indices = [Index(Alarm.NOTE_ID)]
)
data class AlarmEntity(
        @ColumnInfo(name = Alarm.ID) @PrimaryKey(autoGenerate = true) var id: Long = ND_ID,
        @ColumnInfo(name = Alarm.NOTE_ID) var noteId: Long = ND_NOTE_ID,
        @ColumnInfo(name = Alarm.DATE) var date: String = ND_DATE
) {

    fun needInsert() = id == ND_ID

    fun clear() {
        id = ND_ID
        noteId = ND_NOTE_ID
        date = ND_DATE
    }

    companion object {
        const val ND_ID = 0L
        const val ND_NOTE_ID = 0L
        const val ND_DATE = ""
    }

}