package sgtmelon.scriptum.dagger.act;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import dagger.Module;
import dagger.Provides;

@Module
public class ModAct {

    private AppCompatActivity activity;
    private Context context;

    public ModAct(@NonNull AppCompatActivity activity, @NonNull Context context) {
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
