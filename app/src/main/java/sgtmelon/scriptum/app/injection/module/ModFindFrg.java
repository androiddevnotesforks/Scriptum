package sgtmelon.scriptum.app.injection.module;

import androidx.fragment.app.FragmentManager;
import dagger.Module;
import dagger.Provides;
import sgtmelon.scriptum.app.view.frg.FrgBin;
import sgtmelon.scriptum.app.view.frg.FrgNotes;
import sgtmelon.scriptum.app.view.frg.FrgRank;
import sgtmelon.scriptum.office.annot.def.DefFrg;

@Module
public class ModFindFrg {

    @Provides
    public FrgRank provideFrgRank(FragmentManager fm) {
        FrgRank frgRank = (FrgRank) fm.findFragmentByTag(DefFrg.RANK);
        if (frgRank == null) frgRank = new FrgRank();

        return frgRank;
    }

    @Provides
    public FrgNotes provideFrgNotes(FragmentManager fm) {
        FrgNotes frgNotes = (FrgNotes) fm.findFragmentByTag(DefFrg.NOTES);
        if (frgNotes == null) frgNotes = new FrgNotes();

        return frgNotes;
    }

    @Provides
    public FrgBin provideFrgBin(FragmentManager fm) {
        FrgBin frgBin = (FrgBin) fm.findFragmentByTag(DefFrg.BIN);
        if (frgBin == null) frgBin = new FrgBin();

        return frgBin;
    }

}
