package sgtmelon.scriptum.app.model.item;

import androidx.annotation.NonNull;
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
     * TODO: 01.12.2018  Переделать {@link #valueFrom} и {@link #valueTo} в fromValue, toValue
     * TODO: 01.12.2018  Подумай как это лучше реализовать со списками
     */

    private final int tag;

    private final int position;
    private final String valueFrom;
    private final String valueTo;

    public InputItem(@InputDef int tag, int position, @NonNull String valueFrom,
                     @NonNull String valueTo) {
        this.tag = tag;

        this.position = position;
        this.valueFrom = valueFrom;
        this.valueTo = valueTo;
    }

    public InputItem(@InputDef int tag, @NonNull String valueFrom, @NonNull String valueTo) {
        this.tag = tag;

        this.position = -1;
        this.valueFrom = valueFrom;
        this.valueTo = valueTo;
    }

    public int getTag() {
        return tag;
    }

    public int getPosition() {
        return position;
    }

    @NonNull
    public String getValueFrom() {
        return valueFrom;
    }

    @NonNull
    public String getValueTo() {
        return valueTo;
    }

    @NonNull
    @Override
    public String toString() {
        final String stringPosition = position != -1
                ? "position = " + position + " | "
                : "";
        final String stringFrom = "from = " + (!valueFrom.equals("")
                ? valueFrom
                : "empty");
        final String stringTo = "to = " + (!valueTo.equals("")
                ? valueTo
                : "empty");

        return tag + " | " + stringPosition + stringFrom + " | " + stringTo;
    }

}