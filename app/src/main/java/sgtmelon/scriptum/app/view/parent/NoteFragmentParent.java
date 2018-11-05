package sgtmelon.scriptum.app.view.parent;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import sgtmelon.safedialog.library.ColorDialog;
import sgtmelon.safedialog.library.MessageDialog;
import sgtmelon.safedialog.library.MultiplyDialog;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.control.InputControl;
import sgtmelon.scriptum.app.control.MenuControl;
import sgtmelon.scriptum.app.control.MenuControlAnim;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.injection.component.DaggerFragmentComponent;
import sgtmelon.scriptum.app.injection.component.FragmentComponent;
import sgtmelon.scriptum.app.injection.module.FragmentArchModule;
import sgtmelon.scriptum.app.injection.module.blank.FragmentBlankModule;
import sgtmelon.scriptum.app.model.NoteRepo;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.view.callback.NoteCallback;
import sgtmelon.scriptum.app.view.fragment.RollFragment;
import sgtmelon.scriptum.app.view.fragment.TextFragment;
import sgtmelon.scriptum.app.vm.activity.ActivityNoteViewModel;
import sgtmelon.scriptum.app.vm.fragment.FragmentNoteViewModel;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.DialogDef;
import sgtmelon.scriptum.office.intf.MenuIntf;
import sgtmelon.scriptum.office.st.NoteSt;

/**
 * Класс родитель для фрагментов редактирования заметок
 * {@link TextFragment}, {@link RollFragment}
 */
