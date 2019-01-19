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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import sgtmelon.scriptum.app.model.item.RollItem;
import sgtmelon.scriptum.app.view.activity.NoteActivity;
import sgtmelon.scriptum.app.view.activity.PreferenceActivity;
import sgtmelon.scriptum.app.view.callback.MainCallback;
import sgtmelon.scriptum.app.vm.fragment.NotesViewModel;
import sgtmelon.scriptum.databinding.FragmentNotesBinding;
import sgtmelon.scriptum.office.annot.def.BinDef;
import sgtmelon.scriptum.office.annot.def.CheckDef;
import sgtmelon.scriptum.office.annot.def.DialogDef;
import sgtmelon.scriptum.office.annot.def.OptionsDef;
import sgtmelon.scriptum.office.annot.def.TypeNoteDef;
import sgtmelon.scriptum.office.intf.ItemIntf;
import sgtmelon.scriptum.office.intf.MenuIntf;
import sgtmelon.scriptum.office.utils.ColorUtils;
import sgtmelon.scriptum.office.utils.HelpUtils;
import sgtmelon.scriptum.office.utils.TimeUtils;

public final class NotesFragment extends Fragment implements Toolbar.OnMenuItemClickListener,
        ItemIntf.ClickListener, ItemIntf.LongClickListener, MenuIntf.Dialog.NoteMenuClick {

    private static final String TAG = NotesFragment.class.getSimpleName();

    public static boolean updateStatus = true; //Для единовременного обновления статус бара

    @Inject FragmentManager fm;
    @Inject FragmentNotesBinding binding;
    @Inject NotesViewModel vm;
    @Inject OptionsDialog optionsDialog;

    private Context context;
    private MainCallback mainCallback;
    private RoomDb db;
    private View frgView;

    private NoteAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public void onAttach(Context context) {
        Log.i(TAG, "onAttach");
        super.onAttach(context);

        this.context = context;

        if (context instanceof MainCallback) {
            mainCallback = (MainCallback) context;
        } else {
            throw new ClassCastException(MainCallback.class.getSimpleName() +
                    " interface not installed in " + TAG);
        }
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();

        updateAdapter();

        if (updateStatus) updateStatus = false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        final FragmentComponent fragmentComponent = DaggerFragmentComponent.builder()
                .fragmentBlankModule(new FragmentBlankModule(this))
                .fragmentArchModule(new FragmentArchModule(inflater, container))
                .build();
        fragmentComponent.inject(this);

        frgView = binding.getRoot();

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

        final Toolbar toolbar = frgView.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_notes));

        toolbar.inflateMenu(R.menu.fragment_note);
        toolbar.setOnMenuItemClickListener(this);

        final Menu menu = toolbar.getMenu();
        MenuItem mItemSettings = menu.findItem(R.id.preference_item);

        ColorUtils.tintMenuIcon(context, mItemSettings);
    }

    private void setupRecycler() {
        Log.i(TAG, "setupRecycler");

        final DefaultItemAnimator recyclerViewEndAnim = new DefaultItemAnimator() {
            @Override
            public void onAnimationFinished(@NonNull RecyclerView.ViewHolder viewHolder) {
                bind(vm.getListNoteRepo().size());
            }
        };

        recyclerView = frgView.findViewById(R.id.notes_recycler);
        recyclerView.setItemAnimator(recyclerViewEndAnim);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        adapter = new NoteAdapter(context);
        adapter.setClickListener(this);
        adapter.setLongClickListener(this);

        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                mainCallback.changeFabState(dy <= 0);
            }
        });

        optionsDialog.setOnClickListener((dialogInterface, i) -> {
            final int p = optionsDialog.getPosition();
            final NoteItem noteItem = vm.getListNoteRepo().get(p).getNoteItem();

            switch (noteItem.getType()) {
                case TypeNoteDef.text:
                    switch (i) {
                        case OptionsDef.Notes.Text.bind:
                            onMenuBindClick(p);
                            break;
                        case OptionsDef.Notes.Text.convert:
                            onMenuConvertClick(p);
                            break;
                        case OptionsDef.Notes.Text.copy:
                            onMenuCopyClick(p);
                            break;
                        case OptionsDef.Notes.Text.delete:
                            onMenuDeleteClick(p);
                            break;
                    }
                    break;
                case TypeNoteDef.roll:
                    switch (i) {
                        case OptionsDef.Notes.Roll.check:
                            onMenuCheckClick(p);
                            break;
                        case OptionsDef.Notes.Roll.bind:
                            onMenuBindClick(p);
                            break;
                        case OptionsDef.Notes.Roll.convert:
                            onMenuConvertClick(p);
                            break;
                        case OptionsDef.Notes.Roll.copy:
                            onMenuCopyClick(p);
                            break;
                        case OptionsDef.Notes.Roll.delete:
                            onMenuDeleteClick(p);
                            break;
                    }
                    break;
            }
        });
    }

    private void updateAdapter() {
        Log.i(TAG, "updateAdapter");

        final List<NoteRepo> listNoteRepo = vm.loadData(BinDef.out);

        adapter.setList(listNoteRepo);
        adapter.notifyDataSetChanged();

        bind(listNoteRepo.size());
    }

    public void scrollTop() {
        Log.i(TAG, "scrollTop");

        if (recyclerView != null) {
            recyclerView.smoothScrollToPosition(0);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Log.i(TAG, "onMenuItemClick");

        switch (item.getItemId()) {
            case R.id.preference_item:
                startActivity(new Intent(context, PreferenceActivity.class));
                return true;
        }
        return false;
    }

    @Override
    public void onItemClick(@NonNull View view, int p) {
        Log.i(TAG, "onItemClick");

        final long id = vm.getListNoteRepo().get(p).getNoteItem().getId();
        final Intent intent = NoteActivity.getIntent(context, id);

        startActivity(intent);
    }

    @Override
    public void onItemLongClick(@NonNull View view, int p) {
        Log.i(TAG, "onItemLongClick");

        final NoteItem noteItem = vm.getListNoteRepo().get(p).getNoteItem();

        String[] items = new String[0];
        switch (noteItem.getType()) {
            case TypeNoteDef.text:
                items = context.getResources().getStringArray(R.array.dialog_menu_text);

                items[0] = noteItem.isStatus()
                        ? context.getString(R.string.dialog_menu_status_unbind)
                        : context.getString(R.string.dialog_menu_status_bind);
                break;
            case TypeNoteDef.roll:
                items = context.getResources().getStringArray(R.array.dialog_menu_roll);

                items[0] = noteItem.isAllCheck()
                        ? context.getString(R.string.dialog_menu_check_zero)
                        : context.getString(R.string.dialog_menu_check_all);
                items[1] = noteItem.isStatus()
                        ? context.getString(R.string.dialog_menu_status_unbind)
                        : context.getString(R.string.dialog_menu_status_bind);
                break;
        }

        optionsDialog.setArguments(items, p);
        optionsDialog.show(fm, DialogDef.OPTIONS);
    }

    @Override
    public void onMenuCheckClick(int p) {
        Log.i(TAG, "onMenuCheckClick");

        final List<NoteRepo> listNoteRepo = vm.getListNoteRepo();
        final NoteRepo noteRepo = listNoteRepo.get(p);

        final NoteItem noteItem = noteRepo.getNoteItem();

        final int[] checkText = noteItem.getCheck();
        final int check = checkText[0] == checkText[1]
                ? CheckDef.notDone
                : CheckDef.done;

        noteItem.setChange(TimeUtils.getTime(context));
        noteItem.setText(check == CheckDef.notDone
                ? 0
                : checkText[1], checkText[1]);

        db = RoomDb.provideDb(context);
        db.daoRoll().update(noteItem.getId(), check);
        db.daoNote().update(noteItem);
        db.close();

        noteRepo.update(check);
        noteRepo.getStatusItem().updateNote(noteItem, true);

        adapter.setList(listNoteRepo);
        adapter.notifyItemChanged(p);
    }

    @Override
    public void onMenuBindClick(int p) {
        Log.i(TAG, "onMenuBindClick");

        final List<NoteRepo> listNoteRepo = vm.getListNoteRepo();
        final NoteRepo noteRepo = listNoteRepo.get(p);

        final NoteItem noteItem = noteRepo.getNoteItem();
        noteItem.setStatus(!noteItem.isStatus());

        noteRepo.update(noteItem.isStatus());

        db = RoomDb.provideDb(context);
        db.daoNote().update(noteItem.getId(), noteItem.isStatus());
        db.close();

        adapter.setList(listNoteRepo);
        adapter.notifyItemChanged(p);
    }

    @Override
    public void onMenuConvertClick(int p) {
        Log.i(TAG, "onMenuConvertClick");

        final List<NoteRepo> listNoteRepo = vm.getListNoteRepo();
        final NoteRepo noteRepo = listNoteRepo.get(p);

        final NoteItem noteItem = noteRepo.getNoteItem();
        noteItem.setChange(TimeUtils.getTime(context));

        db = RoomDb.provideDb(context);
        switch (noteItem.getType()) {
            case TypeNoteDef.text:
                final String[] textToRoll = noteItem.getText().split("\n");
                final List<RollItem> listRoll = db.daoRoll().insert(noteItem.getId(), textToRoll);

                noteItem.setType(TypeNoteDef.roll);
                noteItem.setText(0, listRoll.size());

                db.daoNote().update(noteItem);

                noteRepo.setListRoll(listRoll);
                break;
            case TypeNoteDef.roll:
                final String rollToText = db.daoRoll().getText(noteItem.getId());

                noteItem.setType(TypeNoteDef.text);
                noteItem.setText(rollToText);

                db.daoNote().update(noteItem);
                db.daoRoll().delete(noteItem.getId());

                noteRepo.setListRoll(new ArrayList<>());
                break;
        }
        db.close();

        noteRepo.getStatusItem().updateNote(noteItem, true);

        adapter.setList(listNoteRepo);
        adapter.notifyItemChanged(p);
    }

    @Override
    public void onMenuCopyClick(int p) {
        Log.i(TAG, "onMenuCopyClick");

        final NoteItem noteItem = vm.getListNoteRepo().get(p).getNoteItem();
        HelpUtils.optionsCopy(context, noteItem);
    }

    @Override
    public void onMenuDeleteClick(int p) {
        Log.i(TAG, "onMenuDeleteClick");

        final List<NoteRepo> listNoteRepo = vm.getListNoteRepo();
        final NoteRepo noteRepo = listNoteRepo.get(p);

        final NoteItem noteItem = noteRepo.getNoteItem();

        db = RoomDb.provideDb(context);
        db.daoNote().update(noteItem.getId(), TimeUtils.getTime(context), true);
        if (noteItem.isStatus()) {
            db.daoNote().update(noteItem.getId(), false);
        }
        db.close();

        noteRepo.update(false);
        listNoteRepo.remove(p);

        adapter.setList(listNoteRepo);
        adapter.notifyItemRemoved(p);
    }

}