package sgtmelon.scriptum.app.model.item

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import org.json.JSONObject
import sgtmelon.scriptum.app.model.NoteModel
import sgtmelon.scriptum.app.room.converter.BoolConverter
import sgtmelon.scriptum.app.model.key.DbField.Roll
import sgtmelon.scriptum.app.model.key.DbField.Note

/**
 * Элемент списка заметок [NoteModel]
 */
@Entity(tableName = Roll.TABLE,
        foreignKeys = [ForeignKey(
                entity = NoteItem::class,
                parentColumns = arrayOf(Note.ID),
                childColumns = arrayOf(Roll.NOTE_ID),
                onUpdate = CASCADE,
                onDelete = CASCADE
        )],
        indices = [Index(Roll.NOTE_ID)]
)
@TypeConverters(BoolConverter::class)
class RollItem {

    @ColumnInfo(name = Roll.ID) @PrimaryKey(autoGenerate = true) var id: Long? = null

    @ColumnInfo(name = Roll.NOTE_ID) var noteId: Long = 0
    @ColumnInfo(name = Roll.POSITION) var position: Int = 0
    @ColumnInfo(name = Roll.CHECK) var isCheck = false
    @ColumnInfo(name = Roll.TEXT) var text = ""

    constructor()

    @Ignore constructor(data: String) {
        with(JSONObject(data)) {
            id = getLong(Roll.ID)
            id = if (id != -1L) id else null

            noteId = getLong(Roll.NOTE_ID)
            position = getInt(Roll.POSITION)
            isCheck = getBoolean(Roll.CHECK)
            text = getString(Roll.TEXT)
        }
    }

    override fun toString() = JSONObject().apply {
        put(Roll.ID, if (id != null) id else -1L)
        put(Roll.NOTE_ID, noteId)
        put(Roll.POSITION, position)
        put(Roll.CHECK, isCheck)
        put(Roll.TEXT, text)
    }.toString()

}