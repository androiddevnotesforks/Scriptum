package sgtmelon.handynotes.element.dialog;

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

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import sgtmelon.handynotes.R;
import sgtmelon.handynotes.office.annot.Dlg;
import sgtmelon.handynotes.office.annot.def.db.DefDb;
import sgtmelon.handynotes.office.blank.BlankDialog;

public class DlgRename extends BlankDialog implements TextView.OnEditorActionListener {

    public void setArguments(int p, String title, ArrayList<String> listName) {
        Bundle arg = new Bundle();
        arg.putInt(DefDb.RK_PS, p);
        arg.putString(Dlg.VALUE, title);
        arg.putStringArrayList(DefDb.RK_NM, listName);
        setArguments(arg);
    }

    private int position;
    private ArrayList<String> listName;

    public int getPosition() {
        return position;
    }

    private EditText nameEnter;

    public String getName() {
        return nameEnter.getText().toString();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getContext();
        Bundle arg = getArguments();

        if (arg != null) {
            position = arg.getInt(DefDb.RK_PS);
            title = arg.getString(Dlg.VALUE);
            listName = arg.getStringArrayList(DefDb.RK_NM);
        } else if (savedInstanceState != null) {
            position = savedInstanceState.getInt(DefDb.RK_PS);
            title = savedInstanceState.getString(Dlg.VALUE);
            listName = savedInstanceState.getStringArrayList(DefDb.RK_NM);
        }

        View view = LayoutInflater.from(context).inflate(R.layout.view_rename, null);
        nameEnter = view.findViewById(R.id.viewRename_et_enter);

        nameEnter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setEnable();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        nameEnter.setOnEditorActionListener(this);

        return  new AlertDialog.Builder(context)
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
        setEnable();
    }

    private AlertDialog dialog;

    private void setEnable(){
        if (dialog == null) dialog = (AlertDialog) getDialog();

        String name = getName();
        if (name.equals("") || listName.contains(name.toUpperCase())) {
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

            if (!name.equals("") && !listName.contains(name.toUpperCase())) {
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).callOnClick();
                return true;
            }
        }
        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(DefDb.RK_PS, position);
        outState.putString(Dlg.VALUE, title);
        outState.putStringArrayList(DefDb.RK_NM, listName);
    }

}
