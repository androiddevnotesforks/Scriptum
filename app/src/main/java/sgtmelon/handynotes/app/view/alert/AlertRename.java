package sgtmelon.handynotes.app.view.alert;

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

    private final Context context;

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

    private EditText renameEnter;

    private void setupEditText() {
        View content = LayoutInflater.from(context).inflate(R.layout.view_rename, null);
        renameEnter = content.findViewById(R.id.viewRename_et_enter);

        setView(content);
    }

    public String getRename() {
        return renameEnter.getText().toString();
    }

    private AlertDialog dialog;
    private List<String> listName;

    public void setTextChange(AlertDialog dialog, List<String> listRankName) {
        this.dialog = dialog;
        this.listName = listRankName;

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);

        renameEnter.addTextChangedListener(this);
        renameEnter.setOnEditorActionListener(this);

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

        if (newName.equals("") || listName.contains(newName.toUpperCase())) {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
        } else {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_DONE) {
            String newName = getRename();
            if (!newName.equals("") && !listName.contains(newName)) {
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).callOnClick();
                return true;
            }
        }
        return false;
    }
}
