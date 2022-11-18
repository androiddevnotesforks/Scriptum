package sgtmelon.scriptum.cleanup.ui.part.toolbar

import android.view.View
import android.view.inputmethod.EditorInfo
import org.hamcrest.Matcher
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.InputItem
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.ui.ParentScreen
import sgtmelon.scriptum.cleanup.ui.screen.note.INoteScreen
import sgtmelon.scriptum.cleanup.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.cleanup.ui.screen.note.TextNoteScreen
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.parent.ui.basic.withBackgroundAppColor
import sgtmelon.scriptum.parent.ui.model.key.NoteState
import sgtmelon.scriptum.parent.ui.parts.toolbar.ToolbarPart
import sgtmelon.test.cappuccino.utils.imeOption
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.isFocused
import sgtmelon.test.cappuccino.utils.typeText
import sgtmelon.test.cappuccino.utils.withBackgroundColor
import sgtmelon.test.cappuccino.utils.withCursor
import sgtmelon.test.cappuccino.utils.withHint
import sgtmelon.test.cappuccino.utils.withImeAction
import sgtmelon.test.cappuccino.utils.withNavigationDrawable
import sgtmelon.test.cappuccino.utils.withText

/**
 * Part of UI abstraction for [TextNoteScreen] or [RollNoteScreen].
 */
@Suppress("UNCHECKED_CAST")
class NoteToolbar<T : ParentScreen, N : NoteItem>(
    parentContainer: Matcher<View>,
    private val callback: INoteScreen<T, N>,
    private val imeCallback: ImeCallback
) : ToolbarPart(parentContainer) {

    //region Views

    private val toolbarContainer = getView(R.id.toolbar_note_parent_container)
    override val toolbar = getView(R.id.toolbar_note_content_container)
    private val nameScroll = getView(R.id.toolbar_note_scroll)

    private val colorView = getView(R.id.toolbar_note_color_view)

    private val nameText = getView(R.id.toolbar_note_text)
    private val nameEnter = getView(R.id.toolbar_note_enter)

    //endregion

    fun onEnterName(name: String) = apply {
        callback.throwOnWrongState(NoteState.EDIT, NoteState.NEW) {
            nameEnter.typeText(name)

            callback.apply {
                for ((i, c) in name.withIndex()) {
                    val valueFrom = if (i == 0) shadowItem.text else name[i - 1].toString()
                    val valueTo = c.toString()

                    inputControl.onNameChange(
                        valueFrom, valueTo, InputItem.Cursor(valueFrom.length, valueTo.length)
                    )
                }

                shadowItem.name = name
            }.fullAssert()
        }
    }

    fun clickBack() {
        clickButton()

        with(callback) {
            if (state == NoteState.EDIT) {
                state = NoteState.READ

                applyItem()

                inputControl.reset()
                fullAssert()
            }
        }
    }

    fun onImeOptionName() {
        nameEnter.imeOption()
        imeCallback.assertToolbarIme()
    }


    fun assertFocus() = callback.throwOnWrongState(NoteState.EDIT, NoteState.NEW) {
        nameEnter.isFocused().withCursor(callback.shadowItem.name.length)
    }

    fun assert() = apply {
        val color = callback.shadowItem.color

        toolbarContainer.isDisplayed()

        toolbar.isDisplayed()
            .withBackgroundAppColor(theme, color, needDark = false)
            .withNavigationDrawable(
                when (callback.state) {
                    NoteState.READ, NoteState.BIN, NoteState.NEW -> sgtmelon.iconanim.R.drawable.ic_cancel_exit
                    NoteState.EDIT -> sgtmelon.iconanim.R.drawable.ic_cancel_enter
                }, R.attr.clContent
            )

        nameScroll.isDisplayed()

        colorView.isDisplayed(value = theme == ThemeDisplayed.DARK) {
            withBackgroundAppColor(theme, color, needDark = true)
        }

        callback.apply {
            when (state) {
                NoteState.READ, NoteState.BIN -> {
                    val name = item.name

                    nameEnter.isDisplayed(value = false)
                    nameText.isDisplayed {
                        if (name.isNotEmpty()) {
                            withText(name, R.attr.clContent)
                        } else {
                            withHint(R.string.hint_text_name, R.attr.clContentSecond)
                        }
                    }
                }
                NoteState.EDIT, NoteState.NEW -> {
                    val name = shadowItem.name

                    nameText.isDisplayed(value = false)
                    nameEnter.isDisplayed()
                        .withImeAction(EditorInfo.IME_ACTION_NEXT)
                        .withBackgroundColor(android.R.color.transparent)
                        .apply {
                            if (name.isNotEmpty()) {
                                withText(name, R.attr.clContent)
                            } else {
                                withHint(R.string.hint_enter_name, R.attr.clContentSecond)
                            }
                        }
                }
            }
        }
    }

    interface ImeCallback {
        fun assertToolbarIme()
    }


    companion object {
        operator fun <T : ParentScreen, N : NoteItem> invoke(
            func: NoteToolbar<T, N>.() -> Unit,
            parentContainer: Matcher<View>,
            callback: INoteScreen<T, N>,
            imeCallback: ImeCallback
        ): NoteToolbar<T, N> {
            return NoteToolbar(parentContainer, callback, imeCallback)
                .assert()
                .apply(func)
        }
    }
}