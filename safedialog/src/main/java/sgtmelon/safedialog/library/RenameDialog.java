package sgtmelon.safedialog.library;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import sgtmelon.safedialog.R;
import sgtmelon.safedialog.office.annot.DialogAnn;
import sgtmelon.safedialog.office.blank.DialogBlank;

public final class RenameDialog extends DialogBlank implements TextView.OnEditorActionListener {

    private int position;
    private ArrayList<String> listName;
    private EditText nameEnter;

    @ColorInt private int colorText, colorHint;
    private String textHint;
    private int textLength;

    public void setArguments(int p, String title, ArrayList<String> listName) {
        final Bundle bundle = new Bundle();

        bundle.putInt(DialogAnn.POSITION, p);
        bundle.putString(DialogAnn.INIT, title);
        bundle.putStringArrayList(DialogAnn.VALUE, listName);

        setArguments(bundle);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle bundle = getArguments();

        if (savedInstanceState != null) {
            position = savedInstanceState.getInt(DialogAnn.POSITION);
            title = savedInstanceState.getString(DialogAnn.INIT);
            listName = savedInstanceState.getStringArrayList(DialogAnn.VALUE);
        } else if (bundle != null) {
            position = bundle.getInt(DialogAnn.POSITION);
            title = bundle.getString(DialogAnn.INIT);
            listName = bundle.getStringArrayList(DialogAnn.VALUE);
        }

        final View view = LayoutInflater.from(context).inflate(R.layout.view_rename, null);
        nameEnter = view.findViewById(R.id.rename_enter);

        nameEnter.setTextColor(colorText);
        nameEnter.setHintTextColor(colorHint);
        nameEnter.setHint(textHint);
        nameEnter.setFilters(new InputFilter[] {new InputFilter.LengthFilter(textLength)});

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

        outState.putInt(DialogAnn.POSITION, position);
        outState.putString(DialogAnn.INIT, title);
        outState.putStringArrayList(DialogAnn.VALUE, listName);
    }

    public void setColorText(int colorText) {
        this.colorText = colorText;
    }

    public void setColorHint(int colorHint) {
        this.colorHint = colorHint;
    }

    public void setTextHint(String textHint) {
        this.textHint = textHint;
    }

    public void setTextLength(int textLength) {
        this.textLength = textLength;
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

        final String name = getName();
        if (TextUtils.isEmpty(name) || listName.contains(name.toUpperCase())) {
            buttonPositive.setEnabled(false);
        } else {
            buttonPositive.setEnabled(true);
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_DONE) {
            final String name = getName();
            if (!TextUtils.isEmpty(name) && !listName.contains(name.toUpperCase())) {
                buttonPositive.callOnClick();
                return true;
            }
        }
        return false;
    }

}
