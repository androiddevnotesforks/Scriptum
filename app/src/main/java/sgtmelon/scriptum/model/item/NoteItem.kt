package sgtmelon.scriptum.model.item

import androidx.room.ColumnInfo
import androidx.room.Relation
import androidx.room.TypeConverters
import sgtmelon.extension.getTime
import sgtmelon.scriptum.adapter.NoteAdapter
import sgtmelon.scriptum.adapter.RollAdapter
import sgtmelon.scriptum.extension.getCheck2
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.data.DbData.Alarm
import sgtmelon.scriptum.model.data.DbData.Note
import sgtmelon.scriptum.model.data.DbData.Roll
import sgtmelon.scriptum.model.key.Complete
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.converter.BoolConverter
import sgtmelon.scriptum.room.converter.NoteTypeConverter

/**
 * Model for store short information about note, use in [NoteAdapter]/[RollAdapter]
 */
@TypeConverters(BoolConverter::class, NoteTypeConverter::class)
data class NoteItem(
        @ColumnInfo(name = Note.ID) var id: Long = Note.Default.ID,
        @ColumnInfo(name = Note.CREATE) var create: String,
        @ColumnInfo(name = Note.CHANGE) var change: String,
        @ColumnInfo(name = Note.NAME) var name: String,
        @ColumnInfo(name = Note.TEXT) var text: String,
        @ColumnInfo(name = Note.COLOR) @Color var color: Int,
        @ColumnInfo(name = Note.TYPE) var type: NoteType,
        @ColumnInfo(name = Note.RANK_ID) var rankId: Long,
        @ColumnInfo(name = Note.RANK_PS) var rankPs: Int,
        @ColumnInfo(name = Note.BIN) var isBin: Boolean,
        @ColumnInfo(name = Note.STATUS) var isStatus: Boolean,

        @Relation(parentColumn = Note.ID, entityColumn = Roll.NOTE_ID)
        val rollList: MutableList<RollItem>,

        @ColumnInfo(name = Alarm.ID) var alarmId: Long = Alarm.Default.NOTE_ID,
        @ColumnInfo(name = Alarm.DATE) var alarmDate: String = Alarm.Default.DATE
) {

    /**
     * [complete] - use if you know check count
     */
    fun updateComplete(complete: Complete? = null) = with(rollList) {
        val check = when(complete){
            Complete.EMPTY -> 0
            Complete.FULL -> size
            null -> getCheck2()
        }

        text = "$check/$size"
    }

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

    fun clearAlarm() {
        alarmId = Alarm.Default.ID
        alarmDate = Alarm.Default.DATE
    }

}