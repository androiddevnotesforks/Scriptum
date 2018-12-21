package sgtmelon.scriptum.office.intf;

import java.util.List;

import sgtmelon.scriptum.app.control.InputControl;
import sgtmelon.scriptum.app.model.item.SelectionItem;

/**
 * Интерфейс для общения с {@link InputControl}
 */
public interface InputIntf {

    void onRankChange(List<Long> valueFrom, List<Long> valueTo);

    void onColorChange(int valueFrom, int valueTo);

    void onNameChange(String valueFrom, String valueTo, SelectionItem selectionFrom,
                      SelectionItem selectionTo);

    void onTextChange(String valueFrom, String valueTo, SelectionItem selectionFrom,
                      SelectionItem selectionTo);

    void onRollChange(int p, String valueFrom, String valueTo, SelectionItem selectionFrom,
                      SelectionItem selectionTo);

    void onRollAdd(int p, String valueTo);

    void onRollRemove(int p, String valueFrom);

    void onRollMove(int valueFrom, int valueTo);

}