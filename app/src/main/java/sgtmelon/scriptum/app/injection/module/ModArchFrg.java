package sgtmelon.scriptum.app.injection.module;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import dagger.Module;
import dagger.Provides;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.injection.ScopeApp;
import sgtmelon.scriptum.app.view.act.ActNote;
import sgtmelon.scriptum.app.viewModel.VmFrgNotes;
import sgtmelon.scriptum.app.viewModel.VmFrgRank;
import sgtmelon.scriptum.app.viewModel.VmFrgText;
import sgtmelon.scriptum.databinding.FrgBinBinding;
import sgtmelon.scriptum.databinding.FrgNotesBinding;
import sgtmelon.scriptum.databinding.FrgRankBinding;
import sgtmelon.scriptum.databinding.FrgRollBinding;
import sgtmelon.scriptum.databinding.FrgTextBinding;
import sgtmelon.scriptum.databinding.IncInfoBinding;

@Module
public class ModArchFrg {

    @Provides
    @ScopeApp
    ActNote provideActNote(Fragment fragment){
        return (ActNote) fragment.getActivity();
    }

    @Provides
    @ScopeApp
    IncInfoBinding provideIncInfoBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.inc_info, container, false);
    }

    @Provides
    @ScopeApp
    FrgRankBinding provideFrgRankBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.frg_rank, container, false);
    }

    @Provides
    @ScopeApp
    FrgNotesBinding provideFrgNotesBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.frg_notes, container, false);
    }

    @Provides
    @ScopeApp
    FrgBinBinding provideFrgBinBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.frg_bin, container, false);
    }

    @Provides
    @ScopeApp
    FrgTextBinding provideFrgTextBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.frg_text, container, false);
    }

    @Provides
    @ScopeApp
    FrgRollBinding provideFrgRollBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.frg_roll, container, false);
    }

    @Provides
    @ScopeApp
    VmFrgRank provideVmFrgRank(Fragment fragment) {
        return ViewModelProviders.of(fragment).get(VmFrgRank.class);
    }

    @Provides
    @ScopeApp
    VmFrgNotes provideVmFrgNotes(Fragment fragment) {
        return ViewModelProviders.of(fragment).get(VmFrgNotes.class);
    }

    @Provides
    @ScopeApp
    VmFrgText provideVmFrgText(Fragment fragment) {
        return ViewModelProviders.of(fragment).get(VmFrgText.class);
    }

}
