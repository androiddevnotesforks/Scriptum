package sgtmelon.scriptum.office.intf;

import java.util.List;

import sgtmelon.scriptum.app.control.InputControl;

/**
 * Интерфейс для общения с {@link InputControl}
 */
public interface InputIntf {

    void onRankChange(List<Long> valueFrom, List<Long> valueTo);

    void onColorChange(int valueFrom, int valueTo);

    void onNameChange(int cursor, String valueFrom, String valueTo);

    void onTextChange(int cursor, String valueFrom, String valueTo);

    void onRollChange(int p, int cursor, String valueFrom, String valueTo);

    void onRollAdd(int p, String valueTo);

    void onRollRemove(int p, String valueFrom);

    void onRollMove(int valueFrom, int valueTo);

}