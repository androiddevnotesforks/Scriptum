package sgtmelon.scriptum.office.intf;

import sgtmelon.scriptum.app.control.InputControl;
import sgtmelon.scriptum.app.view.parent.NoteFragmentParent;

/**
 * Интерфейс для обновления панели управления заметкой в {@link NoteFragmentParent}
 * Биндинг кнопок undo/redo в зависимости от положения {@link InputControl#position}
 */
public interface BindIntf {

    void bindInput();

}
