package sgtmelon.scriptum.app.injection.module;

import javax.inject.Named;

import androidx.fragment.app.FragmentManager;
import dagger.Module;
import dagger.Provides;
import sgtmelon.scriptum.app.injection.ScopeApp;
import sgtmelon.scriptum.element.dialog.DlgColor;
import sgtmelon.scriptum.element.dialog.DlgInfo;
import sgtmelon.scriptum.element.dialog.DlgOptionBin;
import sgtmelon.scriptum.element.dialog.DlgOptionNote;
import sgtmelon.scriptum.element.dialog.DlgRename;
import sgtmelon.scriptum.element.dialog.DlgSheetAdd;
import sgtmelon.scriptum.element.dialog.DlgSort;
import sgtmelon.scriptum.element.dialog.common.DlgMessage;
import sgtmelon.scriptum.element.dialog.common.DlgMultiply;
import sgtmelon.scriptum.element.dialog.common.DlgSingle;
import sgtmelon.scriptum.office.annot.def.DefDlg;

@Module
public class ModFindDlg {

    @Provides
    @ScopeApp
    DlgRename provideDlgRename(FragmentManager fm) {
        DlgRename dlgRename = (DlgRename) fm.findFragmentByTag(DefDlg.RENAME);
        if (dlgRename == null) dlgRename = new DlgRename();

        return dlgRename;
    }

    @Provides
    @ScopeApp
    DlgSheetAdd provideDlgSheetAdd(FragmentManager fm) {
        DlgSheetAdd dlgSheetAdd = (DlgSheetAdd) fm.findFragmentByTag(DefDlg.SHEET_ADD);
        if (dlgSheetAdd == null) dlgSheetAdd = new DlgSheetAdd();

        return dlgSheetAdd;
    }

    @Provides
    @ScopeApp
    DlgOptionNote provideDlgOptionNote(FragmentManager fm) {
        DlgOptionNote dlgOptionNote = (DlgOptionNote) fm.findFragmentByTag(DefDlg.OPTIONS);
        if (dlgOptionNote == null) dlgOptionNote = new DlgOptionNote();

        return dlgOptionNote;
    }

    @Provides
    @ScopeApp
    DlgOptionBin provideDlgOptionBin(FragmentManager fm) {
        DlgOptionBin dlgOptionBin = (DlgOptionBin) fm.findFragmentByTag(DefDlg.OPTIONS);
        if (dlgOptionBin == null) dlgOptionBin = new DlgOptionBin();

        return dlgOptionBin;
    }

    @Provides
    @ScopeApp
    @Named(DefDlg.CLEAR_BIN)
    DlgMessage provideDlgClearBin(FragmentManager fm) {
        DlgMessage dlgClearBin = (DlgMessage) fm.findFragmentByTag(DefDlg.CLEAR_BIN);
        if (dlgClearBin == null) dlgClearBin = new DlgMessage();

        return dlgClearBin;
    }

    @Provides
    @ScopeApp
    @Named(DefDlg.CONVERT)
    DlgMessage provideDlgConvert(FragmentManager fm) {
        DlgMessage dlgConvert = (DlgMessage) fm.findFragmentByTag(DefDlg.CONVERT);
        if (dlgConvert == null) dlgConvert = new DlgMessage();

        return dlgConvert;
    }

    @Provides
    @ScopeApp
    @Named(DefDlg.RANK)
    DlgMultiply provideDlgRank(FragmentManager fm) {
        DlgMultiply dlgRank = (DlgMultiply) fm.findFragmentByTag(DefDlg.RANK);
        if (dlgRank == null) dlgRank = new DlgMultiply();

        return dlgRank;
    }

    @Provides
    @ScopeApp
    DlgColor provideDlgColor(FragmentManager fm) {
        DlgColor dlgColor = (DlgColor) fm.findFragmentByTag(DefDlg.COLOR);
        if (dlgColor == null) dlgColor = new DlgColor();

        return dlgColor;
    }

    @Provides
    @ScopeApp
    DlgSort provideDlgSort(FragmentManager fm) {
        DlgSort dlgSort = (DlgSort) fm.findFragmentByTag(DefDlg.SORT);
        if (dlgSort == null) dlgSort = new DlgSort();

        return dlgSort;
    }

    @Provides
    @ScopeApp
    @Named(DefDlg.SAVE_TIME)
    DlgSingle provideDlgSaveTime(FragmentManager fm) {
        DlgSingle dlgSaveTime = (DlgSingle) fm.findFragmentByTag(DefDlg.SAVE_TIME);
        if (dlgSaveTime == null) dlgSaveTime = new DlgSingle();

        return dlgSaveTime;
    }

    @Provides
    @ScopeApp
    @Named(DefDlg.THEME)
    DlgSingle provideDlgTheme(FragmentManager fm) {
        DlgSingle dlgTheme = (DlgSingle) fm.findFragmentByTag(DefDlg.THEME);
        if (dlgTheme == null) dlgTheme = new DlgSingle();

        return dlgTheme;
    }

    @Provides
    @ScopeApp
    DlgInfo provideDlgInfo(FragmentManager fm) {
        DlgInfo dlgInfo = (DlgInfo) fm.findFragmentByTag(DefDlg.INFO);
        if (dlgInfo == null) dlgInfo = new DlgInfo();

        return dlgInfo;
    }

}
