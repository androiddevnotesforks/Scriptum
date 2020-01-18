package sgtmelon.scriptum.ui.dialog

import android.view.inputmethod.EditorInfo
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.dialog.RenameDialog
import sgtmelon.scriptum.ui.IDialogUi
import sgtmelon.scriptum.ui.IKeyboardOption
import sgtmelon.scriptum.ui.ParentUi

/**
 * Class for UI control of [RenameDialog].
 */
class RenameDialogUi(title: String) : ParentUi(), IDialogUi, IKeyboardOption {

    //region Views

    private val viewContainer = getViewById(R.id.rename_parent_container)

    private val titleText = getViewByText(title).excludeParent(viewContainer)
    private val renameEnter = getViewById(R.id.rename_enter)

    private val cancelButton = getViewByText(R.string.dialog_button_cancel)
    private val applyButton = getViewByText(R.string.dialog_button_apply)

    //endregion

    fun onEnter(name: String, enabled: Boolean = true) = apply {
        renameEnter.typeText(name)
        assert(name, enabled)
    }

    fun onClickCancel() = waitClose { cancelButton.click() }

    fun onClickApply() = waitClose { applyButton.click() }

    override fun onImeOptionClick(isSuccess: Boolean) {
        renameEnter.imeOption()

        if  (isSuccess) waitClose()
    }

    fun assert(enter: String = "", enabled: Boolean = false) {
        viewContainer.isDisplayed()

        titleText.isDisplayed()
        renameEnter.isDisplayed()
                .withImeAction(EditorInfo.IME_ACTION_DONE)
                .withBackgroundColor(android.R.color.transparent)
                .apply {
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