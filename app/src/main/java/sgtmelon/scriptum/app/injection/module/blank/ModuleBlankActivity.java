package sgtmelon.scriptum.app.injection.module.blank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import dagger.Module;
import dagger.Provides;

@Module
public final class ModuleBlankActivity {

    private AppCompatActivity activity;

    public ModuleBlankActivity(@NonNull AppCompatActivity activity) {
        this.activity = activity;
    }

    @Provides
    public AppCompatActivity getActivity() {
        return activity;
    }

    @Provides
    public FragmentManager getFragmentManager() {
        return activity.getSupportFragmentManager();
    }

}
