package sgtmelon.scriptum.ui.item

import android.view.View
import android.view.inputmethod.EditorInfo
import org.hamcrest.Matcher
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.cleanup.data.State
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.presentation.adapter.RollAdapter
import sgtmelon.scriptum.ui.ParentRecyclerItem

/**
 * Class for UI control of [RollAdapter].
 */
class RollItemUi(
    listMatcher: Matcher<View>,
    p: Int,
    private val state: State
) : ParentRecyclerItem<RollItem>(listMatcher, p) {

    private val parentCard by lazy {
        getChild(getViewById(when (state) {
            State.READ, State.BIN -> R.id.roll_read_parent_card
            State.EDIT, State.NEW -> R.id.roll_write_parent_card
        }))
    }

    private val checkBox by lazy {
        getChild(getViewById(when (state) {
            State.READ, State.BIN -> R.id.roll_read_check
            State.EDIT, State.NEW -> R.id.roll_write_check
        }))
    }

    val clickButton by lazy {
        getChild(getViewById(when (state) {
            State.READ, State.BIN -> R.id.roll_read_click_button
            State.EDIT, State.NEW -> R.id.roll_write_drag_button
        }))
    }

    val rollText by lazy {
        getChild(getViewById(when (state) {
            State.READ, State.BIN -> R.id.roll_read_text
            State.EDIT, State.NEW -> R.id.roll_write_enter
        }))
    }

    override fun assert(item: RollItem) {
        parentCard.isDisplayed().withCardBackground(
            R.attr.clBackgroundView,
            R.dimen.item_card_radius,
            R.dimen.item_card_elevation
        )

        val textColor = if (!item.isCheck) R.attr.clContent else R.attr.clContrast

        when (state) {
            State.READ, State.BIN -> {
                checkBox.isDisplayed().isChecked(item.isCheck)

                val description = context.getString(if (item.isCheck) {
                    R.string.description_item_roll_uncheck
                } else {
                    R.string.description_item_roll_check
                }).plus(other = " ").plus(item.text)

                clickButton.isDisplayed(isVisible = state != State.BIN)
                    .withContentDescription(description)

                rollText.isDisplayed().withText(item.text, textColor, R.dimen.text_18sp)
                    .withBackgroundColor(android.R.color.transparent)
            }
            State.EDIT, State.NEW -> {
                checkBox.isDisplayed(isVisible = false)

                val color = if (item.isCheck) R.attr.clAccent else R.attr.clContent
                val description = context.getString(R.string.description_item_roll_move)
                    .plus(other = " ").plus(item.text)
                clickButton.isDisplayed()
                    .withDrawableAttr(R.drawable.ic_move, color)
                    .withContentDescription(description)

                rollText.isDisplayed()
                    .withImeAction(EditorInfo.IME_ACTION_NEXT)
                    .withBackgroundColor(android.R.color.transparent)
                    .apply {
                        if (item.text.isNotEmpty()) {
                            withText(item.text, textColor, R.dimen.text_18sp)
                        } else {
                            withHint(R.string.hind_enter_roll_empty, R.attr.clDisable, R.dimen.text_18sp)
                        }
                    }
            }
        }
    }

}