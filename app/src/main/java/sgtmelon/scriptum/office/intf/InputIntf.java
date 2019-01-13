package sgtmelon.scriptum.office.intf;

import java.util.List;

import sgtmelon.scriptum.app.control.InputControl;
import sgtmelon.scriptum.app.model.item.CursorItem;

/**
 * Интерфейс для общения с {@link InputControl}
 */
public interface InputIntf {

    // TODO: 13.01.2019 Annotation NonNull/Nullable

    void onRankChange(List<Long> valueFrom, List<Long> valueTo);

    void onColorChange(int valueFrom, int valueTo);

    void onNameChange(String valueFrom, String valueTo, CursorItem cursorItem);

    void onTextChange(String valueFrom, String valueTo, CursorItem cursorItem);

    void onRollChange(int p, String valueFrom, String valueTo, CursorItem cursorItem);

    void onRollAdd(int p, String valueTo);

    void onRollRemove(int p, String valueFrom);

    void onRollMove(int valueFrom, int valueTo);

}