package sgtmelon.scriptum.ui.dialog

import sgtmelon.safedialog.MultipleDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.ui.ParentDialogUi
import sgtmelon.scriptum.ui.basic.BasicMatch

/**
 * Class for UI control of [MultipleDialog] when cause long click on note
 */
class NoteDialogUi(private val noteModel: NoteModel) : ParentDialogUi() {

    fun assert() = Assert(noteModel)


    fun onClickBind() = waitClose {
        action { onClickText(R.string.dialog_menu_status_bind) }
        noteModel.noteEntity.isStatus = true
    }

    fun onClickUnbind() = waitClose {
        action { onClickText(R.string.dialog_menu_status_unbind) }
        noteModel.noteEntity.isStatus = false
    }

    fun onClickConvert() = waitClose {
        when (noteModel.noteEntity.type) {
            NoteType.TEXT -> {
                action { onClickText(R.string.dialog_menu_convert_to_roll) }
                noteModel.noteEntity.type = NoteType.ROLL
            }
            NoteType.ROLL -> {
                action { onClickText(R.string.dialog_menu_convert_to_text) }
                noteModel.noteEntity.type = NoteType.TEXT
            }
        }
    }

    fun onClickDelete() = waitClose {
        action { onClickText(R.string.dialog_menu_delete) }
        noteModel.noteEntity.apply {
            isBin = true
            isStatus = false
        }
    }

    fun onClickRestore() = waitClose {
        action { onClickText(R.string.dialog_menu_restore) }
        noteModel.noteEntity.isBin = false
    }

    fun onClickClear() = waitClose { action { onClickText(R.string.dialog_menu_clear) } }


    class Assert(noteModel: NoteModel) : BasicMatch() {
        init {
            noteModel.noteEntity.apply {
                if (isBin) {
                    onDisplayText(R.string.dialog_menu_restore)
                    onDisplayText(R.string.dialog_menu_copy)
                    onDisplayText(R.string.dialog_menu_clear)
                } else {
                    onDisplayText(if (isStatus) R.string.dialog_menu_status_unbind else R.string.dialog_menu_status_bind)
                    onDisplayText(when (type) {
                        NoteType.TEXT -> R.string.dialog_menu_convert_to_roll
                        NoteType.ROLL -> R.string.dialog_menu_convert_to_text
                    })
                    onDisplayText(R.string.dialog_menu_copy)
                    onDisplayText(R.string.dialog_menu_delete)
                }
            }
        }
    }

    companion object {
        operator fun invoke(func: NoteDialogUi.() -> Unit, noteModel: NoteModel) =
                NoteDialogUi(noteModel).apply(func).apply { waitOpen { assert() } }
    }

}