package sgtmelon.scriptum.app.control;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import sgtmelon.scriptum.app.model.item.InputItem;
import sgtmelon.scriptum.office.annot.DbAnn;
import sgtmelon.scriptum.office.annot.def.InputDef;
import sgtmelon.scriptum.office.intf.InputIntf;

/**
 * Класс предназначенный для контроля ввода данных в заметку, применения undo и redo
 * <p>
 * {@link InputDef} - Значения, которые будут содержаться в списке:
 * Name change  - Значение
 * Rank change  - Отмеченные id
 * Color change - Отмеченный цвет
 * Text change  - Значение
 * Roll change  - Номер пункта : значение
 * Roll add     - Номер пункта
 * Roll swipe   - Номер пункта : значение
 * Roll move    - Начало : конец
 */
public final class InputControl implements InputIntf {

    private static final String TAG = InputControl.class.getSimpleName();

    private final List<InputItem> listInput = new ArrayList<>();

    private int position = 0;
    private boolean enable;

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * Проверка доступна ли отмена
     * @return - Есть куда возвращаться или нет
     */
    public boolean checkUndo(){
        if (listInput.size() != 0){
            return position != 0;
        } else {
            return false;
        }
    }

    public InputItem undo() {
        if (checkUndo()){
            position--;
            return listInput.get(position);
        } else {
            return null;
        }
    }

    /**
     * Проверка доступен ли возврат
     * @return - Есть куда возвращаться или нет
     */
    public boolean checkRedo(){
        if (listInput.size() != 0){
            return position != listInput.size() - 1;
        } else {
            return false;
        }
    }

    public InputItem redo() {
        if (checkRedo()){
            position++;
            return listInput.get(position);
        } else {
            return null;
        }
    }

    private void add(InputItem inputItem) {
        Log.i(TAG, "add: " + inputItem.getTag());
        if (enable) {
            remove();
            listInput.add(inputItem);
            position++;
        }
    }

    /**
     * Если позиция не в конце, то удаление ненужной информации перед добавлением новой
     */
    private void remove() {
        if (position < listInput.size() - 1) {
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
    public void onRankChange(List<Long> rankId) {
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
    public void onRollChange(int p, String text) {
        final InputItem inputItem = new InputItem(
                InputDef.roll, Integer.toString(p), text
        );
        add(inputItem);
    }

    @Override
    public void onRollAdd(int p) {
        final InputItem inputItem = new InputItem(
                InputDef.rollAdd, null, Integer.toString(p)
        );
        add(inputItem);
    }

    @Override
    public void onRollRemove(int p, String text) {
        final InputItem inputItem = new InputItem(
                InputDef.rollSwipe, Integer.toString(p), text
        );
        add(inputItem);
    }

    @Override
    public void onRollMove(int dragStart, int dragEnd) {
        final InputItem inputItem = new InputItem(
                InputDef.rollMove, Integer.toString(dragStart), Integer.toString(dragEnd)
        );
        add(inputItem);
    }

    // FIXME: 24.11.2018 remove
    public void listAll() {
        Log.i(TAG, "listAll: ");
        for (InputItem inputItem : listInput) {
            Log.i(TAG, inputItem.toString());
        }
    }

}