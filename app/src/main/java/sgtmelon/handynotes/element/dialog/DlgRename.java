package sgtmelon.handynotes.element.dialog;

import android.app.Dialog;
import android.content.Context;
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
import sgtmelon.handynotes.office.annot.Db;
import sgtmelon.handynotes.office.annot.Dlg;
import sgtmelon.handynotes.office.blank.BlankDialog;

public class DlgRename extends BlankDialog implements TextView.OnEditorActionListener {

    public void setArguments(int p, String title, ArrayList<String> listName) {
        Bundle arg = new Bundle();
        arg.putInt(Db.RK_PS, p);
        arg.putString(Dlg.INIT, title);
        arg.putStringArrayList(Dlg.VALUE, listName);
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
            position = arg.getInt(Db.RK_PS);
            title = arg.getString(Dlg.INIT);
            listName = arg.getStringArrayList(Dlg.VALUE);
        } else if (savedInstanceState != null) {
            position = savedInstanceState.getInt(Db.RK_PS);
            title = savedInstanceState.getString(Dlg.INIT);
            listName = savedInstanceState.getStringArrayList(Dlg.VALUE);
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

        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setView(view)
                .setPositiveButton(getString(R.string.dialog_btn_accept), onPositiveClick)
                .setNegativeButton(getString(R.string.dialog_btn_cancel), (dialog, id) -> dialog.cancel())
                .setCancelable(true)
                .create();
    }

    @Override
    protected void setEnable() {
        super.setEnable();

        String name = getName();
        if (name.equals("") || listName.contains(name.toUpperCase())) {
            buttonPositive.setEnabled(false);
        } else {
            buttonPositive.setEnabled(true);
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_DONE) {
            String name = getName();
            if (!name.equals("") && !listName.contains(name.toUpperCase())) {
                buttonPositive.callOnClick();
                return true;
            }
        }
        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(Db.RK_PS, position);
        outState.putString(Dlg.INIT, title);
        outState.putStringArrayList(Dlg.VALUE, listName);
    }

}
