package sgtmelon.handynotes.model.manager;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.db.converter.ConverterList;
import sgtmelon.handynotes.model.item.ItemRank;
import sgtmelon.handynotes.Help;

public class ManagerRank implements TextWatcher, TextView.OnEditorActionListener {

    private final Context context;

    private List<ItemRank> listRank;
    private List<String> listRankName;

    public ManagerRank(Context context) {
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

    public void tintButton() {
        String rankText = rankEnter.getText().toString().toUpperCase();

        Help.Icon.tintButton(context, rankCancel, R.drawable.ic_button_cancel, rankText);
        Help.Icon.tintButton(context, rankAdd, R.drawable.ic_menu_rank, rankText, !listRankName.contains(rankText));
    }

    public boolean needClearEnter() {
        return rankEnter.isFocused() && !rankEnter.getText().toString().equals("");
    }

    public String clearEnter() {
        String rankNm = rankEnter.getText().toString();
        rankEnter.setText("");
        return rankNm;
    }

    public List<ItemRank> getListRank() {
        return listRank;
    }

    public void setListRank(List<ItemRank> listRank) {
        this.listRank = listRank;
    }

    public List<String> getListRankName() {
        return listRankName;
    }

    public void setListRankName(List<String> listRankName) {
        this.listRankName = listRankName;
    }

    public String[] getVisible() {
        List<String> rankVisible = new ArrayList<>();
        for (ItemRank itemRank : listRank) {
            if (itemRank.isVisible()) {
                rankVisible.add(Integer.toString(itemRank.getId()));
            }
        }
        return ConverterList.fromList(rankVisible);
    }

    public int size() {
        return listRank.size();
    }

    public void add(int position, ItemRank itemRank) {
        listRank.add(position, itemRank);
        listRankName.add(position, itemRank.getName().toUpperCase());
    }

    public void remove(int position){
        listRank.remove(position);
        listRankName.remove(position);
    }

    public ItemRank get(int position) {
        return listRank.get(position);
    }

    public void set(int position, ItemRank itemRank) {
        listRank.set(position, itemRank);
        listRankName.set(position, itemRank.getName().toUpperCase());
    }

    public void move(int oldPosition, int newPosition){
        ItemRank itemRank = listRank.get(oldPosition);

        remove(oldPosition);
        add(newPosition, itemRank);
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
            String rankText = rankEnter.getText().toString().toUpperCase();
            if (!rankText.equals("") && !listRankName.contains(rankText)) {
                onClickListener.onClick(rankAdd);
            }
            return true;
        }
        return false;
    }

}
