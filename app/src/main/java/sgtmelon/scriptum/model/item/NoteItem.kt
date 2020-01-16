package sgtmelon.scriptum.model.item

import androidx.annotation.VisibleForTesting
import androidx.room.ColumnInfo
import androidx.room.Relation
import androidx.room.TypeConverters
import sgtmelon.extension.getTime
import sgtmelon.scriptum.adapter.NoteAdapter
import sgtmelon.scriptum.adapter.RollAdapter
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.data.DbData.Alarm
import sgtmelon.scriptum.model.data.DbData.Note
import sgtmelon.scriptum.model.data.DbData.Roll
import sgtmelon.scriptum.model.key.Complete
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.converter.type.BoolConverter
import sgtmelon.scriptum.room.converter.type.NoteTypeConverter
import kotlin.math.min

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

    fun deepCopy(
            id: Long = this.id,
            create: String = this.create,
            change: String = this.change,
            name: String = this.name,
            text: String = this.text,
            color: Int = this.color,
            type: NoteType = this.type,
            rankId: Long = this.rankId,
            rankPs: Int = this.rankPs,
            isBin: Boolean = this.isBin,
            isStatus: Boolean = this.isStatus,
            rollList: MutableList<RollItem> = this.rollList.map { it.copy() }.toMutableList(),
            alarmId: Long = this.alarmId,
            alarmDate: String = this.alarmDate
    ) = NoteItem(
            id, create, change, name, text, color, type, rankId, rankPs, isBin, isStatus,
            rollList, alarmId, alarmDate
    )


    fun updateComplete(complete: Complete? = null) = apply {
        val checkCount = when(complete){
            null -> getCheck()
            Complete.EMPTY -> 0
            Complete.FULL -> rollList.size
        }

        text = "${min(checkCount, INDICATOR_MAX_COUNT)}/${min(rollList.size, INDICATOR_MAX_COUNT)}"
    }

    /**
     * Check/uncheck all items.
     */
    fun updateCheck(isCheck: Boolean) = apply {
        rollList.forEach { it.isCheck = isCheck }
        updateComplete(if (isCheck) Complete.FULL else Complete.EMPTY)
    }

    fun getCheck(): Int = rollList.filter { it.isCheck }.size


    fun updateTime() = apply { change = getTime() }

    fun delete() = apply {
        updateTime()
        isBin = true
        isStatus = false
    }

    fun restore() = apply {
        updateTime()
        isBin = false
    }

    fun convert() = apply {
        updateTime()
        type = when(type) {
            NoteType.TEXT -> NoteType.ROLL
            NoteType.ROLL -> NoteType.TEXT
        }
    }

    fun textToList() = text.split("\n".toRegex()).filter { it.isNotEmpty() }.toList()

    fun haveRank() = rankId != Note.Default.RANK_ID && rankPs != Note.Default.RANK_PS

    fun haveAlarm() = alarmId != Alarm.Default.ID && alarmDate != Alarm.Default.DATE

    fun clearRank() = apply {
        rankId = Note.Default.RANK_ID
        rankPs = Note.Default.RANK_PS
    }

    fun clearAlarm() = apply {
        alarmId = Alarm.Default.ID
        alarmDate = Alarm.Default.DATE
    }

    fun isSaveEnabled(): Boolean {
        return when (type) {
            NoteType.TEXT -> text.isNotEmpty()
            NoteType.ROLL -> rollList.any { it.text.isNotEmpty() }
        }
    }

    fun isVisible(rankIdVisibleList: List<Long>): Boolean {
        return !haveRank() || rankIdVisibleList.contains(rankId)
    }

    fun isNotVisible(rankIdVisibleList: List<Long>) = !isVisible(rankIdVisibleList)

    companion object {
        const val ROLL_OPTIMAL_SIZE = 4
        const val INDICATOR_MAX_COUNT = 99

        fun getCreate(@Color color: Int, type: NoteType): NoteItem {
            return NoteItem(create = getTime(), color = color, type = type)
        }
    }

}