package sgtmelon.scriptum.app.view.frg;

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
import javax.inject.Named;

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
import sgtmelon.scriptum.app.database.DbRoom;
import sgtmelon.scriptum.app.injection.component.ComFrg;
import sgtmelon.scriptum.app.injection.component.DaggerComFrg;
import sgtmelon.scriptum.app.injection.module.ModBlankFrg;
import sgtmelon.scriptum.app.model.item.ItemNote;
import sgtmelon.scriptum.app.model.repo.RepoNote;
import sgtmelon.scriptum.app.view.act.ActNote;
import sgtmelon.scriptum.app.viewModel.VmFrgNotes;
import sgtmelon.scriptum.databinding.FrgBinBinding;
import sgtmelon.scriptum.element.dialog.DlgOptionBin;
import sgtmelon.scriptum.element.dialog.common.DlgMessage;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.DefDlg;
import sgtmelon.scriptum.office.annot.def.DefIntent;
import sgtmelon.scriptum.office.annot.def.db.DefBin;
import sgtmelon.scriptum.office.intf.IntfDialog;
import sgtmelon.scriptum.office.intf.IntfItem;
import sgtmelon.scriptum.office.st.StNote;
import sgtmelon.scriptum.office.st.StOpen;

public class FrgBin extends Fragment implements IntfItem.Click, IntfItem.LongClick, IntfDialog.OptionBin {

    private static final String TAG = FrgBin.class.getSimpleName();

    public RecyclerView recyclerView;

    @Inject
    Context context;
    @Inject
    FragmentManager fm;

    @Inject
    FrgBinBinding binding;
    @Inject
    VmFrgNotes vm;

    @Inject
    StOpen stOpen;
    @Inject
    @Named(DefDlg.CLEAR_BIN)
    DlgMessage dlgClearBin;

    @Inject
    AdpNote adapter;
    @Inject
    DlgOptionBin dlgOptionBin;

    private DbRoom db;

    private View frgView;
    private MenuItem mItemClearBin;

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

        ComFrg comFrg = DaggerComFrg.builder()
                .modBlankFrg(new ModBlankFrg(this, inflater, container))
                .build();
        comFrg.inject(this);

        frgView = binding.getRoot();

        if (savedInstanceState != null) {
            stOpen = savedInstanceState.getParcelable(DefIntent.STATE_OPEN);
        }

        setupToolbar();
        setupRecycler();

        return frgView;
    }

    private void bind(int listSize) {
        binding.setListEmpty(listSize == 0);
        binding.executePendingBindings();
    }

    private void setupToolbar() {
        Log.i(TAG, "setupToolbar");

        Toolbar toolbar = frgView.findViewById(R.id.incToolbar_tb);
        toolbar.setTitle(getString(R.string.title_frg_bin));

        toolbar.inflateMenu(R.menu.menu_frg_bin);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_frgBin_clear:
                    if (stOpen.isNotOpen()) {
                        stOpen.setOpen(true);

                        dlgClearBin.show(fm, DefDlg.CLEAR_BIN);
                    }
                    return true;
            }
            return false;
        });

        Menu menu = toolbar.getMenu();
        mItemClearBin = menu.findItem(R.id.menu_frgBin_clear);

        Help.Tint.menuIcon(context, mItemClearBin);

        dlgClearBin.setTitle(getString(R.string.dialog_title_clear_bin));
        dlgClearBin.setMessage(getString(R.string.dialog_text_clear_bin));
        dlgClearBin.setPositiveListener((dialogInterface, i) -> {
            db = DbRoom.provideDb(context);
            db.daoNote().clearBin();
            db.close();

            vm.setListRepo(null);

            adapter.setListRepo(vm.getListRepo());
            adapter.notifyDataSetChanged();

            mItemClearBin.setVisible(vm.getListRepo().size() != 0);
            bind(0);
        });
        dlgClearBin.setDismissListener(dialogInterface -> stOpen.setOpen(false));
    }

    private void setupRecycler() {
        Log.i(TAG, "setupRecycler");

        final DefaultItemAnimator recyclerViewEndAnim = new DefaultItemAnimator() {
            @Override
            public void onAnimationFinished(@NonNull RecyclerView.ViewHolder viewHolder) {
                bind(vm.getListRepo().size());
            }
        };

        recyclerView = frgView.findViewById(R.id.frgBin_rv);
        recyclerView.setItemAnimator(recyclerViewEndAnim);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter.setClick(this);
        adapter.setLongClick(this);

        recyclerView.setAdapter(adapter);

        dlgOptionBin.setOptionBin(this);
    }

    private void updateAdapter() {
        Log.i(TAG, "updateAdapter");

        List<RepoNote> listRepo = vm.loadData(DefBin.in);

        adapter.setListRepo(listRepo);
        adapter.notifyDataSetChanged();

        mItemClearBin.setVisible(vm.getListRepo().size() != 0);
        bind(listRepo.size());
    }

    @Override
    public void onItemClick(View view, int p) {
        Log.i(TAG, "onItemClick");

        long ntId = vm.getListRepo().get(p).getItemNote().getId();

        Intent intent = new Intent(context, ActNote.class);
        intent.putExtra(DefIntent.STATE_NOTE, new StNote(false, true));
        intent.putExtra(DefIntent.NOTE_ID, ntId);

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

        adapter.setListRepo(listRepo);
        adapter.notifyItemRemoved(p);

        mItemClearBin.setVisible(vm.getListRepo().size() != 0);
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

        adapter.setListRepo(listRepo);
        adapter.notifyItemRemoved(p);

        mItemClearBin.setVisible(vm.getListRepo().size() != 0);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.i(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);

        outState.putParcelable(DefIntent.STATE_OPEN, stOpen);
    }
}