package sgtmelon.scriptum.app.injection.module.blank;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import dagger.Module;
import dagger.Provides;

@Module
public final class FragmentBlankModule {

    private final Fragment fragment;

    public FragmentBlankModule(Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    Fragment provideFragment() {
        return fragment;
    }

    @Provides
    Context provideContext() {
        return fragment.getContext();
    }

    @Provides
    FragmentManager provideFragmentManager() {
        return fragment.getFragmentManager();
    }

}
