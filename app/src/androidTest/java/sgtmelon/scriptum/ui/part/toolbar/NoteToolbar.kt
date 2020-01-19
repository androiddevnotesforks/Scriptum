package sgtmelon.scriptum.ui.part.toolbar

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.InputItem
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.screen.note.INoteScreen
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Part of UI abstraction for [TextNoteScreen] и [RollNoteScreen]
 */
class NoteToolbar<T : ParentUi>(private val callback: INoteScreen<T>) : ParentUi() {

    //region Views

    private val parentContainer = getViewById(R.id.toolbar_note_parent_container)
    private val contentContainer = getViewById(R.id.toolbar_note_content_container)
    private val nameScroll = getViewById(R.id.toolbar_note_scroll)

    private val colorView = getViewById(R.id.toolbar_note_color_view)

    private val nameText = getViewById(R.id.toolbar_note_text)
    private val nameEnter = getViewById(R.id.toolbar_note_enter)

    //endregion

    fun onEnterName(name: String) = apply {
        callback.throwOnWrongState(State.EDIT, State.NEW) {
            nameEnter.typeText(name)

            callback.apply {
                name.forEachIndexed { i, c ->
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
                shadowItem = noteItem.deepCopy()
                inputControl.reset()
                fullAssert()
            }
        }
    }


    // TODO #TEST (focus on title check)
    // TODO #TEST divider for preLollipop
    fun assert() = apply {
        val color = callback.shadowItem.color

        parentContainer.isDisplayed()
        contentContainer.isDisplayed().withBackgroundAppColor(theme, color, needDark = false)
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
                    nameEnter.isDisplayed {
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

    companion object {
        operator fun <T: ParentUi> invoke(func: NoteToolbar<T>.() -> Unit,
                                          callback: INoteScreen<T>): NoteToolbar<T> {
            return NoteToolbar(callback).assert().apply(func)
        }
    }

}