package sgtmelon.scriptum.app.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
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
import sgtmelon.scriptum.app.adapter.AdapterNote;
import sgtmelon.scriptum.app.database.DbRoom;
import sgtmelon.scriptum.app.injection.component.ComponentFragment;
import sgtmelon.scriptum.app.injection.component.DaggerComponentFragment;
import sgtmelon.scriptum.app.injection.module.blank.ModuleBlankFragment;
import sgtmelon.scriptum.app.model.ModelNote;
import sgtmelon.scriptum.app.model.item.ItemNote;
import sgtmelon.scriptum.app.view.activity.NoteActivity;
import sgtmelon.scriptum.app.vm.NotesViewModel;
import sgtmelon.scriptum.databinding.FragmentBinBinding;
import sgtmelon.scriptum.element.DlgOptionBin;
import sgtmelon.scriptum.element.common.DlgMessage;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.DefDlg;
import sgtmelon.scriptum.office.annot.def.DefIntent;
import sgtmelon.scriptum.office.annot.def.db.DefBin;
import sgtmelon.scriptum.office.intf.IntfDialog;
import sgtmelon.scriptum.office.intf.IntfItem;
import sgtmelon.scriptum.office.st.StOpen;

public final class BinFragment extends Fragment implements IntfItem.Click, IntfItem.LongClick,
        IntfDialog.OptionBin {

    private static final String TAG = BinFragment.class.getSimpleName();

    private final StOpen stOpen = new StOpen();

    private final AdapterNote adapter = new AdapterNote();

    @Inject
    FragmentManager fm;

    @Inject
    FragmentBinBinding binding;
    @Inject
    NotesViewModel vm;

    @Inject
    @Named(DefDlg.CLEAR_BIN)
    DlgMessage dlgClearBin;
    @Inject
    DlgOptionBin dlgOptionBin;

    private Context context;
    private DbRoom db;

    private View frgView;
    private MenuItem mItemClearBin;

    @Override
    public void onAttach(Context context) {
        Log.i(TAG, "onAttach");
        super.onAttach(context);

        this.context = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");

        updateAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        ComponentFragment componentFragment = DaggerComponentFragment.builder()
                .moduleBlankFragment(new ModuleBlankFragment(this, inflater, container))
                .build();
        componentFragment.inject(this);

        frgView = binding.getRoot();

        if (savedInstanceState != null) {
            stOpen.setOpen(savedInstanceState.getBoolean(DefIntent.STATE_OPEN));
        }

        setupToolbar();
        setupRecycler();

        return frgView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.i(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);

        outState.putBoolean(DefIntent.STATE_OPEN, stOpen.isOpen());
    }

    private void bind(int listSize) {
        binding.setListEmpty(listSize == 0);
        binding.executePendingBindings();
    }

    private void setupToolbar() {
        Log.i(TAG, "setupToolbar");

        Toolbar toolbar = frgView.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_frg_bin));

        toolbar.inflateMenu(R.menu.menu_fragment_bin);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.clear_item:
                    if (!stOpen.isOpen()) {
                        stOpen.setOpen(true);

                        dlgClearBin.show(fm, DefDlg.CLEAR_BIN);
                    }
                    return true;
            }
            return false;
        });

        Menu menu = toolbar.getMenu();
        mItemClearBin = menu.findItem(R.id.clear_item);

        Help.Tint.menuIcon(context, mItemClearBin);

        dlgClearBin.setTitle(getString(R.string.dialog_title_clear_bin));
        dlgClearBin.setMessage(getString(R.string.dialog_text_clear_bin));
        dlgClearBin.setPositiveListener((dialogInterface, i) -> {
            db = DbRoom.provideDb(context);
            db.daoNote().clearBin();
            db.close();

            vm.setListModelNote(new ArrayList<>());

            adapter.setListModelNote(vm.getListModelNote());
            adapter.notifyDataSetChanged();

            mItemClearBin.setVisible(vm.getListModelNote().size() != 0);
            bind(0);
        });
        dlgClearBin.setDismissListener(dialogInterface -> stOpen.setOpen(false));
    }

    private void setupRecycler() {
        Log.i(TAG, "setupRecycler");

        final DefaultItemAnimator recyclerViewEndAnim = new DefaultItemAnimator() {
            @Override
            public void onAnimationFinished(@NonNull RecyclerView.ViewHolder viewHolder) {
                bind(vm.getListModelNote().size());
            }
        };

        RecyclerView recyclerView = frgView.findViewById(R.id.bin_recycler);
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

        List<ModelNote> listModelNote = vm.loadData(DefBin.in);

        adapter.setListModelNote(listModelNote);
        adapter.notifyDataSetChanged();

        mItemClearBin.setVisible(vm.getListModelNote().size() != 0);
        bind(listModelNote.size());
    }

    @Override
    public void onItemClick(View view, int p) {
        Log.i(TAG, "onItemClick");

        long id = vm.getListModelNote().get(p).getItemNote().getId();

        Intent intent = new Intent(context, NoteActivity.class);
        intent.putExtra(DefIntent.NOTE_CREATE, false);
        intent.putExtra(DefIntent.NOTE_ID, id);

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

        List<ModelNote> listModelNote = vm.getListModelNote();
        ItemNote itemNote = listModelNote.get(p).getItemNote();

        db = DbRoom.provideDb(context);
        db.daoNote().update(itemNote.getId(), Help.Time.getCurrentTime(context), false);
        db.close();

        listModelNote.remove(p);
        vm.setListModelNote(listModelNote);

        adapter.setListModelNote(listModelNote);
        adapter.notifyItemRemoved(p);

        mItemClearBin.setVisible(vm.getListModelNote().size() != 0);
    }

    @Override
    public void onOptionCopyClick(int p) {
        ItemNote itemNote = vm.getListModelNote().get(p).getItemNote();
        Help.optionsCopy(context, itemNote);
    }

    @Override
    public void onOptionClearClick(int p) {
        Log.i(TAG, "onOptionClearClick");

        List<ModelNote> listModelNote = vm.getListModelNote();
        ItemNote itemNote = listModelNote.get(p).getItemNote();

        db = DbRoom.provideDb(context);
        db.daoNote().delete(itemNote.getId());
        db.close();

        listModelNote.remove(p);
        vm.setListModelNote(listModelNote);

        adapter.setListModelNote(listModelNote);
        adapter.notifyItemRemoved(p);

        mItemClearBin.setVisible(vm.getListModelNote().size() != 0);
    }

}