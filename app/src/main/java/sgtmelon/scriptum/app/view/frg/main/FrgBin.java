package sgtmelon.scriptum.app.view.frg.main;

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

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.adapter.AdpNote;
import sgtmelon.scriptum.app.dataBase.DbRoom;
import sgtmelon.scriptum.app.model.item.ItemNote;
import sgtmelon.scriptum.app.model.repo.RepoNote;
import sgtmelon.scriptum.app.view.act.ActNote;
import sgtmelon.scriptum.app.viewModel.VmFrgNotes;
import sgtmelon.scriptum.dagger.frg.ComFrg;
import sgtmelon.scriptum.dagger.frg.DaggerComFrg;
import sgtmelon.scriptum.dagger.frg.ModFrg;
import sgtmelon.scriptum.databinding.FrgBinBinding;
import sgtmelon.scriptum.element.dialog.DlgOptionBin;
import sgtmelon.scriptum.element.dialog.common.DlgMessage;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.DefDlg;
import sgtmelon.scriptum.office.annot.def.DefNote;
import sgtmelon.scriptum.office.annot.def.db.DefBin;
import sgtmelon.scriptum.office.intf.IntfDialog;
import sgtmelon.scriptum.office.intf.IntfItem;
import sgtmelon.scriptum.office.st.StOpen;

public class FrgBin extends Fragment implements IntfItem.Click, IntfItem.LongClick, IntfDialog.OptionBin {

    //region Variable
    private static final String TAG = "FrgBin";

    private DbRoom db;

    @Inject
    Context context;
    @Inject
    FragmentManager fm;

    @Inject
    FrgBinBinding binding;
    @Inject
    VmFrgNotes vm;

    private View frgView;
    //endregion

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        ComFrg comFrg = DaggerComFrg.builder().modFrg(new ModFrg(this, inflater, container)).build();
        comFrg.inject(this);

        frgView = binding.getRoot();

        if (savedInstanceState != null) stOpen.setOpen(savedInstanceState.getBoolean(DefDlg.OPEN));

        setupToolbar();
        setupRecycler();

        return frgView;
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

    @Inject
    StOpen stOpen;
    @Inject
    DlgMessage dlgClearBin;

    private void setupToolbar() {
        Log.i(TAG, "setupToolbar");

        Toolbar toolbar = frgView.findViewById(R.id.incToolbar_tb);
        toolbar.setTitle(getString(R.string.title_frg_bin));

        toolbar.inflateMenu(R.menu.menu_frg_bin);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_frgBin_clear:
                    if (!stOpen.isOpen()) {
                        stOpen.setOpen();

                        dlgClearBin.show(fm, DefDlg.MESSAGE);
                    }
                    return true;
            }
            return false;
        });

        Menu menu = toolbar.getMenu();
        mItemClearBin = menu.findItem(R.id.menu_frgBin_clear);

        Help.Tint.menuIcon(context, mItemClearBin);

//        dlgClearBin = (DlgMessage) fm.findFragmentByTag(DefDlg.MESSAGE);
//        if (dlgClearBin == null) dlgClearBin = new DlgMessage();

        dlgClearBin.setTitle(getString(R.string.dialog_title_clear_bin));
        dlgClearBin.setMessage(getString(R.string.dialog_text_clear_bin));
        dlgClearBin.setPositiveListener((dialogInterface, i) -> {
            db = DbRoom.provideDb(context);
            db.daoNote().clearBin();
            db.close();

            vm.clearListRepo();

            adapter.update(vm.getListRepo());
            adapter.notifyDataSetChanged();

            setMenuItemClearVisible();
            bind(0);
        });
        dlgClearBin.setDismissListener(dialogInterface -> stOpen.setOpen(false));
    }

    private void setMenuItemClearVisible() {
        Log.i(TAG, "setMenuItemClearVisible");

        if (vm.getListRepo().size() == 0) mItemClearBin.setVisible(false);
        else mItemClearBin.setVisible(true);
    }

    @Inject
    AdpNote adapter;
    @Inject
    DlgOptionBin dlgOptionBin;

    private void setupRecycler() {
        Log.i(TAG, "setupRecycler");

        final DefaultItemAnimator recyclerViewEndAnim = new DefaultItemAnimator() {
            @Override
            public void onAnimationFinished(@NonNull RecyclerView.ViewHolder viewHolder) {
                bind(vm.getListRepo().size());
            }
        };

        RecyclerView recyclerView = frgView.findViewById(R.id.frgBin_rv);
        recyclerView.setItemAnimator(recyclerViewEndAnim);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

//        adapter = new AdpNote();
        adapter.setCallback(this, this);

        recyclerView.setAdapter(adapter);

//        dlgOptionBin = (DlgOptionBin) fm.findFragmentByTag(DefDlg.OPTIONS);
//        if (dlgOptionBin == null) dlgOptionBin = new DlgOptionBin();

        dlgOptionBin.setOptionBin(this);
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

        intent.putExtra(DefNote.CREATE, false);
        intent.putExtra(DefNote.ID, itemNote.getId());

        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int p) {
        Log.i(TAG, "onItemLongClick");

        dlgOptionBin.setArguments(p);
        dlgOptionBin.show(fm, DefDlg.OPTIONS);
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(DefDlg.OPEN, stOpen.isOpen());
    }

}