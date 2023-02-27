package sgtmelon.scriptum.cleanup.domain.model.item

import sgtmelon.extensions.getCalendarText
import sgtmelon.scriptum.infrastructure.adapter.NoteAdapter
import sgtmelon.scriptum.infrastructure.adapter.RollAdapter
import sgtmelon.scriptum.infrastructure.database.DbData.Note
import sgtmelon.scriptum.infrastructure.database.DbData.RollVisible
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.utils.extensions.note.haveAlarm
import sgtmelon.scriptum.infrastructure.utils.extensions.note.haveRank
import sgtmelon.scriptum.infrastructure.utils.extensions.note.type

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
        var result: Int = type.hashCode()

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
    ) : NoteItem(id, create, change, name, text, color, rank, isBin, isStatus, alarm)

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

        override fun equals(other: Any?): Boolean {
            if (!super.equals(other)) return false

            other as Roll

            if (isVisible != other.isVisible) return false
            if (list != other.list) return false

            return true
        }

        override fun hashCode(): Int {
            var result = super.hashCode()

            result = 31 * result + isVisible.hashCode()
            result = 31 * result + list.hashCode()

            return result
        }
    }
}