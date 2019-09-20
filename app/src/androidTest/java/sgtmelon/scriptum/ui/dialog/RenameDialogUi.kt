package sgtmelon.scriptum.ui.dialog

import sgtmelon.scriptum.R
import sgtmelon.scriptum.dialog.RenameDialog
import sgtmelon.scriptum.ui.ParentDialogUi
import sgtmelon.scriptum.ui.basic.click
import sgtmelon.scriptum.ui.basic.isDisplayed
import sgtmelon.scriptum.ui.basic.isEnabled
import sgtmelon.scriptum.ui.basic.typeText

/**
 * Class for UI control of [RenameDialog]
 */
class RenameDialogUi(private val title: String) : ParentDialogUi() {

    //region Views

    private fun getTitleText(text: String) = getViewByText(text).excludeParent(viewContainer)

    private val viewContainer = getViewById(R.id.rename_parent_container)
    private val renameEnter = getViewById(R.id.rename_enter)

    private val cancelButton = getViewByText(R.string.dialog_button_cancel)
    private val applyButton = getViewByText(R.string.dialog_button_apply)

    //endregion

    fun onRename(name: String, enabled: Boolean) = apply {
        renameEnter.typeText(name)
        assert(name, enabled)
    }

    fun onClickCancel() = waitClose { cancelButton.click() }

    fun onClickAccept() = waitClose { applyButton.click() }


    fun assert(enter: String = "", enabled: Boolean = false) {
        getTitleText(title).isDisplayed()

        viewContainer.isDisplayed()

        renameEnter.isDisplayed().apply {
            if (enter.isNotEmpty()) withText(enter) else withHint(R.string.hint_enter_rank_rename)
        }

        cancelButton.isDisplayed().isEnabled()
        applyButton.isDisplayed().isEnabled(enabled)
    }

    companion object {
        operator fun invoke(func: RenameDialogUi.() -> Unit, title: String) =
                RenameDialogUi(title).apply { waitOpen { assert() } }.apply(func)
    }

}