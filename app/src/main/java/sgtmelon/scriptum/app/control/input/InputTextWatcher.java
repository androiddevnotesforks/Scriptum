package sgtmelon.scriptum.app.control.input;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.NonNull;
import sgtmelon.scriptum.app.model.item.CursorItem;
import sgtmelon.scriptum.office.annot.def.InputDef;

/**
 * Контроллер ввода текста для {@link InputControl}
 */
public final class InputTextWatcher implements TextWatcher {

    private final EditText view;
    private final int tag;

    private final Result result;
    private final InputIntf inputIntf;

    private String textFrom = "";
    private int cursorFrom = 0;

    // TODO nullable view

    public InputTextWatcher(EditText view, @InputDef int tag,
                            @NonNull Result result, @NonNull InputIntf inputIntf) {
        this.view = view;
        this.tag = tag;

        this.result = result;
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

        if (textFrom.equals(textTo)) return;

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

        result.onTextResult();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public interface Result {
        void onTextResult();
    }

}