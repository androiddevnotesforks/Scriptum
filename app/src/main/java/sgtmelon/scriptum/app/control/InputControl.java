package sgtmelon.scriptum.app.control;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import sgtmelon.scriptum.app.model.item.CursorItem;
import sgtmelon.scriptum.app.model.item.InputItem;
import sgtmelon.scriptum.office.annot.DbAnn;
import sgtmelon.scriptum.office.annot.def.InputDef;
import sgtmelon.scriptum.office.intf.InputIntf;

/**
 * Класс предназначенный для контроля ввода данных в заметку, применения undo и redo
 * Модель для хранения данных: {@link InputItem}
 * <p>
 * {@link InputDef} - Значения, которые будут содержаться в списке:
 * Name change  - Текст (до/после)
 * Rank change  - Отмеченные id (до/после)
 * Color change - Отмеченный цвет (до/после)
 * Text change  - Текст (до/после)
 * Roll change  - Текст (пункт/до/после)
 * Roll add     - Номер пункта : значение
 * Roll swipe   - Номер пункта : значение
 * Roll move    - Перемещение (до/после)
 */
public final class InputControl implements InputIntf {

    // TODO: 17.12.2018 хранить последние 200 изменений
    // TODO: 27.01.2019 долгое нажатие (надо каким-то образом собирать последнюю информацию о изменённом view)

    private static final String TAG = InputControl.class.getSimpleName();

    private final List<InputItem> listInput = new ArrayList<>();

    private int position = -1;
    private boolean enabled;                // Переменная для предотвращения записи каких-либо изменений
    private boolean changeEnabled = true;   // Переменная для дополнительного контроля (разрешения/запрета) изменения переменной enabled

    public void clear() {
        listInput.clear();
        position = -1;
    }

    /**
     * Проверка доступна ли отмена
     *
     * @return - Есть куда возвращаться или нет
     */
    public boolean isUndoAccess() {
        return listInput.size() != 0 && position != -1;
    }

    @Nullable
    public InputItem undo() {
        return isUndoAccess()
                ? listInput.get(position--)
                : null;
    }

    /**
     * Проверка доступен ли возврат
     *
     * @return - Есть куда возвращаться или нет
     */
    public boolean isRedoAccess() {
        return listInput.size() != 0 && position != listInput.size() - 1;
    }

    @Nullable
    public InputItem redo() {
        return isRedoAccess()
                ? listInput.get(++position)
                : null;
    }

    private void add(@NonNull InputItem inputItem) {
        if (enabled) {
            remove();
            listInput.add(inputItem);
            position++;
        }

        listAll();
    }

    /**
     * Если позиция не в конце, то удаление ненужной информации перед добавлением новой
     */
    private void remove() {
        final int endPosition = listInput.size() - 1;

        if (position != endPosition) {
            for (int i = endPosition; i > position; i--) {
                listInput.remove(i);
            }
        }

        listAll();
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isChangeEnabled() {
        return changeEnabled;
    }

    public void setChangeEnabled(boolean changeEnabled) {
        this.changeEnabled = changeEnabled;
    }

    @Override
    public void onRankChange(@NonNull List<Long> valueFrom, @NonNull List<Long> valueTo) {
        final InputItem inputItem = new InputItem.Builder()
                .setTag(InputDef.rank)
                .setValueFrom(TextUtils.join(DbAnn.Value.DIVIDER, valueFrom))
                .setValueTo(TextUtils.join(DbAnn.Value.DIVIDER, valueTo))
                .create();

        add(inputItem);
    }

    @Override
    public void onColorChange(int valueFrom, int valueTo) {
        final InputItem inputItem = new InputItem.Builder()
                .setTag(InputDef.color)
                .setValueFrom(Integer.toString(valueFrom))
                .setValueTo(Integer.toString(valueTo))
                .create();

        add(inputItem);
    }

    @Override
    public void onNameChange(@NonNull String valueFrom, @NonNull String valueTo, @NonNull CursorItem cursorItem) {
        final InputItem inputItem = new InputItem.Builder()
                .setTag(InputDef.name)
                .setValueFrom(valueFrom)
                .setValueTo(valueTo)
                .setCursorItem(cursorItem)
                .create();

        add(inputItem);
    }

    @Override
    public void onTextChange(@NonNull String valueFrom, @NonNull String valueTo, @NonNull CursorItem cursorItem) {
        final InputItem inputItem = new InputItem.Builder()
                .setTag(InputDef.text)
                .setValueFrom(valueFrom)
                .setValueTo(valueTo)
                .setCursorItem(cursorItem)
                .create();

        add(inputItem);
    }

    @Override
    public void onRollChange(int p, @NonNull String valueFrom, @NonNull String valueTo, @NonNull CursorItem cursorItem) {
        final InputItem inputItem = new InputItem.Builder()
                .setTag(InputDef.roll)
                .setPosition(p)
                .setValueFrom(valueFrom)
                .setValueTo(valueTo)
                .setCursorItem(cursorItem)
                .create();

        add(inputItem);
    }

    @Override
    public void onRollAdd(int p, @NonNull String valueTo) {
        final InputItem inputItem = new InputItem.Builder()
                .setTag(InputDef.rollAdd)
                .setPosition(p)
                .setValueFrom("")
                .setValueTo(valueTo)
                .create();

        add(inputItem);
    }

    @Override
    public void onRollRemove(int p, @NonNull String valueFrom) {
        final InputItem inputItem = new InputItem.Builder()
                .setTag(InputDef.rollRemove)
                .setPosition(p)
                .setValueFrom(valueFrom)
                .setValueTo("")
                .create();

        add(inputItem);
    }

    @Override
    public void onRollMove(int valueFrom, int valueTo) {
        final InputItem inputItem = new InputItem.Builder()
                .setTag(InputDef.rollMove)
                .setValueFrom(Integer.toString(valueFrom))
                .setValueTo(Integer.toString(valueTo))
                .create();

        add(inputItem);
    }

    private void listAll() {
        Log.i(TAG, "listAll:");

        for (int i = 0; i < listInput.size(); i++) {
            final InputItem inputItem = listInput.get(i);
            final String ps = position == i
                    ? " | cursor = " + position
                    : "";

            Log.i(TAG, "i = " + i + " | " + inputItem.toString() + ps);
        }
    }

}