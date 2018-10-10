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
import sgtmelon.handynotes.app.model.item.ItemIntro;
import sgtmelon.handynotes.databinding.IncPageInfoBinding;

public class FrgIntro extends Fragment {

    //region Variable
    private static final String TAG = "FrgIntro";

    private IncPageInfoBinding binding;
    private View frgView;

    private ItemIntro itemIntro;
    //endregion

    public void setItemIntro(ItemIntro itemIntro) {
        this.itemIntro = itemIntro;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        binding = DataBindingUtil.inflate(inflater, R.layout.inc_page_info, container, false);
        frgView = binding.getRoot();

        return frgView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated");

        setupElements();

        bind();
    }

    public void bind() {
        Log.i(TAG, "bind");

        binding.setIcon(itemIntro.getIcon());
        binding.setTitle(itemIntro.getTitle());
        binding.setDetails(itemIntro.getDetails());

        binding.executePendingBindings();
    }

    public LinearLayout container;

    private void setupElements() {
        Log.i(TAG, "setupElements");

        container = frgView.findViewById(R.id.incPageInfo_ll_container);
    }

}
