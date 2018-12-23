package sgtmelon.scriptum.app.model.item;

/**
 * Модель для сохранения в {@link InputItem} курсора текста
 */
public final class CursorItem {

    private final int valueFrom;
    private final int valueTo;

    public CursorItem(int valueFrom, int valueTo) {
        this.valueFrom = valueFrom;
        this.valueTo = valueTo;
    }

    public int getValueFrom() {
        return valueFrom;
    }

    public int getValueTo() {
        return valueTo;
    }

}