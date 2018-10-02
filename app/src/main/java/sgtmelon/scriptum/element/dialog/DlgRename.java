package sgtmelon.scriptum.element.dialog;

import android.app.Dialog;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.office.annot.AnnDb;
import sgtmelon.scriptum.office.annot.def.DefDlg;
import sgtmelon.scriptum.office.blank.BlankDlg;

public final class DlgRename extends BlankDlg implements TextView.OnEditorActionListener {

    private int position;

    private ArrayList<String> listName;
    private EditText nameEnter;

    public void setArguments(int p, String title, ArrayList<String> listName) {
        Bundle arg = new Bundle();

        arg.putInt(AnnDb.RK_PS, p);
        arg.putString(DefDlg.INIT, title);
        arg.putStringArrayList(DefDlg.VALUE, listName);

        setArguments(arg);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arg = getArguments();

        if (arg != null) {
            position = arg.getInt(AnnDb.RK_PS);
            title = arg.getString(DefDlg.INIT);
            listName = arg.getStringArrayList(DefDlg.VALUE);
        } else if (savedInstanceState != null) {
            position = savedInstanceState.getInt(AnnDb.RK_PS);
            title = savedInstanceState.getString(DefDlg.INIT);
            listName = savedInstanceState.getStringArrayList(DefDlg.VALUE);
        }

        View view = LayoutInflater.from(context).inflate(R.layout.view_rename, null);
        nameEnter = view.findViewById(R.id.rename_enter);

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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(AnnDb.RK_PS, position);
        outState.putString(DefDlg.INIT, title);
        outState.putStringArrayList(DefDlg.VALUE, listName);
    }

    public int getPosition() {
        return position;
    }

    public String getName() {
        return nameEnter.getText().toString();
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

}
