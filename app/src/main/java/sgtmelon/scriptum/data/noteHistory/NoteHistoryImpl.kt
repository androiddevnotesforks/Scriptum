package sgtmelon.scriptum.data.noteHistory

import sgtmelon.scriptum.cleanup.extension.removeAtOrNull
import sgtmelon.scriptum.cleanup.presentation.provider.BuildProvider
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
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
        if (isEnabled) {
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


    /**
     * Variable for prevent changes.
     */
    override var isEnabled = true


    override fun onName(valueFrom: String, valueTo: String, cursor: HistoryItem.Cursor) {
        //        add(HistoryItem(HistoryAction.Name, valueFrom, valueTo, cursor))
    }

    override fun onRank(idFrom: Long, psFrom: Int, idTo: Long, psTo: Int) {
        val valueFrom = arrayOf(idFrom, psFrom).joinToString()
        val valueTo = arrayOf(idTo, psTo).joinToString()

        //        add(HistoryItem(HistoryAction.Rank, valueFrom, valueTo))
    }

    override fun onColor(valueFrom: Color, valueTo: Color) {
        //        add(
        //            HistoryItem(
        //                HistoryAction.Color,
        //                valueFrom.ordinal.toString(),
        //                valueTo.ordinal.toString()
        //            )
        //        )
    }

    override fun onTextEnter(valueFrom: String, valueTo: String, cursor: HistoryItem.Cursor) {
        //        add(HistoryItem(HistoryAction.TEXT_CHANGE, valueFrom, valueTo, cursor))
    }

    override fun onRollEnter(
        p: Int, valueFrom: String, valueTo: String,
        cursor: HistoryItem.Cursor
    ) {
        //        add(HistoryItem(HistoryAction.ROLL_CHANGE, valueFrom, valueTo, cursor, p))
    }

    override fun onRollAdd(p: Int, valueTo: String) {
        //        add(HistoryItem(HistoryAction.ROLL_ADD, "", valueTo, null, p))
    }

    override fun onRollRemove(p: Int, valueFrom: String) {
        //        add(HistoryItem(HistoryAction.ROLL_REMOVE, valueFrom, "", null, p))
    }

    override fun onRollMove(valueFrom: Int, valueTo: Int) {
        //        add(HistoryItem(HistoryAction.ROLL_MOVE, valueFrom.toString(), valueTo.toString()))
    }

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