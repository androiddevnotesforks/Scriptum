package sgtmelon.scriptum.app.injection.module.blank;

import android.content.Context;

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
    Context provideContext() {
        return activity;
    }

    @Provides
    FragmentManager provideFragmentManager() {
        return activity.getSupportFragmentManager();
    }

}
