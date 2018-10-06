package sgtmelon.scriptum.app.injection.module.blank;

import androidx.fragment.app.FragmentManager;
import dagger.Module;
import dagger.Provides;
import sgtmelon.scriptum.app.view.activity.PreferenceActivity;

@Module
public final class PreferenceBlankModule {

    private final PreferenceActivity activity;

    public PreferenceBlankModule(PreferenceActivity activity) {
        this.activity = activity;
    }

    @Provides
    public FragmentManager provideFragmentManager() {
        return activity.getSupportFragmentManager();
    }

}
