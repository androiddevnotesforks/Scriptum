package sgtmelon.scriptum.app.view.activity;

import android.os.Bundle;
import android.util.Log;

import javax.inject.Inject;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.control.ControlSave;
import sgtmelon.scriptum.app.database.DbRoom;
import sgtmelon.scriptum.app.injection.component.ComponentActivity;
import sgtmelon.scriptum.app.injection.component.DaggerComponentActivity;
import sgtmelon.scriptum.app.injection.module.blank.ModuleBlankActivity;
import sgtmelon.scriptum.app.model.ModelNote;
import sgtmelon.scriptum.app.model.item.ItemNote;
import sgtmelon.scriptum.app.view.fragment.RollFragment;
import sgtmelon.scriptum.app.view.fragment.TextFragment;
import sgtmelon.scriptum.app.vm.NoteViewModel;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.DefFrg;
import sgtmelon.scriptum.office.annot.def.DefIntent;
import sgtmelon.scriptum.office.annot.def.db.DefType;
import sgtmelon.scriptum.office.blank.BlankAct;
import sgtmelon.scriptum.office.intf.IntfMenu;
import sgtmelon.scriptum.office.st.StNote;

public final class NoteActivity extends BlankAct implements IntfMenu.DeleteClick {

    private static final String TAG = NoteActivity.class.getSimpleName();

    @Inject
    public NoteViewModel vm;

    public ControlSave controlSave;

    @Inject
    FragmentManager fm;

    private DbRoom db;

    private TextFragment textFragment;
    private RollFragment rollFragment;

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();

        controlSave.onPauseSave(vm.getStNote().isEdit());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        ComponentActivity componentActivity = DaggerComponentActivity.builder()
                .moduleBlankActivity(new ModuleBlankActivity(this))
                .build();
        componentActivity.inject(this);

        Bundle bundle = getIntent().getExtras();
        vm.setValue(bundle != null ? bundle : savedInstanceState);

        controlSave = new ControlSave(this);

        setupFrg(savedInstanceState != null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);

        outState.putBoolean(DefIntent.NOTE_CREATE, vm.getStNote().isCreate());
        outState.putInt(DefIntent.NOTE_TYPE, vm.getNtType());
        outState.putLong(DefIntent.NOTE_ID, vm.getNtId());
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");

        controlSave.setNeedSave(false);

        ItemNote itemNote = vm.getModelNote().getItemNote();
        StNote stNote = vm.getStNote();

        if (stNote.isEdit() && !stNote.isCreate()) {                  //Если это редактирование и не только что созданная заметка
            switch (itemNote.getType()) {
                case DefType.text:
                    if (!textFragment.onMenuSaveClick(true)) {   //Если сохранение не выполнено, возвращает старое
                        textFragment.menuNote.setStartColor(itemNote.getColor());

                        ModelNote modelNote = vm.loadData(itemNote.getId());
                        itemNote = modelNote.getItemNote();

                        textFragment.vm.setModelNote(modelNote);
                        textFragment.menuNote.startTint(itemNote.getColor());
                        textFragment.onMenuEditClick(false);
                    }
                    break;
                case DefType.roll:
                    if (!rollFragment.onMenuSaveClick(true)) {   //Если сохранение не выполнено, возвращает старое
                        rollFragment.menuNote.setStartColor(itemNote.getColor());

                        ModelNote modelNote = vm.loadData(itemNote.getId());
                        itemNote = modelNote.getItemNote();

                        rollFragment.vm.setModelNote(modelNote);
                        rollFragment.menuNote.startTint(itemNote.getColor());
                        rollFragment.onMenuEditClick(false);
                        rollFragment.updateAdapter();
                    }
                    break;
            }
        } else if (stNote.isCreate()) {     //Если только что создали заметку
            switch (itemNote.getType()) {   //Если сохранение не выполнено, выход без сохранения
                case DefType.text:
                    if (!textFragment.onMenuSaveClick(true)) super.onBackPressed();
                    break;
                case DefType.roll:
                    if (!rollFragment.onMenuSaveClick(true)) super.onBackPressed();
                    break;
            }
        } else super.onBackPressed();   //Другие случаи (не редактирование)
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

        switch (vm.getModelNote().getItemNote().getType()) {
            case DefType.text:
                if (isSave) textFragment = (TextFragment) fm.findFragmentByTag(DefFrg.TEXT);
                else textFragment = new TextFragment();

                controlSave.setNoteClick(textFragment);

                transaction.replace(R.id.fragment_container, textFragment, DefFrg.TEXT);
                break;
            case DefType.roll:
                if (isSave) rollFragment = (RollFragment) fm.findFragmentByTag(DefFrg.ROLL);
                else rollFragment = new RollFragment();

                controlSave.setNoteClick(rollFragment);

                transaction.replace(R.id.fragment_container, rollFragment, DefFrg.ROLL);
                break;
        }
        transaction.commit();
    }

    @Override
    public void onMenuRestoreClick() {
        Log.i(TAG, "onMenuRestoreClick");

        db = DbRoom.provideDb(this);
        db.daoNote().update(vm.getModelNote().getItemNote().getId(), Help.Time.getCurrentTime(this), false);
        db.close();

        finish();
    }

    @Override
    public void onMenuRestoreOpenClick() {
        Log.i(TAG, "onMenuRestoreOpenClick");

        StNote stNote = vm.getStNote();
        stNote.setBin(false);

        vm.setStNote(stNote);

        ModelNote modelNote = vm.getModelNote();
        ItemNote itemNote = modelNote.getItemNote();
        itemNote.setChange(this);
        itemNote.setBin(false);
        modelNote.setItemNote(itemNote);

        vm.setModelNote(modelNote);

        db = DbRoom.provideDb(this);
        db.daoNote().update(itemNote);
        db.close();

        switch (vm.getModelNote().getItemNote().getType()) {
            case DefType.text:
                textFragment.vm.setModelNote(modelNote);
                textFragment.menuNote.setMenuGroupVisible(false, false, true);
                break;
            case DefType.roll:
                rollFragment.vm.setModelNote(modelNote);
                rollFragment.menuNote.setMenuGroupVisible(false, false, true);
                break;
        }
    }

    @Override
    public void onMenuClearClick() {
        Log.i(TAG, "onMenuClearClick");

        db = DbRoom.provideDb(this);
        db.daoNote().delete(vm.getModelNote().getItemNote().getId());
        db.close();

        vm.setRepoNote(false);

        finish();
    }

    @Override
    public void onMenuDeleteClick() {
        Log.i(TAG, "onMenuDeleteClick");

        ItemNote itemNote = vm.getModelNote().getItemNote();

        db = DbRoom.provideDb(this);
        db.daoNote().update(itemNote.getId(), Help.Time.getCurrentTime(this), true);
        if (itemNote.isStatus()) {
            db.daoNote().update(itemNote.getId(), false);
        }
        db.close();

        vm.setRepoNote(false);

        finish();
    }

}