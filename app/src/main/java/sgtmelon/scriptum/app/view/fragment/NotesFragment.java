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
import sgtmelon.scriptum.app.adapter.AdapterNote;
import sgtmelon.scriptum.app.database.DbRoom;
import sgtmelon.scriptum.app.injection.component.ComponentFragment;
import sgtmelon.scriptum.app.injection.component.DaggerComponentFragment;
import sgtmelon.scriptum.app.injection.module.blank.ModuleBlankFragment;
import sgtmelon.scriptum.app.model.ModelNote;
import sgtmelon.scriptum.app.model.item.ItemNote;
import sgtmelon.scriptum.app.model.item.ItemRoll;
import sgtmelon.scriptum.app.view.activity.NoteActivity;
import sgtmelon.scriptum.app.view.activity.PreferenceActivity;
import sgtmelon.scriptum.app.vm.NotesViewModel;
import sgtmelon.scriptum.databinding.FragmentNotesBinding;
import sgtmelon.scriptum.element.DlgOptionNote;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.DefDlg;
import sgtmelon.scriptum.office.annot.def.DefIntent;
import sgtmelon.scriptum.office.annot.def.db.DefBin;
import sgtmelon.scriptum.office.annot.def.db.DefCheck;
import sgtmelon.scriptum.office.annot.def.db.DefType;
import sgtmelon.scriptum.office.intf.IntfDialog;
import sgtmelon.scriptum.office.intf.IntfItem;

