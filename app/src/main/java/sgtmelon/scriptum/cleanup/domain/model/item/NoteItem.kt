package sgtmelon.scriptum.cleanup.domain.model.item

import kotlin.math.min
import sgtmelon.extensions.getCalendarText
import sgtmelon.scriptum.cleanup.presentation.adapter.RollAdapter
import sgtmelon.scriptum.infrastructure.adapter.NoteAdapter
import sgtmelon.scriptum.infrastructure.database.DbData.Note
import sgtmelon.scriptum.infrastructure.database.DbData.RollVisible
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.utils.extensions.note.copy
import sgtmelon.scriptum.infrastructure.utils.extensions.note.haveAlarm
import sgtmelon.scriptum.infrastructure.utils.extensions.note.haveRank
import sgtmelon.scriptum.infrastructure.utils.extensions.note.isSaveEnabled
import sgtmelon.scriptum.infrastructure.utils.extensions.note.type
import sgtmelon.scriptum.infrastructure.utils.extensions.note.updateTime

/**
 * Model for store short information about note, use in [NoteAdapter]/[RollAdapter].
 */
// TODO may be convert create/change into Calendar?
sealed class NoteItem(
    var id: Long,
    var create: String,
    var change: String,
    var name: String,
    var text: String,
    var color: Color,
    var rank: NoteRank,
    var isBin: Boolean,
    var isStatus: Boolean,
    var alarm: NoteAlarm
) {

    //region Remove after dataBinding refactor

    @Deprecated("Use extensions")
    val haveRankDepr
        get() = haveRank

    @Deprecated("Use extensions")
    val haveAlarmDepr
        get() = haveAlarm

    @Deprecated("Use extensions")
    val typeDepr: NoteType
        get() = type

    @Deprecated("Use extensions")
    val isSaveEnabledDepr
        get() = isSaveEnabled

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
        if (rank != other.rank) return false
        if (isBin != other.isBin) return false
        if (isStatus != other.isStatus) return false
        if (alarm != other.alarm) return false

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
        result = 31 * result + rank.hashCode()
        result = 31 * result + isBin.hashCode()
        result = 31 * result + isStatus.hashCode()
        result = 31 * result + alarm.hashCode()

        return result
    }

    class Text(
        id: Long = Note.Default.ID,
        create: String = getCalendarText(),
        change: String = Note.Default.CHANGE,
        name: String = Note.Default.NAME,
        text: String = Note.Default.TEXT,
        color: Color,
        rank: NoteRank = NoteRank(),
        isBin: Boolean = Note.Default.BIN,
        isStatus: Boolean = Note.Default.STATUS,
        alarm: NoteAlarm = NoteAlarm()
    ) : NoteItem(id, create, change, name, text, color, rank, isBin, isStatus, alarm) {

        fun deepCopy(
            id: Long = this.id,
            create: String = this.create,
            change: String = this.change,
            name: String = this.name,
            text: String = this.text,
            color: Color = this.color,
            rank: NoteRank = this.rank,
            isBin: Boolean = this.isBin,
            isStatus: Boolean = this.isStatus,
            alarm: NoteAlarm = this.alarm.copy()
        ) = Text(id, create, change, name, text, color, rank, isBin, isStatus, alarm)

    }

    class Roll(
        id: Long = Note.Default.ID,
        create: String = getCalendarText(),
        change: String = Note.Default.CHANGE,
        name: String = Note.Default.NAME,
        text: String = Note.Default.TEXT,
        color: Color,
        rank: NoteRank = NoteRank(),
        isBin: Boolean = Note.Default.BIN,
        isStatus: Boolean = Note.Default.STATUS,
        alarm: NoteAlarm = NoteAlarm(),
        var isVisible: Boolean = RollVisible.Default.VALUE,
        val list: MutableList<RollItem> = ArrayList()
    ) : NoteItem(id, create, change, name, text, color, rank, isBin, isStatus, alarm) {

        fun deepCopy(
            id: Long = this.id,
            create: String = this.create,
            change: String = this.change,
            name: String = this.name,
            text: String = this.text,
            color: Color = this.color,
            rank: NoteRank = this.rank,
            isBin: Boolean = this.isBin,
            isStatus: Boolean = this.isStatus,
            alarm: NoteAlarm = this.alarm.copy(),
            isVisible: Boolean = this.isVisible,
            list: MutableList<RollItem> = this.list.copy()
        ) = Roll(
            id, create, change, name, text, color, rank, isBin, isStatus, alarm, isVisible, list
        )


        fun updateComplete(knownCheckCount: Int? = null) = apply {
            val checkCount = knownCheckCount ?: getCheck()
            val checkText = min(checkCount, INDICATOR_MAX_COUNT)
            val allText = min(list.size, INDICATOR_MAX_COUNT)

            text = "$checkText/$allText"
        }

        // TODO may be some optimization: in some cases get last check value (not calculate it from start).
        fun getCheck(): Int = list.count { it.isCheck }


        fun onItemCheck(p: Int) {
            list.getOrNull(p)?.apply { isCheck = !isCheck } ?: return

            updateTime()
            updateComplete()
        }

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