package sgtmelon.scriptum.app.model.item;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import sgtmelon.scriptum.app.control.InputControl;
import sgtmelon.scriptum.office.annot.def.InputDef;

/**
 * Модель для {@link InputControl}
 */
public final class InputItem {

    /**
     * TODO: 01.12.2018  Переделать. Так как не сохраняется значения до изменения и после.
     * <p>
     * TODO: 01.12.2018  В самом начале редактирования не сохраняется значение, которое было изначально
     * TODO: 01.12.2018  В конце редактирования, после нажатия Undo не сохраняется последнее значение
     * <p>
     * TODO: 01.12.2018  Переделать {@link #valueFirst} и {@link #valueSecond} в fromValue, toValue
     * TODO: 01.12.2018  Подумай как это лучше реализовать со списками
     */

    public final int tag;

    private final String valueFirst;
    private final String valueSecond;

    public InputItem(@InputDef int tag, @Nullable String valueFirst, @NonNull String valueSecond) {
        this.tag = tag;

        this.valueFirst = valueFirst;
        this.valueSecond = valueSecond;
    }

    public int getTag() {
        return tag;
    }

    @Nullable
    public String getValueFirst() {
        return valueFirst;
    }

    @NonNull
    public String getValueSecond() {
        return valueSecond;
    }

    @NonNull
    @Override
    public String toString() {
        return tag + " | " + (valueFirst == null ? "" : valueFirst + " - ") + valueSecond;
    }

}