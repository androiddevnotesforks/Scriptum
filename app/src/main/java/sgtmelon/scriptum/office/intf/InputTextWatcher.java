package sgtmelon.scriptum.office.intf;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.NonNull;
import sgtmelon.scriptum.app.control.InputControl;
import sgtmelon.scriptum.app.model.item.CursorItem;
import sgtmelon.scriptum.office.annot.def.InputDef;

/**
 * Контроллер ввода текста для {@link InputControl}
 */
public final class InputTextWatcher implements TextWatcher {

    private final EditText view;
    private final int tag;

    private final BindIntf bindIntf;
    private final InputIntf inputIntf;

    private String textFrom = "";
    private int cursorFrom = 0;

    public InputTextWatcher(@NonNull EditText view, @InputDef int tag,
                            @NonNull BindIntf bindIntf, @NonNull InputIntf inputIntf) {
        this.view = view;
        this.tag = tag;

        this.bindIntf = bindIntf;
        this.inputIntf = inputIntf;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        textFrom = s.toString();
        cursorFrom = view.getSelectionEnd();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        final String textTo = s.toString();
        final int cursorTo = view.getSelectionEnd();

        if (!textFrom.equals(textTo)) {
            final CursorItem cursorItem = new CursorItem(cursorFrom, cursorTo);

            switch (tag) {
                case InputDef.name:
                    inputIntf.onNameChange(textFrom, textTo, cursorItem);
                    break;
                case InputDef.text:
                    inputIntf.onTextChange(textFrom, textTo, cursorItem);
                    break;
            }

            textFrom = textTo;
            cursorFrom = cursorTo;

            bindIntf.bindInput();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}