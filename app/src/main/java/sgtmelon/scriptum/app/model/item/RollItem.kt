package sgtmelon.scriptum.app.model.item

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import org.json.JSONObject
import sgtmelon.scriptum.app.model.NoteModel
import sgtmelon.scriptum.app.room.converter.BoolConverter
import sgtmelon.scriptum.office.annot.DbAnn

/**
 * Элемент списка заметок [NoteModel]
 */
@Entity(tableName = DbAnn.Roll.TABLE,
        foreignKeys = [ForeignKey(
                entity = NoteItem::class,
                parentColumns = arrayOf(DbAnn.Note.ID),
                childColumns = arrayOf(DbAnn.Roll.NOTE_ID),
                onUpdate = CASCADE,
                onDelete = CASCADE
        )],
        indices = [Index(DbAnn.Roll.NOTE_ID)]
)
@TypeConverters(BoolConverter::class)
class RollItem {

    @ColumnInfo(name = DbAnn.Roll.ID) @PrimaryKey(autoGenerate = true) var id: Long? = null

    @ColumnInfo(name = DbAnn.Roll.NOTE_ID) var noteId: Long = 0
    @ColumnInfo(name = DbAnn.Roll.POSITION) var position: Int = 0
    @ColumnInfo(name = DbAnn.Roll.CHECK) var isCheck = false
    @ColumnInfo(name = DbAnn.Roll.TEXT) var text = ""

    constructor()

    @Ignore constructor(data: String) {
        with(JSONObject(data)) {
            id = getLong(DbAnn.Roll.ID)
            id = if (id != -1L) id else null

            noteId = getLong(DbAnn.Roll.NOTE_ID)
            position = getInt(DbAnn.Roll.POSITION)
            isCheck = getBoolean(DbAnn.Roll.CHECK)
            text = getString(DbAnn.Roll.TEXT)
        }
    }

    override fun toString() = JSONObject().apply {
        put(DbAnn.Roll.ID, if (id != null) id else -1L)
        put(DbAnn.Roll.NOTE_ID, noteId)
        put(DbAnn.Roll.POSITION, position)
        put(DbAnn.Roll.CHECK, isCheck)
        put(DbAnn.Roll.TEXT, text)
    }.toString()

}