package sgtmelon.scriptum.office.intf;

import java.util.List;

import androidx.annotation.NonNull;
import sgtmelon.scriptum.app.control.InputControl;
import sgtmelon.scriptum.app.model.item.CursorItem;

/**
 * Интерфейс для общения с {@link InputControl}
 */
public interface InputIntf {

    void setEnabled(boolean enabled);

    boolean isChangeEnabled();

    void onRankChange(@NonNull List<Long> valueFrom, @NonNull List<Long> valueTo);

    void onColorChange(int valueFrom, int valueTo);

    void onNameChange(@NonNull String valueFrom, @NonNull String valueTo,
                      @NonNull CursorItem cursorItem);

    void onTextChange(@NonNull String valueFrom, @NonNull String valueTo,
                      @NonNull CursorItem cursorItem);

    void onRollChange(int p, @NonNull String valueFrom, @NonNull String valueTo,
                      @NonNull CursorItem cursorItem);

    void onRollAdd(int p, @NonNull String valueTo);

    void onRollRemove(int p, @NonNull String valueFrom);

    void onRollMove(int valueFrom, int valueTo);

}