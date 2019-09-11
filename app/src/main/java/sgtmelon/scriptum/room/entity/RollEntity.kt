package sgtmelon.scriptum.room.entity

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
 */
@Entity(tableName = Roll.TABLE,
        foreignKeys = [ForeignKey(
                entity = NoteEntity::class,
                parentColumns = arrayOf(Note.ID),
                childColumns = arrayOf(Roll.NOTE_ID),
                onUpdate = CASCADE,
                onDelete = CASCADE
        )],
        indices = [Index(Roll.NOTE_ID)]
)
@TypeConverters(BoolConverter::class)
data class RollEntity(
        @ColumnInfo(name = Roll.ID) @PrimaryKey(autoGenerate = true) var id: Long? = null,
        @ColumnInfo(name = Roll.NOTE_ID) var noteId: Long = ND_NOTE_ID,
        @ColumnInfo(name = Roll.POSITION) var position: Int = ND_POSITION,
        @ColumnInfo(name = Roll.CHECK) var isCheck: Boolean = ND_CHECK,
        @ColumnInfo(name = Roll.TEXT) var text: String = ND_TEXT
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
        const val ND_NOTE_ID = 0L
        const val ND_POSITION = 0
        const val ND_CHECK = false
        const val ND_TEXT = ""

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