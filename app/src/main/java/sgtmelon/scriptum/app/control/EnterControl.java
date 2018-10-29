package sgtmelon.scriptum.app.control;

import java.util.ArrayList;
import java.util.List;

import sgtmelon.scriptum.app.model.item.InputItem;

/**
 * Класс предназначенный для контроля ввода данных в заметку.
 * Применения undo и redo.
 */
public final class EnterControl {

    // TODO: 30.10.2018 внедрить

    private final List<InputItem> listInput = new ArrayList<>();
    private int position = 0;

    public void add(InputItem inputItem) {
        remove();

        listInput.add(inputItem);
        position++;
    }

    /**
     * Если позиция не в конце, то удаление ненужной информации перед добавлением новой
     */
    private void remove() {
        if (position != listInput.size() - 1) {
            if (position == 0) {
                listInput.clear();
            } else {
                List<InputItem> subListInput = listInput.subList(0, position);
                listInput.clear();
                listInput.addAll(subListInput);
            }
        }
    }

    public InputItem undo() {
        if (listInput.size() != 0) {
            if (position != 0) position--;
            return listInput.get(position);
        }
        return null;
    }

    public InputItem redo() {
        if (listInput.size() != 0) {
            if (position != listInput.size() - 1) position++;
            return listInput.get(position);
        }
        return null;
    }

}
