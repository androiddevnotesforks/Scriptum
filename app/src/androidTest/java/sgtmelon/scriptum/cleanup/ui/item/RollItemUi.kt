package sgtmelon.scriptum.cleanup.ui.item

import android.view.View
import android.view.inputmethod.EditorInfo
import org.hamcrest.Matcher
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.presentation.adapter.RollAdapter
import sgtmelon.scriptum.parent.ui.model.key.NoteState
import sgtmelon.scriptum.parent.ui.parts.recycler.RecyclerItemPart
import sgtmelon.test.cappuccino.utils.isChecked
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.withBackgroundColor
import sgtmelon.test.cappuccino.utils.withCard
import sgtmelon.test.cappuccino.utils.withContentDescription
import sgtmelon.test.cappuccino.utils.withDrawableAttr
import sgtmelon.test.cappuccino.utils.withHint
import sgtmelon.test.cappuccino.utils.withImeAction
import sgtmelon.test.cappuccino.utils.withText

/**
 * Class for UI control of [RollAdapter].
 */
class RollItemUi(
    listMatcher: Matcher<View>,
    p: Int,
    private val state: NoteState
) : RecyclerItemPart<RollItem>(listMatcher, p) {

    private val parentCard by lazy {
        getChild(
            getView(
                when (state) {
                    NoteState.READ, NoteState.BIN -> R.id.roll_read_parent_card
                    NoteState.EDIT, NoteState.NEW -> R.id.roll_write_parent_card
                }
            )
        )
    }

    private val checkBox by lazy {
        getChild(
            getView(
                when (state) {
                    NoteState.READ, NoteState.BIN -> R.id.roll_read_check
                    NoteState.EDIT, NoteState.NEW -> R.id.roll_write_check
                }
            )
        )
    }

    val clickButton by lazy {
        getChild(
            getView(
                when (state) {
                    NoteState.READ, NoteState.BIN -> R.id.roll_read_click_button
                    NoteState.EDIT, NoteState.NEW -> R.id.roll_write_drag_button
                }
            )
        )
    }

    val rollText by lazy {
        getChild(
            getView(
                when (state) {
                    NoteState.READ, NoteState.BIN -> R.id.roll_read_text
                    NoteState.EDIT, NoteState.NEW -> R.id.roll_write_enter
                }
            )
        )
    }

    override fun assert(item: RollItem) {
        parentCard.isDisplayed().withCard(
            R.attr.clBackgroundView,
            R.dimen.item_card_radius,
            R.dimen.item_card_elevation
        )

        val textColor = if (!item.isCheck) R.attr.clContent else R.attr.clContrast

        when (state) {
            NoteState.READ, NoteState.BIN -> {
                checkBox.isDisplayed().isChecked(item.isCheck)

                val description = context.getString(
                    if (item.isCheck) {
                        R.string.description_item_roll_uncheck
                    } else {
                        R.string.description_item_roll_check
                    }
                ).plus(other = " ").plus(item.text)

                clickButton.isDisplayed(value = state != NoteState.BIN)
                    .withContentDescription(description)

                rollText.isDisplayed().withText(item.text, textColor, R.dimen.text_18sp)
                    .withBackgroundColor(android.R.color.transparent)
            }
            NoteState.EDIT, NoteState.NEW -> {
                checkBox.isDisplayed(value = false)

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