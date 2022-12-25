package sgtmelon.scriptum.data.noteHistory

import sgtmelon.scriptum.cleanup.domain.model.annotation.InputAction
import sgtmelon.scriptum.cleanup.domain.model.item.HistoryItem
import sgtmelon.scriptum.cleanup.extension.removeAtOrNull
import sgtmelon.scriptum.cleanup.presentation.provider.BuildProvider
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.test.prod.RunPrivate
import timber.log.Timber

/**
 * Class for control input data inside note and work with undo/redo.
 * Model for store data: [HistoryItem]
 *
 * [InputAction] - Value which describes action:
 * Name change  - Text (before/after)
 * Rank change  - Checked id's (before/after)
 * Color change - Checked color (before/after)
 * Text change  - Text (before/after)
 * Roll change  - Text (text/before/after)
 * Roll add     - Roll number: value
 * Roll swipe   - Roll number: value
 * Roll move    - Move position (before/after)
 */
class NoteHistoryImpl : NoteHistory {

    private var logEnabled = BuildProvider.isDebug()

    @RunPrivate val list = mutableListOf<HistoryItem>()

    /**
     * Position in [list]
     */
    @RunPrivate var position = ND_POSITION

    @RunPrivate val isUndoAccess get() = list.isNotEmpty() && position != ND_POSITION
    @RunPrivate val isRedoAccess get() = list.isNotEmpty() && position != list.lastIndex

    override val access get() = Access(isUndoAccess, isRedoAccess)

    override fun reset() {
        list.clear()
        position = ND_POSITION
    }

    override fun undo(): HistoryItem? = if (isUndoAccess) list.getOrNull(position--) else null

    override fun redo(): HistoryItem? = if (isRedoAccess) list.getOrNull(++position) else null

    @RunPrivate fun add(item: HistoryItem) {
        if (isEnabled) {
            clearToPosition()
            clearToSize()

            list.add(item)
            position++
        }

        listAll()
    }

    /**
     * If position not at end, when remove unused information before adding new.
     */
    @RunPrivate fun clearToPosition() {
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
    @RunPrivate fun clearToSize() {
        while (list.size >= BuildProvider.noteHistoryMaxSize()) {
            list.removeAtOrNull(0)
            position--
        }
    }


    /**
     * Variable for prevent changes.
     */
    override var isEnabled = true

    override fun onRankChange(idFrom: Long, psFrom: Int, idTo: Long, psTo: Int) {
        val valueFrom = arrayOf(idFrom, psFrom).joinToString()
        val valueTo = arrayOf(idTo, psTo).joinToString()

        add(HistoryItem(InputAction.RANK, valueFrom, valueTo))
    }

    override fun onColorChange(valueFrom: Color, valueTo: Color) {
        add(
            HistoryItem(
                InputAction.COLOR,
                valueFrom.ordinal.toString(),
                valueTo.ordinal.toString()
            )
        )
    }

    override fun onNameChange(valueFrom: String, valueTo: String, cursor: HistoryItem.Cursor) {
        add(HistoryItem(InputAction.NAME, valueFrom, valueTo, cursor))
    }

    override fun onTextChange(valueFrom: String, valueTo: String, cursor: HistoryItem.Cursor) {
        add(HistoryItem(InputAction.TEXT, valueFrom, valueTo, cursor))
    }

    override fun onRollChange(
        p: Int, valueFrom: String, valueTo: String,
        cursor: HistoryItem.Cursor
    ) {
        add(HistoryItem(InputAction.ROLL, valueFrom, valueTo, cursor, p))
    }

    override fun onRollAdd(p: Int, valueTo: String) {
        add(HistoryItem(InputAction.ROLL_ADD, "", valueTo, null, p))
    }

    override fun onRollRemove(p: Int, valueFrom: String) {
        add(HistoryItem(InputAction.ROLL_REMOVE, valueFrom, "", null, p))
    }

    override fun onRollMove(valueFrom: Int, valueTo: Int) {
        add(HistoryItem(InputAction.ROLL_MOVE, valueFrom.toString(), valueTo.toString()))
    }

    @RunPrivate fun listAll() {
        if (!logEnabled) return

        Timber.i(message = "listAll:")
        for (i in list.indices) {
            Timber.i(message = "ps=$position | i=$i | item=${list.getOrNull(i).toString()}")
        }
    }

    /**
     * Class for control undo/redo call access
     */
    data class Access(val isUndo: Boolean, val isRedo: Boolean)

    companion object {
        @RunPrivate const val ND_POSITION = -1
    }

}