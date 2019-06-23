package sgtmelon.scriptum.ui.dialog

import androidx.test.espresso.Espresso.pressBack
import sgtmelon.safedialog.MultiplyDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch
import sgtmelon.scriptum.waitAfter
import sgtmelon.scriptum.waitBefore

/**
 * Класс для ui контроля диалога [MultiplyDialog] при долгом нажатии на заметку
 *
 * @author SerjantArbuz
 */
class NoteDialogUi(private val noteModel: NoteModel) : ParentUi() {

    fun assert(func: Assert.() -> Unit) = Assert(noteModel).apply { func() }

    fun onClickBind() = waitAfter(time = 300) { action { onClickText(R.string.dialog_menu_status_bind) } }

    fun onClickUnbind() = waitAfter(time = 300) { action { onClickText(R.string.dialog_menu_status_unbind) } }

    fun onClickConvert() = waitAfter(time = 300) {
        action {
            onClickText(when (noteModel.noteEntity.type) {
                NoteType.TEXT -> R.string.dialog_menu_convert_to_roll
                NoteType.ROLL -> R.string.dialog_menu_convert_to_text
            })
        }
    }

    fun onClickDelete() = waitAfter(time = 300) { action { onClickText(R.string.dialog_menu_delete) } }

    fun onClickRestore() = waitAfter(time = 300) { action { onClickText(R.string.dialog_menu_restore) } }

    fun onClickClear() = waitAfter(time = 300) { action { onClickText(R.string.dialog_menu_clear) } }

    fun onCloseSoft() = waitAfter(time = 300) { pressBack() }

    companion object {
        operator fun invoke(func: NoteDialogUi.() -> Unit, noteModel: NoteModel) =
                NoteDialogUi(noteModel).apply {
                    waitBefore(time = 100) {
                        assert { onDisplayContent() }
                        func()
                    }
                }
    }


    class Assert(private val noteModel: NoteModel) : BasicMatch() {

        fun onDisplayContent() = with(noteModel.noteEntity) {
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