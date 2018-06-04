package sgtmelon.handynotes.model.manager;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import sgtmelon.handynotes.model.item.ItemRank;

public class ListRankManager implements TextWatcher{

    private Context context;

    private List<ItemRank> listRank;
    private List<String> listRankName;

//    private final ImageButton rankCancel, rankAdd;

    public ListRankManager(Context context, ImageButton rankCancel, ImageButton rankAdd) {
        this.context = context;

        listRank = new ArrayList<>();
        listRankName = new ArrayList<>();

    }

    public List<ItemRank> getListRank() {
        return listRank;
    }

    public ItemRank getListRank(int position){
        return listRank.get(position);
    }

    public void setListRank(List<ItemRank> listRank) {
        this.listRank = listRank;
    }

    public void setListRank(int position, ItemRank itemRank){
        listRank.set(position, itemRank);
    }

    public List<String> getListRankName() {
        return listRankName;
    }

    public String getListRankName(int position){
        return listRankName.get(position);
    }

    public void setListRankName(List<String> listRankName) {
        this.listRankName = listRankName;
    }

    public void setListRankName(int position, String rankName){
        listRankName.set(position, rankName);
    }

    public void removeItem(int position){
        listRank.remove(position);
        listRankName.remove(position);
    }

    public int getSize(){
        return listRank.size();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
