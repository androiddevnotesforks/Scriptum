package sgtmelon.scriptum.app.view.act;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.control.SaveNote;
import sgtmelon.scriptum.app.dataBase.DbRoom;
import sgtmelon.scriptum.app.model.item.ItemNote;
import sgtmelon.scriptum.app.model.repo.RepoNote;
import sgtmelon.scriptum.app.view.frg.FrgRoll;
import sgtmelon.scriptum.app.view.frg.FrgText;
import sgtmelon.scriptum.app.viewModel.VmActNote;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.DefFrg;
import sgtmelon.scriptum.office.annot.def.DefNote;
import sgtmelon.scriptum.office.annot.def.db.DefType;
import sgtmelon.scriptum.office.blank.BlankAct;
import sgtmelon.scriptum.office.intf.IntfMenu;
import sgtmelon.scriptum.office.st.StNote;

public class ActNote extends BlankAct implements IntfMenu.DeleteClick {

    //region Variable
    private static final String TAG = "ActNote";

    private DbRoom db;

    private FragmentManager fm;

    public VmActNote vm;

    public SaveNote saveNote;
    //endregion

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");

        saveNote.onPauseSave(vm.getStNote().isEdit());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_note);
        Log.i(TAG, "onCreate");

        fm = getSupportFragmentManager();

        vm = ViewModelProviders.of(this).get(VmActNote.class);

        Bundle bundle = getIntent().getExtras();
        vm.setValue(bundle == null ? savedInstanceState : bundle);

        saveNote = new SaveNote(this);

        setupFrg();
    }

    private FrgText frgText;
    private FrgRoll frgRoll;

    public void setupFrg() {
        Log.i(TAG, "setupFrg");

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        switch (vm.getRepoNote().getItemNote().getType()) {
            case DefType.text:
                frgText = (FrgText) fm.findFragmentByTag(DefFrg.TEXT);
                if (frgText == null) frgText = new FrgText();

                saveNote.setMenuClick(frgText);

                transaction.replace(R.id.actNote_fl_container, frgText, DefFrg.TEXT);
                break;
            case DefType.roll:
                frgRoll = (FrgRoll) fm.findFragmentByTag(DefFrg.ROLL);
                if (frgRoll == null) frgRoll = new FrgRoll();

                saveNote.setMenuClick(frgRoll);

                transaction.replace(R.id.actNote_fl_container, frgRoll, DefFrg.ROLL);
                break;
        }
        transaction.commit();
    }

    @Override
    public void onMenuRestoreClick() {
        Log.i(TAG, "onMenuRestoreClick");

        db = DbRoom.provideDb(this);
        db.daoNote().update(vm.getRepoNote().getItemNote().getId(), Help.Time.getCurrentTime(this), false);
        db.close();

        finish();
    }

    @Override
    public void onMenuRestoreOpenClick() {
        Log.i(TAG, "onMenuRestoreOpenClick");

        StNote stNote = vm.getStNote();
        stNote.setBin(false);

        vm.setStNote(stNote);

        RepoNote repoNote = vm.getRepoNote();
        ItemNote itemNote = repoNote.getItemNote();
        itemNote.setChange(this);
        itemNote.setBin(false);
        repoNote.setItemNote(itemNote);

        vm.setRepoNote(repoNote);

        db = DbRoom.provideDb(this);
        db.daoNote().update(itemNote);
        db.close();

        switch (vm.getRepoNote().getItemNote().getType()) {
            case DefType.text:
                frgText.vm.setRepoNote(repoNote);
                frgText.menuNote.setMenuGroupVisible(false, false, true);
                break;
            case DefType.roll:
                frgRoll.vm.setRepoNote(repoNote);
                frgRoll.menuNote.setMenuGroupVisible(false, false, true);
                break;
        }
    }

    @Override
    public void onMenuClearClick() {
        Log.i(TAG, "onMenuClearClick");

        db = DbRoom.provideDb(this);
        db.daoNote().delete(vm.getRepoNote().getItemNote().getId());
        db.close();

        vm.setRepoNote(false);

        finish();
    }

    @Override
    public void onMenuDeleteClick() {
        Log.i(TAG, "onMenuDeleteClick");

        ItemNote itemNote = vm.getRepoNote().getItemNote();

        db = DbRoom.provideDb(this);
        db.daoNote().update(itemNote.getId(), Help.Time.getCurrentTime(this), true);
        if (itemNote.isStatus()) {
            db.daoNote().update(itemNote.getId(), false);
        }
        db.close();

        vm.setRepoNote(false);

        finish();
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");

        saveNote.setNeedSave(false);

        ItemNote itemNote = vm.getRepoNote().getItemNote();
        StNote stNote = vm.getStNote();

        if (stNote.isEdit() && !stNote.isCreate()) {                  //Если это редактирование и не только что созданная заметка
            switch (itemNote.getType()) {
                case DefType.text:
                    if (!frgText.onMenuSaveClick(true)) {   //Если сохранение не выполнено, возвращает старое
                        frgText.menuNote.setStartColor(itemNote.getColor());

                        RepoNote repoNote = vm.loadData(itemNote.getId());
                        itemNote = repoNote.getItemNote();

                        frgText.vm.setRepoNote(repoNote);
                        frgText.menuNote.startTint(itemNote.getColor());
                        frgText.onMenuEditClick(false);
                    }
                    break;
                case DefType.roll:
                    if (!frgRoll.onMenuSaveClick(true)) {   //Если сохранение не выполнено, возвращает старое
                        frgRoll.menuNote.setStartColor(itemNote.getColor());

                        RepoNote repoNote = vm.loadData(itemNote.getId());
                        itemNote = repoNote.getItemNote();

                        frgRoll.vm.setRepoNote(repoNote);
                        frgRoll.menuNote.startTint(itemNote.getColor());
                        frgRoll.onMenuEditClick(false);
                        frgRoll.updateAdapter();
                    }
                    break;
            }
        } else if (stNote.isCreate()) {     //Если только что создали заметку
            switch (itemNote.getType()) {   //Если сохранение не выполнено, выход без сохранения
                case DefType.text:
                    if (!frgText.onMenuSaveClick(true)) super.onBackPressed();
                    break;
                case DefType.roll:
                    if (!frgRoll.onMenuSaveClick(true)) super.onBackPressed();
                    break;
            }
        } else super.onBackPressed();   //Другие случаи (не редактирование)
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");

        outState.putBoolean(DefNote.CREATE, vm.isCreate());
        outState.putInt(DefNote.TYPE, vm.getType());
        outState.putLong(DefNote.ID, vm.getId());
    }

}