package sgtmelon.scriptum.app.injection.module.blank;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import dagger.Module;
import dagger.Provides;

@Module
public final class ModuleBlankFragment {

    private final Fragment fragment;

    private final LayoutInflater inflater;
    private final ViewGroup container;

    public ModuleBlankFragment(Fragment fragment, LayoutInflater inflater, ViewGroup container) {
        this.fragment = fragment;
        this.inflater = inflater;
        this.container = container;
    }

    @Provides
    Fragment provideFragment() {
        return fragment;
    }

    @Provides
    public FragmentManager provideFragmentManager() {
        return fragment.getFragmentManager();
    }

    @Provides
    LayoutInflater provideInflater() {
        return inflater;
    }

    @Provides
    ViewGroup provideContainer() {
        return container;
    }

}
