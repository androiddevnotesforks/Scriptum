package sgtmelon.scriptum.cleanup.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Note
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Note.Default
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Note.Room
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.converter.key.NoteTypeConverter
import sgtmelon.scriptum.infrastructure.converter.types.BoolConverter
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType

/**
 * Entity with common information about note.
 */
@Entity(tableName = Note.TABLE)
@TypeConverters(BoolConverter::class, ColorConverter::class, NoteTypeConverter::class)
data class NoteEntity(
    @ColumnInfo(name = Note.ID, defaultValue = Room.ID)
    @PrimaryKey(autoGenerate = true)
    var id: Long = Default.ID,

    @ColumnInfo(name = Note.CREATE, defaultValue = Room.CREATE)
    var create: String = Default.CREATE,

    @ColumnInfo(name = Note.CHANGE, defaultValue = Room.CHANGE)
    var change: String = Default.CHANGE,

    @ColumnInfo(name = Note.NAME, defaultValue = Room.NAME)
    var name: String = Default.NAME,

    @ColumnInfo(name = Note.TEXT, defaultValue = Room.TEXT)
    var text: String = Default.TEXT,

    @ColumnInfo(name = Note.COLOR, defaultValue = Room.COLOR)
    var color: Color = Default.COLOR,

    @ColumnInfo(name = Note.TYPE, defaultValue = Room.TYPE)
    var type: NoteType = Default.TYPE,

    @ColumnInfo(name = Note.RANK_ID, defaultValue = Room.RANK_ID)
    var rankId: Long = Default.RANK_ID,

    @ColumnInfo(name = Note.RANK_PS, defaultValue = Room.RANK_PS)
    var rankPs: Int = Default.RANK_PS,

    @ColumnInfo(name = Note.BIN, defaultValue = Room.BIN)
    var isBin: Boolean = Default.BIN,

    @ColumnInfo(name = Note.STATUS, defaultValue = Room.STATUS)
    var isStatus: Boolean = Default.STATUS
) {

    fun haveRank(): Boolean = rankId != Default.RANK_ID && rankPs != Default.RANK_PS
}