public final class NotesFragment extends Fragment implements Toolbar.OnMenuItemClickListener,
        IntfItem.Click, IntfItem.LongClick, IntfDialog.OptionNote {

    private static final String TAG = NotesFragment.class.getSimpleName();

    public static boolean updateStatus = true; //Для единовременного обновления статус бара

    private final AdapterNote adapter = new AdapterNote();

    @Inject
    FragmentManager fm;

    @Inject
    FragmentNotesBinding binding;
    @Inject
    NotesViewModel vm;

    @Inject
    DlgOptionNote dlgOptionNote;

    private Context context;

    private DbRoom db;

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

        ComponentFragment componentFragment = DaggerComponentFragment.builder()
                .moduleBlankFragment(new ModuleBlankFragment(this, inflater, container))
                .build();
        componentFragment.inject(this);

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

        toolbar.inflateMenu(R.menu.menu_fragment_note);
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
                bind(vm.getListModelNote().size());
            }
        };

        RecyclerView recyclerView = frgView.findViewById(R.id.notes_recycler);
        recyclerView.setItemAnimator(recyclerViewEndAnim);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter.setClick(this);
        adapter.setLongClick(this);

        recyclerView.setAdapter(adapter);

        dlgOptionNote.setOptionNote(this);
    }

    private void updateAdapter() {
        Log.i(TAG, "updateAdapter");

        List<ModelNote> listModelNote = vm.loadData(DefBin.out);

        adapter.setListModelNote(listModelNote);
        adapter.notifyDataSetChanged();

        bind(listModelNote.size());
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

        long id = vm.getListModelNote().get(p).getItemNote().getId();

        Intent intent = new Intent(context, NoteActivity.class);
        intent.putExtra(DefIntent.NOTE_CREATE, false);
        intent.putExtra(DefIntent.NOTE_ID, id);

        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int p) {
        Log.i(TAG, "onItemLongClick");

        ItemNote itemNote = vm.getListModelNote().get(p).getItemNote();

        dlgOptionNote.setArguments(p, itemNote.getType(), itemNote.isStatus(), itemNote.isAllCheck());
        dlgOptionNote.show(fm, DefDlg.OPTIONS);
    }

    @Override
    public void onOptionCheckClick(int p) {
        Log.i(TAG, "onOptionCheckClick");

        List<ModelNote> listModelNote = vm.getListModelNote();
        ModelNote modelNote = listModelNote.get(p);

        ItemNote itemNote = modelNote.getItemNote();

        int[] checkText = itemNote.getCheck();
        int check = checkText[0] == checkText[1] ? DefCheck.notDone : DefCheck.done;

        itemNote.setChange(context);
        itemNote.setText(check == DefCheck.notDone ? 0 : checkText[1], checkText[1]);

        db = DbRoom.provideDb(context);
        db.daoRoll().update(itemNote.getId(), check);
        db.daoNote().update(itemNote);
        db.close();

        modelNote.updateListRoll(check);
        modelNote.setItemNote(itemNote);
        modelNote.updateItemStatus();

        listModelNote.set(p, modelNote);
        vm.setListModelNote(listModelNote);

        adapter.setListModelNote(listModelNote);
        adapter.notifyItemChanged(p);
    }

    @Override
    public void onOptionBindClick(int p) {
        Log.i(TAG, "onOptionBindClick");

        List<ModelNote> listModelNote = vm.getListModelNote();
        ModelNote modelNote = listModelNote.get(p);

        ItemNote itemNote = modelNote.getItemNote();
        itemNote.setStatus();

        modelNote.updateItemStatus(itemNote.isStatus());

        db = DbRoom.provideDb(context);
        db.daoNote().update(itemNote.getId(), itemNote.isStatus());
        db.close();

        modelNote.setItemNote(itemNote);

        listModelNote.set(p, modelNote);
        vm.setListModelNote(listModelNote);

        adapter.setListModelNote(listModelNote);
        adapter.notifyItemChanged(p);
    }

    @Override
    public void onOptionConvertClick(int p) {
        Log.i(TAG, "onOptionConvertClick");

        List<ModelNote> listModelNote = vm.getListModelNote();
        ModelNote modelNote = listModelNote.get(p);

        ItemNote itemNote = modelNote.getItemNote();
        itemNote.setChange(context);

        db = DbRoom.provideDb(context);
        switch (itemNote.getType()) {
            case DefType.text:
                String[] textToRoll = itemNote.getText().split("\n");                             //Получаем пункты списка

                List<ItemRoll> listRoll = db.daoRoll().insert(itemNote.getId(), textToRoll);      //Записываем пункты

                itemNote.setType(DefType.roll);
                itemNote.setText(0, listRoll.size());

                db.daoNote().update(itemNote);

                modelNote.setListRoll(listRoll);
                break;
            case DefType.roll:
                String rollToText = db.daoRoll().getText(itemNote.getId());           //Получаем текст заметки

                itemNote.setType(DefType.text);
                itemNote.setText(rollToText);

                db.daoNote().update(itemNote);
                db.daoRoll().delete(itemNote.getId());

                modelNote.setListRoll();
                break;
        }
        db.close();

        modelNote.setItemNote(itemNote);
        modelNote.updateItemStatus();

        listModelNote.set(p, modelNote);
        vm.setListModelNote(listModelNote);

        adapter.setListModelNote(listModelNote);
        adapter.notifyItemChanged(p);
    }

    @Override
    public void onOptionCopyClick(int p) {
        Log.i(TAG, "onOptionCopyClick");

        ItemNote itemNote = vm.getListModelNote().get(p).getItemNote();
        Help.optionsCopy(context, itemNote);
    }

    @Override
    public void onOptionDeleteClick(int p) {
        Log.i(TAG, "onOptionDeleteClick");

        List<ModelNote> listModelNote = vm.getListModelNote();
        ModelNote modelNote = listModelNote.get(p);

        ItemNote itemNote = modelNote.getItemNote();

        db = DbRoom.provideDb(context);
        db.daoNote().update(itemNote.getId(), Help.Time.getCurrentTime(context), true);
        if (itemNote.isStatus()) db.daoNote().update(itemNote.getId(), false);
        db.close();

        modelNote.updateItemStatus(false);
        listModelNote.remove(p);
        vm.setListModelNote(listModelNote);

        adapter.setListModelNote(listModelNote);
        adapter.notifyItemRemoved(p);
    }

}