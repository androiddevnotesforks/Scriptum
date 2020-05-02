package sgtmelon.scriptum.domain.model.item

import sgtmelon.extension.getTime
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.data.DbData.Alarm
import sgtmelon.scriptum.domain.model.data.DbData.Note
import sgtmelon.scriptum.domain.model.key.Complete
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.extension.clearSpace
import sgtmelon.scriptum.extension.getText
import sgtmelon.scriptum.presentation.adapter.NoteAdapter
import sgtmelon.scriptum.presentation.adapter.RollAdapter
import kotlin.math.min

/**
 * Model for store short information about note, use in [NoteAdapter]/[RollAdapter].
 */
sealed class NoteItem(
        var id: Long,
        var create: String,
        var change: String,
        var name: String,
        var text: String,
        @Color var color: Int,
        var rankId: Long,
        var rankPs: Int,
        var isBin: Boolean,
        var isStatus: Boolean,
        var alarmId: Long,
        var alarmDate: String
) {

    // TODO make deepCopy common

    val type: NoteType
        get() = when (this) {
            is Text -> NoteType.TEXT
            is Roll -> NoteType.ROLL
        }

    abstract fun isSaveEnabled(): Boolean

    //region Common functions

    fun switchStatus() = apply { isStatus = !isStatus }

    fun updateTime() = apply { change = getTime() }

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

    fun isVisible(rankIdVisibleList: List<Long>): Boolean {
        return !haveRank() || rankIdVisibleList.contains(rankId)
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
            create: String = getTime(),
            change: String = Note.Default.CHANGE,
            name: String = Note.Default.NAME,
            text: String = Note.Default.TEXT,
            @Color color: Int,
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
                color: Int = this.color,
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


        fun splitText() = text.split("\n".toRegex()).filter { it.isNotEmpty() }.toList()


        fun onSave() = apply {
            name = name.clearSpace()
            updateTime()
        }

        fun onConvert(): Roll {
            val noteItem = Roll(
                    id, create, change, name, text, color, rankId, rankPs, isBin, isStatus,
                    alarmId, alarmDate
            )

            var p = 0
            splitText().forEach {
                noteItem.list.add(RollItem(position = p++, text = it))
            }

            noteItem.updateTime()
            noteItem.updateComplete(Complete.EMPTY)

            return noteItem
        }

        //endregion

        companion object {
            fun getCreate(@Color color: Int): Text = Text(color = color)
        }

    }

    class Roll(
            id: Long = Note.Default.ID,
            create: String = getTime(),
            change: String = Note.Default.CHANGE,
            name: String = Note.Default.NAME,
            text: String = Note.Default.TEXT,
            @Color color: Int,
            rankId: Long = Note.Default.RANK_ID,
            rankPs: Int = Note.Default.RANK_PS,
            isBin: Boolean = Note.Default.BIN,
            isStatus: Boolean = Note.Default.STATUS,
            alarmId: Long = Alarm.Default.ID,
            alarmDate: String = Alarm.Default.DATE,
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
                color: Int = this.color,
                rankId: Long = this.rankId,
                rankPs: Int = this.rankPs,
                isBin: Boolean = this.isBin,
                isStatus: Boolean = this.isStatus,
                alarmId: Long = this.alarmId,
                alarmDate: String = this.alarmDate,
                list: MutableList<RollItem> = this.list.map { it.copy() }.toMutableList()
        ) = Roll(
                id, create, change, name, text, color, rankId, rankPs, isBin, isStatus,
                alarmId, alarmDate, list
        )


        fun updateComplete(complete: Complete? = null) = apply {
            val checkCount = when (complete) {
                null -> getCheck()
                Complete.EMPTY -> 0
                Complete.FULL -> list.size
            }

            val checkText = min(checkCount, INDICATOR_MAX_COUNT)
            val allText = min(list.size, INDICATOR_MAX_COUNT)

            text = "$checkText/$allText"
        }

        /**
         * Check/uncheck all items.
         */
        fun updateCheck(isCheck: Boolean) = apply {
            list.forEach { it.isCheck = isCheck }
            updateComplete(if (isCheck) Complete.FULL else Complete.EMPTY)
        }

        fun getCheck(): Int = list.filter { it.isCheck }.size


        fun onItemCheck(p: Int) {
            list.getOrNull(p)?.apply { isCheck = !isCheck } ?: return

            updateTime()
            updateComplete()
        }

        /**
         * If have some unchecked items - need turn them to true. Otherwise uncheck all items.
         */
        fun onItemLongCheck(): Boolean {
            val check = list.any { !it.isCheck }

            updateTime()
            updateCheck(check)

            return check
        }


        fun onSave() {
            list.apply {
                removeAll { it.text.clearSpace().isEmpty() }
                forEachIndexed { i, item ->
                    item.position = i
                    item.text = item.text.clearSpace()
                }
            }

            name = name.clearSpace()
            updateTime()
            updateComplete()
        }

        fun onConvert(): Text {
            val noteItem = Text(
                    id, create, change, name, text, color, rankId, rankPs, isBin, isStatus,
                    alarmId, alarmDate
            )

            noteItem.updateTime()
            noteItem.text = list.getText()

            return noteItem
        }

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

            if (!list.containsAll(other.list)) return false

            return true
        }

        override fun hashCode(): Int {
            return 31 * super.hashCode() + list.hashCode()
        }

        companion object {
            const val PREVIEW_SIZE = 4
            const val INDICATOR_MAX_COUNT = 99

            fun getCreate(@Color color: Int): Roll = Roll(color = color)
        }

    }

}