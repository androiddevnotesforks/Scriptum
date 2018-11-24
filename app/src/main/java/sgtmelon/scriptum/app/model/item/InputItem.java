package sgtmelon.scriptum.app.model.item;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import sgtmelon.scriptum.app.control.InputControl;
import sgtmelon.scriptum.office.annot.def.InputDef;

/**
 * Модель для {@link InputControl}
 */
public class InputItem {

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