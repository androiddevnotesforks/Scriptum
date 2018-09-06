package sgtmelon.handynotes.app.view.frg;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import sgtmelon.handynotes.R;
import sgtmelon.handynotes.databinding.IncInfoBinding;
import sgtmelon.handynotes.office.annot.AnnIntro;

public class FrgIntro extends Fragment {

    //region Variable
    private static final String TAG = "FrgIntro";

    private IncInfoBinding binding;
    private View frgView;
    private LinearLayout container;

    private int page;
    //endregion

    public void setPage(int page) {
        this.page = page;
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

        binding = DataBindingUtil.inflate(inflater, R.layout.inc_info, container, false);
        frgView = binding.getRoot();

        if (savedInstanceState != null) page = savedInstanceState.getInt(AnnIntro.PAGE);

        return frgView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");

        container = frgView.findViewById(R.id.incInfo_ll_container);
        bind();
    }

    private void bind() {
        Log.i(TAG, "bind");
        binding.setIcon(AnnIntro.icon[page]);
        binding.setTitle(AnnIntro.title[page]);
        binding.setDetails(AnnIntro.details[page]);

        binding.executePendingBindings();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");

        outState.putInt(AnnIntro.PAGE, page);
    }

}
