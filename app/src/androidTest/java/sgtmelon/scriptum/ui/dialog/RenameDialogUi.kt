package sgtmelon.scriptum.ui.dialog

import android.view.inputmethod.EditorInfo
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.imeOption
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.isEnabled
import sgtmelon.scriptum.basic.extension.typeText
import sgtmelon.scriptum.basic.extension.withBackgroundColor
import sgtmelon.scriptum.basic.extension.withHint
import sgtmelon.scriptum.basic.extension.withImeAction
import sgtmelon.scriptum.basic.extension.withText
import sgtmelon.scriptum.basic.extension.withTextColor
import sgtmelon.scriptum.cleanup.presentation.dialog.RenameDialog
import sgtmelon.scriptum.ui.IDialogUi
import sgtmelon.scriptum.ui.IKeyboardOption
import sgtmelon.scriptum.ui.ParentUi

/**
 * Class for UI control of [RenameDialog].
 */
class RenameDialogUi(title: String) : ParentUi(), IDialogUi, IKeyboardOption {

    private var applyEnabled = false

    //region Views

    private val parentContainer = getViewById(R.id.rename_parent_container)

    private val titleText = getViewByText(title).excludeParent(parentContainer)
    private val renameEnter = getViewById(R.id.rename_enter)

    private val cancelButton = getViewByText(sgtmelon.safedialog.R.string.dialog_button_cancel)
    private val applyButton = getViewByText(sgtmelon.safedialog.R.string.dialog_button_apply)

    //endregion

    fun onEnter(name: String, isEnabled: Boolean = true) = apply {
        applyEnabled = isEnabled

        renameEnter.typeText(name)
        assert(name)
    }

    fun onClickCancel() = waitClose { cancelButton.click() }

    fun onClickApply() = waitClose {
        if (!applyEnabled) throw IllegalAccessException("Apply button not enabled")

        applyButton.click()
    }

    override fun onImeOptionClick(isSuccess: Boolean) {
        renameEnter.imeOption()

        if  (isSuccess) waitClose()
    }

    fun assert(enter: String = "") {
        parentContainer.isDisplayed()

        titleText.isDisplayed()
        renameEnter.isDisplayed()
                .withImeAction(EditorInfo.IME_ACTION_DONE)
                .withBackgroundColor(android.R.color.transparent)
                .apply {
                    if (enter.isNotEmpty()) {
                        withText(enter, R.attr.clContent, R.dimen.text_18sp)
                    } else {
                        withHint(
                            R.string.hint_enter_rank_rename,
                            R.attr.clDisable,
                            R.dimen.text_18sp
                        )
                    }
                }

        cancelButton.isDisplayed().isEnabled().withTextColor(R.attr.clAccent)
        applyButton.isDisplayed().isEnabled(applyEnabled) {
            withTextColor(R.attr.clAccent)
        }
    }

    companion object {
        operator fun invoke(func: RenameDialogUi.() -> Unit, title: String): RenameDialogUi {
            return RenameDialogUi(title).apply { waitOpen { assert() } }.apply(func)
        }
    }
}