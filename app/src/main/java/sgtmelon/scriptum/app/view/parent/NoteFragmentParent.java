package sgtmelon.scriptum.app.view.parent;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import sgtmelon.safedialog.library.MessageDialog;
import sgtmelon.safedialog.library.MultiplyDialog;
import sgtmelon.safedialog.library.color.ColorDialog;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.control.InputControl;
import sgtmelon.scriptum.app.control.MenuControl;
import sgtmelon.scriptum.app.control.MenuControlAnim;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.factory.DialogFactory;
import sgtmelon.scriptum.app.model.NoteRepo;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.view.callback.NoteCallback;
import sgtmelon.scriptum.app.view.fragment.RollNoteFragment;
import sgtmelon.scriptum.app.view.fragment.TextNoteFragment;
import sgtmelon.scriptum.app.vm.fragment.NoteFragmentViewModel;
import sgtmelon.scriptum.office.annot.def.ColorDef;
import sgtmelon.scriptum.office.annot.def.DialogDef;
import sgtmelon.scriptum.office.annot.def.InputDef;
import sgtmelon.scriptum.office.annot.def.IntentDef;
import sgtmelon.scriptum.office.intf.BindIntf;
import sgtmelon.scriptum.office.intf.InputTextWatcher;
import sgtmelon.scriptum.office.intf.MenuIntf;
import sgtmelon.scriptum.office.st.NoteSt;
import sgtmelon.scriptum.office.utils.HelpUtils;

/**
 * Класс родитель для фрагментов редактирования заметок
 * {@link TextNoteFragment}, {@link RollNoteFragment}
 */
public abstract class NoteFragmentParent extends Fragment implements
        View.OnClickListener, BindIntf, MenuIntf.Note.NoteMenuClick {

    private static final String TAG = NoteFragmentParent.class.getSimpleName();

    protected final InputControl inputControl = new InputControl();

    // TODO: 17.12.2018 сделать долгое нажатие undo/redo

    protected Context context;
    protected Activity activity;
    protected NoteCallback noteCallback;

    protected MessageDialog convertDialog;

    protected EditText nameEnter;

    protected NoteFragmentViewModel vm;
    protected FragmentManager fm;
    protected RoomDb db;

    protected boolean rankEmpty;

    protected MenuControl menuControl;
    protected MenuIntf.Note.DeleteMenuClick deleteMenuClick;

    private ColorDialog colorDialog;
    private MultiplyDialog rankDialog;

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

        vm = ViewModelProviders.of(this).get(NoteFragmentViewModel.class);
        fm = getFragmentManager();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        convertDialog = DialogFactory.INSTANCE.getConvertDialog(context, fm);
        colorDialog = DialogFactory.INSTANCE.getColorDialog(context, fm);
        rankDialog = DialogFactory.INSTANCE.getRankDialog(context, fm);

        final Bundle bundle = getArguments();
        if (bundle != null) {
            rankEmpty = bundle.getBoolean(IntentDef.RANK_EMPTY);
        } else if (savedInstanceState != null) {
            rankEmpty = savedInstanceState.getBoolean(IntentDef.RANK_EMPTY);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IntentDef.RANK_EMPTY, rankEmpty);
    }

    /**
     * Установка в биндинг слоя неизменяемых данных, как интерфейсы
     */
    protected abstract void setupBinding();

    /**
     * Биндинг отображения элементов управления для конкретного режима редактирования
     *
     * @param editMode - Режим редактирования
     */
    public abstract void bindEdit(boolean editMode);

    @CallSuper
    protected void setupToolbar(@NonNull View view) {
        Log.i(TAG, "setupToolbar");

        final Toolbar toolbar = view.findViewById(R.id.toolbar_note_container);
        final View indicator = view.findViewById(R.id.toolbar_note_color_view);
        final NoteItem noteItem = vm.getNoteRepo().getNoteItem();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            menuControl = new MenuControl(context, activity.getWindow(), toolbar, indicator);
        } else {
            menuControl = new MenuControlAnim(context, activity.getWindow(), toolbar, indicator);
        }

        menuControl.setColor(noteItem.getColor());

        final NoteSt noteSt = noteCallback.getViewModel().getNoteSt();
        menuControl.setDrawable(noteSt.isEdit() && !noteSt.isCreate(), false);

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

            inputControl.onColorChange(noteItem.getColor(), check);
            bindInput();

            noteItem.setColor(check);
            noteRepo.setNoteItem(noteItem);

            vm.setNoteRepo(noteRepo);

            menuControl.startTint(check);
        });

        db = RoomDb.provideDb(context);
        final String[] name = db.daoRank().getName();
        db.close();

        rankDialog.setName(name);
        rankDialog.setPositiveListener((dialogInterface, i) -> {
            final boolean[] check = rankDialog.getCheck();

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

            inputControl.onRankChange(noteItem.getRankId(), rankId);

            noteItem.setRankId(rankId);
            noteItem.setRankPs(rankPs);
            noteRepo.setNoteItem(noteItem);

            vm.setNoteRepo(noteRepo);

            bindInput();
        });
    }

    @CallSuper
    protected void setupEnter(@NonNull View view) {
        Log.i(TAG, "setupEnter");

        nameEnter = view.findViewById(R.id.toolbar_note_enter);
        nameEnter.addTextChangedListener(
                new InputTextWatcher(nameEnter, InputDef.name, this, inputControl)
        );
    }

    public final void startTintToolbar(@ColorDef int colorFrom, @ColorDef int colorTo) {
        menuControl.setColorFrom(colorFrom);
        menuControl.startTint(colorTo);
    }

    public final NoteFragmentViewModel getViewModel() {
        return vm;
    }

    @Override
    public final void onMenuRankClick() {
        Log.i(TAG, "onMenuRankClick");

        HelpUtils.INSTANCE.hideKeyboard(context, activity.getCurrentFocus());

        final NoteItem noteItem = vm.getNoteRepo().getNoteItem();

        db = RoomDb.provideDb(context);
        final boolean[] check = db.daoRank().getCheck(noteItem.getRankId());
        db.close();

        rankDialog.setArguments(check);
        rankDialog.show(fm, DialogDef.RANK);
    }

    @Override
    public final void onMenuColorClick() {
        Log.i(TAG, "onMenuColorClick");

        HelpUtils.INSTANCE.hideKeyboard(context, activity.getCurrentFocus());

        final NoteItem noteItem = vm.getNoteRepo().getNoteItem();

        colorDialog.setArguments(noteItem.getColor());
        colorDialog.show(fm, DialogDef.COLOR);

        menuControl.setColorFrom(noteItem.getColor());
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

        bindEdit(false);

        db = RoomDb.provideDb(context);
        db.daoNote().update(noteItem.getId(), noteItem.isStatus());
        db.close();

        noteRepo.setNoteItem(noteItem);

        vm.setNoteRepo(noteRepo);

        noteCallback.getViewModel().setNoteRepo(noteRepo);
    }

    @Override
    public final void onMenuConvertClick() {
        Log.i(TAG, "onMenuConvertClick");

        convertDialog.show(fm, DialogDef.CONVERT);
    }

}