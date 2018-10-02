package sgtmelon.scriptum.app.injection.module;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import dagger.Module;
import dagger.Provides;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.injection.ScopeArch;
import sgtmelon.scriptum.app.view.activity.NoteActivity;
import sgtmelon.scriptum.app.vm.NotesViewModel;
import sgtmelon.scriptum.app.vm.RankViewModel;
import sgtmelon.scriptum.app.vm.TextViewModel;
import sgtmelon.scriptum.databinding.FragmentBinBinding;
import sgtmelon.scriptum.databinding.FragmentNotesBinding;
import sgtmelon.scriptum.databinding.FragmentRankBinding;
import sgtmelon.scriptum.databinding.FragmentRollBinding;
import sgtmelon.scriptum.databinding.FragmentTextBinding;
import sgtmelon.scriptum.databinding.IncludeInfoBinding;

@Module
public final class ModuleArchFragment {

    @Provides
    @ScopeArch
    NoteActivity provideNoteActivity(Fragment fragment) {
        return (NoteActivity) fragment.getActivity();
    }

    @Provides
    @ScopeArch
    IncludeInfoBinding provideIncludeInfoBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.include_info, container, false);
    }

    @Provides
    @ScopeArch
    FragmentRankBinding provideFragmentRankBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_rank, container, false);
    }

    @Provides
    @ScopeArch
    FragmentNotesBinding provideFragmentNotesBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_notes, container, false);
    }

    @Provides
    @ScopeArch
    FragmentBinBinding provideFragmentBinBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_bin, container, false);
    }

    @Provides
    @ScopeArch
    FragmentTextBinding provideFragmentTextBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_text, container, false);
    }

    @Provides
    @ScopeArch
    FragmentRollBinding provideFragmentRollBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_roll, container, false);
    }

    @Provides
    @ScopeArch
    RankViewModel provideRankViewModel(Fragment fragment) {
        return ViewModelProviders.of(fragment).get(RankViewModel.class);
    }

    @Provides
    @ScopeArch
    NotesViewModel provideNotesViewModel(Fragment fragment) {
        return ViewModelProviders.of(fragment).get(NotesViewModel.class);
    }

    @Provides
    @ScopeArch
    TextViewModel provideTextViewModel(Fragment fragment) {
        return ViewModelProviders.of(fragment).get(TextViewModel.class);
    }

}
