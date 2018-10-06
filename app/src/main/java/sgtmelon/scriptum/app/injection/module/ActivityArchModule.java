package sgtmelon.scriptum.app.injection.module;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import dagger.Module;
import dagger.Provides;
import sgtmelon.scriptum.app.injection.ArchScope;
import sgtmelon.scriptum.app.vm.NoteViewModel;

@Module
public final class ActivityArchModule {

    @Provides
    @ArchScope
    NoteViewModel provideNoteViewModel(AppCompatActivity activity) {
        return ViewModelProviders.of(activity).get(NoteViewModel.class);
    }

}
