package sgtmelon.scriptum.cleanup.domain.model.item

import kotlin.math.min
import sgtmelon.extensions.getCalendarText
import sgtmelon.scriptum.cleanup.extension.copy
import sgtmelon.scriptum.cleanup.extension.getText
import sgtmelon.scriptum.cleanup.presentation.adapter.RollAdapter
import sgtmelon.scriptum.infrastructure.adapter.NoteAdapter
import sgtmelon.scriptum.infrastructure.database.DbData.Alarm
import sgtmelon.scriptum.infrastructure.database.DbData.Note
import sgtmelon.scriptum.infrastructure.database.DbData.RollVisible
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.utils.extensions.updateTime

/**
 * Model for store short information about note, use in [NoteAdapter]/[RollAdapter].
 */
sealed class NoteItem(
    var id: Long,
    var create: String,
    var change: String,
    var name: String,
    var text: String,
    var color: Color,
    var rankId: Long,
    var rankPs: Int,
    var isBin: Boolean,
    var isStatus: Boolean,
    var alarmId: Long,
    var alarmDate: String
) {

    val type: NoteType
        get() = when (this) {
            is Text -> NoteType.TEXT
            is Roll -> NoteType.ROLL
        }

    abstract fun isSaveEnabled(): Boolean

    //region Common functions

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


    fun onDelete() = apply {
        updateTime()
        isBin = true
        isStatus = false
    }

    fun onRestore() = apply {
        updateTime()
        isBin = false
    }

    //endregion

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NoteItem

        if (type != other.type) return false

        if (id != other.id) return false
        if (create != other.create) return false
        if (change != other.change) return false
        if (name != other.name) return false
        if (text != other.text) return false
        if (color != other.color) return false
        if (rankId != other.rankId) return false
        if (rankPs != other.rankPs) return false
        if (isBin != other.isBin) return false
        if (isStatus != other.isStatus) return false
        if (alarmId != other.alarmId) return false
        if (alarmDate != other.alarmDate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = 0

        result = type.hashCode() // 31 * result = always zero
        result = 31 * result + id.hashCode()
        result = 31 * result + create.hashCode()
        result = 31 * result + change.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + text.hashCode()
        result = 31 * result + color.hashCode()
        result = 31 * result + rankId.hashCode()
        result = 31 * result + rankPs.hashCode()
        result = 31 * result + isBin.hashCode()
        result = 31 * result + isStatus.hashCode()
        result = 31 * result + alarmId.hashCode()
        result = 31 * result + alarmDate.hashCode()

        return result
    }

    class Text(
        id: Long = Note.Default.ID,
        create: String = getCalendarText(),
        change: String = Note.Default.CHANGE,
        name: String = Note.Default.NAME,
        text: String = Note.Default.TEXT,
        color: Color,
        rankId: Long = Note.Default.RANK_ID,
        rankPs: Int = Note.Default.RANK_PS,
        isBin: Boolean = Note.Default.BIN,
        isStatus: Boolean = Note.Default.STATUS,
        alarmId: Long = Alarm.Default.ID,
        alarmDate: String = Alarm.Default.DATE
    ) : NoteItem(
        id, create, change, name, text, color, rankId, rankPs, isBin, isStatus,
        alarmId, alarmDate
    ) {

        override fun isSaveEnabled(): Boolean = text.isNotEmpty()

        //region Common functions

        fun deepCopy(
            id: Long = this.id,
            create: String = this.create,
            change: String = this.change,
            name: String = this.name,
            text: String = this.text,
            color: Color = this.color,
            rankId: Long = this.rankId,
            rankPs: Int = this.rankPs,
            isBin: Boolean = this.isBin,
            isStatus: Boolean = this.isStatus,
            alarmId: Long = this.alarmId,
            alarmDate: String = this.alarmDate
        ) = Text(
            id, create, change, name, text, color, rankId, rankPs, isBin, isStatus,
            alarmId, alarmDate
        )

        fun onConvert(): Roll {
            val noteItem = Roll(
                id, create, change, name, text, color, rankId, rankPs, isBin, isStatus,
                alarmId, alarmDate
            )

            for ((i, it) in splitText().withIndex()) {
                noteItem.list.add(RollItem(position = i, text = it))
            }

            noteItem.updateTime()
            noteItem.updateComplete(knownCheckCount = 0)

            return noteItem
        }

        fun splitText() = text.split("\n".toRegex()).filter { it.isNotEmpty() }.toList()

        //endregion

    }

    class Roll(
        id: Long = Note.Default.ID,
        create: String = getCalendarText(),
        change: String = Note.Default.CHANGE,
        name: String = Note.Default.NAME,
        text: String = Note.Default.TEXT,
        color: Color,
        rankId: Long = Note.Default.RANK_ID,
        rankPs: Int = Note.Default.RANK_PS,
        isBin: Boolean = Note.Default.BIN,
        isStatus: Boolean = Note.Default.STATUS,
        alarmId: Long = Alarm.Default.ID,
        alarmDate: String = Alarm.Default.DATE,
        var isVisible: Boolean = RollVisible.Default.VALUE,
        val list: MutableList<RollItem> = ArrayList()
    ) : NoteItem(
        id, create, change, name, text, color, rankId, rankPs, isBin, isStatus,
        alarmId, alarmDate
    ) {

        override fun isSaveEnabled(): Boolean = list.any { it.text.isNotEmpty() }

        //region Common functions

        fun deepCopy(
            id: Long = this.id,
            create: String = this.create,
            change: String = this.change,
            name: String = this.name,
            text: String = this.text,
            color: Color = this.color,
            rankId: Long = this.rankId,
            rankPs: Int = this.rankPs,
            isBin: Boolean = this.isBin,
            isStatus: Boolean = this.isStatus,
            alarmId: Long = this.alarmId,
            alarmDate: String = this.alarmDate,
            isVisible: Boolean = this.isVisible,
            list: MutableList<RollItem> = this.list.copy()
        ) = Roll(
            id, create, change, name, text, color, rankId, rankPs, isBin, isStatus,
            alarmId, alarmDate, isVisible, list
        )


        fun updateComplete(knownCheckCount: Int? = null) = apply {
            val checkCount = knownCheckCount ?: getCheck()
            val checkText = min(checkCount, INDICATOR_MAX_COUNT)
            val allText = min(list.size, INDICATOR_MAX_COUNT)

            text = "$checkText/$allText"
        }

        // TODO may be some optimization: in some cases get last check value (not calculate it from start).
        fun getCheck(): Int = list.filter { it.isCheck }.size


        fun onItemCheck(p: Int) {
            list.getOrNull(p)?.apply { isCheck = !isCheck } ?: return

            updateTime()
            updateComplete()
        }

        fun onConvert() = onConvert(list)

        fun onConvert(list: List<RollItem>): Text {
            val noteItem = Text(
                id, create, change, name, text, color, rankId, rankPs, isBin, isStatus,
                alarmId, alarmDate
            )

            noteItem.updateTime()
            noteItem.text = list.getText()

            return noteItem
        }

        //endregion

        override fun equals(other: Any?): Boolean {
            if (!super.equals(other)) return false

            other as Roll

            if (isVisible != other.isVisible) return false
            if (list.size != other.list.size || !list.containsAll(other.list)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = super.hashCode()

            result = 31 * result + isVisible.hashCode()
            result = 31 * result + list.hashCode()

            return result
        }

        companion object {
            const val PREVIEW_SIZE = 4
            const val INDICATOR_MAX_COUNT = 99
        }
    }
}