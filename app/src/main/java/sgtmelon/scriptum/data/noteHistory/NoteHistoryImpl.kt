package sgtmelon.scriptum.data.noteHistory

import sgtmelon.scriptum.cleanup.extension.removeAtOrNull
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
            clearToPosition()
            clearToSize()

            list.add(action)
            position++
        }

        listAll()
    }

    /**
     * If position not at end, when remove unused information before adding new.
     */
    private fun clearToPosition() {
        val lastPosition = list.lastIndex

        if (position != lastPosition) {
            for (i in lastPosition downTo position + 1) {
                list.removeAtOrNull(i)
            }
        }
    }

    /**
     * If list size bigger than max size, then need clear first N items.
     */
    private fun clearToSize() {
        while (list.size >= BuildProvider.noteHistoryMaxSize()) {
            list.removeAtOrNull(0)
            position--
        }
    }

    /** Variable for prevent changes. */
    override var saveChanges = true

    private fun listAll() {
        if (!BuildProvider.isDebug()) return

        Timber.i(message = "listAll:")
        for (i in list.indices) {
            Timber.i(message = "ps=$position | i=$i | item=${list.getOrNull(i).toString()}")
        }
    }

    companion object {
        private const val ND_POSITION = -1
    }

}