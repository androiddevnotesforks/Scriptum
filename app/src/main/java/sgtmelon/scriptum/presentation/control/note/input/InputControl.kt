package sgtmelon.scriptum.presentation.control.note.input

import android.util.Log
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.domain.model.annotation.InputAction
import sgtmelon.scriptum.domain.model.item.InputItem
import java.util.*

/**
 * Class for control input data inside note and work with undo/redo
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

    var logEnabled = BuildConfig.DEBUG

    private val inputList = ArrayList<InputItem>()

    /**
     * Position in [inputList]
     */
    private var position = -1

    val isUndoAccess get() = inputList.size != 0 && position != -1
    val isRedoAccess get() = inputList.size != 0 && position != inputList.size - 1

    val access get() = Access(isUndoAccess, isRedoAccess)

    fun reset() {
        inputList.clear()
        position = -1
    }

    fun undo(): InputItem? = if (isUndoAccess) inputList[position--] else null

    fun redo(): InputItem? = if (isRedoAccess) inputList[++position] else null

    private fun add(item: InputItem) {
        if (isEnabled) {
            remove()
            inputList.add(item)
            position++
        }

        listAll()
    }

    private fun remove() {
        val endPosition = inputList.size - 1

        /**
         * If position not at end, when remove unused information before add new
         */
        if (position != endPosition) {
            (endPosition downTo position + 1).forEach { inputList.removeAt(it) }
        }

        while (inputList.size >= BuildConfig.INPUT_CONTROL_MAX_SIZE) {
            inputList.removeAt(0)
            position--
        }

        listAll()
    }

    /**
     * Variable for prevent changes.
     */
    override var isEnabled = false

    override fun makeNotEnabled(func: () -> Unit) {
        isEnabled = false
        func()
        isEnabled = true
    }

    override fun onRankChange(idFrom: Long, psFrom: Int, idTo: Long, psTo: Int) =
            add(InputItem(InputAction.RANK,
                    arrayOf(idFrom, psFrom).joinToString(),
                    arrayOf(idTo, psTo).joinToString()
            ))

    override fun onColorChange(valueFrom: Int, valueTo: Int) =
            add(InputItem(InputAction.COLOR, valueFrom.toString(), valueTo.toString()))

    override fun onNameChange(valueFrom: String, valueTo: String, cursor: InputItem.Cursor) =
            add(InputItem(InputAction.NAME, valueFrom, valueTo, cursor))

    override fun onTextChange(valueFrom: String, valueTo: String, cursor: InputItem.Cursor) =
            add(InputItem(InputAction.TEXT, valueFrom, valueTo, cursor))

    override fun onRollChange(p: Int, valueFrom: String, valueTo: String, cursor: InputItem.Cursor) =
            add(InputItem(InputAction.ROLL, valueFrom, valueTo, cursor, p))

    override fun onRollAdd(p: Int, valueTo: String) =
            add(InputItem(InputAction.ROLL_ADD, "", valueTo, null, p))

    override fun onRollRemove(p: Int, valueFrom: String) =
            add(InputItem(InputAction.ROLL_REMOVE, valueFrom, "", null, p))

    override fun onRollMove(valueFrom: Int, valueTo: Int) =
            add(InputItem(InputAction.ROLL_MOVE, valueFrom.toString(), valueTo.toString()))

    private fun listAll() {
        if (!logEnabled) return

        Log.i(TAG, "listAll:")
        for (i in inputList.indices) {
            val ps = if (position == i) " | cursor = $position" else ""
            Log.i(TAG, "i = " + i + " | " + inputList[i].toString() + ps)
        }
    }

    /**
     * Class for control undo/redo call access
     */
    data class Access(val isUndo: Boolean, val isRedo: Boolean)

    companion object {
        private val TAG = InputControl::class.java.simpleName
    }

}