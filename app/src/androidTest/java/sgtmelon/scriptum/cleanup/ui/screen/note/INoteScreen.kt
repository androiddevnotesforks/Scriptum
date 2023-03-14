package sgtmelon.scriptum.cleanup.ui.screen.note

import org.junit.Assert.assertTrue
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.ui.ParentScreen
import sgtmelon.scriptum.data.noteHistory.NoteHistoryImpl
import sgtmelon.scriptum.infrastructure.utils.extensions.note.deepCopy
import sgtmelon.scriptum.parent.ui.model.key.NoteState

/**
 * Interface for communication child ui abstractions with [TextNoteScreen] and [RollNoteScreen]
 */
@Suppress("UNCHECKED_CAST")
interface INoteScreen<T : ParentScreen, N : NoteItem> {

    // TODO #TEST add exit from screen control

    var state: NoteState

    var item: N

    /**
     * Item for changes in edit mode.
     */
    var shadowItem: N

    val isRankEmpty: Boolean

    val history: NoteHistoryImpl

    fun fullAssert(): T

    fun throwOnWrongState(vararg actual: NoteState, func: (callback: INoteScreen<T, N>) -> Unit) {
        assertTrue(actual.contains(state))
        func(this)
    }

    fun applyItem() {
        when (item) {
            is NoteItem.Text -> {
                val copyItem = item.castText().deepCopy()
                shadowItem = copyItem.castN()
            }
            is NoteItem.Roll -> {
                val copyItem = item.castRoll().deepCopy()
                shadowItem = copyItem.castN()
            }
        }
    }

    fun applyShadowText(): NoteItem.Text {
        val copyItem = shadowItem.castText().deepCopy()
        item = copyItem.castN()

        return item.castText()
    }

    fun applyShadowRoll(): NoteItem.Roll {
        /**
         * Need pass isVisible from [item] because it is always change there.
         */
        val isVisible = item.castRoll().isVisible

        val copyItem = shadowItem.castRoll().copy(isVisible = isVisible)
        item = copyItem.castN()

        return item.castRoll()
    }

    private fun NoteItem.castText(): NoteItem.Text = this as? NoteItem.Text ?: onThrowCast()

    private fun NoteItem.castRoll(): NoteItem.Roll = this as? NoteItem.Roll ?: onThrowCast()

    private fun NoteItem.castN(): N = this as? N ?: onThrowCast()

    private fun onThrowCast(): Nothing = throw ClassCastException()

}