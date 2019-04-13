package sgtmelon.scriptum

import sgtmelon.scriptum.control.input.InputControlTest

/**
 * Описание сценариев для UNIT тестов
 * @author SerjantArbuz
 */
@Suppress("unused")
private class ScenarioUnit {

    /**
     * Сценарии для [InputControlTest]
     * # Добавление в список и UNDO при запрещённом добавлении
     * # Добавление в список и REDO при запрещённом добавлении
     * #
     * # Вызов UNDO при пустом списке
     * # Вызов REDO при пустом списке
     * #
     * # Вызов UNDO в крайней позиции
     * # Вызов REDO в крайней позиции
     * #
     * # Успешный вызов UNDO
     * # Успешный вызов REDO
     * #
     * # Отчистка элементов списка, которые находятся дальше позиции добавления
     * # Сброс данных
     *
     * Дерево:
     * on...Change/Add/Remove/Move
     * | add
     * | if (enable)
     * | | remove
     * | | | if (position != endPosition)
     *
     * undo
     * | if (isUndoAccess)  | +++
     * | else | +++
     *
     * redo
     * | if (isRedoAccess) | +++
     * | else | +++
     *
     * isUndoAccess
     * | size != 0 && position != -1 | +++
     *
     * isRedoAccess
     * | size != 0 && position != size - 1 | +++
     */

}