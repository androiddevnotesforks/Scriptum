package sgtmelon.scriptum.office.intf

import sgtmelon.scriptum.app.control.InputControl

/**
 * Интерфейс для обновления панели управления заметкой в [NoteFragmentParent]
 * Биндинг кнопок undo/redo в зависимости от положения [InputControl.position]
 */
interface BindIntf {

    fun bindInput()

}
