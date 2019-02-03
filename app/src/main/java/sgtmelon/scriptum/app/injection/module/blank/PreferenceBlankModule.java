package sgtmelon.scriptum.app.injection.module.blank;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import dagger.Module;
import dagger.Provides;
import sgtmelon.scriptum.app.view.activity.PreferenceActivity;
import sgtmelon.scriptum.office.utils.PrefUtils;

@Module
public final class PreferenceBlankModule {

    private final PreferenceActivity activity;

    public PreferenceBlankModule(@NonNull PreferenceActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Provides
    Context provideContext() {
        return activity;
    }

    @NonNull
    @Provides
    FragmentManager provideFragmentManager() {
        return activity.getSupportFragmentManager();
    }

    @NonNull
    @Provides
    PrefUtils providePrefUtils() {
        return new PrefUtils(activity);
    }

}
