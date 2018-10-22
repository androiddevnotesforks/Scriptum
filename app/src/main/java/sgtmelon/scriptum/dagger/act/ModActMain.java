package sgtmelon.scriptum.dagger.act;

import androidx.fragment.app.FragmentManager;
import dagger.Module;
import dagger.Provides;
import sgtmelon.scriptum.app.view.frg.main.FrgBin;
import sgtmelon.scriptum.app.view.frg.main.FrgNotes;
import sgtmelon.scriptum.app.view.frg.main.FrgRank;
import sgtmelon.scriptum.element.dialog.DlgSheetAdd;
import sgtmelon.scriptum.office.annot.def.DefDlg;
import sgtmelon.scriptum.office.annot.def.DefFrg;
import sgtmelon.scriptum.office.st.StPage;

@Module(includes = ModAct.class)
class ModActMain {

    @Provides
    StPage provideStPage() {
        return new StPage();
    }

    @Provides
    FrgRank provideFrgRank(FragmentManager fm) {
        FrgRank frgRank = (FrgRank) fm.findFragmentByTag(DefFrg.RANK);
        if (frgRank == null) frgRank = new FrgRank();

        return frgRank;
    }

    @Provides
    FrgNotes provideFrgNotes(FragmentManager fm) {
        FrgNotes frgNotes = (FrgNotes) fm.findFragmentByTag(DefFrg.NOTES);
        if (frgNotes == null) frgNotes = new FrgNotes();

        return frgNotes;
    }

    @Provides
    FrgBin provideFrgBin(FragmentManager fm) {
        FrgBin frgBin = (FrgBin) fm.findFragmentByTag(DefFrg.BIN);
        if (frgBin == null) frgBin = new FrgBin();

        return frgBin;
    }

    @Provides
    DlgSheetAdd provideDlgSheetAdd(FragmentManager fm) {
        DlgSheetAdd dlgSheetAdd = (DlgSheetAdd) fm.findFragmentByTag(DefDlg.SHEET_ADD);
        if (dlgSheetAdd == null) dlgSheetAdd = new DlgSheetAdd();

        return dlgSheetAdd;
    }

}
