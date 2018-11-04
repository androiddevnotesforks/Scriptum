package sgtmelon.scriptum.app.control;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import sgtmelon.scriptum.app.model.item.InputItem;
import sgtmelon.scriptum.office.annot.DbAnn;
import sgtmelon.scriptum.office.annot.def.InputDef;
import sgtmelon.scriptum.office.intf.InputIntf;

/**
 * Класс предназначенный для контроля ввода данных в заметку, применения undo и redo
 * {@link InputDef}
 * <p>
 * Значения, которые будут содержаться в списке:
 * name change  - значение
 * rank change  - отмеченные id
 * color change - отмеченный цвет
 * text change  - значение
 * roll change  - номер пункта : значение
 * roll add     - номер пункта : значение
 * roll swipe   - номер пункта : значение
 * roll move    - начало : конец
 */
public final class InputControl implements InputIntf {

    // TODO: 30.10.2018 внедрить

    private final List<InputItem> listInput = new ArrayList<>();
    private int position = 0;

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

    private void add(InputItem inputItem) {
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
                final List<InputItem> subListInput = listInput.subList(0, position);
                listInput.clear();
                listInput.addAll(subListInput);
            }
        }
    }

    @Override
    public void onRankChange(Long[] rankId) {
        final InputItem inputItem = new InputItem(
                InputDef.rank, null, TextUtils.join(DbAnn.divider, rankId)
        );
        add(inputItem);
    }

    @Override
    public void onColorChange(int color) {
        final InputItem inputItem = new InputItem(
                InputDef.color, null, Integer.toString(color)
        );
        add(inputItem);
    }

    @Override
    public void onNameChange(String text) {
        final InputItem inputItem = new InputItem(
                InputDef.name, null, text
        );
        add(inputItem);
    }

    @Override
    public void onTextChange(String text) {
        final InputItem inputItem = new InputItem(
                InputDef.text, null, text
        );
        add(inputItem);
    }

    @Override
    public void onRollChange(int position, String text) {
        final InputItem inputItem = new InputItem(
                InputDef.roll, Integer.toString(position), text
        );
        add(inputItem);
    }

    @Override
    public void onRollAdd(int position, String text) {
        final InputItem inputItem = new InputItem(
                InputDef.rollAdd, Integer.toString(position), text
        );
        add(inputItem);
    }

    @Override
    public void onRollSwipe(int position, String text) {
        final InputItem inputItem = new InputItem(
                InputDef.rollSwipe, Integer.toString(position), text
        );
        add(inputItem);
    }

    @Override
    public void onRollMove(int start, int end) {
        final InputItem inputItem = new InputItem(
                InputDef.rollMove, Integer.toString(start), Integer.toString(end)
        );
        add(inputItem);
    }

}
