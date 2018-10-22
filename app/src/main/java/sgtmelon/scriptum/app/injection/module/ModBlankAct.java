package sgtmelon.scriptum.app.injection.module;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import dagger.Module;
import dagger.Provides;

@Module
public class ModBlankAct {

    private AppCompatActivity activity;
    private Context context;

    public ModBlankAct(@NonNull AppCompatActivity activity, @NonNull Context context) {
        this.activity = activity;
        this.context = context;
    }

    @Provides
    public AppCompatActivity getActivity() {
        return activity;
    }

    @Provides
    public Context getContext() {
        return context;
    }

    @Provides
    public FragmentManager getFragmentManager() {
        return activity.getSupportFragmentManager();
    }

}
