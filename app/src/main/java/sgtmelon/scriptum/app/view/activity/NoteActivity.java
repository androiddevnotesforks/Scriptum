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
import sgtmelon.scriptum.app.view.fragment.RollNoteFragment;
import sgtmelon.scriptum.app.view.fragment.TextNoteFragment;
import sgtmelon.scriptum.app.view.parent.BaseActivityParent;
import sgtmelon.scriptum.app.vm.activity.NoteActivityViewModel;
import sgtmelon.scriptum.app.vm.fragment.NoteFragmentViewModel;
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
    private NoteActivityViewModel vm;

    private SaveControl saveControl;

    private TextNoteFragment textNoteFragment;
    private RollNoteFragment rollNoteFragment;

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
        vm = ViewModelProviders.of(this).get(NoteActivityViewModel.class);

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
                    if (!textNoteFragment.onMenuSaveClick(true, false)) {   //Если сохранение не выполнено, возвращает старое
                        final int colorFrom = noteItem.getColor();
                        final int colorTo = vm.resetFragmentData(
                                noteItem.getId(), textNoteFragment.getViewModel()
                        );

                        textNoteFragment.startTintToolbar(colorFrom, colorTo);
                        textNoteFragment.onMenuEditClick(false);
                    }
                    break;
                case TypeNoteDef.roll:
                    if (!rollNoteFragment.onMenuSaveClick(true, false)) {   //Если сохранение не выполнено, возвращает старое
                        final int colorFrom = noteItem.getColor();
                        final int colorTo = vm.resetFragmentData(
                                noteItem.getId(), rollNoteFragment.getViewModel()
                        );

                        rollNoteFragment.startTintToolbar(colorFrom, colorTo);
                        rollNoteFragment.onMenuEditClick(false);
                        rollNoteFragment.updateAdapter();
                    }
                    break;
            }
        } else if (noteSt.isCreate()) {     //Если только что создали заметку
            switch (noteItem.getType()) {   //Если сохранение не выполнено, выход без сохранения
                case TypeNoteDef.text:
                    if (!textNoteFragment.onMenuSaveClick(true, false)) {
                        super.onBackPressed();
                    }
                    break;
                case TypeNoteDef.roll:
                    if (!rollNoteFragment.onMenuSaveClick(true, false)) {
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
                textNoteFragment = isSave
                        ? (TextNoteFragment) fm.findFragmentByTag(FragmentDef.TEXT)
                        : TextNoteFragment.getInstance(vm.isRankEmpty());

                saveControl.setNoteMenuClick(textNoteFragment);

                transaction.replace(R.id.note_fragment_container, textNoteFragment, FragmentDef.TEXT);
                break;
            case TypeNoteDef.roll:
                rollNoteFragment = isSave
                        ? (RollNoteFragment) fm.findFragmentByTag(FragmentDef.ROLL)
                        : RollNoteFragment.getInstance(vm.isRankEmpty());

                saveControl.setNoteMenuClick(rollNoteFragment);

                transaction.replace(R.id.note_fragment_container, rollNoteFragment, FragmentDef.ROLL);
                break;
        }
        transaction.commit();
    }

    @Override
    public SaveControl getSaveControl() {
        return saveControl;
    }

    @Override
    public NoteActivityViewModel getViewModel() {
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
        NoteFragmentViewModel viewModel;

        switch (vm.getNoteRepo().getNoteItem().getType()) {
            case TypeNoteDef.text:
                viewModel = textNoteFragment.getViewModel();
                viewModel.setNoteRepo(noteRepo);

                textNoteFragment.onMenuEditClick(false);
                break;
            case TypeNoteDef.roll:
                viewModel = rollNoteFragment.getViewModel();
                viewModel.setNoteRepo(noteRepo);

                rollNoteFragment.onMenuEditClick(false);
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