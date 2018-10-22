package sgtmelon.scriptum.app.injection.module;

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

    @Provides
    public DlgSheetAdd provideDlgSheetAdd(FragmentManager fm) {
        DlgSheetAdd dlgSheetAdd = (DlgSheetAdd) fm.findFragmentByTag(DefDlg.SHEET_ADD);
        if (dlgSheetAdd == null) dlgSheetAdd = new DlgSheetAdd();

        return dlgSheetAdd;
    }

    @Provides
    public DlgMessage provideDlgMessage(FragmentManager fm) {
        DlgMessage dlgMessage = (DlgMessage) fm.findFragmentByTag(DefDlg.MESSAGE);
        if (dlgMessage == null) dlgMessage = new DlgMessage();

        return dlgMessage;
    }

    @Provides
    public DlgColor provideDlgColor(FragmentManager fm) {
        DlgColor dlgColor = (DlgColor) fm.findFragmentByTag(DefDlg.COLOR);
        if (dlgColor == null) dlgColor = new DlgColor();

        return dlgColor;
    }

    @Provides
    public DlgMultiply provideDlgMultiply(FragmentManager fm) {
        DlgMultiply dlgMultiply = (DlgMultiply) fm.findFragmentByTag(DefDlg.MULTIPLY);
        if (dlgMultiply == null) dlgMultiply = new DlgMultiply();

        return dlgMultiply;
    }

}
