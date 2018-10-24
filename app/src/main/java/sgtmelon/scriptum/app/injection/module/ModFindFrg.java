package sgtmelon.scriptum.app.injection.module;

import androidx.fragment.app.FragmentManager;
import dagger.Module;
import dagger.Provides;
import sgtmelon.scriptum.app.injection.ScopeApp;
import sgtmelon.scriptum.app.view.frg.FrgBin;
import sgtmelon.scriptum.app.view.frg.FrgNotes;
import sgtmelon.scriptum.app.view.frg.FrgRank;
import sgtmelon.scriptum.office.annot.def.DefFrg;

@Module
public class ModFindFrg {

    @Provides
    @ScopeApp
    FrgRank provideFrgRank(FragmentManager fm) {
        FrgRank frgRank = (FrgRank) fm.findFragmentByTag(DefFrg.RANK);
        if (frgRank == null) frgRank = new FrgRank();

        return frgRank;
    }

    @Provides
    @ScopeApp
    FrgNotes provideFrgNotes(FragmentManager fm) {
        FrgNotes frgNotes = (FrgNotes) fm.findFragmentByTag(DefFrg.NOTES);
        if (frgNotes == null) frgNotes = new FrgNotes();

        return frgNotes;
    }

    @Provides
    @ScopeApp
    FrgBin provideFrgBin(FragmentManager fm) {
        FrgBin frgBin = (FrgBin) fm.findFragmentByTag(DefFrg.BIN);
        if (frgBin == null) frgBin = new FrgBin();

        return frgBin;
    }

}
