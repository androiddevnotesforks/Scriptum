package sgtmelon.scriptum.app.view.activity;

import android.os.Bundle;
import android.util.Log;

import javax.inject.Inject;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.control.SaveControl;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.injection.component.ActivityComponent;
import sgtmelon.scriptum.app.injection.component.DaggerActivityComponent;
import sgtmelon.scriptum.app.injection.module.blank.ActivityBlankModule;
import sgtmelon.scriptum.app.model.NoteModel;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.view.fragment.RollFragment;
import sgtmelon.scriptum.app.view.fragment.TextFragment;
import sgtmelon.scriptum.app.view.parent.ActivityParent;
import sgtmelon.scriptum.app.vm.activity.NoteViewModel;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.FragmentDef;
import sgtmelon.scriptum.office.annot.def.IntentDef;
import sgtmelon.scriptum.office.annot.def.db.TypeDef;
import sgtmelon.scriptum.office.intf.MenuIntf;
import sgtmelon.scriptum.office.st.NoteSt;

public final class NoteActivity extends ActivityParent implements MenuIntf.Note.DeleteMenuClick {

    private static final String TAG = NoteActivity.class.getSimpleName();

    @Inject
    public NoteViewModel vm;

    public SaveControl saveControl;

    @Inject
    FragmentManager fm;

    private RoomDb db;

    private TextFragment textFragment;
    private RollFragment rollFragment;

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();

        saveControl.onPauseSave(vm.getNoteSt().isEdit());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        ActivityComponent activityComponent = DaggerActivityComponent.builder()
                .activityBlankModule(new ActivityBlankModule(this))
                .build();
        activityComponent.inject(this);

        Bundle bundle = getIntent().getExtras();
        vm.setValue(bundle != null ? bundle : savedInstanceState);

        saveControl = new SaveControl(this);

        setupFrg(savedInstanceState != null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);

        outState.putBoolean(IntentDef.NOTE_CREATE, vm.getNoteSt().isCreate());
        outState.putInt(IntentDef.NOTE_TYPE, vm.getNtType());
        outState.putLong(IntentDef.NOTE_ID, vm.getNtId());
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");

        saveControl.setNeedSave(false);

        NoteItem noteItem = vm.getNoteModel().getNoteItem();
        NoteSt noteSt = vm.getNoteSt();

        if (noteSt.isEdit() && !noteSt.isCreate()) {                  //Если это редактирование и не только что созданная заметка
            switch (noteItem.getType()) {
                case TypeDef.text:
                    if (!textFragment.onMenuSaveClick(true)) {   //Если сохранение не выполнено, возвращает старое
                        textFragment.menuNote.setStartColor(noteItem.getColor());

                        NoteModel noteModel = vm.loadData(noteItem.getId());
                        noteItem = noteModel.getNoteItem();

                        textFragment.vm.setNoteModel(noteModel);
                        textFragment.menuNote.startTint(noteItem.getColor());
                        textFragment.onMenuEditClick(false);
                    }
                    break;
                case TypeDef.roll:
                    if (!rollFragment.onMenuSaveClick(true)) {   //Если сохранение не выполнено, возвращает старое
                        rollFragment.menuNote.setStartColor(noteItem.getColor());

                        NoteModel noteModel = vm.loadData(noteItem.getId());
                        noteItem = noteModel.getNoteItem();

                        rollFragment.vm.setNoteModel(noteModel);
                        rollFragment.menuNote.startTint(noteItem.getColor());
                        rollFragment.onMenuEditClick(false);
                        rollFragment.updateAdapter();
                    }
                    break;
            }
        } else if (noteSt.isCreate()) {     //Если только что создали заметку
            switch (noteItem.getType()) {   //Если сохранение не выполнено, выход без сохранения
                case TypeDef.text:
                    if (!textFragment.onMenuSaveClick(true)) super.onBackPressed();
                    break;
                case TypeDef.roll:
                    if (!rollFragment.onMenuSaveClick(true)) super.onBackPressed();
                    break;
            }
        } else super.onBackPressed();   //Другие случаи (не редактирование)
    }

    public void setupFrg(boolean isSave) {
        Log.i(TAG, "setupFrg");

        if (!isSave) {
            NoteSt noteSt = vm.getNoteSt();
            noteSt.setFirst(true);
            vm.setNoteSt(noteSt);
        }

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        switch (vm.getNoteModel().getNoteItem().getType()) {
            case TypeDef.text:
                if (isSave) textFragment = (TextFragment) fm.findFragmentByTag(FragmentDef.TEXT);
                else textFragment = new TextFragment();

                saveControl.setNoteMenuClick(textFragment);

                transaction.replace(R.id.fragment_container, textFragment, FragmentDef.TEXT);
                break;
            case TypeDef.roll:
                if (isSave) rollFragment = (RollFragment) fm.findFragmentByTag(FragmentDef.ROLL);
                else rollFragment = new RollFragment();

                saveControl.setNoteMenuClick(rollFragment);

                transaction.replace(R.id.fragment_container, rollFragment, FragmentDef.ROLL);
                break;
        }
        transaction.commit();
    }

    @Override
    public void onMenuRestoreClick() {
        Log.i(TAG, "onMenuRestoreClick");

        db = RoomDb.provideDb(this);
        db.daoNote().update(vm.getNoteModel().getNoteItem().getId(), Help.Time.getCurrentTime(this), false);
        db.close();

        finish();
    }

    @Override
    public void onMenuRestoreOpenClick() {
        Log.i(TAG, "onMenuRestoreOpenClick");

        NoteSt noteSt = vm.getNoteSt();
        noteSt.setBin(false);

        vm.setNoteSt(noteSt);

        NoteModel noteModel = vm.getNoteModel();
        NoteItem noteItem = noteModel.getNoteItem();

        noteItem.setChange(Help.Time.getCurrentTime(this));
        noteItem.setBin(false);
        noteModel.setNoteItem(noteItem);

        vm.setNoteModel(noteModel);

        db = RoomDb.provideDb(this);
        db.daoNote().update(noteItem);
        db.close();

        switch (vm.getNoteModel().getNoteItem().getType()) {
            case TypeDef.text:
                textFragment.vm.setNoteModel(noteModel);
                textFragment.menuNote.setMenuGroupVisible(false, false, true);
                break;
            case TypeDef.roll:
                rollFragment.vm.setNoteModel(noteModel);
                rollFragment.menuNote.setMenuGroupVisible(false, false, true);
                break;
        }
    }

    @Override
    public void onMenuClearClick() {
        Log.i(TAG, "onMenuClearClick");

        db = RoomDb.provideDb(this);
        db.daoNote().delete(vm.getNoteModel().getNoteItem().getId());
        db.close();

        vm.setRepoNote(false);

        finish();
    }

    @Override
    public void onMenuDeleteClick() {
        Log.i(TAG, "onMenuDeleteClick");

        NoteItem noteItem = vm.getNoteModel().getNoteItem();

        db = RoomDb.provideDb(this);
        db.daoNote().update(noteItem.getId(), Help.Time.getCurrentTime(this), true);
        if (noteItem.isStatus()) {
            db.daoNote().update(noteItem.getId(), false);
        }
        db.close();

        vm.setRepoNote(false);

        finish();
    }

}