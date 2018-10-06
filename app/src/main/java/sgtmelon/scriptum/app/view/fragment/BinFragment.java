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
import sgtmelon.scriptum.app.adapter.NoteAdapter;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.injection.component.DaggerFragmentComponent;
import sgtmelon.scriptum.app.injection.component.FragmentComponent;
import sgtmelon.scriptum.app.injection.module.blank.FragmentBlankModule;
import sgtmelon.scriptum.app.model.NoteModel;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.view.activity.NoteActivity;
import sgtmelon.scriptum.app.vm.NotesViewModel;
import sgtmelon.scriptum.databinding.FragmentBinBinding;
import sgtmelon.scriptum.element.common.MessageDialog;
import sgtmelon.scriptum.element.common.OptionsDialog;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.DialogDef;
import sgtmelon.scriptum.office.annot.def.IntentDef;
import sgtmelon.scriptum.office.annot.def.db.BinDef;
import sgtmelon.scriptum.office.intf.DialogIntf;
import sgtmelon.scriptum.office.intf.ItemIntf;
import sgtmelon.scriptum.office.st.OpenSt;

public final class BinFragment extends Fragment implements ItemIntf.Click, ItemIntf.LongClick,
        DialogIntf.OptionBin {

    private static final String TAG = BinFragment.class.getSimpleName();

    private final OpenSt openSt = new OpenSt();

    private final NoteAdapter adapter = new NoteAdapter();

    @Inject
    FragmentManager fm;

    @Inject
    FragmentBinBinding binding;
    @Inject
    NotesViewModel vm;

    @Inject
    @Named(DialogDef.CLEAR_BIN)
    MessageDialog dlgClearBin;
    @Inject
    OptionsDialog optionsDialog;

    private Context context;
    private RoomDb db;

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

        FragmentComponent fragmentComponent = DaggerFragmentComponent.builder()
                .fragmentBlankModule(new FragmentBlankModule(this, inflater, container))
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
        toolbar.setTitle(getString(R.string.title_frg_bin));

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

        dlgClearBin.setTitle(getString(R.string.dialog_title_clear_bin));
        dlgClearBin.setMessage(getString(R.string.dialog_text_clear_bin));
        dlgClearBin.setPositiveListener((dialogInterface, i) -> {
            db = RoomDb.provideDb(context);
            db.daoNote().clearBin();
            db.close();

            vm.setListModel(new ArrayList<>());

            adapter.setListNoteModel(vm.getListModel());
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

        RecyclerView recyclerView = frgView.findViewById(R.id.bin_recycler);
        recyclerView.setItemAnimator(recyclerViewEndAnim);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter.setClick(this);
        adapter.setLongClick(this);

        recyclerView.setAdapter(adapter);

        optionsDialog.setOnClickListener((dialogInterface, i) -> {
            int p = optionsDialog.getPosition(); // TODO: 06.10.2018 defValues
            switch (i) {
                case 0:
                    onOptionRestoreClick(p);
                    break;
                case 1:
                    onOptionCopyClick(p);
                    break;
                case 2:
                    onOptionClearClick(p);
                    break;
            }
        });
    }

    private void updateAdapter() {
        Log.i(TAG, "updateAdapter");

        List<NoteModel> listNoteModel = vm.loadData(BinDef.in);

        adapter.setListNoteModel(listNoteModel);
        adapter.notifyDataSetChanged();

        mItemClearBin.setVisible(vm.getListModel().size() != 0);
        bind(listNoteModel.size());
    }

    @Override
    public void onItemClick(View view, int p) {
        Log.i(TAG, "onItemClick");

        long id = vm.getListModel().get(p).getNoteItem().getId();

        Intent intent = new Intent(context, NoteActivity.class);
        intent.putExtra(IntentDef.NOTE_CREATE, false);
        intent.putExtra(IntentDef.NOTE_ID, id);

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
    public void onOptionRestoreClick(int p) {
        Log.i(TAG, "onOptionRestoreClick");

        List<NoteModel> listNoteModel = vm.getListModel();
        NoteItem noteItem = listNoteModel.get(p).getNoteItem();

        db = RoomDb.provideDb(context);
        db.daoNote().update(noteItem.getId(), Help.Time.getCurrentTime(context), false);
        db.close();

        listNoteModel.remove(p);
        vm.setListModel(listNoteModel);

        adapter.setListNoteModel(listNoteModel);
        adapter.notifyItemRemoved(p);

        mItemClearBin.setVisible(vm.getListModel().size() != 0);
    }

    @Override
    public void onOptionCopyClick(int p) {
        NoteItem noteItem = vm.getListModel().get(p).getNoteItem();
        Help.optionsCopy(context, noteItem);
    }

    @Override
    public void onOptionClearClick(int p) {
        Log.i(TAG, "onOptionClearClick");

        List<NoteModel> listNoteModel = vm.getListModel();
        NoteItem noteItem = listNoteModel.get(p).getNoteItem();

        db = RoomDb.provideDb(context);
        db.daoNote().delete(noteItem.getId());
        db.close();

        listNoteModel.remove(p);
        vm.setListModel(listNoteModel);

        adapter.setListNoteModel(listNoteModel);
        adapter.notifyItemRemoved(p);

        mItemClearBin.setVisible(vm.getListModel().size() != 0);
    }

}