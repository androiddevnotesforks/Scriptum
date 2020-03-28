package sgtmelon.scriptum.extension

import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.key.NoteType

fun NoteItem.onSave() = when (type) {
    NoteType.TEXT -> onSaveText()
    NoteType.ROLL -> onSaveRoll()
}

private fun NoteItem.onSaveText() {
    name = name.clearSpace()
    updateTime()
}

private fun NoteItem.onSaveRoll() {
    rollList.apply {
        removeAll { it.text.clearSpace().isEmpty() }
        forEachIndexed { i, item ->
            item.position = i
            item.text = item.text.clearSpace()
        }
    }

    name = name.clearSpace()
    updateTime().updateComplete()
}


fun NoteItem.onItemCheck(p: Int) {
    rollList.getOrNull(p)?.apply { isCheck = !isCheck }
    updateTime().updateComplete()
}

/**
 * If have some unchecked items - need turn them to true. Otherwise uncheck all items.
 */
fun NoteItem.onItemLongCheck(): Boolean {
    val check = rollList.any { !it.isCheck }

    updateTime().updateCheck(check)

    return check
}