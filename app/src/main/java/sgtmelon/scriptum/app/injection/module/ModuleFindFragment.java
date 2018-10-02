package sgtmelon.scriptum.app.injection.module;

import androidx.fragment.app.FragmentManager;
import dagger.Module;
import dagger.Provides;
import sgtmelon.scriptum.app.injection.ScopeArch;
import sgtmelon.scriptum.app.view.fragment.BinFragment;
import sgtmelon.scriptum.app.view.fragment.NotesFragment;
import sgtmelon.scriptum.app.view.fragment.RankFragment;
import sgtmelon.scriptum.office.annot.def.DefFrg;

@Module
public final class ModuleFindFragment {

    @Provides
    @ScopeArch
    RankFragment provideRankFragment(FragmentManager fm) {
        RankFragment rankFragment = (RankFragment) fm.findFragmentByTag(DefFrg.RANK);
        if (rankFragment == null) rankFragment = new RankFragment();

        return rankFragment;
    }

    @Provides
    @ScopeArch
    NotesFragment provideNotesFragment(FragmentManager fm) {
        NotesFragment notesFragment = (NotesFragment) fm.findFragmentByTag(DefFrg.NOTES);
        if (notesFragment == null) notesFragment = new NotesFragment();

        return notesFragment;
    }

    @Provides
    @ScopeArch
    BinFragment provideBinFragment(FragmentManager fm) {
        BinFragment binFragment = (BinFragment) fm.findFragmentByTag(DefFrg.BIN);
        if (binFragment == null) binFragment = new BinFragment();

        return binFragment;
    }

}
