package sgtmelon.scriptum.app.control.touch

import sgtmelon.scriptum.app.control.input.InputControl

/**
 * Интерфейс для обновления панели управления заметкой в [NoteFragmentParent]
 * Биндинг кнопок undo/redo в зависимости от положения [InputControl.position]
 */
interface BindIntf {

    fun bindInput()

}
