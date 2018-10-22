package sgtmelon.scriptum.app.injection.module;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import dagger.Module;
import dagger.Provides;

@Module
public class ModBlankFrg {

    private Fragment fragment;

    private LayoutInflater inflater;
    private ViewGroup container;

    public ModBlankFrg(Fragment fragment, LayoutInflater inflater, ViewGroup container) {
        this.fragment = fragment;
        this.inflater = inflater;
        this.container = container;
    }

    @Provides
    public Fragment provideFragment() {
        return fragment;
    }

    @Provides
    public Context provideContext() {
        return fragment.getContext();
    }

    @Provides
    public FragmentManager provideFragmentManager() {
        return fragment.getFragmentManager();
    }

    @Provides
    public LayoutInflater provideInflater() {
        return inflater;
    }

    @Provides
    public ViewGroup provideContainer() {
        return container;
    }

    @Provides
    public Window provideWindow(){
        FragmentActivity activity = fragment.getActivity();

        if (activity != null) return activity.getWindow();
        else return null;
    }

    @Provides
    public LinearLayoutManager provideLinearLayout(Context context){
        return new LinearLayoutManager(context);
    }

}
