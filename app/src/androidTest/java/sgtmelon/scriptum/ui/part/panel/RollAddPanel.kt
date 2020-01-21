package sgtmelon.scriptum.ui.part.panel

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.screen.note.INoteScreen
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import kotlin.random.Random

/**
 * Part of UI abstraction for [RollNoteScreen]
 */
class RollAddPanel<T: ParentUi>(private val callback: INoteScreen<T>) : ParentUi() {

    //region Views

    private val enterContainer = getViewById(R.id.roll_add_panel_container)
    private val dividerView = getViewById(R.id.roll_add_panel_divider_view)

    private val textEnter = getViewById(R.id.roll_add_panel_enter)
    private val addButton = getViewById(R.id.roll_add_panel_button)

    //endregion

    private var enterText: String = ""
        set(value) {
            field = value
            assert()
        }

    fun onAdd(text: String) {
        textEnter.typeText(text)

        enterText = text

        if (Random.nextBoolean()) {
            addButton.click()

            callback.shadowItem.rollList.apply {
                add(size, RollItem(position = size, text = text))
            }
        } else {
            addButton.longClick()

            callback.shadowItem.rollList.add(0, RollItem(position = 0, text = text))
        }

        enterText = ""
    }


    fun assert() = apply {
        val visible = with(callback) { state == State.EDIT || state == State.NEW }

        enterContainer.isDisplayed(visible).withBackgroundAttr(R.attr.clPrimary)

        dividerView.isDisplayed(visible) {
            withSize(heightId = R.dimen.layout_1dp)
        }.withBackgroundAttr(R.attr.clDivider)

        val enterEmpty = enterText.isEmpty()

        textEnter.isDisplayed(visible) {
            if (!enterEmpty) {
                withText(enterText, R.attr.clContent, R.dimen.text_18sp)
            } else {
                withHint(R.string.hint_enter_roll, R.attr.clDisable, R.dimen.text_18sp)
            }
        }

        val addTint = if (enterEmpty) R.attr.clDisable else R.attr.clAccent
        addButton.isDisplayed(visible).isEnabled(!enterEmpty)
                .withDrawableAttr(R.drawable.ic_add, addTint)
                .withContentDescription(R.string.description_enter_roll_add)
    }

    companion object {
        operator fun <T: ParentUi> invoke(func: RollAddPanel<T>.() -> Unit,
                                          callback: INoteScreen<T>): RollAddPanel<T> {
            return RollAddPanel(callback).assert().apply(func)
        }
    }

}