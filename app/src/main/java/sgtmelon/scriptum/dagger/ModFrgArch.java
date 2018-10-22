package sgtmelon.scriptum.dagger;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import dagger.Module;
import dagger.Provides;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.viewModel.VmFrgNotes;
import sgtmelon.scriptum.app.viewModel.VmFrgRank;
import sgtmelon.scriptum.app.viewModel.VmFrgText;
import sgtmelon.scriptum.databinding.FrgBinBinding;
import sgtmelon.scriptum.databinding.FrgNotesBinding;
import sgtmelon.scriptum.databinding.FrgRankBinding;
import sgtmelon.scriptum.databinding.FrgRollBinding;
import sgtmelon.scriptum.databinding.FrgTextBinding;

@Module
public class ModFrgArch {

    @Provides
    public FrgRankBinding provideFrgRankBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.frg_rank, container, false);
    }

    @Provides
    public FrgNotesBinding provideFrgNotesBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.frg_notes, container, false);
    }

    @Provides
    public FrgBinBinding provideFrgBinBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.frg_bin, container, false);
    }

    @Provides
    public FrgTextBinding provideFrgTextBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.frg_text, container, false);
    }

    @Provides
    public FrgRollBinding provideFrgRollBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.frg_roll, container, false);
    }

    @Provides
    public VmFrgRank provideVmFrgRank(Fragment fragment){
        return ViewModelProviders.of(fragment).get(VmFrgRank.class);
    }

    @Provides
    public VmFrgNotes provideVmFrgNotes(Fragment fragment){
        return ViewModelProviders.of(fragment).get(VmFrgNotes.class);
    }

    @Provides
    public VmFrgText provideVmFrgText(Fragment fragment){
        return ViewModelProviders.of(fragment).get(VmFrgText.class);
    }

}
