package sgtmelon.scriptum.app.injection.module;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import dagger.Module;
import dagger.Provides;
import sgtmelon.scriptum.R;
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
    ActNote provideActNote(Fragment fragment){
        return (ActNote) fragment.getActivity();
    }

    @Provides
    IncInfoBinding provideIncInfoBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.inc_info, container, false);
    }

    @Provides
    FrgRankBinding provideFrgRankBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.frg_rank, container, false);
    }

    @Provides
    FrgNotesBinding provideFrgNotesBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.frg_notes, container, false);
    }

    @Provides
    FrgBinBinding provideFrgBinBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.frg_bin, container, false);
    }

    @Provides
    FrgTextBinding provideFrgTextBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.frg_text, container, false);
    }

    @Provides
    FrgRollBinding provideFrgRollBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.frg_roll, container, false);
    }

    @Provides
    VmFrgRank provideVmFrgRank(Fragment fragment) {
        return ViewModelProviders.of(fragment).get(VmFrgRank.class);
    }

    @Provides
    VmFrgNotes provideVmFrgNotes(Fragment fragment) {
        return ViewModelProviders.of(fragment).get(VmFrgNotes.class);
    }

    @Provides
    VmFrgText provideVmFrgText(Fragment fragment) {
        return ViewModelProviders.of(fragment).get(VmFrgText.class);
    }

}
