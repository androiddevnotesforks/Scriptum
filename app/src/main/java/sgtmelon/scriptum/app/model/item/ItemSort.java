package sgtmelon.scriptum.app.model.item;

import sgtmelon.scriptum.office.annot.def.DefSort;

public class ItemSort {

    private String text;
    private int key;

    public ItemSort(String text, int key) {
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

    public void setKey(@DefSort int key) {
        this.key = key;
    }
}
