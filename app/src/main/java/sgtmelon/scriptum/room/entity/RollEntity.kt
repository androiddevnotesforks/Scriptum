package sgtmelon.scriptum.room.entity

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import org.json.JSONObject
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.data.DbData.Note
import sgtmelon.scriptum.model.data.DbData.Roll
import sgtmelon.scriptum.model.data.DbData.Roll.Default
import sgtmelon.scriptum.model.data.DbData.Roll.Room
import sgtmelon.scriptum.room.converter.BoolConverter

/**
 * Element of list in [NoteModel]
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
        @ColumnInfo(name = Roll.ID, defaultValue = Room.ID) @PrimaryKey(autoGenerate = true) var id: Long? = Default.ID,
        @ColumnInfo(name = Roll.NOTE_ID, defaultValue = Room.NOTE_ID) var noteId: Long = Default.NOTE_ID,
        @ColumnInfo(name = Roll.POSITION, defaultValue = Room.POSITION) var position: Int = Default.POSITION,
        @ColumnInfo(name = Roll.CHECK, defaultValue = Room.CHECK) var isCheck: Boolean = Default.CHECK,
        @ColumnInfo(name = Roll.TEXT, defaultValue = Room.TEXT) var text: String = Default.TEXT
) {

    /**
     * Replace [id] null value to -1 for [get] function
     */
    override fun toString() = JSONObject().apply {
        put(Roll.ID, if (id != null) id else -1L)
        put(Roll.NOTE_ID, noteId)
        put(Roll.POSITION, position)
        put(Roll.CHECK, isCheck)
        put(Roll.TEXT, text)
    }.toString()

    companion object {
        operator fun get(data: String): RollEntity? = try {
            JSONObject(data).let {
                val id = it.getLong(Roll.ID)
                return@let RollEntity(
                        if (id != -1L) id else null,
                        it.getLong(Roll.NOTE_ID),
                        it.getInt(Roll.POSITION),
                        it.getBoolean(Roll.CHECK),
                        it.getString(Roll.TEXT)
                )
            }
        } catch (e: Throwable) {
            null
        }
    }

}