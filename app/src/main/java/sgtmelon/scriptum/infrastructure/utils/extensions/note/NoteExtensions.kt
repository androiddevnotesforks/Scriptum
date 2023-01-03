@file:JvmName(name = "NoteExtensionsUtils")

package sgtmelon.scriptum.infrastructure.utils.extensions.note

import sgtmelon.extensions.getCalendarText
import sgtmelon.extensions.removeExtraSpace
import sgtmelon.scriptum.cleanup.domain.model.item.NoteAlarm
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NoteRank
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.extension.clearAdd
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.utils.extensions.getIndicatorText

// TODO create tests

val NoteItem.type: NoteType
    get() = when (this) {
        is NoteItem.Text -> NoteType.TEXT
        is NoteItem.Roll -> NoteType.ROLL
    }

fun NoteItem.updateTime() = run { change = getCalendarText() }

fun NoteItem.switchStatus() = run { isStatus = !isStatus }

val NoteItem.haveRank get() = rank != NoteRank()

val NoteItem.haveAlarm get() = alarm != NoteAlarm()

fun NoteItem.clearRank() = apply { rank = NoteRank() }

fun NoteItem.clearAlarm() = apply { alarm = NoteAlarm() }

val NoteItem.isSaveEnabled: Boolean
    get() {
        return when (this) {
            is NoteItem.Text -> text.isNotEmpty()
            is NoteItem.Roll -> list.any { it.text.isNotEmpty() }
        }
    }

fun NoteItem.Roll.updateComplete() = apply {
    text = getCompleteText(list.getCheckCount(), list.size)
}

private fun getCompleteText(check: Int, size: Int): String {
    return "${check.getIndicatorText()}/${size.getIndicatorText()}"
}

//region On.. functions

fun NoteItem.onSave() {
    name = name.removeExtraSpace()
    updateTime()

    when (this) {
        is NoteItem.Text -> Unit
        is NoteItem.Roll -> onSave()
    }
}

private fun NoteItem.Roll.onSave() {
    list.removeAll { it.text.removeExtraSpace().isEmpty() }

    var checkCount = 0
    list.forEachIndexed { i, it ->
        if (it.isCheck) {
            checkCount++
        }

        it.position = i
        it.text = it.text.removeExtraSpace()
    }

    text = getCompleteText(checkCount, list.size)
}

// TODO move it inside noteConverter?
fun NoteItem.Text.onConvert(): NoteItem.Roll {
    val list = text.splitToRoll()
    val completeText = getCompleteText(check = 0, list.size)

    val item = NoteItem.Roll(
        id, create, change, name, completeText, color, rank.copy(), isBin, isStatus, alarm.copy()
    )

    item.list.clearAdd(list)
    item.updateTime()

    return item
}

// TODO move it inside noteConverter?
fun NoteItem.Roll.onConvert(): NoteItem.Text {
    val item = NoteItem.Text(
        id, create, change, name, text, color, rank.copy(), isBin, isStatus, alarm.copy()
    )

    item.updateTime()
    item.text = list.joinToText()

    return item
}

fun NoteItem.onDelete() = apply {
    updateTime()
    clearAlarm()
    isBin = true
    isStatus = false
}

fun NoteItem.onRestore() = apply {
    updateTime()
    isBin = false
}

fun NoteItem.Roll.onItemCheck(p: Int) = apply {
    list.getOrNull(p)?.apply { isCheck = !isCheck } ?: return@apply

    updateTime()
    updateComplete()
}

//endregion

fun NoteItem.Text.copy(
    id: Long = this.id,
    create: String = this.create,
    change: String = this.change,
    name: String = this.name,
    text: String = this.text,
    color: Color = this.color,
    rank: NoteRank = this.rank.copy(),
    isBin: Boolean = this.isBin,
    isStatus: Boolean = this.isStatus,
    alarm: NoteAlarm = this.alarm.copy()
): NoteItem.Text {
    return NoteItem.Text(id, create, change, name, text, color, rank, isBin, isStatus, alarm)
}

fun NoteItem.Roll.copy(
    id: Long = this.id,
    create: String = this.create,
    change: String = this.change,
    name: String = this.name,
    text: String = this.text,
    color: Color = this.color,
    rank: NoteRank = this.rank.copy(),
    isBin: Boolean = this.isBin,
    isStatus: Boolean = this.isStatus,
    alarm: NoteAlarm = this.alarm.copy(),
    isVisible: Boolean = this.isVisible,
    list: MutableList<RollItem> = this.list.copy()
): NoteItem.Roll {
    return NoteItem.Roll(
        id, create, change, name, text, color, rank, isBin, isStatus, alarm, isVisible, list
    )
}