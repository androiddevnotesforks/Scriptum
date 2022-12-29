@file:JvmName(name = "NoteExtensionsUtils")

package sgtmelon.scriptum.infrastructure.utils.extensions

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem

fun NoteItem.switchStatus() {
    isStatus = !isStatus
}