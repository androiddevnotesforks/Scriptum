package sgtmelon.scriptum.app.model.item;

import androidx.annotation.NonNull;
import sgtmelon.scriptum.app.adapter.SortAdapter;
import sgtmelon.scriptum.element.SortDialog;

/**
 * Модель для сортировки {@link SortDialog}, {@link SortAdapter}
 */
public final class SortItem {

    private String text;
    private int key;

    public SortItem(@NonNull String text, int key) {
        this.text = text;
        this.key = key;
    }

    @NonNull
    public String getText() {
        return text;
    }

    public void setText(@NonNull String text) {
        this.text = text;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

}