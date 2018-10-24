package sgtmelon.scriptum.app.injection.module;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import dagger.Module;
import dagger.Provides;
import sgtmelon.scriptum.app.injection.ScopeApp;
import sgtmelon.scriptum.app.viewModel.VmActNote;

@Module
public class ModArchAct {

    @Provides
    @ScopeApp
    VmActNote provideVmActNote(AppCompatActivity activity) {
        return ViewModelProviders.of(activity).get(VmActNote.class);
    }

}
