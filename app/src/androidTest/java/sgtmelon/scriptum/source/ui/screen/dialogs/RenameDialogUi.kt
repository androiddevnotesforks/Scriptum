package sgtmelon.scriptum.source.ui.screen.dialogs

import android.view.inputmethod.EditorInfo
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.dialogs.RenameDialog
import sgtmelon.scriptum.source.ui.feature.DialogUi
import sgtmelon.scriptum.source.ui.feature.KeyboardClose
import sgtmelon.scriptum.source.ui.feature.KeyboardIme
import sgtmelon.scriptum.source.ui.parts.UiPart
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.excludeParent
import sgtmelon.test.cappuccino.utils.imeOption
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.isEnabled
import sgtmelon.test.cappuccino.utils.typeText
import sgtmelon.test.cappuccino.utils.withBackgroundDrawable
import sgtmelon.test.cappuccino.utils.withCursor
import sgtmelon.test.cappuccino.utils.withHint
import sgtmelon.test.cappuccino.utils.withImeAction
import sgtmelon.test.cappuccino.utils.withSelection
import sgtmelon.test.cappuccino.utils.withText
import sgtmelon.test.cappuccino.utils.withTextColor

/**
 * Class for UI control of [RenameDialog].
 */
class RenameDialogUi(title: String) : UiPart(),
    DialogUi,
    KeyboardIme,
    KeyboardClose {

    private var applyEnabled = false

    private val parentContainer = getView(R.id.rename_parent_container)
    private val titleText = getViewByText(title).excludeParent(parentContainer)
    private val renameEnter = getView(R.id.rename_enter)
    private val cancelButton = getViewByText(sgtmelon.safedialog.R.string.dialog_button_cancel)
    private val applyButton = getViewByText(sgtmelon.safedialog.R.string.dialog_button_apply)

    fun enter(name: String, isEnabled: Boolean = true) = apply {
        applyEnabled = isEnabled
        renameEnter.typeText(name)
        assert(name)
    }

    fun cancel() = waitClose {
        /** Close keyboard to be sure - show anim will not interrupt button action. */
        closeKeyboard()
        cancelButton.click()
    }

    fun apply() = waitClose {
        if (!applyEnabled) throw IllegalAccessException("Apply button not enabled")

        applyButton.click()
    }

    override fun imeClick(isSuccess: Boolean) {
        renameEnter.imeOption()

        if (isSuccess) waitClose()
    }

    fun assert(enter: String = "") {
        parentContainer.isDisplayed()

        titleText.isDisplayed().withTextColor(R.attr.clContent)
        renameEnter.isDisplayed()
            .withImeAction(EditorInfo.IME_ACTION_DONE)
            .withBackgroundDrawable(R.drawable.bg_rename_enter)
            .apply {
                if (enter.isNotEmpty()) {
                    withText(enter, R.attr.clContent, R.dimen.text_18sp)
                } else {
                    withHint(R.string.hint_enter_rank_rename, R.attr.clContentThird, R.dimen.text_18sp)
                }
            }

        cancelButton.isDisplayed().isEnabled().withTextColor(R.attr.clContentSecond)
        applyButton.isDisplayed().isEnabled(applyEnabled) {
            withTextColor(R.attr.clAccent)
        }
    }

    fun assertSelection(enter: String) {
        renameEnter.withSelection(enter)
    }

    fun assertSelectionEnd(enter: String) {
        renameEnter.withCursor(enter.length)
    }

    companion object {
        inline operator fun invoke(func: RenameDialogUi.() -> Unit, title: String): RenameDialogUi {
            return RenameDialogUi(title).apply {
                waitOpen {
                    assertSelection(title)
                    assert(title)
                }
            }.apply(func)
        }
    }
}