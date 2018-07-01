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
import sgtmelon.handynotes.app.db.DbRoom;
import sgtmelon.handynotes.app.model.item.ItemNote;
import sgtmelon.handynotes.app.model.item.ItemRoll;
import sgtmelon.handynotes.app.model.repo.RepoNote;
import sgtmelon.handynotes.office.st.StNote;
import sgtmelon.handynotes.app.ui.act.ActMain;
import sgtmelon.handynotes.app.ui.act.ActNote;
import sgtmelon.handynotes.app.ui.act.ActSettings;
import sgtmelon.handynotes.databinding.FrgNotesBinding;
import sgtmelon.handynotes.office.Help;
import sgtmelon.handynotes.office.annot.Db;
import sgtmelon.handynotes.office.annot.def.db.DefBin;
import sgtmelon.handynotes.office.annot.def.db.DefCheck;
import sgtmelon.handynotes.office.annot.def.db.DefType;
import sgtmelon.handynotes.office.intf.IntfAlert;
import sgtmelon.handynotes.office.intf.IntfItem;
import sgtmelon.handynotes.view.alert.AlertOption;

public class FrgNote extends Fragment implements Toolbar.OnMenuItemClickListener,
        IntfItem.Click, IntfItem.LongClick, IntfAlert.OptionNote {

    //region Variable
    final String TAG = "FrgNote";

    private DbRoom db;

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

    private List<RepoNote> listRepoNote;
    private AdapterNote adapterNote;

    private void setupRecyclerView() {
        Log.i(TAG, "setupRecyclerView");

        final DefaultItemAnimator recyclerViewEndAnim = new DefaultItemAnimator() {
            @Override
            public void onAnimationFinished(RecyclerView.ViewHolder viewHolder) {
                bind(listRepoNote.size());
            }
        };

        RecyclerView recyclerView = frgView.findViewById(R.id.frgNotes_rv);
        recyclerView.setItemAnimator(recyclerViewEndAnim);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        listRepoNote = new ArrayList<>();

        adapterNote = new AdapterNote();
        recyclerView.setAdapter(adapterNote);

        adapterNote.setCallback(this, this);
    }

    public void updateAdapter() {
        Log.i(TAG, "updateAdapter");

        db = DbRoom.provideDb(context);
        listRepoNote = db.daoNote().get(context, DefBin.out, Help.Pref.getSortNoteOrder(context));
        db.close();

        adapterNote.updateAdapter(listRepoNote);
        adapterNote.notifyDataSetChanged();

        bind(listRepoNote.size());
    }

    @Override
    public void onItemClick(View view, int p) {
        Log.i(TAG, "onItemClick");

        ItemNote itemNote = listRepoNote.get(p).getItemNote();

        Intent intent = new Intent(context, ActNote.class);

        intent.putExtra(Db.NT_ID, itemNote.getId());
        intent.putExtra(Db.RK_VS, activity.frgRank.controlRank.getVisible());
        intent.putExtra(StNote.KEY_CREATE, false);

        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int p) {
        Log.i(TAG, "onItemLongClick");

        AlertOption alertOption = new AlertOption(context, listRepoNote.get(p).getItemNote(), p);
        alertOption.setOptionNote(this);
        alertOption.showOptionNote();
    }

    @Override
    public void onOptionCheckClick(ItemNote itemNote, int p, @DefCheck int rollCheck, String rollAll) {
        Log.i(TAG, "onOptionCheckClick");

        itemNote.setChange(Help.Time.getCurrentTime(context));
        itemNote.setText(Help.Note.getCheckStr(rollCheck, rollAll));

        db = DbRoom.provideDb(context);
        db.daoRoll().update(itemNote.getId(), rollCheck);
        db.daoNote().update(itemNote);
        db.close();

        RepoNote repoNote = listRepoNote.get(p);
        repoNote.updateListRoll(rollCheck);
        repoNote.setItemNote(itemNote);
        repoNote.updateItemStatus();
        listRepoNote.set(p, repoNote);

        adapterNote.updateAdapter(listRepoNote);
        adapterNote.notifyItemChanged(p);

        activity.frgRank.updateAdapter(false);
    }

    @Override
    public void onOptionBindClick(ItemNote itemNote, int p) {
        Log.i(TAG, "onOptionBindClick");

        RepoNote repoNote = listRepoNote.get(p);

        if (!itemNote.isStatus()) {
            itemNote.setStatus(true);
            repoNote.updateItemStatus(true);
        } else {
            itemNote.setStatus(false);
            repoNote.updateItemStatus(false);
        }

        db = DbRoom.provideDb(context);
        db.daoNote().update(itemNote.getId(), itemNote.isStatus());
        db.close();

        repoNote.setItemNote(itemNote);
        listRepoNote.set(p, repoNote);

        adapterNote.updateAdapter(listRepoNote);
        adapterNote.notifyItemChanged(p);
    }

    @Override
    public void onOptionConvertClick(ItemNote itemNote, int p) {
        Log.i(TAG, "onOptionConvertClick");

        itemNote.setChange(Help.Time.getCurrentTime(context));

        RepoNote repoNote = listRepoNote.get(p);

        db = DbRoom.provideDb(context);
        switch (itemNote.getType()) {
            case DefType.text:
                String[] textToRoll = itemNote.getText().split("\n");                             //Получаем пункты списка

                List<ItemRoll> listRoll = db.daoRoll().insert(itemNote.getId(), textToRoll);      //Записываем пункты

                itemNote.setType(DefType.roll);
                itemNote.setText(Help.Note.getCheckStr(0, listRoll.size()));

                db.daoNote().update(itemNote);

                repoNote.setListRoll(listRoll);
                break;
            case DefType.roll:
                String rollToText = db.daoRoll().getText(itemNote.getId());           //Получаем текст заметки

                itemNote.setType(DefType.text);
                itemNote.setText(rollToText);

                db.daoNote().update(itemNote);
                db.daoRoll().delete(itemNote.getId());

                repoNote.setListRoll();
                break;
        }
        db.close();

        repoNote.setItemNote(itemNote);
        repoNote.updateItemStatus();
        listRepoNote.set(p, repoNote);

        adapterNote.updateAdapter(listRepoNote);
        adapterNote.notifyItemChanged(p);

        activity.frgRank.updateAdapter(false);
    }

    @Override
    public void onOptionDeleteClick(ItemNote itemNote, int p) {
        Log.i(TAG, "onOptionDeleteClick");

        db = DbRoom.provideDb(context);
        db.daoNote().update(itemNote.getId(), Help.Time.getCurrentTime(context), true);
        if (itemNote.isStatus()) {
            db.daoNote().update(itemNote.getId(), false);
        }
        db.close();

        listRepoNote.get(p).updateItemStatus(false);
        listRepoNote.remove(p);

        adapterNote.updateAdapter(listRepoNote);
        adapterNote.notifyItemRemoved(p);

        activity.frgBin.updateAdapter();
    }

}
