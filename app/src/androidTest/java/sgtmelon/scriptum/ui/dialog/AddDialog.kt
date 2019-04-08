package sgtmelon.scriptum.ui.dialog

import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.model.key.NoteType
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch

class AddDialog : ParentUi() {

    fun assert(func: Assert.() -> Unit) = Assert().apply { func() }

    fun onClickItem(type: NoteType) = action {
        onClickText(when (type) {
            NoteType.TEXT -> R.string.dialog_add_text
            NoteType.ROLL -> R.string.dialog_add_roll
        })
    }

    companion object {
        operator fun invoke(func: AddDialog.() -> Unit) = AddDialog().apply { func() }
    }

    class Assert : BasicMatch() {

        fun onDisplayContent() {
            onDisplay(R.id.add_navigation)
            onDisplayText(R.string.dialog_add_text)
            onDisplayText(R.string.dialog_add_roll)
        }

    }

}