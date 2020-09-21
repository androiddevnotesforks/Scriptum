package sgtmelon.scriptum.ui.part.toolbar

import android.view.inputmethod.EditorInfo
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.InputItem
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.screen.note.INoteScreen
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Part of UI abstraction for [TextNoteScreen] or [RollNoteScreen].
 */
@Suppress("UNCHECKED_CAST")
class NoteToolbar<T : ParentUi, N : NoteItem>(
    private val callback: INoteScreen<T, N>,
    private val imeCallback: ImeCallback
) : ParentToolbar() {

    //region Views

    private val parentContainer = getViewById(R.id.toolbar_note_parent_container)
    val contentContainer = getViewById(R.id.toolbar_note_content_container)
    private val nameScroll = getViewById(R.id.toolbar_note_scroll)

    private val colorView = getViewById(R.id.toolbar_note_color_view)

    private val nameText = getViewById(R.id.toolbar_note_text)
    private val nameEnter = getViewById(R.id.toolbar_note_enter)

    //endregion

    fun onEnterName(name: String) = apply {
        callback.throwOnWrongState(State.EDIT, State.NEW) {
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

    fun onClickBack() {
        getToolbarButton().click()

        with(callback) {
            if (state == State.EDIT) {
                state = State.READ

                when (noteItem) {
                    is NoteItem.Text -> {
                        val copyItem = (noteItem as? NoteItem.Text)?.deepCopy() ?: onThrowCast()
                        shadowItem = copyItem as? N ?: onThrowCast()
                    }
                    is NoteItem.Roll -> {
                        val copyItem = (noteItem as? NoteItem.Roll)?.deepCopy() ?: onThrowCast()
                        shadowItem = copyItem as? N ?: onThrowCast()
                    }
                }

                inputControl.reset()
                fullAssert()
            }
        }
    }

    fun onImeOptionName() {
        nameEnter.imeOption()
        imeCallback.assertToolbarIme()
    }


    fun assertFocus() = callback.throwOnWrongState(State.EDIT, State.NEW) {
        nameEnter.isFocused().withCursor(callback.shadowItem.name.length)
    }

    fun assert() = apply {
        val color = callback.shadowItem.color

        parentContainer.isDisplayed()

        contentContainer.isDisplayed()
            .withBackgroundAppColor(theme, color, needDark = false)
            .withNavigationDrawable(when (callback.state) {
                State.READ, State.BIN, State.NEW -> R.drawable.ic_cancel_exit
                State.EDIT -> R.drawable.ic_cancel_enter
            }, R.attr.clContent)

        nameScroll.isDisplayed()

        colorView.isDisplayed(visible = theme == Theme.DARK) {
            withBackgroundAppColor(theme, color, needDark = true)
        }

        callback.apply {
            when (state) {
                State.READ, State.BIN -> {
                    val name = noteItem.name

                    nameEnter.isDisplayed(visible = false)
                    nameText.isDisplayed {
                        if (name.isNotEmpty()) {
                            withText(name, R.attr.clContent)
                        } else {
                            withHint(R.string.hint_text_name, R.attr.clContentSecond)
                        }
                    }
                }
                State.EDIT, State.NEW -> {
                    val name = shadowItem.name

                    nameText.isDisplayed(visible = false)
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
        operator fun <T : ParentUi, N : NoteItem> invoke(
            func: NoteToolbar<T, N>.() -> Unit,
            callback: INoteScreen<T, N>,
            imeCallback: ImeCallback
        ): NoteToolbar<T, N> {
            return NoteToolbar(callback, imeCallback).apply { assert() }.apply(func)
        }
    }

}