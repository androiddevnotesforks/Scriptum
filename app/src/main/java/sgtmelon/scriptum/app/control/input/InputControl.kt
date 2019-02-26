package sgtmelon.scriptum.app.control.input

import android.text.TextUtils
import android.util.Log
import sgtmelon.scriptum.app.model.item.CursorItem
import sgtmelon.scriptum.app.model.item.InputItem
import sgtmelon.scriptum.office.annot.DbAnn
import sgtmelon.scriptum.office.annot.def.InputDef
import java.util.*

/**
 * Класс предназначенный для контроля ввода данных в заметку, применения undo и redo
 * Модель для хранения данных: [InputItem]
 *
 * [InputDef] - Значения, которые будут содержаться в списке:
 * Name change  - Текст (до/после)
 * Rank change  - Отмеченные id (до/после)
 * Color change - Отмеченный цвет (до/после)
 * Text change  - Текст (до/после)
 * Roll change  - Текст (пункт/до/после)
 * Roll add     - Номер пункта : значение
 * Roll swipe   - Номер пункта : значение
 * Roll move    - Перемещение (до/после)
 */
class InputControl : InputIntf {

    // TODO: 17.12.2018 хранить последние 200 изменений
    // TODO: 27.01.2019 долгое нажатие (надо каким-то образом собирать последнюю информацию о изменённом view)
    // TODO: 10.12.2018 добавить длинное нажатие на кнопку undo/redo - для возвращение в один из концов

    private val listInput = ArrayList<InputItem>()

    /**
     * Позиция в массиве
     */
    private var position = -1

    /**
     * Переменная для предотвращения записи каких-либо изменений
     */
    private var enabled: Boolean = false

    /**
     * Переменная для дополнительного контроля (разрешения/запрета) изменения переменной enabled
     */
    override var isChangeEnabled = true

    /**
     * Проверка доступна ли отмена
     *
     * @return - Есть куда возвращаться или нет
     */
    val isUndoAccess: Boolean
        get() = listInput.size != 0 && position != -1

    /**
     * Проверка доступен ли возврат
     *
     * @return - Есть куда возвращаться или нет
     */
    val isRedoAccess: Boolean
        get() = listInput.size != 0 && position != listInput.size - 1

    fun clear() {
        listInput.clear()
        position = -1
    }

    fun undo(): InputItem? {
        return if (isUndoAccess) listInput[position--]
        else null
    }

    fun redo(): InputItem? {
        return if (isRedoAccess) listInput[++position]
        else null
    }

    private fun add(inputItem: InputItem) {
        if (enabled) {
            remove()
            listInput.add(inputItem)
            position++
        }

        listAll()
    }

    /**
     * Если позиция не в конце, то удаление ненужной информации перед добавлением новой
     */
    private fun remove() {
        val endPosition = listInput.size - 1

        if (position != endPosition) {
            for (i in endPosition downTo position + 1) {
                listInput.removeAt(i)
            }
        }

        listAll()
    }

    override fun setEnabled(enabled: Boolean) {
        this.enabled = enabled
    }

    override fun onRankChange(valueFrom: List<Long>, valueTo: List<Long>) {
        val inputItem = InputItem.Builder()
                .setTag(InputDef.rank)
                .setValueFrom(TextUtils.join(DbAnn.Value.DIVIDER, valueFrom))
                .setValueTo(TextUtils.join(DbAnn.Value.DIVIDER, valueTo))
                .create()

        add(inputItem)
    }

    override fun onColorChange(valueFrom: Int, valueTo: Int) {
        val inputItem = InputItem.Builder()
                .setTag(InputDef.color)
                .setValueFrom(Integer.toString(valueFrom))
                .setValueTo(Integer.toString(valueTo))
                .create()

        add(inputItem)
    }

    override fun onNameChange(valueFrom: String, valueTo: String,
                              cursorItem: CursorItem) {
        val inputItem = InputItem.Builder()
                .setTag(InputDef.name)
                .setValueFrom(valueFrom)
                .setValueTo(valueTo)
                .setCursorItem(cursorItem)
                .create()

        add(inputItem)
    }

    override fun onTextChange(valueFrom: String, valueTo: String,
                              cursorItem: CursorItem) {
        val inputItem = InputItem.Builder()
                .setTag(InputDef.text)
                .setValueFrom(valueFrom)
                .setValueTo(valueTo)
                .setCursorItem(cursorItem)
                .create()

        add(inputItem)
    }

    override fun onRollChange(p: Int, valueFrom: String, valueTo: String,
                              cursorItem: CursorItem) {
        val inputItem = InputItem.Builder()
                .setTag(InputDef.roll)
                .setPosition(p)
                .setValueFrom(valueFrom)
                .setValueTo(valueTo)
                .setCursorItem(cursorItem)
                .create()

        add(inputItem)
    }

    override fun onRollAdd(p: Int, valueTo: String) {
        val inputItem = InputItem.Builder()
                .setTag(InputDef.rollAdd)
                .setPosition(p)
                .setValueFrom("")
                .setValueTo(valueTo)
                .create()

        add(inputItem)
    }

    override fun onRollRemove(p: Int, valueFrom: String) {
        val inputItem = InputItem.Builder()
                .setTag(InputDef.rollRemove)
                .setPosition(p)
                .setValueFrom(valueFrom)
                .setValueTo("")
                .create()

        add(inputItem)
    }

    override fun onRollMove(valueFrom: Int, valueTo: Int) {
        val inputItem = InputItem.Builder()
                .setTag(InputDef.rollMove)
                .setValueFrom(Integer.toString(valueFrom))
                .setValueTo(Integer.toString(valueTo))
                .create()

        add(inputItem)
    }

    private fun listAll() {
        Log.i(TAG, "listAll:")

        for (i in listInput.indices) {
            val inputItem = listInput[i]
            val ps = if (position == i) " | cursor = $position"
            else ""

            Log.i(TAG, "i = " + i + " | " + inputItem.toString() + ps)
        }
    }

    companion object {
        private val TAG = InputControl::class.java.simpleName
    }

}