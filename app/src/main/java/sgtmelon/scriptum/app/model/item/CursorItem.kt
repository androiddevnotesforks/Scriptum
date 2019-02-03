package sgtmelon.scriptum.app.model.item

/**
 * Модель для сохранения в [InputItem] курсора текста
 */
class CursorItem(val valueFrom: Int, val valueTo: Int) {

    override fun toString(): String {
        return "valueFrom = $valueFrom | valueTo = $valueTo"
    }

}