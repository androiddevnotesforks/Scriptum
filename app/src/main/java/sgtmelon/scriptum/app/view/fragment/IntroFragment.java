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
import sgtmelon.scriptum.app.injection.component.DaggerFragmentComponent;
import sgtmelon.scriptum.app.injection.component.FragmentComponent;
import sgtmelon.scriptum.app.injection.module.FragmentArchModule;
import sgtmelon.scriptum.app.injection.module.blank.FragmentBlankModule;
import sgtmelon.scriptum.databinding.IncludeInfoBinding;
import sgtmelon.scriptum.office.annot.IntroAnn;
import sgtmelon.scriptum.office.annot.def.IntentDef;
import sgtmelon.scriptum.office.st.PageSt;

public final class IntroFragment extends Fragment {

    private static final String TAG = IntroFragment.class.getSimpleName();

    @Inject
    IncludeInfoBinding binding;

    private PageSt pageSt;

    private View frgView;
    private LinearLayout container;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        FragmentComponent fragmentComponent = DaggerFragmentComponent.builder()
                .fragmentBlankModule(new FragmentBlankModule(this))
                .fragmentArchModule(new FragmentArchModule(inflater, container))
                .build();
        fragmentComponent.inject(this);

        frgView = binding.getRoot();

        if (savedInstanceState != null) {
            pageSt.setPage(savedInstanceState.getInt(IntentDef.STATE_PAGE));
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

        outState.putInt(IntentDef.STATE_PAGE, pageSt.getPage());
    }

    public void setPageSt(PageSt pageSt) {
        this.pageSt = pageSt;
    }

    public void setChange(float alpha, float scale) {
        Log.i(TAG, "setChange");

        container.setAlpha(alpha);
        container.setScaleX(scale);
        container.setScaleY(scale);
    }

    private void bind() {
        Log.i(TAG, "bind");
        int page = pageSt.getPage();

        binding.setIcon(IntroAnn.icon[page]);
        binding.setTitle(IntroAnn.title[page]);
        binding.setDetails(IntroAnn.details[page]);

        binding.executePendingBindings();
    }

}
