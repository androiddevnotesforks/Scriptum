package sgtmelon.scriptum.dagger.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import dagger.Module;
import dagger.Provides;
import sgtmelon.scriptum.app.viewModel.VmActNote;

@Module(includes = ModAct.class)
class ModActNote {

    @Provides
    VmActNote provideVmActNote(AppCompatActivity activity) {
        return ViewModelProviders.of(activity).get(VmActNote.class);
    }

}
