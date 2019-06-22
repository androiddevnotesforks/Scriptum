package sgtmelon.scriptum.ui.dialog

import androidx.test.espresso.Espresso.pressBack
import sgtmelon.safedialog.MultiplyDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch

/**
 * Класс для ui контроля диалога [MultiplyDialog] при долгом нажатии на заметку
 *
 * @author SerjantArbuz
 */
class NoteDialogUi(private val noteEntity: NoteEntity) : ParentUi() {

    fun assert(func: Assert.() -> Unit) = Assert(noteEntity).apply { func() }

    fun onClickBind() = waitClose { action { onClickText(R.string.dialog_menu_status_bind) } }

    fun onClickUnbind() = waitClose { action { onClickText(R.string.dialog_menu_status_unbind) } }

    fun onClickConvert(noteType: NoteType) = waitClose {
        action {
            onClickText(when (noteType) {
                NoteType.TEXT -> R.string.dialog_menu_convert_to_roll
                NoteType.ROLL -> R.string.dialog_menu_convert_to_text
            })
        }
    }

    fun onClickDelete() = waitClose { action { onClickText(R.string.dialog_menu_delete) } }

    fun onClickRestore() = waitClose { action { onClickText(R.string.dialog_menu_restore) } }

    fun onClickClear() = waitClose { action { onClickText(R.string.dialog_menu_clear) } }

    fun onCloseSoft() = pressBack()

    private fun waitClose(func: () -> Unit) {
        func()
        Thread.sleep(500)
    }

    companion object {
        operator fun invoke(func: NoteDialogUi.() -> Unit, noteEntity: NoteEntity) =
                NoteDialogUi(noteEntity).apply {
                    assert { onDisplayContent() }
                    func()
                }
    }


    class Assert(private val noteEntity: NoteEntity) : BasicMatch() {

        fun onDisplayContent() = with(noteEntity) {
            if (isBin) {
                onDisplayText(R.string.dialog_menu_restore)
                onDisplayText(R.string.dialog_menu_copy)
                onDisplayText(R.string.dialog_menu_clear)
            } else {
                onDisplayText(if (isStatus) R.string.dialog_menu_status_unbind else R.string.dialog_menu_status_bind)
                onDisplayText(when(type) {
                    NoteType.TEXT -> R.string.dialog_menu_convert_to_roll
                    NoteType.ROLL -> R.string.dialog_menu_convert_to_text
                })
                onDisplayText(R.string.dialog_menu_copy)
                onDisplayText(R.string.dialog_menu_delete)
            }
        }
    }

}