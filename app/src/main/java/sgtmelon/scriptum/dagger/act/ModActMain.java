package sgtmelon.scriptum.dagger.act;

import androidx.fragment.app.FragmentManager;
import dagger.Module;
import dagger.Provides;
import sgtmelon.scriptum.app.view.frg.main.FrgBin;
import sgtmelon.scriptum.app.view.frg.main.FrgNotes;
import sgtmelon.scriptum.app.view.frg.main.FrgRank;
import sgtmelon.scriptum.office.annot.def.DefFrg;

@Module(includes = ModAct.class)
class ModActMain {

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

}
