package sgtmelon.scriptum.cleanup.ui.part.panel

import android.view.inputmethod.EditorInfo
import kotlin.random.Random
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.basic.extension.click
import sgtmelon.scriptum.cleanup.basic.extension.imeOption
import sgtmelon.scriptum.cleanup.basic.extension.isDisplayed
import sgtmelon.scriptum.cleanup.basic.extension.isEnabled
import sgtmelon.scriptum.cleanup.basic.extension.isFocused
import sgtmelon.scriptum.cleanup.basic.extension.longClick
import sgtmelon.scriptum.cleanup.basic.extension.typeText
import sgtmelon.scriptum.cleanup.basic.extension.withBackgroundAttr
import sgtmelon.scriptum.cleanup.basic.extension.withBackgroundColor
import sgtmelon.scriptum.cleanup.basic.extension.withContentDescription
import sgtmelon.scriptum.cleanup.basic.extension.withCursor
import sgtmelon.scriptum.cleanup.basic.extension.withDrawableAttr
import sgtmelon.scriptum.cleanup.basic.extension.withHint
import sgtmelon.scriptum.cleanup.basic.extension.withImeAction
import sgtmelon.scriptum.cleanup.basic.extension.withSize
import sgtmelon.scriptum.cleanup.basic.extension.withText
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.extension.clearSpace
import sgtmelon.scriptum.cleanup.testData.State
import sgtmelon.scriptum.cleanup.ui.ParentUi
import sgtmelon.scriptum.cleanup.ui.screen.note.INoteScreen
import sgtmelon.scriptum.cleanup.ui.screen.note.RollNoteScreen

/**
 * Part of UI abstraction for [RollNoteScreen]
 */
class RollEnterPanel<T: ParentUi>(private val callback: INoteScreen<T, NoteItem.Roll>) : ParentUi() {

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

    fun onEnterText(text: String) = apply {
        callback.throwOnWrongState(State.EDIT, State.NEW) {
            textEnter.typeText(text)
            enterText = text
        }
    }

    fun onAdd(text: String) = apply {
        callback.throwOnWrongState(State.EDIT, State.NEW) {
            onEnterText(text)

            val actualText = text.clearSpace()

            if (Random.nextBoolean()) {
                addButton.click()

                callback.shadowItem.list.apply {
                    add(size, RollItem(position = size, text = actualText))
                }
            } else {
                addButton.longClick()

                callback.shadowItem.list.apply {
                    add(0, RollItem(position = 0, text = actualText))

                    for ((i , item) in withIndex()) {
                        item.position = i
                    }
                }
            }

            enterText = ""
        }
    }

    fun onImeOptionEnter() {
        callback.throwOnWrongState(State.EDIT, State.NEW) {
            val actualText = enterText.clearSpace()

            textEnter.imeOption()

            if (actualText.isEmpty()) {
                callback.apply {
                    state = State.READ

                    item = shadowItem.deepCopy()
                    item.onSave()

                    inputControl.reset()
                }.fullAssert()
            } else {
                callback.shadowItem.list.apply {
                    add(size, RollItem(position = size, text = actualText))
                }

                enterText = ""
            }
        }
    }


    fun assertFocus() = callback.throwOnWrongState(State.EDIT, State.NEW) {
        textEnter.isFocused().withCursor(enterText.length)
    }

    fun assert() = apply {
        val visible = with(callback) { state == State.EDIT || state == State.NEW }

        enterContainer.isDisplayed(visible).withBackgroundAttr(R.attr.clPrimary)

        dividerView.isDisplayed(visible) {
            withSize(heightId = R.dimen.layout_1dp)
        }.withBackgroundAttr(R.attr.clDivider)

        textEnter.isDisplayed(visible)
                .withImeAction(EditorInfo.IME_ACTION_DONE)
                .withBackgroundColor(android.R.color.transparent)
                .apply {
                    if (enterText.isNotEmpty()) {
                        withText(enterText, R.attr.clContent, R.dimen.text_18sp)
                    } else {
                        withHint(R.string.hint_enter_roll, R.attr.clDisable, R.dimen.text_18sp)
                    }
                }

        val addEnable = enterText.clearSpace().isNotEmpty()
        val addTint = if (addEnable) R.attr.clAccent else R.attr.clDisable
        addButton.isDisplayed(visible).isEnabled(addEnable)
                .withDrawableAttr(R.drawable.ic_add, addTint)
                .withContentDescription(R.string.description_enter_roll_add)
    }

    companion object {
        operator fun <T : ParentUi> invoke(
            func: RollEnterPanel<T>.() -> Unit,
            callback: INoteScreen<T, NoteItem.Roll>
        ): RollEnterPanel<T> {
            return RollEnterPanel(callback).assert().apply(func)
        }
    }
}