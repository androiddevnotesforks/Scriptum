package sgtmelon.scriptum.app.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.control.SaveControl;
import sgtmelon.scriptum.app.model.NoteRepo;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.view.callback.NoteCallback;
import sgtmelon.scriptum.app.view.fragment.RollFragment;
import sgtmelon.scriptum.app.view.fragment.TextFragment;
import sgtmelon.scriptum.app.view.parent.BaseActivityParent;
import sgtmelon.scriptum.app.vm.activity.ActivityNoteViewModel;
import sgtmelon.scriptum.app.vm.fragment.FragmentNoteViewModel;
import sgtmelon.scriptum.office.annot.def.FragmentDef;
import sgtmelon.scriptum.office.annot.def.IntentDef;
import sgtmelon.scriptum.office.annot.def.TypeNoteDef;
import sgtmelon.scriptum.office.intf.MenuIntf;
import sgtmelon.scriptum.office.st.NoteSt;

public final class NoteActivity extends BaseActivityParent
        implements NoteCallback, MenuIntf.Note.DeleteMenuClick {

    //Если Id не существует то завершать активити

    private static final String TAG = NoteActivity.class.getSimpleName();

    private FragmentManager fm;
    private ActivityNoteViewModel vm;

    private SaveControl saveControl;

    private TextFragment textFragment;
    private RollFragment rollFragment;

    @NonNull
    public static Intent getIntent(@NonNull Context context, int type) {
        final Intent intent = new Intent(context, NoteActivity.class);

        intent.putExtra(IntentDef.NOTE_CREATE, true);
        intent.putExtra(IntentDef.NOTE_TYPE, type);

        return intent;
    }

    @NonNull
    public static Intent getIntent(@NonNull Context context, long id) {
        final Intent intent = new Intent(context, NoteActivity.class);

        intent.putExtra(IntentDef.NOTE_CREATE, false);
        intent.putExtra(IntentDef.NOTE_ID, id);

        return intent;
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();

        saveControl.onPauseSave(vm.getNoteSt().isEdit());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        fm = getSupportFragmentManager();
        vm = ViewModelProviders.of(this).get(ActivityNoteViewModel.class);

        final Bundle bundle = getIntent().getExtras();
        vm.setValue(bundle != null ? bundle : savedInstanceState);

        saveControl = new SaveControl(this);

        setupFragment(savedInstanceState != null);
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

        final NoteItem noteItem = vm.getNoteRepo().getNoteItem();
        final NoteSt noteSt = vm.getNoteSt();

        if (noteSt.isEdit() && !noteSt.isCreate()) {                  //Если это редактирование и не только что созданная заметка
            switch (noteItem.getType()) {
                case TypeNoteDef.text:
                    if (!textFragment.onMenuSaveClick(true, false)) {   //Если сохранение не выполнено, возвращает старое
                        final int colorFrom = noteItem.getColor();
                        final int colorTo = vm.resetFragmentData(
                                noteItem.getId(), textFragment.getViewModel()
                        );

                        textFragment.startTintToolbar(colorFrom, colorTo);
                        textFragment.onMenuEditClick(false);
                    }
                    break;
                case TypeNoteDef.roll:
                    if (!rollFragment.onMenuSaveClick(true, false)) {   //Если сохранение не выполнено, возвращает старое
                        final int colorFrom = noteItem.getColor();
                        final int colorTo = vm.resetFragmentData(
                                noteItem.getId(), rollFragment.getViewModel()
                        );

                        rollFragment.startTintToolbar(colorFrom, colorTo);
                        rollFragment.onMenuEditClick(false);
                        rollFragment.updateAdapter();
                    }
                    break;
            }
        } else if (noteSt.isCreate()) {     //Если только что создали заметку
            switch (noteItem.getType()) {   //Если сохранение не выполнено, выход без сохранения
                case TypeNoteDef.text:
                    if (!textFragment.onMenuSaveClick(true, false)) {
                        super.onBackPressed();
                    }
                    break;
                case TypeNoteDef.roll:
                    if (!rollFragment.onMenuSaveClick(true, false)) {
                        super.onBackPressed();
                    }
                    break;
            }
        } else {
            super.onBackPressed();   //Другие случаи (не редактирование)
        }
    }

    @Override
    public void setupFragment(boolean isSave) {
        Log.i(TAG, "setupFragment");

        if (!isSave) {
            final NoteSt noteSt = vm.getNoteSt();
            noteSt.setFirst(true);
        }

        final FragmentTransaction transaction = fm.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        switch (vm.getNoteRepo().getNoteItem().getType()) {
            case TypeNoteDef.text:
                textFragment = isSave
                        ? (TextFragment) fm.findFragmentByTag(FragmentDef.TEXT)
                        : TextFragment.getInstance(vm.isRankEmpty());

                saveControl.setNoteMenuClick(textFragment);

                transaction.replace(R.id.fragment_container, textFragment, FragmentDef.TEXT);
                break;
            case TypeNoteDef.roll:
                rollFragment = isSave
                        ? (RollFragment) fm.findFragmentByTag(FragmentDef.ROLL)
                        : RollFragment.getInstance(vm.isRankEmpty());

                saveControl.setNoteMenuClick(rollFragment);

                transaction.replace(R.id.fragment_container, rollFragment, FragmentDef.ROLL);
                break;
        }
        transaction.commit();
    }

    @Override
    public SaveControl getSaveControl() {
        return saveControl;
    }

    @Override
    public ActivityNoteViewModel getViewModel() {
        return vm;
    }

    @Override
    public void onMenuRestoreClick() {
        Log.i(TAG, "onMenuRestoreClick");

        vm.onMenuRestoreClick();
        finish();
    }

    @Override
    public void onMenuRestoreOpenClick() {
        Log.i(TAG, "onMenuRestoreOpenClick");

        vm.onMenuRestoreOpenClick();

        final NoteRepo noteRepo = vm.getNoteRepo();
        FragmentNoteViewModel viewModel;

        switch (vm.getNoteRepo().getNoteItem().getType()) {
            case TypeNoteDef.text:
                viewModel = textFragment.getViewModel();
                viewModel.setNoteRepo(noteRepo);

                textFragment.onMenuEditClick(false);
                break;
            case TypeNoteDef.roll:
                viewModel = rollFragment.getViewModel();
                viewModel.setNoteRepo(noteRepo);

                rollFragment.onMenuEditClick(false);
                break;
        }
    }

    @Override
    public void onMenuClearClick() {
        Log.i(TAG, "onMenuClearClick");

        vm.onMenuClearClick();
        finish();
    }

    @Override
    public void onMenuDeleteClick() {
        Log.i(TAG, "onMenuDeleteClick");

        vm.onMenuDeleteClick();
        finish();
    }

}