package sgtmelon.scriptum.cleanup.ui.part.toolbar

import android.view.inputmethod.EditorInfo
import androidx.test.espresso.Espresso.closeSoftKeyboard
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.ui.IKeyboardOption
import sgtmelon.scriptum.cleanup.ui.ParentScreen
import sgtmelon.scriptum.cleanup.ui.screen.main.RankScreen
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.imeOption
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.isEnabled
import sgtmelon.test.cappuccino.utils.longClick
import sgtmelon.test.cappuccino.utils.typeText
import sgtmelon.test.cappuccino.utils.withBackgroundAttr
import sgtmelon.test.cappuccino.utils.withBackgroundColor
import sgtmelon.test.cappuccino.utils.withCard
import sgtmelon.test.cappuccino.utils.withContentDescription
import sgtmelon.test.cappuccino.utils.withDrawableAttr
import sgtmelon.test.cappuccino.utils.withHint
import sgtmelon.test.cappuccino.utils.withImeAction
import sgtmelon.test.cappuccino.utils.withNavigationDrawable
import sgtmelon.test.cappuccino.utils.withText

/**
 * Part of UI abstraction for [RankScreen]
 */
class RankToolbar : ParentScreen(), IKeyboardOption {

    //region Views

    private val parentContainer = getViewById(R.id.toolbar)
    private val parentCard = getViewById(R.id.enter_card)

    private val nameEnter = getViewById(R.id.rank_enter)
    private val clearButton = getViewById(R.id.clear_button)
    private val addButton = getViewById(R.id.add_button)

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

        parentCard.isDisplayed().withCard(
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
        inline operator fun invoke(func: RankToolbar.() -> Unit) = RankToolbar().apply(func)
    }
}