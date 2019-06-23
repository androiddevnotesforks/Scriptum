package sgtmelon.scriptum.ui.screen.note

import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.annotation.Theme
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
        callback.noteModel.noteEntity.name = name
        callback.fullAssert()
    }

    // TODO #TEST возврат данных, контроль выхода с экрана
    fun onClickBack() {
        action { onClickToolbarButton() }

//        with(callback) {
//            state = when (state) {
//                State.READ -> TODO()
//                State.BIN -> TODO()
//                State.EDIT -> if (noteModel.isSaveEnabled()) State.READ else State.READ
//                State.NEW -> TODO()
//            }
//        }
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

            val name = noteModel.noteEntity.name

            when (state) {
                State.READ, State.BIN -> {
                    notDisplay(R.id.toolbar_note_enter)

                    if (name.isNotEmpty()) {
                        onDisplay(R.id.toolbar_note_text, name)
                    } else {
                        onDisplayHint(R.id.toolbar_note_text, R.string.hint_view_name)
                    }
                }
                State.EDIT, State.NEW -> {
                    if (name.isNotEmpty()) {
                        onDisplay(R.id.toolbar_note_enter, name)
                    } else {
                        onDisplayHint(R.id.toolbar_note_enter, R.string.hint_enter_name)
                    }

                    notDisplay(R.id.toolbar_note_text)
                }
            }
        }

    }

}