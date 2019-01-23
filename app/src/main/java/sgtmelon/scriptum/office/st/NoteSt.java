package sgtmelon.scriptum.office.st;

import androidx.annotation.NonNull;

/**
 * Состояние для фрагментов с заметками, необходимое для управления её редактированием
 */
public final class NoteSt {

    private boolean create;
    private boolean edit;
    private boolean bin;
    private boolean first = true;

    public NoteSt(boolean create) {
        this.create = create;
        edit = create;
    }

    public boolean isCreate() {
        return create;
    }

    public void setCreate(boolean create) {
        this.create = create;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public boolean isBin() {
        return bin;
    }

    public void setBin(boolean bin) {
        this.bin = bin;
    }

    @NonNull
    @Override
    public String toString() {
        return "create = " + create + "\n" +
                "first = " + first + "\n" +
                "edit = " + edit + "\n" +
                "bin = " + bin;
    }
}