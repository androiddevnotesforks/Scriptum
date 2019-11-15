package sgtmelon.scriptum.model.item

import androidx.room.ColumnInfo
import androidx.room.Relation
import androidx.room.TypeConverters
import sgtmelon.extension.getTime
import sgtmelon.scriptum.adapter.NoteAdapter
import sgtmelon.scriptum.adapter.RollAdapter
import sgtmelon.scriptum.extension.getCheck
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.data.DbData
import sgtmelon.scriptum.model.data.DbData.Alarm
import sgtmelon.scriptum.model.data.DbData.Note
import sgtmelon.scriptum.model.data.DbData.Roll
import sgtmelon.scriptum.model.key.Complete
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.converter.BoolConverter
import sgtmelon.scriptum.room.converter.NoteTypeConverter

/**
 * Model for store short information about note, use in [NoteAdapter]/[RollAdapter].
 */
@TypeConverters(BoolConverter::class, NoteTypeConverter::class)
data class NoteItem(
        @ColumnInfo(name = Note.ID) var id: Long = Note.Default.ID,
        @ColumnInfo(name = Note.CREATE) var create: String,
        @ColumnInfo(name = Note.CHANGE) var change: String = Note.Default.CHANGE,
        @ColumnInfo(name = Note.NAME) var name: String = Note.Default.NAME,
        @ColumnInfo(name = Note.TEXT) var text: String = Note.Default.TEXT,
        @ColumnInfo(name = Note.COLOR) @Color var color: Int,
        @ColumnInfo(name = Note.TYPE) var type: NoteType,
        @ColumnInfo(name = Note.RANK_ID) var rankId: Long = Note.Default.RANK_ID,
        @ColumnInfo(name = Note.RANK_PS) var rankPs: Int = Note.Default.RANK_PS,
        @ColumnInfo(name = Note.BIN) var isBin: Boolean = Note.Default.BIN,
        @ColumnInfo(name = Note.STATUS) var isStatus: Boolean = Note.Default.STATUS,

        @Relation(parentColumn = Note.ID, entityColumn = Roll.NOTE_ID)
        val rollList: MutableList<RollItem> = ArrayList(),

        @ColumnInfo(name = Alarm.ID) var alarmId: Long = Alarm.Default.ID,
        @ColumnInfo(name = Alarm.DATE) var alarmDate: String = Alarm.Default.DATE
) {

    /**
     * TODO #TEST write unit test
     */

    /**
     * [complete] - use if you know check count.
     */
    fun updateComplete(complete: Complete? = null): Int = with(rollList) {
        val check = when(complete){
            Complete.EMPTY -> 0
            Complete.FULL -> size
            null -> getCheck()
        }

        text = "$check/$size"

        return check
    }

    /**
     * Check/uncheck all items.
     */
    fun updateCheck(isCheck: Boolean) = rollList.forEach { it.isCheck = isCheck }

    fun delete() = apply {
        change = getTime()
        isBin = true
        isStatus = false
    }

    fun restore() = apply {
        change = getTime()
        isBin = false
    }

    fun convert() = apply {
        change = getTime()
        type = when(type) {
            NoteType.TEXT -> NoteType.ROLL
            NoteType.ROLL -> NoteType.TEXT
        }
    }

    fun textToList() = text.split("\n".toRegex()).filter { it.isNotEmpty() }.toList()

    fun haveRank() = rankId != Note.Default.RANK_ID

    fun haveAlarm() = alarmId != Alarm.Default.ID && alarmDate != Alarm.Default.DATE

    fun clearAlarm() {
        alarmId = Alarm.Default.ID
        alarmDate = Alarm.Default.DATE
    }

    fun clearRank() {
        rankId = Note.Default.RANK_ID
        rankPs = Note.Default.RANK_PS
    }

    fun isSaveEnabled(): Boolean = when (type) {
        NoteType.TEXT -> text.isNotEmpty()
        NoteType.ROLL -> rollList.any { it.text.isNotEmpty() }
    }

    fun isVisible(rankIdVisibleList: List<Long>): Boolean {
        return rankId == Note.Default.RANK_ID || rankIdVisibleList.contains(rankId)
    }

    fun isNotVisible(rankIdVisibleList: List<Long>) = !isVisible(rankIdVisibleList)

    companion object {
        const val ROLL_OPTIMAL_SIZE = 4

        fun getCreate(create: String, @Color color: Int, type: NoteType): NoteItem {
            return NoteItem(create = create, color = color, type = type)
        }
    }

}