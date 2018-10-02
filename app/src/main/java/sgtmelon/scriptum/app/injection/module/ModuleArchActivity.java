package sgtmelon.scriptum.app.injection.module;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import dagger.Module;
import dagger.Provides;
import sgtmelon.scriptum.app.injection.ScopeArch;
import sgtmelon.scriptum.app.vm.NoteViewModel;

@Module
public final class ModuleArchActivity {

    @Provides
    @ScopeArch
    NoteViewModel provideNoteViewModel(AppCompatActivity activity) {
        return ViewModelProviders.of(activity).get(NoteViewModel.class);
    }

}
