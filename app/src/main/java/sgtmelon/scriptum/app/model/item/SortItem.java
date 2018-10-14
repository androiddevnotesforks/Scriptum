package sgtmelon.scriptum.app.model.item;

import sgtmelon.scriptum.app.adapter.SortAdapter;
import sgtmelon.scriptum.element.SortDialog;

/**
 * Модель для сортировки {@link SortDialog}, {@link SortAdapter}
 */
public final class SortItem {

    private String text;
    private int key;

    public SortItem(String text, int key) {
        this.text = text;
        this.key = key;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

}
