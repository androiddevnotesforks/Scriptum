package sgtmelon.scriptum.parent.ui.feature

import android.view.View
import org.hamcrest.Matcher
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.cleanup.ui.screen.note.TextNoteScreen
import sgtmelon.scriptum.parent.ui.model.exception.EmptyListException
import sgtmelon.scriptum.parent.ui.model.key.NoteState
import sgtmelon.test.cappuccino.utils.getRandomPosition

/**
 * Abstraction for screens with ability to open [NoteItem] screen.
 */
interface OpenNote {

    val recyclerView: Matcher<View>

    fun getItem(p: Int): Callback

    fun openText(
        item: NoteItem.Text,
        p: Int? = recyclerView.getRandomPosition(),
        isRankEmpty: Boolean = true,
        func: TextNoteScreen.() -> Unit = {}
    ) {
        if (p == null) throw EmptyListException()

        getItem(p).openText(item, isRankEmpty, func)
        TextNoteScreen(func, NoteState.READ, item, isRankEmpty)
    }

    fun openRoll(
        item: NoteItem.Roll,
        p: Int? = recyclerView.getRandomPosition(),
        isRankEmpty: Boolean = true,
        func: RollNoteScreen.() -> Unit = {}
    ) {
        if (p == null) throw EmptyListException()

        getItem(p).openRoll(item, isRankEmpty, func)
        RollNoteScreen(func, NoteState.READ, item, isRankEmpty)
    }

    interface Callback {

        fun openClick(item: NoteItem)

        fun openText(item: NoteItem.Text, isRankEmpty: Boolean, func: TextNoteScreen.() -> Unit) {
            openClick(item)
            TextNoteScreen(func, NoteState.READ, item, isRankEmpty)
        }

        fun openRoll(item: NoteItem.Roll, isRankEmpty: Boolean, func: RollNoteScreen.() -> Unit) {
            openClick(item)
            RollNoteScreen(func, NoteState.READ, item, isRankEmpty)
        }
    }
}