package sgtmelon.scriptum.source.ui.parts.toolbar

import android.view.View
import android.view.inputmethod.EditorInfo
import org.hamcrest.Matcher
import sgtmelon.scriptum.R
import sgtmelon.scriptum.source.ui.action.longClick
import sgtmelon.scriptum.source.ui.feature.KeyboardIme
import sgtmelon.scriptum.source.ui.screen.main.RankScreen
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.imeOption
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.isEnabled
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
import sgtmelon.test.common.halfChance

/**
 * UI abstraction of Toolbar with ability to add rank in [RankScreen].
 */
class RankToolbar(parentContainer: Matcher<View>) : ToolbarPart(parentContainer),
    KeyboardIme {

    //region Views

    override val toolbar = getView(R.id.toolbar)

    private val enterCard = getView(R.id.enter_card)
    private val nameEnter = getView(R.id.rank_enter)
    private val clearButton = getView(R.id.clear_button)
    private val addButton = getView(R.id.add_button)

    //endregion

    private var currentEnter = ""

    /**
     * [isGood] means -> had current [name] ability to be added or not.
     */
    fun enter(name: String, isGood: Boolean = true) = apply {
        currentEnter = name
        nameEnter.typeText(name)
        assert(isGood)
    }

    fun clear() = apply {
        currentEnter = ""
        clearButton.click()
        assert()
    }

    fun add() = apply { if (halfChance()) addToEnd() else addToStart() }

    fun addToEnd() = apply { add { addButton.click() } }

    fun addToStart() = apply { add { addButton.longClick(commandAutomator) } }

    /**
     * Keyboard will be closed by screen code.
     */
    private inline fun add(onClick: () -> Unit) {
        currentEnter = ""
        onClick()
        assert()
    }

    override fun imeClick(isSuccess: Boolean) {
        nameEnter.imeOption()

        if (isSuccess) {
            currentEnter = ""
            assert()
        }
    }

    fun assert(isAddEnabled: Boolean = false) = apply {
        toolbar.isDisplayed()
            .withBackgroundAttr(R.attr.colorPrimary)
            .withNavigationDrawable(resourceId = null)

        enterCard.isDisplayed()
            .withCard(R.attr.clBackgroundEnter, R.dimen.item_card_radius, R.dimen.no_elevation)

        val isEnterEmpty = currentEnter.isEmpty()
        nameEnter.isDisplayed()
            .withImeAction(EditorInfo.IME_ACTION_DONE)
            .withBackgroundColor(android.R.color.transparent)
            .apply {
                if (!isEnterEmpty) {
                    withText(currentEnter, R.attr.clContent, R.dimen.text_18sp)
                } else {
                    withHint(R.string.hint_enter_rank_new, R.attr.clContentThird, R.dimen.text_18sp)
                }
            }

        val clearTint = if (!isEnterEmpty) R.attr.clContent else R.attr.clContentThird
        clearButton.isDisplayed().isEnabled(!isEnterEmpty)
            .withDrawableAttr(sgtmelon.iconanim.R.drawable.ic_cancel_enter, clearTint)
            .withContentDescription(R.string.description_enter_rank_clear)

        val addTint = if (isAddEnabled) R.attr.clAccent else R.attr.clContentThird
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