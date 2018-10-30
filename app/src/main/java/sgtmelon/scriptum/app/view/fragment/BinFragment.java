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
import sgtmelon.safedialog.library.MessageDialog;
import sgtmelon.safedialog.library.OptionsDialog;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.adapter.NoteAdapter;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.injection.component.DaggerFragmentComponent;
import sgtmelon.scriptum.app.injection.component.FragmentComponent;
import sgtmelon.scriptum.app.injection.module.FragmentArchModule;
import sgtmelon.scriptum.app.injection.module.blank.FragmentBlankModule;
import sgtmelon.scriptum.app.model.NoteRepo;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.view.activity.NoteActivity;
import sgtmelon.scriptum.app.vm.fragment.NotesViewModel;
import sgtmelon.scriptum.databinding.FragmentBinBinding;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.DialogDef;
import sgtmelon.scriptum.office.annot.def.IntentDef;
import sgtmelon.scriptum.office.annot.def.OptionsDef;
import sgtmelon.scriptum.office.annot.def.StateDef;
import sgtmelon.scriptum.office.intf.ItemIntf;
import sgtmelon.scriptum.office.intf.MenuIntf;
import sgtmelon.scriptum.office.st.OpenSt;

public final class BinFragment extends Fragment implements ItemIntf.ClickListener,
        ItemIntf.LongClickListener, MenuIntf.Dialog.DeleteMenuClick {

    private static final String TAG = BinFragment.class.getSimpleName();

    private final OpenSt openSt = new OpenSt();

    @Inject FragmentManager fm;
    @Inject FragmentBinBinding binding;
    @Inject NotesViewModel vm;
    @Inject OptionsDialog optionsDialog;

    @Inject
    @Named(DialogDef.CLEAR_BIN)
    MessageDialog dlgClearBin;

    private NoteAdapter adapter;
    private Context context;
    private RoomDb db;
    private View frgView;

    private MenuItem mItemClearBin;
    private RecyclerView recyclerView;

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

        FragmentComponent fragmentComponent = DaggerFragmentComponent.builder()
                .fragmentBlankModule(new FragmentBlankModule(this))
                .fragmentArchModule(new FragmentArchModule(inflater, container))
                .build();
        fragmentComponent.inject(this);

        frgView = binding.getRoot();

        if (savedInstanceState != null) {
            openSt.setOpen(savedInstanceState.getBoolean(IntentDef.STATE_OPEN));
        }

        setupToolbar();
        setupRecycler();

        return frgView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.i(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);

        outState.putBoolean(IntentDef.STATE_OPEN, openSt.isOpen());
    }

    private void bind(int listSize) {
        binding.setListEmpty(listSize == 0);
        binding.executePendingBindings();
    }

    private void setupToolbar() {
        Log.i(TAG, "setupToolbar");

        Toolbar toolbar = frgView.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_bin));

        toolbar.inflateMenu(R.menu.fragment_bin);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.clear_item:
                    if (!openSt.isOpen()) {
                        openSt.setOpen(true);

                        dlgClearBin.show(fm, DialogDef.CLEAR_BIN);
                    }
                    return true;
            }
            return false;
        });

        Menu menu = toolbar.getMenu();
        mItemClearBin = menu.findItem(R.id.clear_item);

        Help.Tint.menuIcon(context, mItemClearBin);

        dlgClearBin.setPositiveListener((dialogInterface, i) -> {
            db = RoomDb.provideDb(context);
            db.daoNote().clearBin();
            db.close();

            vm.setListModel(new ArrayList<>());

            adapter.setList(vm.getListModel());
            adapter.notifyDataSetChanged();

            mItemClearBin.setVisible(vm.getListModel().size() != 0);
            bind(0);
        });
        dlgClearBin.setDismissListener(dialogInterface -> openSt.setOpen(false));
    }

    private void setupRecycler() {
        Log.i(TAG, "setupRecycler");

        final DefaultItemAnimator recyclerViewEndAnim = new DefaultItemAnimator() {
            @Override
            public void onAnimationFinished(@NonNull RecyclerView.ViewHolder viewHolder) {
                bind(vm.getListModel().size());
            }
        };

        recyclerView = frgView.findViewById(R.id.bin_recycler);
        recyclerView.setItemAnimator(recyclerViewEndAnim);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new NoteAdapter(context);

        adapter.setClickListener(this);
        adapter.setLongClickListener(this);

        recyclerView.setAdapter(adapter);

        optionsDialog.setOnClickListener((dialogInterface, i) -> {
            int p = optionsDialog.getPosition();
            switch (i) {
                case OptionsDef.Bin.restore:
                    onMenuRestoreClick(p);
                    break;
                case OptionsDef.Bin.copy:
                    onMenuCopyClick(p);
                    break;
                case OptionsDef.Bin.clear:
                    onMenuClearClick(p);
                    break;
            }
        });
    }

    private void updateAdapter() {
        Log.i(TAG, "updateAdapter");

        List<NoteRepo> listNoteRepo = vm.loadData(StateDef.Bin.in);

        adapter.setList(listNoteRepo);
        adapter.notifyDataSetChanged();

        mItemClearBin.setVisible(vm.getListModel().size() != 0);
        bind(listNoteRepo.size());
    }

    public void scrollTop(){
        Log.i(TAG, "scrollTop");

        if (recyclerView != null){
            recyclerView.smoothScrollToPosition(0);
        }
    }

    @Override
    public void onItemClick(View view, int p) {
        Log.i(TAG, "onItemClick");

        long id = vm.getListModel().get(p).getNoteItem().getId();
        Intent intent = NoteActivity.getIntent(context, id);

        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int p) {
        Log.i(TAG, "onItemLongClick");

        String[] items = context.getResources().getStringArray(R.array.dialog_menu_bin);

        optionsDialog.setArguments(items, p);
        optionsDialog.show(fm, DialogDef.OPTIONS);
    }

    @Override
    public void onMenuRestoreClick(int p) {
        Log.i(TAG, "onMenuRestoreClick");

        List<NoteRepo> listNoteRepo = vm.getListModel();
        NoteItem noteItem = listNoteRepo.get(p).getNoteItem();

        db = RoomDb.provideDb(context);
        db.daoNote().update(noteItem.getId(), Help.Time.getCurrentTime(context), false);
        db.close();

        listNoteRepo.remove(p);
        vm.setListModel(listNoteRepo);

        adapter.setList(listNoteRepo);
        adapter.notifyItemRemoved(p);

        mItemClearBin.setVisible(vm.getListModel().size() != 0);
    }

    @Override
    public void onMenuCopyClick(int p) {
        NoteItem noteItem = vm.getListModel().get(p).getNoteItem();
        Help.optionsCopy(context, noteItem);
    }

    @Override
    public void onMenuClearClick(int p) {
        Log.i(TAG, "onMenuClearClick");

        List<NoteRepo> listNoteRepo = vm.getListModel();
        NoteItem noteItem = listNoteRepo.get(p).getNoteItem();

        db = RoomDb.provideDb(context);
        db.daoNote().delete(noteItem.getId());
        db.close();

        listNoteRepo.remove(p);
        vm.setListModel(listNoteRepo);

        adapter.setList(listNoteRepo);
        adapter.notifyItemRemoved(p);

        mItemClearBin.setVisible(vm.getListModel().size() != 0);
    }

}