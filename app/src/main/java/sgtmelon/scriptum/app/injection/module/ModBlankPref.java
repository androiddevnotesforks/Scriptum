package sgtmelon.scriptum.app.injection.module;

import android.content.SharedPreferences;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import androidx.fragment.app.FragmentManager;
import dagger.Module;
import dagger.Provides;
import sgtmelon.scriptum.app.view.act.ActSettings;

@Module
public class ModBlankPref {

    private final PreferenceFragment preferenceFragment;
    private final ActSettings activity;

    public ModBlankPref(PreferenceFragment preferenceFragment) {
        this.preferenceFragment = preferenceFragment;
        activity = (ActSettings) preferenceFragment.getActivity();
    }

    @Provides
    public PreferenceFragment providePreferenceFragment() {
        return preferenceFragment;
    }

    @Provides
    public ActSettings provideActivity(){
        return activity;
    }

    @Provides
    public FragmentManager provideFragmentManager(){
        return activity.getSupportFragmentManager();
    }

    @Provides
    public SharedPreferences provideSharedPreferences(){
        return PreferenceManager.getDefaultSharedPreferences(activity);
    }

}
