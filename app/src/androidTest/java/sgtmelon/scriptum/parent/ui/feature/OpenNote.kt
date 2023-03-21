package sgtmelon.scriptum.parent.ui.feature

import android.view.View
import org.hamcrest.Matcher
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.cleanup.ui.screen.note.TextNoteScreen
import sgtmelon.scriptum.parent.ui.model.exception.EmptyListException
import sgtmelon.scriptum.parent.ui.model.key.NoteState
import sgtmelon.scriptum.parent.ui.screen.note.NoteScreen
import sgtmelon.test.cappuccino.utils.getRandomPosition

/**
 * Abstraction for screens with ability to open [NoteItem] screen.
 */
interface OpenNote {

    val recyclerView: Matcher<View>

    fun getItem(p: Int): Callback

    val openNoteState: NoteState

    fun openText(
        item: NoteItem.Text,
        p: Int? = recyclerView.getRandomPosition(),
        isRankEmpty: Boolean = true,
        func: TextNoteScreen.() -> Unit = {}
    ) {
        if (p == null) throw EmptyListException()

        getItem(p).openText(item, openNoteState, isRankEmpty, func)
    }

    fun openRoll(
        item: NoteItem.Roll,
        p: Int? = recyclerView.getRandomPosition(),
        isRankEmpty: Boolean = true,
        func: RollNoteScreen.() -> Unit = {}
    ) {
        if (p == null) throw EmptyListException()

        getItem(p).openRoll(item, openNoteState, isRankEmpty, func)
    }

    interface Callback {

        fun openClick(item: NoteItem)

        fun openText(
            item: NoteItem.Text,
            state: NoteState,
            isRankEmpty: Boolean,
            func: TextNoteScreen.() -> Unit
        ) {
            openClick(item)
            NoteScreen().openText(func, state, item, isRankEmpty)
        }

        fun openRoll(
            item: NoteItem.Roll,
            state: NoteState,
            isRankEmpty: Boolean,
            func: RollNoteScreen.() -> Unit
        ) {
            openClick(item)
            NoteScreen().openRoll(func, state, item, isRankEmpty)
        }
    }
}