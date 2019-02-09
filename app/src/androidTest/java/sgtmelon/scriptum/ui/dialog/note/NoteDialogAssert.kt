package sgtmelon.scriptum.ui.dialog.note

import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.basic.BasicMatch
import sgtmelon.scriptum.office.annot.def.TypeNoteDef

class NoteDialogAssert : BasicMatch() {

    fun onDisplayContent(noteItem: NoteItem) {
        when (noteItem.isBin) {
            true -> {
                onDisplayText(R.string.dialog_menu_restore)
                onDisplayText(R.string.dialog_menu_copy)
                onDisplayText(R.string.dialog_menu_clear)
            }
            false -> {
                when (noteItem.type) {
                    TypeNoteDef.text -> {
                        onDisplayText(when (noteItem.isStatus) {
                            true -> R.string.dialog_menu_status_unbind
                            false -> R.string.dialog_menu_status_bind
                        })
                        onDisplayText(R.string.dialog_menu_convert_to_roll)
                        onDisplayText(R.string.dialog_menu_copy)
                        onDisplayText(R.string.dialog_menu_delete)
                    }
                    TypeNoteDef.roll -> {
                        onDisplayText(when (noteItem.isAllCheck) {
                            true -> R.string.dialog_menu_check_zero
                            false -> R.string.dialog_menu_check_all
                        })
                        onDisplayText(when (noteItem.isStatus) {
                            true -> R.string.dialog_menu_status_unbind
                            false -> R.string.dialog_menu_status_bind
                        })
                        onDisplayText(R.string.dialog_menu_convert_to_text)
                        onDisplayText(R.string.dialog_menu_copy)
                        onDisplayText(R.string.dialog_menu_delete)
                    }
                }
            }
        }
    }

}