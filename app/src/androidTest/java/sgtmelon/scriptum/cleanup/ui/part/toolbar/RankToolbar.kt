package sgtmelon.scriptum.cleanup.ui.part.toolbar

import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.test.espresso.Espresso.closeSoftKeyboard
import org.hamcrest.Matcher
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.ui.IKeyboardOption
import sgtmelon.scriptum.parent.ui.parts.toolbar.ToolbarPart
import sgtmelon.scriptum.parent.ui.screen.main.RankScreen
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
 * UI abstraction of Toolbar with ability to add rank in [RankScreen].
 */
class RankToolbar(parentContainer: Matcher<View>) : ToolbarPart(parentContainer),
    IKeyboardOption {

    // TODO rename functions
    // TODO move KeyboardOption into feature package

    //region Views

    override val toolbar = getView(R.id.toolbar)

    private val enterCard = getView(R.id.enter_card)
    private val nameEnter = getView(R.id.rank_enter)
    private val clearButton = getView(R.id.clear_button)
    private val addButton = getView(R.id.add_button)

    //endregion

    private var currentEnter = ""

    fun onEnterName(name: String, isEnabled: Boolean = true) = apply {
        currentEnter = name

        nameEnter.typeText(name)
        assert(isAddEnabled = isEnabled)
    }

    fun onClickClear() {
        currentEnter = ""

        clearButton.click()
        assert()
    }

    fun onClickAdd() {
        currentEnter = ""

        closeSoftKeyboard()
        addButton.click()
        assert()
    }

    fun onLongClickAdd() {
        currentEnter = ""

        closeSoftKeyboard()
        addButton.longClick()
        assert()
    }

    override fun onImeOptionClick(isSuccess: Boolean) {
        nameEnter.imeOption()

        if (isSuccess) {
            currentEnter = ""

            closeSoftKeyboard()
            assert()
        }
    }


    fun assert(isAddEnabled: Boolean = false) {
        toolbar.isDisplayed()
            .withBackgroundAttr(R.attr.colorPrimary)
            .withNavigationDrawable(resourceId = null)

        enterCard.isDisplayed().withCard(
            R.attr.clBackgroundEnter,
            R.dimen.radius_8dp,
            R.dimen.elevation_2dp
        )

        val isEnterEmpty = currentEnter.isEmpty()

        nameEnter.isDisplayed()
            .withImeAction(EditorInfo.IME_ACTION_DONE)
            .withBackgroundColor(android.R.color.transparent)
            .apply {
                if (!isEnterEmpty) {
                    withText(currentEnter, R.attr.clContent, R.dimen.text_18sp)
                } else {
                    withHint(R.string.hint_enter_rank_new, R.attr.clDisable, R.dimen.text_18sp)
                }
            }

        val clearTint = if (!isEnterEmpty) R.attr.clContent else R.attr.clDisable
        clearButton.isDisplayed().isEnabled(!isEnterEmpty)
            .withDrawableAttr(sgtmelon.iconanim.R.drawable.ic_cancel_enter, clearTint)
            .withContentDescription(R.string.description_enter_rank_clear)

        val addTint = if (isAddEnabled) R.attr.clAccent else R.attr.clDisable
        addButton.isDisplayed().isEnabled(isAddEnabled)
            .withDrawableAttr(R.drawable.ic_rank, addTint)
            .withContentDescription(R.string.description_enter_rank_add)
    }

    companion object {
        inline operator fun invoke(
            func: RankToolbar.() -> Unit,
            parentContainer: Matcher<View>
        ): RankToolbar {
            return RankToolbar(parentContainer).apply(func)
        }
    }
}