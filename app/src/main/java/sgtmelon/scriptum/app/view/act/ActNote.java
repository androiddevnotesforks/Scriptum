package sgtmelon.scriptum.app.view.act;

import android.os.Bundle;
import android.util.Log;

import javax.inject.Inject;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.control.CtrlSave;
import sgtmelon.scriptum.app.database.DbRoom;
import sgtmelon.scriptum.app.injection.component.ComAct;
import sgtmelon.scriptum.app.injection.component.DaggerComAct;
import sgtmelon.scriptum.app.injection.module.ModBlankAct;
import sgtmelon.scriptum.app.model.item.ItemNote;
import sgtmelon.scriptum.app.model.repo.RepoNote;
import sgtmelon.scriptum.app.view.frg.FrgRoll;
import sgtmelon.scriptum.app.view.frg.FrgText;
import sgtmelon.scriptum.app.viewModel.VmActNote;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.DefFrg;
import sgtmelon.scriptum.office.annot.def.DefIntent;
import sgtmelon.scriptum.office.annot.def.db.DefType;
import sgtmelon.scriptum.office.blank.BlankAct;
import sgtmelon.scriptum.office.intf.IntfMenu;
import sgtmelon.scriptum.office.st.StNote;

public class ActNote extends BlankAct implements IntfMenu.DeleteClick {

    private static final String TAG = ActNote.class.getSimpleName();

    @Inject
    public VmActNote vm;

    @Inject
    public CtrlSave ctrlSave;

    @Inject
    FragmentManager fm;

    private DbRoom db;

    private FrgText frgText;
    private FrgRoll frgRoll;

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();

        ctrlSave.onPauseSave(vm.getStNote().isEdit());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_note);

        ComAct comAct = DaggerComAct.builder().modBlankAct(new ModBlankAct(this, this)).build();
        comAct.inject(this);

        Bundle bundle = getIntent().getExtras();
        vm.setValue(bundle == null ? savedInstanceState : bundle);

        setupFrg(savedInstanceState != null);
    }

    public void setupFrg(boolean isSave) {
        Log.i(TAG, "setupFrg");

        if (!isSave) {
            StNote stNote = vm.getStNote();
            stNote.setFirst(true);
            vm.setStNote(stNote);
        }

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        switch (vm.getRepoNote().getItemNote().getType()) {
            case DefType.text:
                if (isSave) frgText = (FrgText) fm.findFragmentByTag(DefFrg.TEXT);
                else frgText = new FrgText();

                ctrlSave.setNoteClick(frgText);

                transaction.replace(R.id.actNote_fl_container, frgText, DefFrg.TEXT);
                break;
            case DefType.roll:
                if (isSave) frgRoll = (FrgRoll) fm.findFragmentByTag(DefFrg.ROLL);
                else frgRoll = new FrgRoll();

                ctrlSave.setNoteClick(frgRoll);

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

        ctrlSave.setNeedSave(false);

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
        Log.i(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);

        outState.putParcelable(DefIntent.STATE_NOTE, vm.getStNote());
        outState.putInt(DefIntent.NOTE_TYPE, vm.getNtType());
        outState.putLong(DefIntent.NOTE_ID, vm.getNtId());
    }

}