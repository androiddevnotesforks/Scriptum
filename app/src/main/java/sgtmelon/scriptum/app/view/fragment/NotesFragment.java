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
import sgtmelon.scriptum.app.model.NoteModel;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.model.item.RollItem;
import sgtmelon.scriptum.app.view.MainView;
import sgtmelon.scriptum.app.view.activity.NoteActivity;
import sgtmelon.scriptum.app.view.activity.PreferenceActivity;
import sgtmelon.scriptum.app.vm.fragment.NotesViewModel;
import sgtmelon.scriptum.databinding.FragmentNotesBinding;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.DialogDef;
import sgtmelon.scriptum.office.annot.def.OptionsDef;
import sgtmelon.scriptum.office.annot.def.db.BinDef;
import sgtmelon.scriptum.office.annot.def.db.CheckDef;
import sgtmelon.scriptum.office.annot.def.db.TypeDef;
import sgtmelon.scriptum.office.intf.ItemIntf;
import sgtmelon.scriptum.office.intf.MenuIntf;

public final class NotesFragment extends Fragment implements Toolbar.OnMenuItemClickListener,
        ItemIntf.ClickListener, ItemIntf.LongClickListener, MenuIntf.Dialog.NoteMenuClick {

    private static final String TAG = NotesFragment.class.getSimpleName();

    public static boolean updateStatus = true; //Для единовременного обновления статус бара

    @Inject FragmentManager fm;
    @Inject FragmentNotesBinding binding;
    @Inject NotesViewModel vm;
    @Inject OptionsDialog optionsDialog;

    private Context context;
    private MainView mainView;
    private RoomDb db;
    private View frgView;
    private NoteAdapter adapter;

    @Override
    public void onAttach(Context context) {
        Log.i(TAG, "onAttach");
        super.onAttach(context);

        this.context = context;

        if (context instanceof MainView) {
            mainView = (MainView) context;
        } else {
            throw new IllegalStateException("MainView interface not installed");
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

        FragmentComponent fragmentComponent = DaggerFragmentComponent.builder()
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

        Toolbar toolbar = frgView.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_notes));

        toolbar.inflateMenu(R.menu.fragment_note);
        toolbar.setOnMenuItemClickListener(this);

        Menu menu = toolbar.getMenu();
        MenuItem mItemSettings = menu.findItem(R.id.preference_item);

        Help.Tint.menuIcon(context, mItemSettings);
    }

    private void setupRecycler() {
        Log.i(TAG, "setupRecycler");

        final DefaultItemAnimator recyclerViewEndAnim = new DefaultItemAnimator() {
            @Override
            public void onAnimationFinished(@NonNull RecyclerView.ViewHolder viewHolder) {
                bind(vm.getListModel().size());
            }
        };

        RecyclerView recyclerView = frgView.findViewById(R.id.notes_recycler);
        recyclerView.setItemAnimator(recyclerViewEndAnim);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new NoteAdapter(context);

        adapter.setClickListener(this);
        adapter.setLongClickListener(this);

        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                mainView.changeFabState(dy <= 0);
            }
        });

        optionsDialog.setOnClickListener((dialogInterface, i) -> {
            int p = optionsDialog.getPosition();
            NoteItem noteItem = vm.getListModel().get(p).getNoteItem();

            switch (noteItem.getType()) {
                case TypeDef.text:
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
                case TypeDef.roll:
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

        List<NoteModel> listNoteModel = vm.loadData(BinDef.out);

        adapter.setList(listNoteModel);
        adapter.notifyDataSetChanged();

        bind(listNoteModel.size());
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Log.i(TAG, "onMenuItemClick");

        switch (item.getItemId()) {
            case R.id.preference_item:
                Intent intent = new Intent(context, PreferenceActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
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

        NoteItem noteItem = vm.getListModel().get(p).getNoteItem();

        String[] items = new String[0];
        switch (noteItem.getType()) {
            case TypeDef.text:
                items = context.getResources().getStringArray(R.array.dialog_menu_text);

                items[0] = noteItem.isStatus()
                        ? context.getString(R.string.dialog_menu_status_unbind)
                        : context.getString(R.string.dialog_menu_status_bind);
                break;
            case TypeDef.roll:
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

        List<NoteModel> listNoteModel = vm.getListModel();
        NoteModel noteModel = listNoteModel.get(p);

        NoteItem noteItem = noteModel.getNoteItem();

        int[] checkText = noteItem.getCheck();
        int check = checkText[0] == checkText[1] ? CheckDef.notDone : CheckDef.done;

        noteItem.setChange(Help.Time.getCurrentTime(context));
        noteItem.setText(check == CheckDef.notDone ? 0 : checkText[1], checkText[1]);

        db = RoomDb.provideDb(context);
        db.daoRoll().update(noteItem.getId(), check);
        db.daoNote().update(noteItem);
        db.close();

        noteModel.updateListRoll(check);
        noteModel.setNoteItem(noteItem);
        noteModel.updateItemStatus();

        listNoteModel.set(p, noteModel);
        vm.setListModel(listNoteModel);

        adapter.setList(listNoteModel);
        adapter.notifyItemChanged(p);
    }

    @Override
    public void onMenuBindClick(int p) {
        Log.i(TAG, "onMenuBindClick");

        List<NoteModel> listNoteModel = vm.getListModel();
        NoteModel noteModel = listNoteModel.get(p);

        NoteItem noteItem = noteModel.getNoteItem();
        noteItem.setStatus(!noteItem.isStatus());

        noteModel.updateItemStatus(noteItem.isStatus());

        db = RoomDb.provideDb(context);
        db.daoNote().update(noteItem.getId(), noteItem.isStatus());
        db.close();

        noteModel.setNoteItem(noteItem);

        listNoteModel.set(p, noteModel);
        vm.setListModel(listNoteModel);

        adapter.setList(listNoteModel);
        adapter.notifyItemChanged(p);
    }

    @Override
    public void onMenuConvertClick(int p) {
        Log.i(TAG, "onMenuConvertClick");

        List<NoteModel> listNoteModel = vm.getListModel();
        NoteModel noteModel = listNoteModel.get(p);

        NoteItem noteItem = noteModel.getNoteItem();
        noteItem.setChange(Help.Time.getCurrentTime(context));

        db = RoomDb.provideDb(context);
        switch (noteItem.getType()) {
            case TypeDef.text:
                String[] textToRoll = noteItem.getText().split("\n");                             //Получаем пункты списка

                List<RollItem> listRoll = db.daoRoll().insert(noteItem.getId(), textToRoll);      //Записываем пункты

                noteItem.setType(TypeDef.roll);
                noteItem.setText(0, listRoll.size());

                db.daoNote().update(noteItem);

                noteModel.setListRoll(listRoll);
                break;
            case TypeDef.roll:
                String rollToText = db.daoRoll().getText(noteItem.getId());           //Получаем текст заметки

                noteItem.setType(TypeDef.text);
                noteItem.setText(rollToText);

                db.daoNote().update(noteItem);
                db.daoRoll().delete(noteItem.getId());

                noteModel.setListRoll();
                break;
        }
        db.close();

        noteModel.setNoteItem(noteItem);
        noteModel.updateItemStatus();

        listNoteModel.set(p, noteModel);
        vm.setListModel(listNoteModel);

        adapter.setList(listNoteModel);
        adapter.notifyItemChanged(p);
    }

    @Override
    public void onMenuCopyClick(int p) {
        Log.i(TAG, "onMenuCopyClick");

        NoteItem noteItem = vm.getListModel().get(p).getNoteItem();
        Help.optionsCopy(context, noteItem);
    }

    @Override
    public void onMenuDeleteClick(int p) {
        Log.i(TAG, "onMenuDeleteClick");

        List<NoteModel> listNoteModel = vm.getListModel();
        NoteModel noteModel = listNoteModel.get(p);

        NoteItem noteItem = noteModel.getNoteItem();

        db = RoomDb.provideDb(context);
        db.daoNote().update(noteItem.getId(), Help.Time.getCurrentTime(context), true);
        if (noteItem.isStatus()) db.daoNote().update(noteItem.getId(), false);
        db.close();

        noteModel.updateItemStatus(false);
        listNoteModel.remove(p);
        vm.setListModel(listNoteModel);

        adapter.setList(listNoteModel);
        adapter.notifyItemRemoved(p);
    }

}