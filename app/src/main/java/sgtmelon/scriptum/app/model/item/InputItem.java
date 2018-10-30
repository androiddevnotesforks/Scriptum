package sgtmelon.scriptum.app.model.item;

import sgtmelon.scriptum.app.control.EnterControl;

/**
 * Модель для {@link EnterControl}
 */
public class InputItem {

    // TODO: 30.10.2018 написать дефолтные значения - метки

    private String tag;
    private String value;

    public InputItem(String tag, String value) {
        this.tag = tag;
        this.value = value;
    }

    public String getTag() {
        return tag;
    }

    public String getValue() {
        return value;
    }

}