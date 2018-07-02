package sgtmelon.handynotes.app.control;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import sgtmelon.handynotes.R;

import sgtmelon.handynotes.office.Help;

public class ControlRank implements TextWatcher, TextView.OnEditorActionListener {

    private final Context context;

    private List<String> listName;

    public void setListName(List<String> listName) {
        this.listName = listName;
    }

    public ControlRank(Context context) {
        this.context = context;
    }

    private View.OnClickListener onClickListener;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private ImageButton rankCancel, rankAdd;
    private EditText rankEnter;

    public void setControl(ImageButton rankCancel, ImageButton rankAdd, EditText rankEnter) {
        this.rankCancel = rankCancel;
        this.rankAdd = rankAdd;
        this.rankEnter = rankEnter;
    }

    //TODO переделай смену цвета в XML
    public void tintButton() {
        String name = rankEnter.getText().toString().toUpperCase();

        Help.Icon.tintButton(context, rankCancel, R.drawable.ic_button_cancel, name);
        Help.Icon.tintButton(context, rankAdd, R.drawable.ic_menu_rank, name, !listName.contains(name));
    }

    public boolean needClearEnter() {
        return rankEnter.isFocused() && !rankEnter.getText().toString().equals("");
    }

    public String clearEnter() {
        String rankNm = rankEnter.getText().toString();
        rankEnter.setText("");
        return rankNm;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        tintButton();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_DONE) {
            String name = rankEnter.getText().toString().toUpperCase();
            if (!name.equals("") && !listName.contains(name)) {
                onClickListener.onClick(rankAdd);
            }
            return true;
        }
        return false;
    }

}
