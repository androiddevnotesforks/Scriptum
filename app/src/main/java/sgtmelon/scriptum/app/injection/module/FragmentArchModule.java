package sgtmelon.scriptum.app.injection.module;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import dagger.Module;
import dagger.Provides;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.injection.ArchScope;
import sgtmelon.scriptum.app.view.activity.NoteActivity;
import sgtmelon.scriptum.app.vm.fragment.FragmentNoteViewModel;
import sgtmelon.scriptum.app.vm.fragment.NotesViewModel;
import sgtmelon.scriptum.app.vm.fragment.RankViewModel;
import sgtmelon.scriptum.databinding.FragmentBinBinding;
import sgtmelon.scriptum.databinding.FragmentNotesBinding;
import sgtmelon.scriptum.databinding.FragmentRankBinding;
import sgtmelon.scriptum.databinding.FragmentRollBinding;
import sgtmelon.scriptum.databinding.FragmentTextBinding;
import sgtmelon.scriptum.databinding.IncludeInfoBinding;

@Module
public final class FragmentArchModule {

    private final LayoutInflater inflater;
    private final ViewGroup container;

    public FragmentArchModule(LayoutInflater inflater, ViewGroup container) {
        this.inflater = inflater;
        this.container = container;
    }

    @Provides
    @ArchScope
    NoteActivity provideNoteActivity(Fragment fragment) {
        return (NoteActivity) fragment.getActivity();
    }

    @Provides
    @ArchScope
    IncludeInfoBinding provideIncludeInfoBinding() {
        return DataBindingUtil.inflate(inflater, R.layout.include_info, container, false);
    }

    @Provides
    @ArchScope
    FragmentRankBinding provideFragmentRankBinding() {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_rank, container, false);
    }

    @Provides
    @ArchScope
    FragmentNotesBinding provideFragmentNotesBinding() {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_notes, container, false);
    }

    @Provides
    @ArchScope
    FragmentBinBinding provideFragmentBinBinding() {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_bin, container, false);
    }

    @Provides
    @ArchScope
    FragmentTextBinding provideFragmentTextBinding() {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_text, container, false);
    }

    @Provides
    @ArchScope
    FragmentRollBinding provideFragmentRollBinding() {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_roll, container, false);
    }

    @Provides
    @ArchScope
    RankViewModel provideRankViewModel(Fragment fragment) {
        return ViewModelProviders.of(fragment).get(RankViewModel.class);
    }

    @Provides
    @ArchScope
    NotesViewModel provideNotesViewModel(Fragment fragment) {
        return ViewModelProviders.of(fragment).get(NotesViewModel.class);
    }

    @Provides
    @ArchScope
    FragmentNoteViewModel provideNoteViewModel(Fragment fragment) {
        return ViewModelProviders.of(fragment).get(FragmentNoteViewModel.class);
    }

}
