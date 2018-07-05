package sgtmelon.handynotes.app.view.frg;

import android.arch.lifecycle.ViewModelProviders;
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

import java.util.List;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.app.adapter.AdpNote;
import sgtmelon.handynotes.app.dataBase.DbRoom;
import sgtmelon.handynotes.app.model.item.ItemNote;
import sgtmelon.handynotes.app.model.item.ItemRoll;
import sgtmelon.handynotes.app.model.repo.RepoNote;
import sgtmelon.handynotes.app.view.act.ActMain;
import sgtmelon.handynotes.app.view.act.ActNote;
import sgtmelon.handynotes.app.view.act.ActSettings;
import sgtmelon.handynotes.app.viewModel.VmFrgBin;
import sgtmelon.handynotes.databinding.FrgNotesBinding;
import sgtmelon.handynotes.element.alert.AlertOption;
import sgtmelon.handynotes.office.Help;
import sgtmelon.handynotes.office.annotation.Db;
import sgtmelon.handynotes.office.annotation.def.DefPages;
import sgtmelon.handynotes.office.annotation.def.db.DefBin;
import sgtmelon.handynotes.office.annotation.def.db.DefCheck;
import sgtmelon.handynotes.office.annotation.def.db.DefType;
import sgtmelon.handynotes.office.interfaces.IntfAlert;
import sgtmelon.handynotes.office.interfaces.IntfItem;

public class FrgNotes extends Fragment implements Toolbar.OnMenuItemClickListener,
        IntfItem.Click, IntfItem.LongClick, IntfAlert.OptionNote {

    //region Variable
    private static final String TAG = "FrgNotes";

    private DbRoom db;

    private FrgNotesBinding binding;
    private View frgView;

    private Context context;
    private ActMain activity;

    private VmFrgBin vm;
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

        vm = ViewModelProviders.of(this).get(VmFrgBin.class);

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

    private AdpNote adapter;

    private void setupRecyclerView() {
        Log.i(TAG, "setupRecyclerView");

        final DefaultItemAnimator recyclerViewEndAnim = new DefaultItemAnimator() {
            @Override
            public void onAnimationFinished(RecyclerView.ViewHolder viewHolder) {
                bind(vm.getListRepo().size());
            }
        };

        RecyclerView recyclerView = frgView.findViewById(R.id.frgNotes_rv);
        recyclerView.setItemAnimator(recyclerViewEndAnim);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AdpNote();
        recyclerView.setAdapter(adapter);

        adapter.setCallback(this, this);
    }

    // FIXME: 05.07.2018 Выдаёт nullPointer при нажатии на видимость категории после поворота экрана

    public void updateAdapter() {
        Log.i(TAG, "updateAdapter");

        List<RepoNote> listRepo = vm.loadData(DefBin.out);

        adapter.update(listRepo);
        adapter.notifyDataSetChanged();

        bind(listRepo.size());
    }

    @Override
    public void onItemClick(View view, int p) {
        Log.i(TAG, "onItemClick");

        ItemNote itemNote = vm.getListRepo().get(p).getItemNote();

        Intent intent = new Intent(context, ActNote.class);

        intent.putExtra(Db.NT_ID, itemNote.getId());
        intent.putExtra(Db.RK_VS, activity.frgRank.vm.getRepoRank().getVisible());
        intent.putExtra(DefPages.CREATE, false);

        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int p) {
        Log.i(TAG, "onItemLongClick");

        AlertOption alertOption = new AlertOption(context, vm.getListRepo().get(p).getItemNote(), p);
        alertOption.setOptionNote(this);
        alertOption.showOptionNote();
    }

    @Override
    public void onOptionCheckClick(ItemNote itemNote, int p, @DefCheck int check, int rollCount) {
        Log.i(TAG, "onOptionCheckClick");

        itemNote.setChange(context);
        itemNote.setText(check == DefCheck.notDone ? 0 : rollCount, rollCount);

        db = DbRoom.provideDb(context);
        db.daoRoll().update(itemNote.getId(), check);
        db.daoNote().update(itemNote);
        db.close();

        List<RepoNote> listRepo = vm.getListRepo();
        RepoNote repoNote = listRepo.get(p);

        repoNote.updateListRoll(check);
        repoNote.setItemNote(itemNote);
        repoNote.updateItemStatus();

        listRepo.set(p, repoNote);
        vm.setListRepo(listRepo);

        adapter.update(listRepo);
        adapter.notifyItemChanged(p);

        activity.frgRank.updateAdapter();
    }

    @Override
    public void onOptionBindClick(ItemNote itemNote, int p) {
        Log.i(TAG, "onOptionBindClick");

        List<RepoNote> listRepo = vm.getListRepo();
        RepoNote repoNote = listRepo.get(p);

        itemNote.setStatus(!itemNote.isStatus());
        repoNote.updateItemStatus(!itemNote.isStatus());

        db = DbRoom.provideDb(context);
        db.daoNote().update(itemNote.getId(), itemNote.isStatus());
        db.close();

        repoNote.setItemNote(itemNote);

        listRepo.set(p, repoNote);
        vm.setListRepo(listRepo);

        adapter.update(listRepo);
        adapter.notifyItemChanged(p);
    }

    @Override
    public void onOptionConvertClick(ItemNote itemNote, int p) {
        Log.i(TAG, "onOptionConvertClick");

        itemNote.setChange(context);

        List<RepoNote> listRepo = vm.getListRepo();
        RepoNote repoNote = listRepo.get(p);

        db = DbRoom.provideDb(context);
        switch (itemNote.getType()) {
            case DefType.text:
                String[] textToRoll = itemNote.getText().split("\n");                             //Получаем пункты списка

                List<ItemRoll> listRoll = db.daoRoll().insert(itemNote.getId(), textToRoll);      //Записываем пункты

                itemNote.setType(DefType.roll);
                itemNote.setText(0, listRoll.size());

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

        listRepo.set(p, repoNote);
        vm.setListRepo(listRepo);

        adapter.update(listRepo);
        adapter.notifyItemChanged(p);

        activity.frgRank.updateAdapter();
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

        List<RepoNote> listRepo = vm.getListRepo();
        listRepo.get(p).updateItemStatus(false);
        listRepo.remove(p);
        vm.setListRepo(listRepo);

        adapter.update(listRepo);
        adapter.notifyItemRemoved(p);

        activity.frgBin.updateAdapter();
    }

}