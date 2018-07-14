package sgtmelon.handynotes.element.dialog.main;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;

import androidx.appcompat.app.AlertDialog;
import sgtmelon.handynotes.R;
import sgtmelon.handynotes.office.annot.Db;
import sgtmelon.handynotes.office.annot.Dlg;
import sgtmelon.handynotes.office.blank.BlankDialog;

public class DlgRename extends BlankDialog
        implements TextWatcher, TextView.OnEditorActionListener {

    public void setArguments(String title, String[] listName) {
        Bundle arg = new Bundle();
        arg.putString(Dlg.VALUE, title);

        // FIXME: 14.07.2018 сделай нормально
        for (int i = 0; i < listName.length; i++){
            listName[i] = listName[i].toUpperCase();
        }

        arg.putStringArray(Db.RK_NM, listName);

        setArguments(arg);
    }

    private String[] listName;

    private String title;

    public String getTitle() {
        return title;
    }

    private EditText nameEnter;

    public String getName() {
        return nameEnter.getText().toString();
    }

    private AlertDialog dialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getContext();
        Bundle arg = getArguments();

        if (arg != null) {
            title = arg.getString(Dlg.VALUE);
            listName = arg.getStringArray(Db.RK_NM);
        } else if (savedInstanceState != null) {
            title = savedInstanceState.getString(Dlg.VALUE);
            listName = savedInstanceState.getStringArray(Db.RK_NM);
        }

        View view = LayoutInflater.from(context).inflate(R.layout.view_rename, null);
        nameEnter = view.findViewById(R.id.viewRename_et_enter);

        nameEnter.addTextChangedListener(this);
        nameEnter.setOnEditorActionListener(this);

        return  new AlertDialog.Builder(context, R.style.AppTheme_AlertDialog)
                .setTitle(title)
                .setView(view)
                .setPositiveButton(getString(R.string.dialog_btn_accept), positiveClick)
                .setNegativeButton(getString(R.string.dialog_btn_cancel), (dialog, id) -> dialog.cancel())
                .setCancelable(true)
                .create();
    }

    @Override
    public void onStart() {
        super.onStart();
        dialog = (AlertDialog) getDialog();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String name = getName();
        if (dialog == null) dialog = (AlertDialog) getDialog();

        if (name.equals("") || Arrays.asList(listName).contains(name.toUpperCase())) {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
        } else {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_DONE) {
            String name = getName();
            if (dialog == null) dialog = (AlertDialog) getDialog();

            if (!name.equals("") && !Arrays.asList(listName).contains(name.toUpperCase())) {
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).callOnClick();
                return true;
            }
        }
        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(Dlg.VALUE, title);
        outState.putStringArray(Db.RK_NM, listName);
    }

}
