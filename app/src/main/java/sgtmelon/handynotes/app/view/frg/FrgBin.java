package sgtmelon.handynotes.app.view.frg;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import sgtmelon.handynotes.R;
import sgtmelon.handynotes.app.adapter.AdpNote;
import sgtmelon.handynotes.app.dataBase.DbRoom;
import sgtmelon.handynotes.app.model.item.ItemNote;
import sgtmelon.handynotes.app.model.repo.RepoNote;
import sgtmelon.handynotes.app.view.act.ActNote;
import sgtmelon.handynotes.app.viewModel.VmFrgBin;
import sgtmelon.handynotes.databinding.FrgBinBinding;
import sgtmelon.handynotes.element.dialog.DialogClearBin;
import sgtmelon.handynotes.element.dialog.DialogOptionBin;
import sgtmelon.handynotes.office.Help;
import sgtmelon.handynotes.office.annot.Db;
import sgtmelon.handynotes.office.annot.Frg;
import sgtmelon.handynotes.office.annot.def.DefPage;
import sgtmelon.handynotes.office.annot.def.db.DefBin;
import sgtmelon.handynotes.office.intf.IntfDialog;
import sgtmelon.handynotes.office.intf.IntfItem;

public class FrgBin extends Fragment implements
        IntfItem.Click, IntfItem.LongClick, IntfDialog.OptionBin {

    //region Variable
    private static final String TAG = "FrgBin";

    private DbRoom db;

    private Context context;

    private FrgBinBinding binding;
    private View frgView;

    private VmFrgBin vm;
    //endregion

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach");
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        binding = DataBindingUtil.inflate(inflater, R.layout.frg_bin, container, false);
        frgView = binding.getRoot();

        return frgView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated");

        vm = ViewModelProviders.of(this).get(VmFrgBin.class);

        setupToolbar();
        setupRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");

        updateAdapter();
    }

    private void bind(int listSize) {
        binding.setListEmpty(listSize == 0);
        binding.executePendingBindings();
    }

    private MenuItem mItemClearBin;
    private DialogClearBin clearBin;

    private void setupToolbar() {
        Log.i(TAG, "setupToolbar");

        FragmentManager fm = getFragmentManager();

        Toolbar toolbar = frgView.findViewById(R.id.incToolbar_tb);
        toolbar.setTitle(getString(R.string.title_frg_bin));

        toolbar.inflateMenu(R.menu.menu_frg_bin);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_frgBin_clear:
                    if (!clearBin.isVisible()) clearBin.show(fm, Frg.CLEAR_BIN);
                    return true;
            }
            return false;
        });

        Menu menu = toolbar.getMenu();
        mItemClearBin = menu.findItem(R.id.menu_frgBin_clear);

        Help.Icon.tintMenuIcon(context, mItemClearBin);

        clearBin = (DialogClearBin) fm.findFragmentByTag(Frg.CLEAR_BIN);
        if (clearBin == null) clearBin = new DialogClearBin();

        clearBin.setPositiveButton((dialogInterface, i) -> {
            db = DbRoom.provideDb(context);
            db.daoNote().clearBin();
            db.close();

            vm.setListRepo();

            adapter.update(vm.getListRepo());
            adapter.notifyDataSetChanged();

            setMenuItemClearVisible();
            bind(0);
        });
    }

    private void setMenuItemClearVisible() {
        Log.i(TAG, "setMenuItemClearVisible");

        if (vm.getListRepo().size() == 0) mItemClearBin.setVisible(false);
        else mItemClearBin.setVisible(true);
    }

    private AdpNote adapter;
    private DialogOptionBin optionBin;

    private void setupRecyclerView() {
        Log.i(TAG, "setupRecyclerView");

        final DefaultItemAnimator recyclerViewEndAnim = new DefaultItemAnimator() {
            @Override
            public void onAnimationFinished(RecyclerView.ViewHolder viewHolder) {
                bind(vm.getListRepo().size());
            }
        };

        RecyclerView recyclerView = frgView.findViewById(R.id.frgBin_rv);
        recyclerView.setItemAnimator(recyclerViewEndAnim);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AdpNote();
        recyclerView.setAdapter(adapter);

        adapter.setCallback(this, this);

        FragmentManager fm = getFragmentManager();

        optionBin = (DialogOptionBin) fm.findFragmentByTag(Frg.OPTIONS);
        if (optionBin == null) optionBin = new DialogOptionBin();
        optionBin.setOptionBin(this);
    }

    private void updateAdapter() {
        Log.i(TAG, "updateAdapter");

        List<RepoNote> listRepo = vm.loadData(DefBin.in);

        adapter.update(listRepo);
        adapter.notifyDataSetChanged();

        setMenuItemClearVisible();
        bind(listRepo.size());
    }

    @Override
    public void onItemClick(View view, int p) {
        Log.i(TAG, "onItemClick");

        ItemNote itemNote = vm.getListRepo().get(p).getItemNote();

        Intent intent = new Intent(context, ActNote.class);

        intent.putExtra(Db.NT_ID, itemNote.getId());
        intent.putExtra(DefPage.CREATE, false);

        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int p) {
        Log.i(TAG, "onItemLongClick");

        optionBin.setArguments(p);
        optionBin.show(getFragmentManager(), Frg.OPTIONS);
    }

    @Override
    public void onOptionRestoreClick(int p) {
        Log.i(TAG, "onOptionRestoreClick");

        List<RepoNote> listRepo = vm.getListRepo();
        ItemNote itemNote = listRepo.get(p).getItemNote();

        db = DbRoom.provideDb(context);
        db.daoNote().update(itemNote.getId(), Help.Time.getCurrentTime(context), false);
        db.close();

        listRepo.remove(p);
        vm.setListRepo(listRepo);

        adapter.update(listRepo);
        adapter.notifyItemRemoved(p);

        setMenuItemClearVisible();
    }

    @Override
    public void onOptionCopyClick(int p) {
        ItemNote itemNote = vm.getListRepo().get(p).getItemNote();
        Help.optionsCopy(context, itemNote);
    }

    @Override
    public void onOptionClearClick(int p) {
        Log.i(TAG, "onOptionClearClick");

        List<RepoNote> listRepo = vm.getListRepo();
        ItemNote itemNote = listRepo.get(p).getItemNote();

        db = DbRoom.provideDb(context);
        db.daoNote().delete(itemNote.getId());
        db.close();

        listRepo.remove(p);
        vm.setListRepo(listRepo);

        adapter.update(listRepo);
        adapter.notifyItemRemoved(p);

        setMenuItemClearVisible();
    }

}