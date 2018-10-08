package sgtmelon.scriptum.app.injection.module.blank;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import dagger.Module;
import dagger.Provides;

@Module
public final class ActivityBlankModule {

    private final AppCompatActivity activity;

    public ActivityBlankModule(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return activity;
    }

    @Provides
    Context provideContext() {
        return activity;
    }

    @Provides
    public FragmentManager provideFragmentManager() {
        return activity.getSupportFragmentManager();
    }

}
