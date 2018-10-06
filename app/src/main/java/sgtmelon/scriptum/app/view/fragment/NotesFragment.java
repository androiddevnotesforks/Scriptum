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
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.adapter.NoteAdapter;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.injection.component.DaggerFragmentComponent;
import sgtmelon.scriptum.app.injection.component.FragmentComponent;
import sgtmelon.scriptum.app.injection.module.blank.FragmentBlankModule;
import sgtmelon.scriptum.app.model.NoteModel;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.model.item.RollItem;
import sgtmelon.scriptum.app.view.activity.NoteActivity;
import sgtmelon.scriptum.app.view.activity.PreferenceActivity;
import sgtmelon.scriptum.app.vm.NotesViewModel;
import sgtmelon.scriptum.databinding.FragmentNotesBinding;
import sgtmelon.scriptum.element.common.OptionsDialog;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.DialogDef;
import sgtmelon.scriptum.office.annot.def.IntentDef;
import sgtmelon.scriptum.office.annot.def.db.BinDef;
import sgtmelon.scriptum.office.annot.def.db.CheckDef;
import sgtmelon.scriptum.office.annot.def.db.TypeDef;
import sgtmelon.scriptum.office.intf.DialogIntf;
import sgtmelon.scriptum.office.intf.ItemIntf;

public final class NotesFragment extends Fragment implements Toolbar.OnMenuItemClickListener,
        ItemIntf.Click, ItemIntf.LongClick, DialogIntf.OptionNote {

    private static final String TAG = NotesFragment.class.getSimpleName();

    public static boolean updateStatus = true; //Для единовременного обновления статус бара

    private final NoteAdapter adapter = new NoteAdapter();

    @Inject
    FragmentManager fm;

    @Inject
    FragmentNotesBinding binding;
    @Inject
    NotesViewModel vm;

    @Inject
    OptionsDialog optionsDialog;

    private Context context;

    private RoomDb db;

    private View frgView;

    @Override
    public void onAttach(Context context) {
        Log.i(TAG, "onAttach");
        super.onAttach(context);

        this.context = context;
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
                .fragmentBlankModule(new FragmentBlankModule(this, inflater, container))
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
        toolbar.setTitle(getString(R.string.title_frg_notes));

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

        adapter.setClick(this);
        adapter.setLongClick(this);

        recyclerView.setAdapter(adapter);

        optionsDialog.setOnClickListener((dialogInterface, i) -> {
            int p = optionsDialog.getPosition(); // TODO: 06.10.2018 defValues
            NoteItem noteItem = vm.getListModel().get(p).getNoteItem();

            switch (noteItem.getType()) {
                case TypeDef.text:
                    switch (i) {
                        case 0:
                            onOptionBindClick(p);
                            break;
                        case 1:
                            onOptionConvertClick(p);
                            break;
                        case 2:
                            onOptionCopyClick(p);
                            break;
                        case 3:
                            onOptionDeleteClick(p);
                            break;
                    }
                    break;
                case TypeDef.roll:
                    switch (i) {
                        case 0:
                            onOptionCheckClick(p);
                            break;
                        case 1:
                            onOptionBindClick(p);
                            break;
                        case 2:
                            onOptionConvertClick(p);
                            break;
                        case 3:
                            onOptionCopyClick(p);
                            break;
                        case 4:
                            onOptionDeleteClick(p);
                            break;
                    }
                    break;
            }
        });
    }

    private void updateAdapter() {
        Log.i(TAG, "updateAdapter");

        List<NoteModel> listNoteModel = vm.loadData(BinDef.out);

        adapter.setListNoteModel(listNoteModel);
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

        Intent intent = new Intent(context, NoteActivity.class);
        intent.putExtra(IntentDef.NOTE_CREATE, false);
        intent.putExtra(IntentDef.NOTE_ID, id);

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
    public void onOptionCheckClick(int p) {
        Log.i(TAG, "onOptionCheckClick");

        List<NoteModel> listNoteModel = vm.getListModel();
        NoteModel noteModel = listNoteModel.get(p);

        NoteItem noteItem = noteModel.getNoteItem();

        int[] checkText = noteItem.getCheck();
        int check = checkText[0] == checkText[1] ? CheckDef.notDone : CheckDef.done;

        noteItem.setChange(context);
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

        adapter.setListNoteModel(listNoteModel);
        adapter.notifyItemChanged(p);
    }

    @Override
    public void onOptionBindClick(int p) {
        Log.i(TAG, "onOptionBindClick");

        List<NoteModel> listNoteModel = vm.getListModel();
        NoteModel noteModel = listNoteModel.get(p);

        NoteItem noteItem = noteModel.getNoteItem();
        noteItem.setStatus();

        noteModel.updateItemStatus(noteItem.isStatus());

        db = RoomDb.provideDb(context);
        db.daoNote().update(noteItem.getId(), noteItem.isStatus());
        db.close();

        noteModel.setNoteItem(noteItem);

        listNoteModel.set(p, noteModel);
        vm.setListModel(listNoteModel);

        adapter.setListNoteModel(listNoteModel);
        adapter.notifyItemChanged(p);
    }

    @Override
    public void onOptionConvertClick(int p) {
        Log.i(TAG, "onOptionConvertClick");

        List<NoteModel> listNoteModel = vm.getListModel();
        NoteModel noteModel = listNoteModel.get(p);

        NoteItem noteItem = noteModel.getNoteItem();
        noteItem.setChange(context);

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

        adapter.setListNoteModel(listNoteModel);
        adapter.notifyItemChanged(p);
    }

    @Override
    public void onOptionCopyClick(int p) {
        Log.i(TAG, "onOptionCopyClick");

        NoteItem noteItem = vm.getListModel().get(p).getNoteItem();
        Help.optionsCopy(context, noteItem);
    }

    @Override
    public void onOptionDeleteClick(int p) {
        Log.i(TAG, "onOptionDeleteClick");

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

        adapter.setListNoteModel(listNoteModel);
        adapter.notifyItemRemoved(p);
    }

}