package sgtmelon.handynotes.view.alert;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import sgtmelon.handynotes.R;

public class AlertRename extends AlertDialog.Builder implements TextWatcher, TextView.OnEditorActionListener {

    private Context context;

    @SuppressWarnings("unused")
    public AlertRename(@NonNull Context context) {
        super(context);

        this.context = context;

        setupEditText();
    }

    public AlertRename(@NonNull Context context, int themeResId) {
        super(context, themeResId);

        this.context = context;

        setupEditText();
    }

    private EditText enterName;

    private void setupEditText() {
        View content = LayoutInflater.from(context).inflate(R.layout.view_rename, null);
        enterName = content.findViewById(R.id.editText_viewRename_enter);

        setView(content);
    }

    public String getRename() {
        return enterName.getText().toString();
    }

    private AlertDialog dialog;
    private List<String> listRankName;

    public void setTextChange(AlertDialog dialog, List<String> listRankName) {
        this.dialog = dialog;
        this.listRankName = listRankName;

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);

        enterName.addTextChangedListener(this);
        enterName.setOnEditorActionListener(this);

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String newName = getRename();

        if (newName.equals("") || listRankName.contains(newName)) {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
        } else {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_DONE) {
            String newName = getRename();
            if (!newName.equals("") && !listRankName.contains(newName)) {
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).callOnClick();
                return true;
            }
        }
        return false;
    }
}
