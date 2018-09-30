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
import sgtmelon.scriptum.app.model.item.ItemRoll;
import sgtmelon.scriptum.app.model.repo.RepoNote;
import sgtmelon.scriptum.app.view.act.ActNote;
import sgtmelon.scriptum.app.view.act.ActSettings;
import sgtmelon.scriptum.app.viewModel.VmFrgNotes;
import sgtmelon.scriptum.databinding.FrgNotesBinding;
import sgtmelon.scriptum.element.dialog.DlgOptionNote;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.DefDlg;
import sgtmelon.scriptum.office.annot.def.DefIntent;
import sgtmelon.scriptum.office.annot.def.db.DefBin;
import sgtmelon.scriptum.office.annot.def.db.DefCheck;
import sgtmelon.scriptum.office.annot.def.db.DefType;
import sgtmelon.scriptum.office.intf.IntfDialog;
import sgtmelon.scriptum.office.intf.IntfItem;
import sgtmelon.scriptum.office.st.StNote;

public class FrgNotes extends Fragment implements Toolbar.OnMenuItemClickListener,
        IntfItem.Click, IntfItem.LongClick, IntfDialog.OptionNote {

    private static final String TAG = FrgNotes.class.getSimpleName();

    public static boolean updateStatus = true; //Для единовременного обновления статус бара

    public RecyclerView recyclerView;

    @Inject
    Context context;
    @Inject
    FragmentManager fm;

    @Inject
    FrgNotesBinding binding;
    @Inject
    VmFrgNotes vm;

    @Inject
    AdpNote adapter;

    @Inject
    DlgOptionNote dlgOptionNote;

    private DbRoom db;
    private View frgView;

    @Override
    public void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();

        updateAdapter();

        if (updateStatus) updateStatus = false;
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
        toolbar.setTitle(getString(R.string.title_frg_note));

        toolbar.inflateMenu(R.menu.menu_frg_note);
        toolbar.setOnMenuItemClickListener(this);

        Menu menu = toolbar.getMenu();
        MenuItem mItemSettings = menu.findItem(R.id.menu_frgNote_settings);

        Help.Tint.menuIcon(context, mItemSettings);
    }

    private void setupRecycler() {
        Log.i(TAG, "setupRecycler");

        final DefaultItemAnimator recyclerViewEndAnim = new DefaultItemAnimator() {
            @Override
            public void onAnimationFinished(@NonNull RecyclerView.ViewHolder viewHolder) {
                bind(vm.getListRepo().size());
            }
        };

        recyclerView = frgView.findViewById(R.id.frgNotes_rv);
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

        List<RepoNote> listRepo = vm.loadData(DefBin.out);

        adapter.setListRepo(listRepo);
        adapter.notifyDataSetChanged();

        bind(listRepo.size());
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Log.i(TAG, "onMenuItemClick");

        switch (item.getItemId()) {
            case R.id.menu_frgNote_settings:
                Intent intent = new Intent(context, ActSettings.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    @Override
    public void onItemClick(View view, int p) {
        Log.i(TAG, "onItemClick");

        long ntId = vm.getListRepo().get(p).getItemNote().getId();

        Intent intent = new Intent(context, ActNote.class);
        intent.putExtra(DefIntent.STATE_NOTE, new StNote(false, false));
        intent.putExtra(DefIntent.NOTE_ID, ntId);

        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int p) {
        Log.i(TAG, "onItemLongClick");

        ItemNote itemNote = vm.getListRepo().get(p).getItemNote();

        dlgOptionNote.setArguments(p, itemNote.getType(), itemNote.isStatus(), itemNote.isAllCheck());
        dlgOptionNote.show(fm, DefDlg.OPTIONS);
    }

    @Override
    public void onOptionCheckClick(int p) {
        Log.i(TAG, "onOptionCheckClick");

        List<RepoNote> listRepo = vm.getListRepo();
        RepoNote repoNote = listRepo.get(p);

        ItemNote itemNote = repoNote.getItemNote();

        int[] checkText = itemNote.getCheck();
        int check = checkText[0] == checkText[1] ? DefCheck.notDone : DefCheck.done;

        itemNote.setChange(context);
        itemNote.setText(check == DefCheck.notDone ? 0 : checkText[1], checkText[1]);

        db = DbRoom.provideDb(context);
        db.daoRoll().update(itemNote.getId(), check);
        db.daoNote().update(itemNote);
        db.close();

        repoNote.updateListRoll(check);
        repoNote.setItemNote(itemNote);
        repoNote.updateItemStatus();

        listRepo.set(p, repoNote);
        vm.setListRepo(listRepo);

        adapter.setListRepo(listRepo);
        adapter.notifyItemChanged(p);
    }

    @Override
    public void onOptionBindClick(int p) {
        Log.i(TAG, "onOptionBindClick");

        List<RepoNote> listRepo = vm.getListRepo();
        RepoNote repoNote = listRepo.get(p);

        ItemNote itemNote = repoNote.getItemNote();
        itemNote.setStatus();

        repoNote.updateItemStatus(itemNote.isStatus());

        db = DbRoom.provideDb(context);
        db.daoNote().update(itemNote.getId(), itemNote.isStatus());
        db.close();

        repoNote.setItemNote(itemNote);

        listRepo.set(p, repoNote);
        vm.setListRepo(listRepo);

        adapter.setListRepo(listRepo);
        adapter.notifyItemChanged(p);
    }

    @Override
    public void onOptionConvertClick(int p) {
        Log.i(TAG, "onOptionConvertClick");

        List<RepoNote> listRepo = vm.getListRepo();
        RepoNote repoNote = listRepo.get(p);

        ItemNote itemNote = repoNote.getItemNote();
        itemNote.setChange(context);

        db = DbRoom.provideDb(context);
        switch (itemNote.getType()) {
            case DefType.text:
                String[] textToRoll = itemNote.getText().split("\n");                             //Получаем пункты списка

                List<ItemRoll> listRoll = db.daoRoll().insert(itemNote.getId(), textToRoll);      //Записываем пункты

                itemNote.setType(DefType.roll);
                itemNote.setText(0, listRoll.size());

                db.daoNote().update(itemNote);

                repoNote.setListRoll(listRoll);
                break;
            case DefType.roll:
                String rollToText = db.daoRoll().getText(itemNote.getId());           //Получаем текст заметки

                itemNote.setType(DefType.text);
                itemNote.setText(rollToText);

                db.daoNote().update(itemNote);
                db.daoRoll().delete(itemNote.getId());

                repoNote.setListRoll();
                break;
        }
        db.close();

        repoNote.setItemNote(itemNote);
        repoNote.updateItemStatus();

        listRepo.set(p, repoNote);
        vm.setListRepo(listRepo);

        adapter.setListRepo(listRepo);
        adapter.notifyItemChanged(p);
    }

    @Override
    public void onOptionCopyClick(int p) {
        Log.i(TAG, "onOptionCopyClick");

        ItemNote itemNote = vm.getListRepo().get(p).getItemNote();
        Help.optionsCopy(context, itemNote);
    }

    @Override
    public void onOptionDeleteClick(int p) {
        Log.i(TAG, "onOptionDeleteClick");

        List<RepoNote> listRepo = vm.getListRepo();
        RepoNote repoNote = listRepo.get(p);

        ItemNote itemNote = repoNote.getItemNote();

        db = DbRoom.provideDb(context);
        db.daoNote().update(itemNote.getId(), Help.Time.getCurrentTime(context), true);
        if (itemNote.isStatus()) db.daoNote().update(itemNote.getId(), false);
        db.close();

        repoNote.updateItemStatus(false);
        listRepo.remove(p);
        vm.setListRepo(listRepo);

        adapter.setListRepo(listRepo);
        adapter.notifyItemRemoved(p);
    }

}