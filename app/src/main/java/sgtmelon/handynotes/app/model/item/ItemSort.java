package sgtmelon.handynotes.app.model.item;

import sgtmelon.handynotes.office.annot.def.DefSort;

public class ItemSort {

    private String text;
    @DefSort
    private int key;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @DefSort
    public int getKey() {
        return key;
    }

    public void setKey(@DefSort int key) {
        this.key = key;
    }
}
