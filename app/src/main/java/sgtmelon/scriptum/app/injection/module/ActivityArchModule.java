package sgtmelon.scriptum.app.injection.module;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import dagger.Module;
import dagger.Provides;
import sgtmelon.scriptum.app.injection.ArchScope;
import sgtmelon.scriptum.app.vm.activity.ActivityNoteViewModel;

@Module
public final class ActivityArchModule {

    @Provides
    @ArchScope
    ActivityNoteViewModel provideNoteViewModel(AppCompatActivity activity) {
        return ViewModelProviders.of(activity).get(ActivityNoteViewModel.class);
    }

}
