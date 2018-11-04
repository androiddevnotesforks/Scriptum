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

    void onRollChange(int position, String text);

    void onRollAdd(int position, String text);

    void onRollSwipe(int position, String text);

    void onRollMove(int start, int end);

}
