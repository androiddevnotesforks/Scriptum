package sgtmelon.scriptum.app.model.item;

import androidx.annotation.NonNull;
import sgtmelon.scriptum.app.control.InputControl;
import sgtmelon.scriptum.office.annot.def.InputDef;

/**
 * Модель для {@link InputControl}
 */
public final class InputItem {

    private final int tag;
    private final int position;
    private final int cursor; // TODO: 17.12.2018 selectionStart/End

    private final String valueFrom;
    private final String valueTo;

    private InputItem(@InputDef int tag, int position, int cursor, @NonNull String valueFrom,
                      @NonNull String valueTo) {
        if (tag == InputDef.indefinite) {
            throw new NullPointerException(InputItem.class.getSimpleName() + "#tag is indefinite");
        }

        this.tag = tag;
        this.position = position;
        this.cursor = cursor;

        this.valueFrom = valueFrom;
        this.valueTo = valueTo;
    }

    public int getTag() {
        return tag;
    }

    public int getPosition() {
        return position;
    }

    public int getCursor() {
        return cursor;
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

    public static class Builder {

        private int tag = InputDef.indefinite;
        private int position = -1;
        private int cursor = -1;

        private String valueFrom = null;
        private String valueTo = null;

        public Builder setTag(@InputDef int tag) {
            this.tag = tag;
            return this;
        }

        public Builder setPosition(int position) {
            this.position = position;
            return this;
        }

        public Builder setCursor(int cursor) {
            this.cursor = cursor;
            return this;
        }

        public Builder setValueFrom(String valueFrom) {
            this.valueFrom = valueFrom;
            return this;
        }

        public Builder setValueTo(String valueTo) {
            this.valueTo = valueTo;
            return this;
        }

        public InputItem create() {
            if (tag == InputDef.indefinite) {
                throw new NullPointerException(InputItem.class.getSimpleName() + "#tag is indefinite");
            }

            if (valueFrom == null) {
                throw new NullPointerException(InputItem.class.getSimpleName() + "#valueFrom is null");
            }

            if (valueTo == null) {
                throw new NullPointerException(InputItem.class.getSimpleName() + "#valueTo is null");
            }

            return new InputItem(tag, position, cursor, valueFrom, valueTo);
        }

    }

}