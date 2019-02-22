package sgtmelon.scriptum.office.intf

import sgtmelon.scriptum.app.control.InputControl
import sgtmelon.scriptum.app.screen.note.NoteFragmentParent

/**
 * Интерфейс для обновления панели управления заметкой в [NoteFragmentParent]
 * Биндинг кнопок undo/redo в зависимости от положения [InputControl.position]
 */
interface BindIntf {

    fun bindInput()

}
