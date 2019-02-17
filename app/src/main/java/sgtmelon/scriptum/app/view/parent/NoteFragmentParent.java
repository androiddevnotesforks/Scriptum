package sgtmelon.scriptum.app.view.parent;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import sgtmelon.safedialog.library.MessageDialog;
import sgtmelon.safedialog.library.MultiplyDialog;
import sgtmelon.safedialog.library.color.ColorDialog;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.control.InputControl;
import sgtmelon.scriptum.app.control.MenuControl;
import sgtmelon.scriptum.app.control.MenuControlAnim;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.factory.DialogFactory;
import sgtmelon.scriptum.app.view.callback.NoteCallback;
import sgtmelon.scriptum.app.view.fragment.note.RollNoteFragment;
import sgtmelon.scriptum.app.view.fragment.note.TextNoteFragment;
import sgtmelon.scriptum.app.vm.fragment.note.ParentNoteViewModel;
import sgtmelon.scriptum.office.annot.def.ColorDef;
import sgtmelon.scriptum.office.annot.def.DialogDef;
import sgtmelon.scriptum.office.annot.def.InputDef;
import sgtmelon.scriptum.office.annot.def.IntentDef;
import sgtmelon.scriptum.office.intf.BindIntf;
import sgtmelon.scriptum.office.intf.InputTextWatcher;
import sgtmelon.scriptum.office.intf.MenuIntf;
import sgtmelon.scriptum.office.state.NoteState;
import sgtmelon.scriptum.office.utils.AppUtils;

/**
 * Класс родитель для фрагментов редактирования заметок
 * {@link TextNoteFragment}, {@link RollNoteFragment}
 */
public abstract class NoteFragmentParent extends Fragment implements
        View.OnClickListener, BindIntf, MenuIntf.Note.NoteMenuClick {

    protected final InputControl inputControl = new InputControl();

    // TODO: 17.12.2018 сделать долгое нажатие undo/redo

    protected Context context;
    protected Activity activity;
    protected NoteCallback noteCallback;

    protected MessageDialog convertDialog;

    protected EditText nameEnter;

    protected ParentNoteViewModel vm;
    private FragmentManager fm;
    protected RoomDb db;

    protected boolean rankEmpty;

    protected MenuControl menuControl;
    protected MenuIntf.Note.DeleteMenuClick deleteMenuClick;

    private ColorDialog colorDialog;
    private MultiplyDialog rankDialog;

    @Override
    public void onAttach(Context context) { // TODO as ?:
        super.onAttach(context);

        this.context = context;
        activity = getActivity();

        if (context instanceof NoteCallback) {
            noteCallback = (NoteCallback) context;
        } else {
            throw new ClassCastException(NoteCallback.class.getSimpleName() +
                    " interface not installed");
        }

        if (context instanceof MenuIntf.Note.DeleteMenuClick) {
            deleteMenuClick = (MenuIntf.Note.DeleteMenuClick) context;
        } else {
            throw new ClassCastException(MenuIntf.Note.DeleteMenuClick.class.getSimpleName() +
                    " interface not installed");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
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
        final Toolbar toolbar = view.findViewById(R.id.toolbar_note_container);
        final View indicator = view.findViewById(R.id.toolbar_note_color_view);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            menuControl = new MenuControl(context, activity.getWindow(), toolbar, indicator);
        } else {
            menuControl = new MenuControlAnim(context, activity.getWindow(), toolbar, indicator);
        }

        menuControl.setColor(vm.getNoteColor());

        final NoteState noteState = noteCallback.getViewModel().getNoteState();
        menuControl.setDrawable(noteState.isEdit() && !noteState.isCreate(), false);

        toolbar.setNavigationOnClickListener(this);
    }

    @CallSuper
    protected void setupDialog() {
        colorDialog.setTitle(getString(R.string.dialog_title_color));
        colorDialog.setPositiveListener((dialogInterface, i) -> {
            final int check = colorDialog.getCheck();

            vm.onColorDialog(check);
            bindInput();

            menuControl.startTint(check);
        });

        rankDialog.setName(vm.getRankDialogName());
        rankDialog.setPositiveListener((dialogInterface, i) -> {
            vm.onRankDialog(rankDialog.getCheck());
            bindInput();
        });
    }

    @CallSuper
    protected void setupEnter(@NonNull View view) {
        nameEnter = view.findViewById(R.id.toolbar_note_enter);
        nameEnter.addTextChangedListener(
                new InputTextWatcher(nameEnter, InputDef.name, this, inputControl)
        );
    }

    public final void startTintToolbar(@ColorDef int colorFrom, @ColorDef int colorTo) {
        menuControl.setColorFrom(colorFrom);
        menuControl.startTint(colorTo);
    }

    public final ParentNoteViewModel getViewModel() {
        return vm;
    }

    @Override
    public final void onMenuRankClick() {
        AppUtils.INSTANCE.hideKeyboard(activity);

        rankDialog.setArguments(vm.onMenuRank());
        rankDialog.show(fm, DialogDef.RANK);
    }

    @Override
    public final void onMenuColorClick() {
        AppUtils.INSTANCE.hideKeyboard(activity);

        final int color = vm.getNoteColor();

        colorDialog.setArguments(color);
        colorDialog.show(fm, DialogDef.COLOR);

        menuControl.setColorFrom(color);
    }

    @Override
    public final void onMenuBindClick() {
        vm.onMenuBind();
        bindEdit(false); // TODO save
    }

    @Override
    public final void onMenuConvertClick() {
        AppUtils.INSTANCE.hideKeyboard(activity);
        convertDialog.show(fm, DialogDef.CONVERT);
    }

}