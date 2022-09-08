package sgtmelon.scriptum.cleanup.ui.dialog

import android.view.inputmethod.EditorInfo
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.presentation.dialog.RenameDialog
import sgtmelon.scriptum.cleanup.ui.IDialogUi
import sgtmelon.scriptum.cleanup.ui.IKeyboardOption
import sgtmelon.scriptum.cleanup.ui.ParentUi
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.imeOption
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.isEnabled
import sgtmelon.test.cappuccino.utils.typeText
import sgtmelon.test.cappuccino.utils.withBackgroundColor
import sgtmelon.test.cappuccino.utils.withHint
import sgtmelon.test.cappuccino.utils.withImeAction
import sgtmelon.test.cappuccino.utils.withText
import sgtmelon.test.cappuccino.utils.withTextColor

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

        titleText.isDisplayed().withTextColor(R.attr.clContent)
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

        cancelButton.isDisplayed().isEnabled().withTextColor(R.attr.clContentSecond)
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