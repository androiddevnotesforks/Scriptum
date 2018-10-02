package sgtmelon.scriptum.app.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.injection.component.ComponentFragment;
import sgtmelon.scriptum.app.injection.component.DaggerComponentFragment;
import sgtmelon.scriptum.app.injection.module.blank.ModuleBlankFragment;
import sgtmelon.scriptum.databinding.IncludeInfoBinding;
import sgtmelon.scriptum.office.annot.AnnIntro;
import sgtmelon.scriptum.office.annot.def.DefIntent;
import sgtmelon.scriptum.office.st.StPage;

public final class IntroFragment extends Fragment {

    private static final String TAG = IntroFragment.class.getSimpleName();

    @Inject
    IncludeInfoBinding binding;

    private StPage stPage;

    private View frgView;
    private LinearLayout container;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        ComponentFragment componentFragment = DaggerComponentFragment.builder()
                .moduleBlankFragment(new ModuleBlankFragment(this, inflater, container))
                .build();
        componentFragment.inject(this);

        frgView = binding.getRoot();

        if (savedInstanceState != null) {
            stPage.setPage(savedInstanceState.getInt(DefIntent.STATE_PAGE));
        }

        return frgView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        container = frgView.findViewById(R.id.info_container);
        bind();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);

        outState.putInt(DefIntent.STATE_PAGE, stPage.getPage());
    }

    public void setStPage(StPage stPage) {
        this.stPage = stPage;
    }

    public void setChange(float alpha, float scale) {
        Log.i(TAG, "setChange");

        container.setAlpha(alpha);
        container.setScaleX(scale);
        container.setScaleY(scale);
    }

    private void bind() {
        Log.i(TAG, "bind");
        int page = stPage.getPage();

        binding.setIcon(AnnIntro.icon[page]);
        binding.setTitle(AnnIntro.title[page]);
        binding.setDetails(AnnIntro.details[page]);

        binding.executePendingBindings();
    }

}
