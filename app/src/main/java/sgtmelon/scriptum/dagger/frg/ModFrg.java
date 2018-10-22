package sgtmelon.scriptum.dagger.frg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import dagger.Module;
import dagger.Provides;
import sgtmelon.scriptum.app.adapter.AdpNote;

@Module
public class ModFrg {

    private Fragment fragment;

    private LayoutInflater inflater;
    private ViewGroup container;

    public ModFrg(Fragment fragment, LayoutInflater inflater, ViewGroup container) {
        this.fragment = fragment;
        this.inflater = inflater;
        this.container = container;
    }

    @Provides
    public Fragment getFragment() {
        return fragment;
    }

    @Provides
    public Context getContext() {
        return fragment.getContext();
    }

    @Provides
    public FragmentManager getFragmentManager() {
        return fragment.getFragmentManager();
    }

    @Provides
    public LayoutInflater getInflater() {
        return inflater;
    }

    @Provides
    public ViewGroup getContainer() {
        return container;
    }

    @Provides
    AdpNote getAdpNote() {
        return new AdpNote();
    }

    // TODO: 22.10.2018 добавить modFrgRank, рефакторинг

}
