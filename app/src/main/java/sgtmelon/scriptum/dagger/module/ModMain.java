package sgtmelon.scriptum.dagger.module;

import android.content.Context;

import javax.inject.Singleton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import dagger.Module;
import dagger.Provides;
import sgtmelon.scriptum.app.view.frg.FrgBin;
import sgtmelon.scriptum.app.view.frg.FrgNotes;
import sgtmelon.scriptum.app.view.frg.FrgRank;
import sgtmelon.scriptum.element.dialog.DlgSheetAdd;
import sgtmelon.scriptum.office.annot.def.DefDlg;
import sgtmelon.scriptum.office.annot.def.DefFrg;
import sgtmelon.scriptum.office.st.StPage;

@Module
public class ModMain {

    private FragmentManager fm;

    public ModMain(AppCompatActivity activity) {
        fm = activity.getSupportFragmentManager();
    }

    @Provides
    public FragmentManager provideFragmentManager() {
        return fm;
    }

    @Provides
    @Singleton
    public StPage provideStPage() {
        return new StPage();
    }

    @Provides
    @Singleton
    public FrgRank provideFrgRank() {
        FrgRank frgRank = (FrgRank) fm.findFragmentByTag(DefFrg.RANK);
        if (frgRank == null) frgRank = new FrgRank();

        return frgRank;
    }

    @Provides
    @Singleton
    public FrgNotes provideFrgNotes() {
        FrgNotes frgNotes = (FrgNotes) fm.findFragmentByTag(DefFrg.NOTES);
        if (frgNotes == null) frgNotes = new FrgNotes();

        return frgNotes;
    }

    @Provides
    @Singleton
    public FrgBin provideFrgBin() {
        FrgBin frgBin = (FrgBin) fm.findFragmentByTag(DefFrg.BIN);
        if (frgBin == null) frgBin = new FrgBin();

        return frgBin;
    }

    @Provides
    @Singleton
    public DlgSheetAdd provideDlgSheetAdd() {
        DlgSheetAdd dlgSheetAdd = (DlgSheetAdd) fm.findFragmentByTag(DefDlg.SHEET_ADD);
        if (dlgSheetAdd == null) dlgSheetAdd = new DlgSheetAdd();

        return dlgSheetAdd;
    }

}
