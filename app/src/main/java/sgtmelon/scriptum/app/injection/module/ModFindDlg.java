package sgtmelon.scriptum.app.injection.module;

import javax.inject.Named;

import androidx.fragment.app.FragmentManager;
import dagger.Module;
import dagger.Provides;
import sgtmelon.scriptum.element.dialog.DlgColor;
import sgtmelon.scriptum.element.dialog.DlgOptionBin;
import sgtmelon.scriptum.element.dialog.DlgOptionNote;
import sgtmelon.scriptum.element.dialog.DlgRename;
import sgtmelon.scriptum.element.dialog.DlgSheetAdd;
import sgtmelon.scriptum.element.dialog.common.DlgMessage;
import sgtmelon.scriptum.element.dialog.common.DlgMultiply;
import sgtmelon.scriptum.office.annot.def.DefDlg;

@Module
public class ModFindDlg {

    @Provides
    public DlgRename provideDlgRename(FragmentManager fm) {
        DlgRename dlgRename = (DlgRename) fm.findFragmentByTag(DefDlg.RENAME);
        if (dlgRename == null) dlgRename = new DlgRename();

        return dlgRename;
    }

    @Provides
    public DlgSheetAdd provideDlgSheetAdd(FragmentManager fm) {
        DlgSheetAdd dlgSheetAdd = (DlgSheetAdd) fm.findFragmentByTag(DefDlg.SHEET_ADD);
        if (dlgSheetAdd == null) dlgSheetAdd = new DlgSheetAdd();

        return dlgSheetAdd;
    }

    @Provides
    public DlgOptionNote provideDlgOptionNote(FragmentManager fm) {
        DlgOptionNote dlgOptionNote = (DlgOptionNote) fm.findFragmentByTag(DefDlg.OPTIONS);
        if (dlgOptionNote == null) dlgOptionNote = new DlgOptionNote();

        return dlgOptionNote;
    }

    @Provides
    public DlgOptionBin provideDlgOptionBin(FragmentManager fm) {
        DlgOptionBin dlgOptionBin = (DlgOptionBin) fm.findFragmentByTag(DefDlg.OPTIONS);
        if (dlgOptionBin == null) dlgOptionBin = new DlgOptionBin();

        return dlgOptionBin;
    }

    @Provides @Named(DefDlg.CLEAR_BIN)
    public DlgMessage provideDlgClearBin(FragmentManager fm) {
        DlgMessage dlgClearBin = (DlgMessage) fm.findFragmentByTag(DefDlg.CLEAR_BIN);
        if (dlgClearBin == null) dlgClearBin = new DlgMessage();

        return dlgClearBin;
    }

    @Provides @Named(DefDlg.CONVERT)
    public DlgMessage provideDlgConvert(FragmentManager fm) {
        DlgMessage dlgConvert = (DlgMessage) fm.findFragmentByTag(DefDlg.CONVERT);
        if (dlgConvert == null) dlgConvert = new DlgMessage();

        return dlgConvert;
    }

    @Provides @Named(DefDlg.RANK)
    public DlgMultiply provideDlgRank(FragmentManager fm) {
        DlgMultiply dlgRank = (DlgMultiply) fm.findFragmentByTag(DefDlg.RANK);
        if (dlgRank == null) dlgRank = new DlgMultiply();

        return dlgRank;
    }

    @Provides
    public DlgColor provideDlgColor(FragmentManager fm) {
        DlgColor dlgColor = (DlgColor) fm.findFragmentByTag(DefDlg.COLOR);
        if (dlgColor == null) dlgColor = new DlgColor();

        return dlgColor;
    }

}
