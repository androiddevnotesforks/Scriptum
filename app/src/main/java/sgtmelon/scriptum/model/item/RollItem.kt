package sgtmelon.scriptum.model.item

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import org.json.JSONObject
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.data.DbData.Note
import sgtmelon.scriptum.model.data.DbData.Roll
import sgtmelon.scriptum.room.converter.BoolConverter

/**
 * Элемент списка пунктов в [NoteModel]
 *
 * @author SerjantArbuz
 *
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
class RollItem(
        @ColumnInfo(name = Roll.ID) @PrimaryKey(autoGenerate = true) var id: Long? = null,
        @ColumnInfo(name = Roll.NOTE_ID) var noteId: Long = 0,
        @ColumnInfo(name = Roll.POSITION) var position: Int = 0,
        @ColumnInfo(name = Roll.CHECK) var isCheck: Boolean = false,
        @ColumnInfo(name = Roll.TEXT) var text: String = ""
) {

    override fun toString() = JSONObject().apply {
        put(Roll.ID, if (id != null) id else -1L)
        put(Roll.NOTE_ID, noteId)
        put(Roll.POSITION, position)
        put(Roll.CHECK, isCheck)
        put(Roll.TEXT, text)
    }.toString()

    companion object {
        operator fun get(data: String) = with(JSONObject(data)){
            val id = getLong(Roll.ID)

            return@with RollItem(
                    if (id != -1L) id else null,
                    getLong(Roll.NOTE_ID),
                    getInt(Roll.POSITION),
                    getBoolean(Roll.CHECK),
                    getString(Roll.TEXT)
            )
        }
    }

}