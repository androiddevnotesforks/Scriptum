package sgtmelon.scriptum.data.noteHistory

import sgtmelon.scriptum.cleanup.presentation.provider.BuildProvider
import timber.log.Timber

/**
 * Class for control data history inside note and work with undo/redo.
 */
class NoteHistoryImpl : NoteHistory {

    private val list = mutableListOf<HistoryAction>()

    /** Current position in [list]. */
    private var position = ND_POSITION

    private val isUndoAvailable get() = list.isNotEmpty() && position != ND_POSITION
    private val isRedoAvailable get() = list.isNotEmpty() && position != list.lastIndex

    override val available get() = HistoryMoveAvailable(isUndoAvailable, isRedoAvailable)

    override fun reset() {
        list.clear()
        position = ND_POSITION
    }

    override fun undo(): HistoryAction? = if (isUndoAvailable) list.getOrNull(position--) else null

    override fun redo(): HistoryAction? = if (isRedoAvailable) list.getOrNull(++position) else null

    override fun add(action: HistoryAction) {
        if (saveChanges) {
            removeListTail()
            checkListOverflow()

            list.add(action)
            position++
        }

        listAll()
    }

    /**
     * If position not last index, when remove unused information before adding a new one.
     */
    private fun removeListTail() {
        while (position != list.lastIndex) {
            list.removeLastOrNull()
        }
    }

    /**
     * If list size bigger than max size, when need clear first N items.
     */
    private fun checkListOverflow() {
        while (list.size >= BuildProvider.noteHistoryMaxSize()) {
            list.removeFirstOrNull()
            position--
        }
    }

    /** Variable for prevent changes in [add] call. */
    override var saveChanges = true

    private fun listAll() {
        if (!BuildProvider.isDebug()) return

        Timber.i(message = "Note history list (ps=$position):")
        for (i in list.indices) {
            Timber.i(message = "i=$i | item=${list.getOrNull(i).toString()}")
        }
    }

    companion object {
        private const val ND_POSITION = -1
    }
}