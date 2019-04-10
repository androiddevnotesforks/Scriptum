package sgtmelon.scriptum.model.item

/**
 * Модель для сохранения в [InputItem] курсора текста
 */
class CursorItem(val valueFrom: Int, val valueTo: Int) {

    fun getValue(undo: Boolean) = if (undo) valueFrom else valueTo

    override fun toString() = "valueFrom = $valueFrom | valueTo = $valueTo"

}