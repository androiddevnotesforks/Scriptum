package sgtmelon.scriptum.ui.dialog

import sgtmelon.safedialog.MessageDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.ui.note.NoteActivity
import sgtmelon.scriptum.ui.ParentDialogUi
import sgtmelon.scriptum.ui.basic.BasicMatch
import sgtmelon.scriptum.waitAfter

/**
 * Class for UI control of [MessageDialog] which open from [NoteActivity] on convert
 */
class ConvertDialogUi(private val noteModel: NoteModel) : ParentDialogUi() {

    fun assert() = Assert(noteModel)


    fun onClickNo() = waitAfter(time = 300) { action { onClickText(R.string.dialog_button_no) } }

    fun onClickYes() = waitAfter(time = 300) { action { onClickText(R.string.dialog_button_yes) } }


    class Assert(noteModel: NoteModel) : BasicMatch() {
        init {
            onDisplayText(R.string.dialog_title_convert)

            onDisplayText(when (noteModel.noteEntity.type) {
                NoteType.TEXT -> R.string.dialog_text_convert_to_roll
                NoteType.ROLL -> R.string.dialog_roll_convert_to_text
            })

            onDisplayText(R.string.dialog_button_no)
            onDisplayText(R.string.dialog_button_yes)
        }
    }

    companion object {
        operator fun invoke(func: ConvertDialogUi.() -> Unit, noteModel: NoteModel) =
                ConvertDialogUi(noteModel).apply {
                    func()
                    waitOpen { assert() }
                }
    }

}