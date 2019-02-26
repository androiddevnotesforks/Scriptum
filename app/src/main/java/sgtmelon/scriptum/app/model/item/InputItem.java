package sgtmelon.scriptum.app.model.item;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import sgtmelon.scriptum.app.control.input.InputControl;
import sgtmelon.scriptum.office.annot.def.InputDef;

/**
 * Модель для {@link InputControl}
 */
public final class InputItem {

    private final int tag;
    private final int position;

    private final String valueFrom;
    private final String valueTo;

    private final CursorItem cursorItem;

    private InputItem(@InputDef int tag, int position, @NonNull String valueFrom,
                      @NonNull String valueTo, @Nullable CursorItem cursorItem) {
        if (tag == InputDef.indefinite) {
            throw new NullPointerException(InputItem.class.getSimpleName() + "#tag is indefinite");
        }

        this.tag = tag;
        this.position = position;

        this.valueFrom = valueFrom;
        this.valueTo = valueTo;

        this.cursorItem = cursorItem;
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

    @Nullable
    public CursorItem getCursorItem() {
        return cursorItem;
    }

    @NonNull
    @Override
    public String toString() {
        final String stringPosition = position != -1
                ? "position = " + position + " | "
                : "";

        final String stringValueFrom = "from = " + (!valueFrom.equals("")
                ? valueFrom
                : "empty");

        final String stringCursorFrom = cursorItem != null
                ? Integer.toString(cursorItem.getValueFrom())
                : "(null)";

        final String stringValueTo = "to = " + (!valueTo.equals("")
                ? valueTo
                : "empty");

        final String stringCursorTo = cursorItem != null
                ? Integer.toString(cursorItem.getValueTo())
                : "(null)";

        return tag + " | " + stringPosition +
                stringValueFrom + " / " + stringCursorFrom + " | " +
                stringValueTo + " / " + stringCursorTo;
    }

    public static class Builder {

        private int tag = InputDef.indefinite;
        private int position = -1;

        private String valueFrom = null;
        private String valueTo = null;

        private CursorItem cursorItem = null;

        public Builder setTag(@InputDef int tag) {
            this.tag = tag;
            return this;
        }

        public Builder setPosition(int position) {
            this.position = position;
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

        public Builder setCursorItem(CursorItem cursorItem) {
            this.cursorItem = cursorItem;
            return this;
        }

        public InputItem create() {
            if (tag == InputDef.indefinite) {
                throw new NullPointerException(InputItem.class.getSimpleName() +
                        "#tag is indefinite");
            }

            if (valueFrom == null) {
                throw new NullPointerException(InputItem.class.getSimpleName() +
                        "#valueFrom is null");
            }

            if (valueTo == null) {
                throw new NullPointerException(InputItem.class.getSimpleName() +
                        "#valueTo is null");
            }

            if (tag == InputDef.name || tag == InputDef.text || tag == InputDef.roll) {
                if (cursorItem == null) {
                    throw new NullPointerException(InputItem.class.getSimpleName() +
                            "#cursorItem is null");
                }
            }

            return new InputItem(tag, position, valueFrom, valueTo, cursorItem);
        }

    }

}