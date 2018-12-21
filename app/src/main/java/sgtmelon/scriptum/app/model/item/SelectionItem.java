package sgtmelon.scriptum.app.model.item;

import androidx.annotation.NonNull;

/**
 * Модель для сохранения в {@link InputItem} выделения текста
 */
public final class SelectionItem {

    private final int start;
    private final int end;

    public SelectionItem(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    @NonNull
    @Override
    public String toString() {
        return start == end
                ? "(" + start + ")"
                : "(" + start + ":" + end + ")";
    }

    public static class Builder {

        private int start;
        private int end;

        public Builder setStart(int start) {
            this.start = start;
            return this;
        }

        public Builder setEnd(int end) {
            this.end = end;
            return this;
        }

        public SelectionItem create(){
            return new SelectionItem(start, end);
        }

    }

}