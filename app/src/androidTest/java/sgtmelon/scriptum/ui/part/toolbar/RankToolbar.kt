package sgtmelon.scriptum.ui.part.toolbar

import android.view.inputmethod.EditorInfo
import androidx.test.espresso.Espresso.closeSoftKeyboard
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.ui.IKeyboardOption
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.screen.main.RankScreen

/**
 * Part of UI abstraction for [RankScreen]
 */
class RankToolbar : ParentUi(), IKeyboardOption {

    //region Views

    private val parentContainer = getViewById(R.id.toolbar_rank_container)
    private val parentCard = getViewById(R.id.toolbar_rank_card)

    private val nameEnter = getViewById(R.id.toolbar_rank_enter)
    private val clearButton = getViewById(R.id.toolbar_rank_clear_button)
    private val addButton = getViewById(R.id.toolbar_rank_add_button)

    //endregion

    private var enter = ""

    fun onEnterName(name: String, isEnabled: Boolean = true) = apply {
        enter = name

        nameEnter.typeText(name)
        assert(isAddEnabled = isEnabled)
    }

    fun onClickClear() {
        enter = ""

        clearButton.click()
        assert()
    }

    fun onClickAdd() {
        enter = ""

        closeSoftKeyboard()
        addButton.click()
        assert()
    }

    fun onLongClickAdd() {
        enter = ""

        closeSoftKeyboard()
        addButton.longClick()
        assert()
    }

    override fun onImeOptionClick(isSuccess: Boolean) {
        nameEnter.imeOption()

        if (isSuccess) {
            enter = ""

            closeSoftKeyboard()
            assert()
        }
    }


    fun assert(isAddEnabled: Boolean = false) {
        parentContainer.isDisplayed()
            .withBackgroundAttr(R.attr.colorPrimary)
            .withNavigationDrawable(resourceId = null)

        parentCard.isDisplayed().withCardBackground(R.attr.clBackgroundEnter)

        val enterEmpty = enter.isEmpty()

        nameEnter.isDisplayed()
                .withImeAction(EditorInfo.IME_ACTION_DONE)
                .withBackgroundColor(android.R.color.transparent)
                .apply {
                    if (!enterEmpty) {
                        withText(enter, R.attr.clContent, R.dimen.text_18sp)
                    } else {
                        withHint(R.string.hint_enter_rank_new, R.attr.clDisable, R.dimen.text_18sp)
                    }
                }

        val clearTint = if (!enterEmpty) R.attr.clContent else R.attr.clDisable
        clearButton.isDisplayed().isEnabled(!enterEmpty)
                .withDrawableAttr(R.drawable.ic_cancel_enter, clearTint)
                .withContentDescription(R.string.description_enter_rank_clear)

        val addTint = if (isAddEnabled) R.attr.clAccent else R.attr.clDisable
        addButton.isDisplayed().isEnabled(isAddEnabled)
                .withDrawableAttr(R.drawable.ic_rank, addTint)
                .withContentDescription(R.string.description_enter_rank_add)
    }

    companion object {
        operator fun invoke(func: RankToolbar.() -> Unit) = RankToolbar().apply(func)
    }
}