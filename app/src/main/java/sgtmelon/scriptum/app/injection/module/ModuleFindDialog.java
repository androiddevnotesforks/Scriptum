package sgtmelon.scriptum.app.injection.module;

import androidx.fragment.app.FragmentManager;
import dagger.Module;
import dagger.Provides;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.injection.ScopeArch;
import sgtmelon.scriptum.element.*;
import sgtmelon.scriptum.element.common.DlgMessage;
import sgtmelon.scriptum.element.common.DlgMultiply;
import sgtmelon.scriptum.element.common.DlgSheet;
import sgtmelon.scriptum.element.common.DlgSingle;
import sgtmelon.scriptum.office.annot.def.DefDlg;

import javax.inject.Named;

@Module
public final class ModuleFindDialog {

    @Provides
    @ScopeArch
    DlgRename provideDlgRename(FragmentManager fm) {
        DlgRename dlgRename = (DlgRename) fm.findFragmentByTag(DefDlg.RENAME);
        if (dlgRename == null) dlgRename = new DlgRename();

        return dlgRename;
    }

    @Provides
    @ScopeArch
    DlgSheet provideDlgSheetAdd(FragmentManager fm) {
        DlgSheet dlgSheetAdd = (DlgSheet) fm.findFragmentByTag(DefDlg.SHEET_ADD);
        if (dlgSheetAdd == null) {
            dlgSheetAdd = new DlgSheet();
            dlgSheetAdd.setLayout(R.layout.sheet_add);
            dlgSheetAdd.setNavigation(R.id.add_navigation);
        }
        return dlgSheetAdd;
    }

    @Provides
    @ScopeArch
    DlgOptionNote provideDlgOptionNote(FragmentManager fm) {
        DlgOptionNote dlgOptionNote = (DlgOptionNote) fm.findFragmentByTag(DefDlg.OPTIONS);
        if (dlgOptionNote == null) dlgOptionNote = new DlgOptionNote();

        return dlgOptionNote;
    }

    @Provides
    @ScopeArch
    DlgOptionBin provideDlgOptionBin(FragmentManager fm) {
        DlgOptionBin dlgOptionBin = (DlgOptionBin) fm.findFragmentByTag(DefDlg.OPTIONS);
        if (dlgOptionBin == null) dlgOptionBin = new DlgOptionBin();

        return dlgOptionBin;
    }

    @Provides
    @ScopeArch
    @Named(DefDlg.CLEAR_BIN)
    DlgMessage provideDlgClearBin(FragmentManager fm) {
        DlgMessage dlgClearBin = (DlgMessage) fm.findFragmentByTag(DefDlg.CLEAR_BIN);
        if (dlgClearBin == null) dlgClearBin = new DlgMessage();

        return dlgClearBin;
    }

    @Provides
    @ScopeArch
    @Named(DefDlg.CONVERT)
    DlgMessage provideDlgConvert(FragmentManager fm) {
        DlgMessage dlgConvert = (DlgMessage) fm.findFragmentByTag(DefDlg.CONVERT);
        if (dlgConvert == null) dlgConvert = new DlgMessage();

        return dlgConvert;
    }

    @Provides
    @ScopeArch
    @Named(DefDlg.RANK)
    DlgMultiply provideDlgRank(FragmentManager fm) {
        DlgMultiply dlgRank = (DlgMultiply) fm.findFragmentByTag(DefDlg.RANK);
        if (dlgRank == null) dlgRank = new DlgMultiply();

        return dlgRank;
    }

    @Provides
    @ScopeArch
    DlgColor provideDlgColor(FragmentManager fm) {
        DlgColor dlgColor = (DlgColor) fm.findFragmentByTag(DefDlg.COLOR);
        if (dlgColor == null) dlgColor = new DlgColor();

        return dlgColor;
    }

    @Provides
    @ScopeArch
    DlgSort provideDlgSort(FragmentManager fm) {
        DlgSort dlgSort = (DlgSort) fm.findFragmentByTag(DefDlg.SORT);
        if (dlgSort == null) dlgSort = new DlgSort();

        return dlgSort;
    }

    @Provides
    @ScopeArch
    @Named(DefDlg.SAVE_TIME)
    DlgSingle provideDlgSaveTime(FragmentManager fm) {
        DlgSingle dlgSaveTime = (DlgSingle) fm.findFragmentByTag(DefDlg.SAVE_TIME);
        if (dlgSaveTime == null) dlgSaveTime = new DlgSingle();

        return dlgSaveTime;
    }

    @Provides
    @ScopeArch
    @Named(DefDlg.THEME)
    DlgSingle provideDlgTheme(FragmentManager fm) {
        DlgSingle dlgTheme = (DlgSingle) fm.findFragmentByTag(DefDlg.THEME);
        if (dlgTheme == null) dlgTheme = new DlgSingle();

        return dlgTheme;
    }

    @Provides
    @ScopeArch
    DlgInfo provideDlgInfo(FragmentManager fm) {
        DlgInfo dlgInfo = (DlgInfo) fm.findFragmentByTag(DefDlg.INFO);
        if (dlgInfo == null) dlgInfo = new DlgInfo();

        return dlgInfo;
    }

}
