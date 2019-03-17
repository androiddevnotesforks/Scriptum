package sgtmelon.scriptum.ui.dialog

import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.key.NoteType
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch

class NoteDialog : ParentUi() {

    fun assert(func: Assert.() -> Unit) = Assert().apply { func() }

    fun onClickRestore() = action { onClickText(R.string.dialog_menu_restore) }

    fun onClickClear() = action { onClickText(R.string.dialog_menu_clear) }

    fun onClickCopy() = action { onClickText(R.string.dialog_menu_copy) }

    companion object {
        operator fun invoke(func: NoteDialog.() -> Unit) = NoteDialog().apply { func() }
    }

    class Assert : BasicMatch() {

        fun onDisplayContent(noteItem: NoteItem) {
            when (noteItem.isBin) {
                true -> {
                    onDisplayText(R.string.dialog_menu_restore)
                    onDisplayText(R.string.dialog_menu_copy)
                    onDisplayText(R.string.dialog_menu_clear)
                }
                false -> {
                    when (noteItem.type) {
                        NoteType.TEXT -> {
                            onDisplayText(when (noteItem.isStatus) {
                                true -> R.string.dialog_menu_status_unbind
                                false -> R.string.dialog_menu_status_bind
                            })
                            onDisplayText(R.string.dialog_menu_convert_to_roll)
                            onDisplayText(R.string.dialog_menu_copy)
                            onDisplayText(R.string.dialog_menu_delete)
                        }
                        NoteType.ROLL -> {
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

}