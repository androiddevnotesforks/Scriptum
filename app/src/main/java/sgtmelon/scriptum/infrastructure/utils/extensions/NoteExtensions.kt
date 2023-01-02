@file:JvmName(name = "NoteExtensionsUtils")

package sgtmelon.scriptum.infrastructure.utils.extensions

import sgtmelon.extensions.getCalendarText
import sgtmelon.extensions.removeExtraSpace
import sgtmelon.scriptum.cleanup.domain.model.item.NoteAlarm
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NoteRank
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.extension.getText
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType

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
    list.forEachIndexed { i, it ->
        it.position = i
        it.text = it.text.removeExtraSpace()
    }

    updateComplete()
}

// TODO move it inside noteConverter?
fun NoteItem.Text.onConvert(): NoteItem.Roll {
    val item = NoteItem.Roll(
        id, create, change, name, text, color, rank.copy(), isBin, isStatus, alarm.copy()
    )

    item.list.addAll(splitText().mapIndexed { i, it -> RollItem(position = i, text = it) })
    item.updateTime()
    item.updateComplete(knownCheckCount = 0)

    return item
}

// TODO move it inside noteConverter?
fun NoteItem.Roll.onConvert(): NoteItem.Text {
    val item = NoteItem.Text(
        id, create, change, name, text, color, rank.copy(), isBin, isStatus, alarm.copy()
    )

    item.updateTime()
    item.text = list.getText()

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

//endregion