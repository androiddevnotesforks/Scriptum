package sgtmelon.scriptum.cleanup.ui.part.panel

import android.view.inputmethod.EditorInfo
import kotlin.random.Random
import sgtmelon.extensions.removeExtraSpace
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.ui.ParentScreen
import sgtmelon.scriptum.cleanup.ui.screen.note.INoteScreen
import sgtmelon.scriptum.cleanup.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.parent.ui.model.key.NoteState
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.imeOption
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.isEnabled
import sgtmelon.test.cappuccino.utils.isFocused
import sgtmelon.test.cappuccino.utils.longClick
import sgtmelon.test.cappuccino.utils.typeText
import sgtmelon.test.cappuccino.utils.withBackgroundAttr
import sgtmelon.test.cappuccino.utils.withBackgroundColor
import sgtmelon.test.cappuccino.utils.withContentDescription
import sgtmelon.test.cappuccino.utils.withCursor
import sgtmelon.test.cappuccino.utils.withDrawableAttr
import sgtmelon.test.cappuccino.utils.withHint
import sgtmelon.test.cappuccino.utils.withImeAction
import sgtmelon.test.cappuccino.utils.withSize
import sgtmelon.test.cappuccino.utils.withText

/**
 * Part of UI abstraction for [RollNoteScreen]
 */
class RollEnterPanel<T : ParentScreen>(private val callback: INoteScreen<T, NoteItem.Roll>) :
    ParentScreen() {

    //region Views

    private val enterContainer = getViewById(R.id.parent_container)
    private val dividerView = getViewById(R.id.enter_divider_view)

    private val textEnter = getViewById(R.id.roll_enter)
    private val addButton = getViewById(R.id.add_button)

    //endregion

    private var enterText: String = ""
        set(value) {
            field = value
            assert()
        }

    fun onEnterText(text: String) = apply {
        callback.throwOnWrongState(NoteState.EDIT, NoteState.NEW) {
            textEnter.typeText(text)
            enterText = text
        }
    }

    fun onAdd(text: String) = apply {
        callback.throwOnWrongState(NoteState.EDIT, NoteState.NEW) {
            onEnterText(text)

            val actualText = text.removeExtraSpace()

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
        callback.throwOnWrongState(NoteState.EDIT, NoteState.NEW) {
            val actualText = enterText.removeExtraSpace()

            textEnter.imeOption()

            if (actualText.isEmpty()) {
                callback.apply {
                    state = NoteState.READ

                    item = shadowItem.deepCopy()
                    item.onSave()

                    history.reset()
                }.fullAssert()
            } else {
                callback.shadowItem.list.apply {
                    add(size, RollItem(position = size, text = actualText))
                }

                enterText = ""
            }
        }
    }


    fun assertFocus() = callback.throwOnWrongState(NoteState.EDIT, NoteState.NEW) {
        textEnter.isFocused().withCursor(enterText.length)
    }

    fun assert() = apply {
        val visible = with(callback) { state == NoteState.EDIT || state == NoteState.NEW }

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

        val addEnable = enterText.removeExtraSpace().isNotEmpty()
        val addTint = if (addEnable) R.attr.clAccent else R.attr.clDisable
        addButton.isDisplayed(visible).isEnabled(addEnable)
                .withDrawableAttr(R.drawable.ic_add, addTint)
                .withContentDescription(R.string.description_enter_roll_add)
    }

    companion object {
        operator fun <T : ParentScreen> invoke(
            func: RollEnterPanel<T>.() -> Unit,
            callback: INoteScreen<T, NoteItem.Roll>
        ): RollEnterPanel<T> {
            return RollEnterPanel(callback).assert().apply(func)
        }
    }
}