package sgtmelon.scriptum.ui.screen.note

import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.InputItem
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch

/**
 * Часть UI абстракции для [TextNoteScreen] и [RollNoteScreen]
 *
 * @author SerjantArbuz
 */
class NoteToolbar(private val callback: INoteScreen) : ParentUi() {

    fun assert(func: Assert.() -> Unit) = Assert(callback).apply { func() }

    fun onEnterName(name: String) = callback.throwOnWrongState(State.EDIT, State.NEW) {
        action { onEnter(R.id.toolbar_note_enter, name) }

        callback.apply {
            name.forEachIndexed { i, c ->
                val valueFrom = if (i == 0) shadowModel.noteEntity.text else name[i - 1].toString()
                val valueTo = c.toString()

                inputControl.onNameChange(
                        valueFrom, valueTo, InputItem.Cursor(valueFrom.length, valueTo.length)
                )
            }

            shadowModel.noteEntity.name = name
        }.fullAssert()
    }

    fun onClickBack() {
        action { onClickToolbarButton() }

        with(callback) {
            if (state == State.EDIT) {
                state = State.READ
                shadowModel = NoteModel(noteModel)
                inputControl.reset()
                fullAssert()
            }
        }
    }

    companion object {
        operator fun invoke(func: NoteToolbar.() -> Unit, callback: INoteScreen) =
                NoteToolbar(callback).apply {
                    assert { onDisplayContent() }
                    func()
                }
    }

    class Assert(private val callback: INoteScreen) : BasicMatch() {

        // TODO #TEST (focus on title check)

        fun onDisplayContent(): Unit = with(callback) {
            onDisplay(R.id.toolbar_note_container)
            onDisplay(R.id.toolbar_note_scroll)

            if (theme == Theme.dark) {
                onDisplay(R.id.toolbar_note_color_view)
            } else {
                notDisplay(R.id.toolbar_note_color_view)
            }

            when (state) {
                State.READ, State.BIN -> {
                    notDisplay(R.id.toolbar_note_enter)

                    val name = noteModel.noteEntity.name
                    if (name.isNotEmpty()) {
                        onDisplay(R.id.toolbar_note_text, name)
                    } else {
                        onDisplayHint(R.id.toolbar_note_text, R.string.hint_view_name)
                    }
                }
                State.EDIT, State.NEW -> {
                    notDisplay(R.id.toolbar_note_text)

                    val name = shadowModel.noteEntity.name
                    if (name.isNotEmpty()) {
                        onDisplay(R.id.toolbar_note_enter, name)
                    } else {
                        onDisplayHint(R.id.toolbar_note_enter, R.string.hint_enter_name)
                    }
                }
            }
        }

    }

}