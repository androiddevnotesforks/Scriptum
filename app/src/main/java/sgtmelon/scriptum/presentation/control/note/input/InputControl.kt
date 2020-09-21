package sgtmelon.scriptum.presentation.control.note.input

import android.util.Log
import sgtmelon.scriptum.domain.model.annotation.InputAction
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.item.InputItem
import sgtmelon.scriptum.extension.removeAtOrNull
import sgtmelon.scriptum.presentation.provider.BuildProvider

/**
 * Class for control input data inside note and work with undo/redo.
 * Model for store data: [InputItem]
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
class InputControl : IInputControl {

    private var logEnabled = BuildProvider.isDebug()

    @RunPrivate val list = mutableListOf<InputItem>()

    /**
     * Position in [list]
     */
    @RunPrivate var position = ND_POSITION

    /**
     * Variable for prevent changes.
     */
    @RunPrivate var isEnabled = true

    @RunPrivate val isUndoAccess get() = list.isNotEmpty() && position != ND_POSITION
    @RunPrivate val isRedoAccess get() = list.isNotEmpty() && position != list.lastIndex

    override val access get() = Access(isUndoAccess, isRedoAccess)

    override fun reset() {
        list.clear()
        position = ND_POSITION
    }

    override fun undo(): InputItem? = if (isUndoAccess) list.getOrNull(position--) else null

    override fun redo(): InputItem? = if (isRedoAccess) list.getOrNull(++position) else null

    @RunPrivate fun add(item: InputItem) {
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
        while (list.size >= BuildProvider.inputControlMaxSize()) {
            list.removeAtOrNull(0)
            position--
        }
    }


    override fun makeNotEnabled(func: () -> Unit) {
        isEnabled = false
        func()
        isEnabled = true
    }

    override fun onRankChange(idFrom: Long, psFrom: Int, idTo: Long, psTo: Int) {
        val valueFrom = arrayOf(idFrom, psFrom).joinToString()
        val valueTo = arrayOf(idTo, psTo).joinToString()

        add(InputItem(InputAction.RANK, valueFrom, valueTo))
    }

    override fun onColorChange(valueFrom: Int, valueTo: Int) {
        add(InputItem(InputAction.COLOR, valueFrom.toString(), valueTo.toString()))
    }

    override fun onNameChange(valueFrom: String, valueTo: String, cursor: InputItem.Cursor) {
        add(InputItem(InputAction.NAME, valueFrom, valueTo, cursor))
    }

    override fun onTextChange(valueFrom: String, valueTo: String, cursor: InputItem.Cursor) {
        add(InputItem(InputAction.TEXT, valueFrom, valueTo, cursor))
    }

    override fun onRollChange(p: Int, valueFrom: String, valueTo: String,
                              cursor: InputItem.Cursor) {
        add(InputItem(InputAction.ROLL, valueFrom, valueTo, cursor, p))
    }

    override fun onRollAdd(p: Int, valueTo: String) {
        add(InputItem(InputAction.ROLL_ADD, "", valueTo, null, p))
    }

    override fun onRollRemove(p: Int, valueFrom: String) {
        add(InputItem(InputAction.ROLL_REMOVE, valueFrom, "", null, p))
    }

    override fun onRollMove(valueFrom: Int, valueTo: Int) {
        add(InputItem(InputAction.ROLL_MOVE, valueFrom.toString(), valueTo.toString()))
    }

    @RunPrivate fun listAll() {
        if (!logEnabled) return

        Log.i(TAG, "listAll:")
        for (i in list.indices) {
            val ps = if (position == i) " | cursor = $position" else ""
            Log.i(TAG, "i = " + i + " | " + list.getOrNull(i).toString() + ps)
        }
    }

    /**
     * Class for control undo/redo call access
     */
    data class Access(val isUndo: Boolean, val isRedo: Boolean)

    companion object {
        private val TAG = InputControl::class.java.simpleName

        @RunPrivate const val ND_POSITION = -1
    }

}