public abstract class NoteFragmentParent extends Fragment
        implements View.OnClickListener, MenuIntf.Note.NoteMenuClick {

    private static final String TAG = NoteFragmentParent.class.getSimpleName();

    protected final InputControl inputControl = new InputControl();

    protected Context context;
    protected Activity activity;
    protected NoteCallback noteCallback;

    @Inject
    @Named(DialogDef.CONVERT)
    protected MessageDialog dlgConvert;

    protected EditText nameEnter;

    @Inject protected FragmentManager fm;
    protected RoomDb db;
    protected View frgView;
    @Inject protected FragmentNoteViewModel vm;

    protected MenuControl menuControl;

    @Inject ColorDialog colorDialog;
    @Inject
    @Named(DialogDef.RANK)
    MultiplyDialog dlgRank;

    private MenuIntf.Note.DeleteMenuClick deleteMenuClick;

    @Override
    public void onAttach(Context context) {
        Log.i(TAG, "onAttach");
        super.onAttach(context);

        this.context = context;
        activity = getActivity();

        if (context instanceof NoteCallback) {
            noteCallback = (NoteCallback) context;
        } else {
            throw new ClassCastException(NoteCallback.class.getSimpleName() +
                    " interface not installed in " + TAG);
        }

        if (context instanceof MenuIntf.Note.DeleteMenuClick) {
            deleteMenuClick = (MenuIntf.Note.DeleteMenuClick) context;
        } else {
            throw new ClassCastException(MenuIntf.Note.DeleteMenuClick.class.getSimpleName() +
                    " interface not installed in " + TAG);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        final FragmentComponent fragmentComponent = DaggerFragmentComponent.builder()
                .fragmentBlankModule(new FragmentBlankModule(this))
                .fragmentArchModule(new FragmentArchModule(inflater, container))
                .build();
        fragmentComponent.inject(this);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected abstract void bind(boolean keyEdit);

    @CallSuper
    protected void setupToolbar() {
        Log.i(TAG, "setupToolbar");

        final NoteItem noteItem = vm.getNoteRepo().getNoteItem();

        final Toolbar toolbar = frgView.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.activity_note);

        final View indicator = frgView.findViewById(R.id.color_view);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            menuControl = new MenuControl(context, activity.getWindow());
        } else {
            menuControl = new MenuControlAnim(context, activity.getWindow());
        }

        menuControl.setToolbar(toolbar);
        menuControl.setIndicator(indicator);
        menuControl.setType(noteItem.getType());
        menuControl.setColor(noteItem.getColor());

        menuControl.setNoteMenuClick(this);
        menuControl.setDeleteMenuClick(deleteMenuClick);

        final NoteSt noteSt = noteCallback.getViewModel().getNoteSt();

        menuControl.setupDrawable();
        menuControl.setDrawable(noteSt.isEdit() && !noteSt.isCreate(), false);

        menuControl.setupMenu(noteItem.isStatus());

        toolbar.setOnMenuItemClickListener(menuControl);
        toolbar.setNavigationOnClickListener(this);
    }

    @CallSuper
    protected void setupDialog() {
        Log.i(TAG, "setupDialog");

        colorDialog.setTitle(getString(R.string.dialog_title_color));
        colorDialog.setPositiveListener((dialogInterface, i) -> {
            int check = colorDialog.getCheck();

            final NoteRepo noteRepo = vm.getNoteRepo();
            final NoteItem noteItem = noteRepo.getNoteItem();

            inputControl.onColorChange(noteItem.getColor());

            noteItem.setColor(check);
            noteRepo.setNoteItem(noteItem);

            vm.setNoteRepo(noteRepo);

            menuControl.startTint(check);
        });

        db = RoomDb.provideDb(context);
        final String[] name = db.daoRank().getName();
        db.close();

        dlgRank.setRows(name);
        dlgRank.setPositiveListener((dialogInterface, i) -> {
            final boolean[] check = dlgRank.getCheck();

            db = RoomDb.provideDb(context);
            final Long[] id = db.daoRank().getId();
            db.close();

            final List<Long> rankId = new ArrayList<>();
            final List<Long> rankPs = new ArrayList<>();

            for (int j = 0; j < id.length; j++) {
                if (check[j]) {
                    rankId.add(id[j]);
                    rankPs.add((long) j);
                }
            }

            final NoteRepo noteRepo = vm.getNoteRepo();
            final NoteItem noteItem = noteRepo.getNoteItem();

            inputControl.onRankChange(noteItem.getRankId());

            noteItem.setRankId(rankId);
            noteItem.setRankPs(rankPs);
            noteRepo.setNoteItem(noteItem);

            vm.setNoteRepo(noteRepo);
        });
    }

    @CallSuper
    protected void setupEnter() {
        Log.i(TAG, "setupEnter");

        nameEnter = frgView.findViewById(R.id.name_enter);
        nameEnter.addTextChangedListener(new TextWatcher() {
            private String textBefore;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textBefore = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                final String textChanged = charSequence.toString();
                if (!TextUtils.isEmpty(textBefore) && !textChanged.equals(textBefore)) {
                    inputControl.onNameChange(textBefore);
                    textBefore = textChanged;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public final MenuControl getMenuControl() {
        return menuControl;
    }

    public final void setMenuControl(MenuControl menuControl) {
        this.menuControl = menuControl;
    }

    public final FragmentNoteViewModel getViewModel() {
        return vm;
    }

    public final void setViewModel(FragmentNoteViewModel viewModel) {
        vm = viewModel;
    }

    @Override
    public final void onMenuRankClick() {
        Log.i(TAG, "onMenuRankClick");

        Help.hideKeyboard(context, activity.getCurrentFocus());

        final NoteItem noteItem = vm.getNoteRepo().getNoteItem();

        db = RoomDb.provideDb(context);
        final boolean[] check = db.daoRank().getCheck(noteItem.getRankId());
        db.close();

        dlgRank.setArguments(check);
        dlgRank.show(fm, DialogDef.RANK);
    }

    @Override
    public final void onMenuColorClick() {
        Log.i(TAG, "onMenuColorClick");

        Help.hideKeyboard(context, activity.getCurrentFocus());

        final NoteItem noteItem = vm.getNoteRepo().getNoteItem();

        colorDialog.setArguments(noteItem.getColor());
        colorDialog.show(fm, DialogDef.COLOR);

        menuControl.setStartColor(noteItem.getColor());
    }

    @Override
    public final void onMenuBindClick() {
        Log.i(TAG, "onMenuBindClick");

        final NoteRepo noteRepo = vm.getNoteRepo();
        final NoteItem noteItem = noteRepo.getNoteItem();

        if (!noteItem.isStatus()) {
            noteItem.setStatus(true);
            noteRepo.update(true);
        } else {
            noteItem.setStatus(false);
            noteRepo.update(false);
        }

        menuControl.setStatusTitle(noteItem.isStatus());

        db = RoomDb.provideDb(context);
        db.daoNote().update(noteItem.getId(), noteItem.isStatus());
        db.close();

        noteRepo.setNoteItem(noteItem);

        vm.setNoteRepo(noteRepo);

        final ActivityNoteViewModel viewModel = noteCallback.getViewModel();
        viewModel.setNoteRepo(noteRepo);
        noteCallback.setViewModel(viewModel);
    }

    @Override
    public final void onMenuConvertClick() {
        Log.i(TAG, "onMenuConvertClick");

        dlgConvert.show(fm, DialogDef.CONVERT);
    }

}
