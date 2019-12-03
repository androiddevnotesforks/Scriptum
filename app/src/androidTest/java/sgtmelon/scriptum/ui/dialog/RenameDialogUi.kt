package sgtmelon.scriptum.ui.dialog

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.dialog.RenameDialog
import sgtmelon.scriptum.ui.IDialogUi
import sgtmelon.scriptum.ui.ParentUi

/**
 * Class for UI control of [RenameDialog].
 */
class RenameDialogUi(private val title: String) : ParentUi(), IDialogUi {

    //region Views

    private fun getTitleText(text: String) = getViewByText(text).excludeParent(viewContainer)

    private val viewContainer = getViewById(R.id.rename_parent_container)
    private val renameEnter = getViewById(R.id.rename_enter)

    private val cancelButton = getViewByText(R.string.dialog_button_cancel)
    private val applyButton = getViewByText(R.string.dialog_button_apply)

    //endregion

    fun onEnter(name: String, enabled: Boolean = true) = apply {
        renameEnter.typeText(name)
        assert(name, enabled)
    }

    fun onClickCancel() = waitClose { cancelButton.click() }

    fun onClickAccept() = waitClose { applyButton.click() }


    fun assert(enter: String = "", enabled: Boolean = false) {
        getTitleText(title).isDisplayed()

        viewContainer.isDisplayed()

        renameEnter.isDisplayed {
            if (enter.isNotEmpty()) {
                withText(enter, dimenId = R.dimen.text_18sp)
            } else {
                withHint(R.string.hint_enter_rank_rename, dimenId = R.dimen.text_18sp)
            }
        }

        cancelButton.isDisplayed().isEnabled().withTextColor(R.attr.clAccent)
        applyButton.isDisplayed().isEnabled(enabled) {
            withTextColor(R.attr.clAccent)
        }
    }

    companion object {
        operator fun invoke(func: RenameDialogUi.() -> Unit, title: String): RenameDialogUi {
            return RenameDialogUi(title).apply { waitOpen { assert() } }.apply(func)
        }
    }

}