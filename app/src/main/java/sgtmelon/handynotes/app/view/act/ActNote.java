package sgtmelon.handynotes.app.view.act;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.app.control.CtrlSave;
import sgtmelon.handynotes.app.dataBase.DbRoom;
import sgtmelon.handynotes.app.model.item.ItemNote;
import sgtmelon.handynotes.app.model.repo.RepoNote;
import sgtmelon.handynotes.app.view.frg.FrgRoll;
import sgtmelon.handynotes.app.view.frg.FrgText;
import sgtmelon.handynotes.app.viewModel.VmActNote;
import sgtmelon.handynotes.office.Help;
import sgtmelon.handynotes.office.annot.Db;
import sgtmelon.handynotes.office.annot.Frg;
import sgtmelon.handynotes.office.annot.def.DefPage;
import sgtmelon.handynotes.office.annot.def.db.DefType;
import sgtmelon.handynotes.office.intf.IntfMenu;
import sgtmelon.handynotes.office.st.StNote;

public class ActNote extends AppCompatActivity implements IntfMenu.DeleteClick {

    private static final String TAG = "ActNote";

    private DbRoom db;

    public VmActNote vm;

    public CtrlSave ctrlSave;

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");

        ctrlSave.onPauseSave(vm.getStNote().isEdit());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_note);
        Log.i(TAG, "onCreate");

        vm = ViewModelProviders.of(this).get(VmActNote.class);

        Bundle bundle = getIntent().getExtras();
        vm.setValue(bundle == null ? savedInstanceState : bundle);

        ctrlSave = new CtrlSave(this);

        setupFrg(savedInstanceState != null);
    }

    private FrgText frgText;
    private FrgRoll frgRoll;

    public void setupFrg(boolean isSaved) {
        Log.i(TAG, "setupFrg");

        FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        switch (vm.getRepoNote().getItemNote().getType()) {
            case DefType.text:
                if (isSaved) frgText = (FrgText) manager.findFragmentByTag(Frg.TEXT);
                else frgText = new FrgText();

                ctrlSave.setMenuClick(frgText);

                transaction.replace(R.id.actNote_fl_container, frgText, Frg.TEXT);
                break;
            case DefType.roll:
                if (isSaved) frgRoll = (FrgRoll) manager.findFragmentByTag(Frg.ROLL);
                else frgRoll = new FrgRoll();

                ctrlSave.setMenuClick(frgRoll);

                transaction.replace(R.id.actNote_fl_container, frgRoll, Frg.ROLL);
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
    public void onMenuDeleteForeverClick() {
        Log.i(TAG, "onMenuDeleteForeverClick");

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
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");

        outState.putBoolean(DefPage.CREATE, vm.isCreate());
        outState.putInt(Db.NT_TP, vm.getType());
        outState.putLong(Db.NT_ID, vm.getId());
    }

}