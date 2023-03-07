package sgtmelon.scriptum.cleanup.domain.model.item

import kotlinx.serialization.Serializable
import sgtmelon.extensions.getCalendarText
import sgtmelon.scriptum.infrastructure.adapter.NoteAdapter
import sgtmelon.scriptum.infrastructure.adapter.RollAdapter
import sgtmelon.scriptum.infrastructure.database.DbData.Note
import sgtmelon.scriptum.infrastructure.database.DbData.RollVisible
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.utils.extensions.note.type

/**
 * Model for store short information about note, use in [NoteAdapter]/[RollAdapter].
 */
// TODO may be convert create/change into Calendar?
@Serializable
sealed class NoteItem {

    abstract var id: Long
    abstract var create: String
    abstract var change: String
    abstract var name: String
    abstract var text: String
    abstract var color: Color
    abstract var rank: NoteRank
    abstract var isBin: Boolean
    abstract var isStatus: Boolean
    abstract var alarm: NoteAlarm

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

    @Serializable
    class Text(
        override var id: Long = Note.Default.ID,
        override var create: String = getCalendarText(),
        override var change: String = Note.Default.CHANGE,
        override var name: String = Note.Default.NAME,
        override var text: String = Note.Default.TEXT,
        override var color: Color,
        override var rank: NoteRank = NoteRank(),
        override var isBin: Boolean = Note.Default.BIN,
        override var isStatus: Boolean = Note.Default.STATUS,
        override var alarm: NoteAlarm = NoteAlarm()
    ) : NoteItem()

    @Serializable
    class Roll(
        override var id: Long = Note.Default.ID,
        override var create: String = getCalendarText(),
        override var change: String = Note.Default.CHANGE,
        override var name: String = Note.Default.NAME,
        override var text: String = Note.Default.TEXT,
        override var color: Color,
        override var rank: NoteRank = NoteRank(),
        override var isBin: Boolean = Note.Default.BIN,
        override var isStatus: Boolean = Note.Default.STATUS,
        override var alarm: NoteAlarm = NoteAlarm(),
        var isVisible: Boolean = RollVisible.Default.VALUE,
        val list: MutableList<RollItem> = ArrayList()
    ) : NoteItem() {

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