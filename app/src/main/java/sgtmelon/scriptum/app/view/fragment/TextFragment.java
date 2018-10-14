package sgtmelon.scriptum.app.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.injection.component.DaggerFragmentComponent;
import sgtmelon.scriptum.app.injection.component.FragmentComponent;
import sgtmelon.scriptum.app.injection.module.FragmentArchModule;
import sgtmelon.scriptum.app.injection.module.blank.FragmentBlankModule;
import sgtmelon.scriptum.app.model.NoteModel;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.model.item.RollItem;
import sgtmelon.scriptum.app.view.parent.NoteFragmentParent;
import sgtmelon.scriptum.databinding.FragmentTextBinding;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.db.TypeDef;
import sgtmelon.scriptum.office.st.NoteSt;

public final class TextFragment extends NoteFragmentParent {

    private static final String TAG = TextFragment.class.getSimpleName();

    @Inject
    FragmentTextBinding binding;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        super.onCreateView(inflater, container, savedInstanceState);

        FragmentComponent fragmentComponent = DaggerFragmentComponent.builder()
                .fragmentBlankModule(new FragmentBlankModule(this))
                .fragmentArchModule(new FragmentArchModule(inflater, container))
                .build();
        fragmentComponent.inject(this);

        frgView = binding.getRoot();

        return frgView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        if (vm.isEmpty()) vm.setNoteModel(activity.vm.getNoteModel());

        setupToolbar();
        setupDialog();
        setupEnter();

        onMenuEditClick(activity.vm.getNoteSt().isEdit());

        NoteSt noteSt = activity.vm.getNoteSt();
        noteSt.setFirst(false);
        activity.vm.setNoteSt(noteSt);
    }

    private void bind(boolean keyEdit) {
        Log.i(TAG, "bind: keyEdit=" + keyEdit);

        binding.setNoteItem(vm.getNoteModel().getNoteItem());
        binding.setKeyEdit(keyEdit);

        binding.executePendingBindings();
    }

    @Override
    protected void setupDialog() {
        Log.i(TAG, "setupDialog");
        super.setupDialog();

        dlgConvert.setMessage(getString(R.string.dialog_text_convert_to_roll));
        dlgConvert.setPositiveListener((dialogInterface, i) -> {
            NoteModel noteModel = vm.getNoteModel();
            NoteItem noteItem = noteModel.getNoteItem();
            String[] textToRoll = noteItem.getText().split("\n");   //Получаем пункты списка

            db = RoomDb.provideDb(context);
            List<RollItem> listRoll = db.daoRoll().insert(noteItem.getId(), textToRoll);

            noteItem.setChange(Help.Time.getCurrentTime(context));
            noteItem.setType(TypeDef.roll);
            noteItem.setText(0, listRoll.size());

            db.daoNote().update(noteItem);
            db.close();

            noteModel.setNoteItem(noteItem);
            noteModel.setListRoll(listRoll);

            vm.setNoteModel(noteModel);
            activity.vm.setNoteModel(noteModel);
            activity.setupFrg(false);
        });
    }

    private void setupEnter() {
        Log.i(TAG, "setupEnter");

        final EditText nameEnter = frgView.findViewById(R.id.name_enter);
        final EditText textEnter = frgView.findViewById(R.id.text_enter);

        nameEnter.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_NEXT) {
                textEnter.requestFocus();
                return true;
            }
            return false;
        });
    }

    /**
     * Нажатие на клавишу назад
     */
    @Override
    public void onClick(View view) {
        Log.i(TAG, "onClick");

        Help.hideKeyboard(context, activity.getCurrentFocus());

        NoteSt noteSt = activity.vm.getNoteSt();
        NoteModel noteModel = vm.getNoteModel();
        NoteItem noteItem = noteModel.getNoteItem();

        if (!noteSt.isCreate() && noteSt.isEdit() && !noteItem.getText().equals("")) { //Если редактирование и текст в хранилище не пустой
            menuNote.setStartColor(noteItem.getColor());

            db = RoomDb.provideDb(context);
            noteModel = db.daoNote().get(context, noteItem.getId());
            noteItem = noteModel.getNoteItem();
            db.close();

            vm.setNoteModel(noteModel);
            activity.vm.setNoteModel(noteModel);

            onMenuEditClick(false);

            menuNote.startTint(noteItem.getColor());
        } else {
            activity.saveControl.setNeedSave(false);
            activity.finish();
        }
    }

    @Override
    public boolean onMenuSaveClick(boolean editModeChange) {
        Log.i(TAG, "onMenuSaveClick");

        NoteModel noteModel = vm.getNoteModel();
        NoteItem noteItem = noteModel.getNoteItem();
        if (!noteItem.getText().equals("")) {
            noteItem.setChange(Help.Time.getCurrentTime(context));

            if (editModeChange) {
                Help.hideKeyboard(context, activity.getCurrentFocus());
                onMenuEditClick(false);
            }

            db = RoomDb.provideDb(context);
            NoteSt noteSt = activity.vm.getNoteSt();
            if (noteSt.isCreate()) {
                noteSt.setCreate(false);
                activity.vm.setNoteSt(noteSt);

                long ntId = db.daoNote().insert(noteItem);
                noteItem.setId(ntId);
            } else {
                db.daoNote().update(noteItem);
            }
            db.daoRank().update(noteItem.getId(), noteItem.getRankId());
            db.close();

            noteModel.setNoteItem(noteItem);

            vm.setNoteModel(noteModel);
            activity.vm.setNoteModel(noteModel);
            return true;
        } else return false;
    }

    @Override
    public void onMenuEditClick(boolean editMode) {
        Log.i(TAG, "onMenuEditClick: " + editMode);

        NoteSt noteSt = activity.vm.getNoteSt();
        noteSt.setEdit(editMode);

        menuNote.setDrawable(editMode && !noteSt.isCreate(), !noteSt.isCreate());
        menuNote.setMenuGroupVisible(noteSt.isBin(), editMode, !noteSt.isBin() && !editMode);

        bind(editMode);

        activity.vm.setNoteSt(noteSt);
        activity.saveControl.setSaveHandlerEvent(editMode);
    }

    @Override
    public void onMenuCheckClick() {
        Log.i(TAG, "onMenuCheckClick");

    }

}