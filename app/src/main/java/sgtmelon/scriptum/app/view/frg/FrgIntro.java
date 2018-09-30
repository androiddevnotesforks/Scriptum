package sgtmelon.scriptum.app.view.frg;

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
import sgtmelon.scriptum.app.injection.component.ComFrg;
import sgtmelon.scriptum.app.injection.component.DaggerComFrg;
import sgtmelon.scriptum.app.injection.module.ModBlankFrg;
import sgtmelon.scriptum.databinding.IncInfoBinding;
import sgtmelon.scriptum.office.annot.AnnIntro;
import sgtmelon.scriptum.office.annot.def.DefIntent;
import sgtmelon.scriptum.office.st.StPage;

public class FrgIntro extends Fragment {

    private static final String TAG = FrgIntro.class.getSimpleName();

    @Inject
    IncInfoBinding binding;
    @Inject
    StPage stPage;

    private View frgView;
    private LinearLayout container;

    public void setPage(int page) {
        stPage.setPage(page);
    }

    public void setChange(float alpha, float scale) {
        Log.i(TAG, "setChange");

        container.setAlpha(alpha);
        container.setScaleX(scale);
        container.setScaleY(scale);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        ComFrg comFrg = DaggerComFrg.builder()
                .modBlankFrg(new ModBlankFrg(this, inflater, container))
                .build();
        comFrg.inject(this);

        if (savedInstanceState != null) {
            stPage = savedInstanceState.getParcelable(DefIntent.STATE_PAGE);
        }

        return frgView = binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        container = frgView.findViewById(R.id.incInfo_ll_container);
        bind();
    }

    private void bind() {
        Log.i(TAG, "bind");
        int page = stPage.getPage();

        binding.setIcon(AnnIntro.icon[page]);
        binding.setTitle(AnnIntro.title[page]);
        binding.setDetails(AnnIntro.details[page]);

        binding.executePendingBindings();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);

        outState.putParcelable(DefIntent.STATE_PAGE, stPage);
    }

}
