package sgtmelon.scriptum.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import sgtmelon.scriptum.extension.getCheck
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.data.DbData.Note
import sgtmelon.scriptum.model.data.DbData.Note.Default
import sgtmelon.scriptum.model.data.DbData.Note.Room
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.converter.BoolConverter
import sgtmelon.scriptum.room.converter.NoteTypeConverter

/**
 * Element of information about note for [NoteModel]
 */
@Entity(tableName = Note.TABLE)
@TypeConverters(BoolConverter::class, NoteTypeConverter::class)
data class NoteEntity(
        @ColumnInfo(name = Note.ID, defaultValue = Room.ID) @PrimaryKey(autoGenerate = true) var id: Long = Default.ID,
        @ColumnInfo(name = Note.CREATE, defaultValue = Room.CREATE) var create: String = Default.CREATE,
        @ColumnInfo(name = Note.CHANGE, defaultValue = Room.CHANGE) var change: String = Default.CHANGE,
        @ColumnInfo(name = Note.NAME, defaultValue = Room.NAME) var name: String = Default.NAME,
        @ColumnInfo(name = Note.TEXT, defaultValue = Room.TEXT) var text: String = Default.TEXT,
        @ColumnInfo(name = Note.COLOR, defaultValue = Room.COLOR) @Color var color: Int = Default.COLOR,
        @ColumnInfo(name = Note.TYPE, defaultValue = Room.TYPE) var type: NoteType = Default.TYPE,
        @ColumnInfo(name = Note.RANK_ID, defaultValue = Room.RANK_ID) var rankId: Long = Default.RANK_ID,
        @ColumnInfo(name = Note.RANK_PS, defaultValue = Room.RANK_PS) var rankPs: Int = Default.RANK_PS,
        @ColumnInfo(name = Note.BIN, defaultValue = Room.BIN) var isBin: Boolean = Default.BIN,
        @ColumnInfo(name = Note.STATUS, defaultValue = Room.STATUS) var isStatus: Boolean = Default.STATUS
) {

    fun setCompleteText(check: Int, size: Int) {
        text = "$check/$size"
    }

    fun setCompleteText(rollList: List<RollEntity>) {
        setCompleteText(rollList.getCheck(), rollList.size)
    }

    fun splitTextForRoll() =
            text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

    // TODO #RELEASE rename
    fun setText(list: List<RollEntity>) {
        text = list.joinToString(separator = "\n") { it.text }
    }

    fun isVisible(rankIdVisibleList: List<Long>) =
            rankId == Default.RANK_ID || rankIdVisibleList.contains(rankId)

    fun isNotVisible(rankIdVisibleList: List<Long>) =
            rankId != Default.RANK_ID && !rankIdVisibleList.contains(rankId)

    fun haveRank() = rankId != Default.RANK_ID

}