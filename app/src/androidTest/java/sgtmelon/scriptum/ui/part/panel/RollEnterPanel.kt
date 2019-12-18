package sgtmelon.scriptum.ui.part.panel

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.screen.note.INoteScreen
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import kotlin.random.Random

/**
 * Part of UI abstraction for [RollNoteScreen]
 */
class RollEnterPanel<T: ParentUi>(private val callback: INoteScreen<T>) : ParentUi() {

    //region Views

    private val enterContainer = getViewById(R.id.roll_note_enter_container)
    private val textEnter = getViewById(R.id.roll_note_enter)
    private val addButton = getViewById(R.id.roll_note_add_button)

    //endregion

    private var enterText: String = ""
        set(value) {
            field = value
            assert()
        }

    fun onAddRoll(text: String) {
        textEnter.typeText(text)

        enterText = text
        if (Random.nextBoolean()) addButton.click() else addButton.longClick()
        enterText = ""
    }


    fun assert() = apply {
        val visible = with(callback) { state == State.EDIT || state == State.NEW }

        enterContainer.isDisplayed(visible).withBackgroundAttr(R.attr.clPrimary)

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
        operator fun <T: ParentUi> invoke(func: RollEnterPanel<T>.() -> Unit,
                                          callback: INoteScreen<T>): RollEnterPanel<T> {
            return RollEnterPanel(callback).assert().apply(func)
        }
    }

}