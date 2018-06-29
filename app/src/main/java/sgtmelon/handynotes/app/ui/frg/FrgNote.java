package sgtmelon.handynotes.app.ui.frg;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.app.adapter.AdapterNote;
import sgtmelon.handynotes.app.data.DataRoom;
import sgtmelon.handynotes.databinding.FrgNotesBinding;
import sgtmelon.handynotes.office.annot.def.db.DefBin;
import sgtmelon.handynotes.office.annot.def.db.DefCheck;
import sgtmelon.handynotes.office.annot.def.db.DefType;
import sgtmelon.handynotes.app.model.item.ItemNote;
import sgtmelon.handynotes.app.model.state.StateNote;
import sgtmelon.handynotes.office.annot.Db;
import sgtmelon.handynotes.office.Help;
import sgtmelon.handynotes.office.intf.IntfItem;
import sgtmelon.handynotes.office.intf.IntfAlert;
import sgtmelon.handynotes.app.model.item.ItemRollView;
import sgtmelon.handynotes.app.ui.act.ActMain;
import sgtmelon.handynotes.app.ui.act.ActNote;
import sgtmelon.handynotes.app.ui.act.ActSettings;
import sgtmelon.handynotes.view.alert.AlertOption;

public class FrgNote extends Fragment implements Toolbar.OnMenuItemClickListener,
        IntfItem.Click, IntfItem.LongClick, IntfAlert.OptionNote {

    //region Variable
    final String TAG = "FrgNote";

    private DataRoom db;

    private View frgView;
    private Context context;
    private ActMain activity;

    private FrgNotesBinding binding;
    //endregion

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");

        updateAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        binding = DataBindingUtil.inflate(inflater, R.layout.frg_notes, container, false);


        frgView = binding.getRoot();

        context = getContext();
        activity = (ActMain) getActivity();

        setupToolbar();
        setupRecyclerView();

        return frgView;
    }

    private void bind(int listSize) {
        binding.setListEmpty(listSize == 0);
        binding.executePendingBindings();
    }

    private void setupToolbar() {
        Log.i(TAG, "setupToolbar");

        Toolbar toolbar = frgView.findViewById(R.id.incToolbar_tb);
        toolbar.setTitle(getString(R.string.title_frg_note));

        toolbar.inflateMenu(R.menu.menu_frg_note);
        toolbar.setOnMenuItemClickListener(this);

        Menu menu = toolbar.getMenu();
        MenuItem mItemSettings = menu.findItem(R.id.menu_frgNote_settings);

        Help.Icon.tintMenuIcon(context, mItemSettings);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Log.i(TAG, "onMenuItemClick");

        switch (item.getItemId()) {
            case R.id.menu_frgNote_settings:
                Intent intent = new Intent(context, ActSettings.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    private List<ItemNote> listNote;
    private AdapterNote adapterNote;

    private void setupRecyclerView() {
        Log.i(TAG, "setupRecyclerView");

        final DefaultItemAnimator recyclerViewEndAnim = new DefaultItemAnimator() {
            @Override
            public void onAnimationFinished(RecyclerView.ViewHolder viewHolder) {
                bind(listNote.size());
            }
        };

        RecyclerView recyclerView = frgView.findViewById(R.id.frgNotes_rv);
        recyclerView.setItemAnimator(recyclerViewEndAnim);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        listNote = new ArrayList<>();

        adapterNote = new AdapterNote();
        recyclerView.setAdapter(adapterNote);

        adapterNote.setCallback(this, this);
    }

    public void updateAdapter() {
        Log.i(TAG, "updateAdapter");

        db = DataRoom.provideDb(context);
        listNote = db.daoNote().get(DefBin.out, Help.Pref.getSortNoteOrder(context));
        db.close();

        adapterNote.updateAdapter(listNote);
        adapterNote.setManagerRoll(activity.managerRoll);

        adapterNote.notifyDataSetChanged();

        bind(listNote.size());
    }

    @Override
    public void onItemClick(View view, int p) {
        Log.i(TAG, "onItemClick");

        ItemNote itemNote = listNote.get(p);

        Intent intent = new Intent(context, ActNote.class);

        intent.putExtra(Db.NT_ID, itemNote.getId());
        intent.putExtra(Db.RK_VS, activity.frgRank.managerRank.getVisible());
        intent.putExtra(StateNote.KEY_CREATE, false);

        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int p) {
        Log.i(TAG, "onItemLongClick");

        AlertOption alertOption = new AlertOption(context, listNote.get(p), p);
        alertOption.setOptionNote(this);
        alertOption.showOptionNote();
    }

    @Override
    public void onOptionCheckClick(ItemNote itemNote, int p, @DefCheck int rollCheck, String rollAll) {
        Log.i(TAG, "onOptionCheckClick");

        itemNote.setChange(Help.Time.getCurrentTime(context));
        itemNote.setText(Help.Note.getCheckStr(rollCheck, rollAll));

        db = DataRoom.provideDb(context);
        db.daoRoll().update(itemNote.getId(), rollCheck);
        db.daoNote().update(itemNote);
        db.close();

        activity.managerRoll.updateList(itemNote.getId(), rollCheck);

        listNote.set(p, itemNote);

        adapterNote.updateAdapter(listNote);
        adapterNote.setManagerRoll(activity.managerRoll);

        adapterNote.notifyItemChanged(p);

        activity.managerStatus.updateItemBind(itemNote);

        activity.frgRank.updateAdapter(false);
    }

    @Override
    public void onOptionBindClick(ItemNote itemNote, int p) {
        Log.i(TAG, "onOptionBindClick");

        if (!itemNote.isStatus()) {
            itemNote.setStatus(true);
            activity.managerStatus.insertItem(itemNote, activity.frgRank.managerRank.getVisible());
        } else {
            itemNote.setStatus(false);
            activity.managerStatus.removeItem(itemNote);
        }

        db = DataRoom.provideDb(context);
        db.daoNote().update(itemNote.getId(), itemNote.isStatus());
        db.close();

        listNote.set(p, itemNote);

        adapterNote.updateAdapter(listNote);
        adapterNote.notifyItemChanged(p);
    }

    @Override
    public void onOptionConvertClick(ItemNote itemNote, int p) {
        Log.i(TAG, "onOptionConvertClick");

        itemNote.setChange(Help.Time.getCurrentTime(context));

        db = DataRoom.provideDb(context);
        switch (itemNote.getType()) {
            case DefType.text:
                String[] textToRoll = itemNote.getText().split("\n");                             //Получаем пункты списка

                ItemRollView itemRollView = db.daoRoll().insert(itemNote.getId(), textToRoll);      //Записываем пункты

                itemNote.setType(DefType.roll);
                itemNote.setText(Help.Note.getCheckStr(0, itemRollView.getSize()));

                db.daoNote().update(itemNote);

                activity.managerRoll.insertList(itemNote.getId(), itemRollView);
                break;
            case DefType.roll:
                String rollToText = db.daoRoll().getText(itemNote.getId());           //Получаем текст заметки

                itemNote.setType(DefType.text);
                itemNote.setText(rollToText);

                db.daoNote().update(itemNote);
                db.daoRoll().deleteRoll(itemNote.getId());

                activity.managerRoll.removeList(itemNote.getCreate());
                break;
        }
        db.close();

        listNote.set(p, itemNote);

        adapterNote.updateAdapter(listNote);
        adapterNote.setManagerRoll(activity.managerRoll);

        adapterNote.notifyItemChanged(p);

        activity.managerStatus.updateItemBind(itemNote);

        activity.frgRank.updateAdapter(false);
    }

    @Override
    public void onOptionDeleteClick(ItemNote itemNote, int p) {
        Log.i(TAG, "onOptionDeleteClick");

        db = DataRoom.provideDb(context);
        db.daoNote().update(itemNote.getId(), Help.Time.getCurrentTime(context), true);
        if (itemNote.isStatus()) {
            db.daoNote().update(itemNote.getId(), false);
        }
        db.close();

        listNote.remove(p);
        adapterNote.updateAdapter(listNote);
        adapterNote.notifyItemRemoved(p);

        activity.managerStatus.removeItem(itemNote);

        activity.frgBin.updateAdapter();
    }

}
