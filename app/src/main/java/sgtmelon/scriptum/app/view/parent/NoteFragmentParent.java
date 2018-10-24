package sgtmelon.scriptum.app.view.parent;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import sgtmelon.safedialog.library.ColorDialog;
import sgtmelon.safedialog.library.MessageDialog;
import sgtmelon.safedialog.library.MultiplyDialog;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.control.MenuControl;
import sgtmelon.scriptum.app.control.MenuControlAnim;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.injection.component.DaggerFragmentComponent;
import sgtmelon.scriptum.app.injection.component.FragmentComponent;
import sgtmelon.scriptum.app.injection.module.FragmentArchModule;
import sgtmelon.scriptum.app.injection.module.blank.FragmentBlankModule;
import sgtmelon.scriptum.app.model.NoteModel;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.view.callback.NoteCallback;
import sgtmelon.scriptum.app.vm.activity.ActivityNoteViewModel;
import sgtmelon.scriptum.app.vm.fragment.FragmentNoteViewModel;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.DialogDef;
import sgtmelon.scriptum.office.conv.ListConv;
import sgtmelon.scriptum.office.intf.MenuIntf;
import sgtmelon.scriptum.office.st.NoteSt;

public abstract class NoteFragmentParent extends Fragment
        implements View.OnClickListener, MenuIntf.Note.NoteMenuClick {

    private static final String TAG = NoteFragmentParent.class.getSimpleName();

    protected Context context;
    protected Activity activity;
    protected NoteCallback noteCallback;

    @Inject
    protected FragmentManager fm;
    @Inject
    @Named(DialogDef.CONVERT)
    protected MessageDialog dlgConvert;
    protected RoomDb db;
    protected View frgView;
    @Inject
    protected FragmentNoteViewModel vm;
    protected MenuControl menuControl;

    @Inject
    ColorDialog colorDialog;
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
            throw new IllegalStateException("NoteCallback interface not installed in " + TAG);
        }

        if (context instanceof MenuIntf.Note.DeleteMenuClick) {
            deleteMenuClick = (MenuIntf.Note.DeleteMenuClick) context;
        } else {
            throw new IllegalStateException("MenuIntf.Note.DeleteMenuClick interface not installed in " + TAG);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        FragmentComponent fragmentComponent = DaggerFragmentComponent.builder()
                .fragmentBlankModule(new FragmentBlankModule(this))
                .fragmentArchModule(new FragmentArchModule(inflater, container))
                .build();
        fragmentComponent.inject(this);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected abstract void bind(boolean keyEdit);

    protected void setupToolbar() {
        Log.i(TAG, "setupToolbar");

        NoteItem noteItem = vm.getNoteModel().getNoteItem();

        Toolbar toolbar = frgView.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.activity_note);

        View indicator = frgView.findViewById(R.id.color_view);

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

        NoteSt noteSt = noteCallback.getViewModel().getNoteSt();

        menuControl.setupDrawable();
        menuControl.setDrawable(noteSt.isEdit() && !noteSt.isCreate(), false);

        menuControl.setupMenu(noteItem.isStatus());

        toolbar.setOnMenuItemClickListener(menuControl);
        toolbar.setNavigationOnClickListener(this);
    }

    protected void setupDialog() {
        Log.i(TAG, "setupDialog");

        colorDialog.setTitle(getString(R.string.dialog_title_color));
        colorDialog.setPositiveListener((dialogInterface, i) -> {
            int check = colorDialog.getCheck();

            NoteModel noteModel = vm.getNoteModel();
            NoteItem noteItem = noteModel.getNoteItem();
            noteItem.setColor(check);
            noteModel.setNoteItem(noteItem);

            vm.setNoteModel(noteModel);

            menuControl.startTint(check);
        });

        db = RoomDb.provideDb(context);
        String[] name = db.daoRank().getName();
        db.close();

        dlgRank.setRows(name);
        dlgRank.setPositiveListener((dialogInterface, i) -> {
            boolean[] check = dlgRank.getCheck();

            db = RoomDb.provideDb(context);
            Long[] id = db.daoRank().getId();
            db.close();

            List<Long> rankId = new ArrayList<>();
            List<Long> rankPs = new ArrayList<>();

            for (int j = 0; j < id.length; j++) {
                if (check[j]) {
                    rankId.add(id[j]);
                    rankPs.add((long) j);
                }
            }

            NoteModel noteModel = vm.getNoteModel();

            NoteItem noteItem = noteModel.getNoteItem();
            noteItem.setRankId(ListConv.fromList(rankId));
            noteItem.setRankPs(ListConv.fromList(rankPs));
            noteModel.setNoteItem(noteItem);

            vm.setNoteModel(noteModel);
        });
    }

    public MenuControl getMenuControl() {
        return menuControl;
    }

    public void setMenuControl(MenuControl menuControl) {
        this.menuControl = menuControl;
    }

    public FragmentNoteViewModel getViewModel() {
        return vm;
    }

    public void setViewModel(FragmentNoteViewModel viewModel) {
        vm = viewModel;
    }

    @Override
    public void onMenuRankClick() {
        Log.i(TAG, "onMenuRankClick");

        Help.hideKeyboard(context, activity.getCurrentFocus());

        NoteItem noteItem = vm.getNoteModel().getNoteItem();

        db = RoomDb.provideDb(context);
        boolean[] check = db.daoRank().getCheck(noteItem.getRankId());
        db.close();

        dlgRank.setArguments(check);
        dlgRank.show(fm, DialogDef.RANK);
    }

    @Override
    public void onMenuColorClick() {
        Log.i(TAG, "onMenuColorClick");

        Help.hideKeyboard(context, activity.getCurrentFocus());

        NoteItem noteItem = vm.getNoteModel().getNoteItem();

        colorDialog.setArguments(noteItem.getColor());
        colorDialog.show(fm, DialogDef.COLOR);

        menuControl.setStartColor(noteItem.getColor());
    }

    @Override
    public void onMenuBindClick() {
        Log.i(TAG, "onMenuBindClick");

        NoteModel noteModel = vm.getNoteModel();
        NoteItem noteItem = noteModel.getNoteItem();

        if (!noteItem.isStatus()) {
            noteItem.setStatus(true);
            noteModel.updateItemStatus(true);
        } else {
            noteItem.setStatus(false);
            noteModel.updateItemStatus(false);
        }

        menuControl.setStatusTitle(noteItem.isStatus());

        db = RoomDb.provideDb(context);
        db.daoNote().update(noteItem.getId(), noteItem.isStatus());
        db.close();

        noteModel.setNoteItem(noteItem);

        vm.setNoteModel(noteModel);

        ActivityNoteViewModel viewModel = noteCallback.getViewModel();
        viewModel.setNoteModel(noteModel);
        noteCallback.setViewModel(viewModel);
    }

    @Override
    public void onMenuConvertClick() {
        Log.i(TAG, "onMenuConvertClick");

        dlgConvert.show(fm, DialogDef.CONVERT);
    }

}
