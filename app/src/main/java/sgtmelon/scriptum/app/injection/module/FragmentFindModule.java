package sgtmelon.scriptum.app.injection.module;

import androidx.fragment.app.FragmentManager;
import dagger.Module;
import dagger.Provides;
import sgtmelon.scriptum.app.injection.ArchScope;
import sgtmelon.scriptum.app.view.fragment.BinFragment;
import sgtmelon.scriptum.app.view.fragment.NotesFragment;
import sgtmelon.scriptum.app.view.fragment.RankFragment;
import sgtmelon.scriptum.office.annot.def.FragmentDef;

@Module
public final class FragmentFindModule {

    @Provides
    @ArchScope
    RankFragment provideRankFragment(FragmentManager fm) {
        RankFragment rankFragment = (RankFragment) fm.findFragmentByTag(FragmentDef.RANK);
        if (rankFragment == null) rankFragment = new RankFragment();

        return rankFragment;
    }

    @Provides
    @ArchScope
    NotesFragment provideNotesFragment(FragmentManager fm) {
        NotesFragment notesFragment = (NotesFragment) fm.findFragmentByTag(FragmentDef.NOTES);
        if (notesFragment == null) notesFragment = new NotesFragment();

        return notesFragment;
    }

    @Provides
    @ArchScope
    BinFragment provideBinFragment(FragmentManager fm) {
        BinFragment binFragment = (BinFragment) fm.findFragmentByTag(FragmentDef.BIN);
        if (binFragment == null) binFragment = new BinFragment();

        return binFragment;
    }

}