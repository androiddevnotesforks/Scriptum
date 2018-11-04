package sgtmelon.scriptum.office.intf;

import sgtmelon.scriptum.app.control.InputControl;

/**
 * Интерфейс для общения с {@link InputControl}
 */
public interface InputIntf {

    void onRankChange(Long[] rankId);

    void onColorChange(int color);

    void onNameChange(String text);

    void onTextChange(String text);

    void onRollChange(int p, String text);

    void onRollAdd(int p);

    void onRollRemove(int p, String text);

    void onRollMove(int dragStart, int dragEnd);

}
