package sgtmelon.scriptum.control.input

import android.util.Log
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.model.annotation.InputAction
import sgtmelon.scriptum.model.item.InputItem
import java.util.*

/**
 * Класс предназначенный для контроля ввода данных в заметку, применения undo и redo
 * Модель для хранения данных: [InputItem]
 *
 * [InputAction] - Значения, которые будут содержаться в списке:
 * Name change  - Текст (до/после)
 * Rank change  - Отмеченные id (до/после)
 * Color change - Отмеченный цвет (до/после)
 * Text change  - Текст (до/после)
 * Roll change  - Текст (пункт/до/после)
 * Roll add     - Номер пункта : значение
 * Roll swipe   - Номер пункта : значение
 * Roll move    - Перемещение (до/после)
 */
class InputControl : InputCallback {

    var logEnabled = BuildConfig.DEBUG

    private val inputList = ArrayList<InputItem>()

    /**
     * Позиция в массиве
     */
    private var position = -1

    /**
     * Проверка доступна ли отмена
     *
     * @return - Есть куда возвращаться или нет
     */
    val isUndoAccess get() = inputList.size != 0 && position != -1

    /**
     * Проверка доступен ли возврат
     *
     * @return - Есть куда возвращаться или нет
     */
    val isRedoAccess get() = inputList.size != 0 && position != inputList.size - 1

    val access get() = Access(isUndoAccess, isRedoAccess)

    fun reset() {
        inputList.clear()
        position = -1
    }

    fun undo(): InputItem? {
        return if (isUndoAccess) inputList[position--]
        else null
    }

    fun redo(): InputItem? {
        return if (isRedoAccess) inputList[++position]
        else null
    }

    private fun add(inputItem: InputItem) {
        if (isEnabled) {
            remove()
            inputList.add(inputItem)
            position++
        }

        listAll()
    }

    /**
     * Если позиция не в конце, то удаление ненужной информации перед добавлением новой
     */
    private fun remove() {
        val endPosition = inputList.size - 1

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
     * Переменная для предотвращения записи каких-либо изменений
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

    companion object {
        private val TAG = InputControl::class.java.simpleName
    }

    /**
     * Класс для контроля возможности отмены/возврата действий
     */
    data class Access(val isUndo: Boolean, val isRedo: Boolean)

}