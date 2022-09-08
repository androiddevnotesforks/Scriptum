package sgtmelon.scriptum.cleanup.ui.part.toolbar

import android.view.inputmethod.EditorInfo
import androidx.test.espresso.Espresso.closeSoftKeyboard
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.basic.extension.isDisplayed
import sgtmelon.scriptum.cleanup.basic.extension.isEnabled
import sgtmelon.scriptum.cleanup.basic.extension.withBackgroundAttr
import sgtmelon.scriptum.cleanup.basic.extension.withBackgroundColor
import sgtmelon.scriptum.cleanup.basic.extension.withCardBackground
import sgtmelon.scriptum.cleanup.basic.extension.withContentDescription
import sgtmelon.scriptum.cleanup.basic.extension.withDrawableAttr
import sgtmelon.scriptum.cleanup.basic.extension.withHint
import sgtmelon.scriptum.cleanup.basic.extension.withImeAction
import sgtmelon.scriptum.cleanup.basic.extension.withNavigationDrawable
import sgtmelon.scriptum.cleanup.basic.extension.withText
import sgtmelon.scriptum.cleanup.ui.IKeyboardOption
import sgtmelon.scriptum.cleanup.ui.ParentUi
import sgtmelon.scriptum.cleanup.ui.screen.main.RankScreen
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.imeOption
import sgtmelon.test.cappuccino.utils.longClick
import sgtmelon.test.cappuccino.utils.typeText

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

        parentCard.isDisplayed().withCardBackground(
            R.attr.clBackgroundEnter,
            R.dimen.radius_8dp,
            R.dimen.elevation_2dp
        )

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
            .withDrawableAttr(sgtmelon.iconanim.R.drawable.ic_cancel_enter, clearTint)
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