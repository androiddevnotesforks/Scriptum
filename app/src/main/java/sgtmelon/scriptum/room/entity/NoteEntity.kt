package sgtmelon.scriptum.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import sgtmelon.scriptum.extension.getCheck
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.data.DbData.Note
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.converter.BoolConverter
import sgtmelon.scriptum.room.converter.NoteTypeConverter

/**
 * Element of information about note for [NoteModel]
 */
@Entity(tableName = Note.TABLE)
@TypeConverters(BoolConverter::class, NoteTypeConverter::class)
data class NoteEntity(
        @ColumnInfo(name = Note.ID) @PrimaryKey(autoGenerate = true) var id: Long = ND_ID,
        @ColumnInfo(name = Note.CREATE) var create: String = ND_CREATE,
        @ColumnInfo(name = Note.CHANGE) var change: String = ND_CHANGE,
        @ColumnInfo(name = Note.NAME) var name: String = ND_NAME,
        @ColumnInfo(name = Note.TEXT) var text: String = ND_TEXT,
        @ColumnInfo(name = Note.COLOR) @Color var color: Int = ND_COLOR,
        @ColumnInfo(name = Note.TYPE) var type: NoteType = NoteType.TEXT,
        @ColumnInfo(name = Note.RANK_ID) var rankId: Long = ND_RANK_ID,
        @ColumnInfo(name = Note.RANK_PS) var rankPs: Int = ND_RANK_PS,
        @ColumnInfo(name = Note.BIN) var isBin: Boolean = ND_BIN,
        @ColumnInfo(name = Note.STATUS) var isStatus: Boolean = ND_STATUS
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
            rankId == ND_RANK_ID || rankIdVisibleList.contains(rankId)

    fun isNotVisible(rankIdVisibleList: List<Long>) =
            rankId != ND_RANK_ID && !rankIdVisibleList.contains(rankId)

    fun haveRank() = rankId != ND_RANK_ID

    companion object {
        const val ND_ID = 0L
        const val ND_CREATE = ""
        const val ND_CHANGE = ""
        const val ND_NAME = ""
        const val ND_TEXT = ""
        const val ND_COLOR = 0
        const val ND_RANK_ID = -1L
        const val ND_RANK_PS = -1
        const val ND_BIN = false
        const val ND_STATUS = false
    }

}