package sgtmelon.scriptum.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.adapter.AdapterPager;
import sgtmelon.scriptum.app.view.fragment.IntroFragment;
import sgtmelon.scriptum.office.annot.AnnIntro;
import sgtmelon.scriptum.office.st.StPage;

public final class IntroActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private static final String TAG = IntroActivity.class.getSimpleName();

    private final AdapterPager adapterPager = new AdapterPager(getSupportFragmentManager());;

    private View pageIndicator;
    private Button pageButtonEnd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        setupViewPager();
    }

    private void setupViewPager() {
        Log.i(TAG, "setupViewPager");

        ViewPager viewPager = findViewById(R.id.intro_pager);

        pageIndicator = findViewById(R.id.page_indicator);
        pageButtonEnd = findViewById(R.id.end_button);

        pageButtonEnd.setOnClickListener(view -> {
            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        pageButtonEnd.setAlpha(0);
        pageButtonEnd.setEnabled(false);

        IntroFragment introFragment;
        for (int i = 0; i < AnnIntro.count; i++) {
            introFragment = new IntroFragment();
            introFragment.setStPage(new StPage(i));
            adapterPager.addItem(introFragment);
        }

        viewPager.setAdapter(adapterPager);
        viewPager.setOffscreenPageLimit(adapterPager.getCount() - 1);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        float targetAlpha = (float) Math.max(0.2, 1 - positionOffset);
        float targetScale = (float) Math.max(0.75, 1 - positionOffset);

        adapterPager.getItem(position).setChange(targetAlpha, targetScale);

        if (position != adapterPager.getCount() - 1) {
            targetAlpha = (float) Math.max(0.2, positionOffset);
            targetScale = (float) Math.max(0.75, positionOffset);

            adapterPager.getItem(position + 1).setChange(targetAlpha, targetScale);
        }

        if (position == adapterPager.getCount() - 1) {
            pageButtonEnd.setEnabled(true);
        } else pageButtonEnd.setEnabled(false);

        if (position == adapterPager.getCount() - 2) {
            pageIndicator.setTranslationY(positionOffset * pageButtonEnd.getHeight());
            pageIndicator.setAlpha(1 - positionOffset);
            pageButtonEnd.setAlpha(positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
