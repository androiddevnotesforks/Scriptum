package sgtmelon.handynotes.app.view.act;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import sgtmelon.handynotes.R;
import sgtmelon.handynotes.app.adapter.AdpPager;
import sgtmelon.handynotes.app.view.frg.FrgIntro;
import sgtmelon.handynotes.office.annot.AnnIntro;

public class ActIntro extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private static final String TAG = "ActIntro";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_intro);
        Log.i(TAG, "onCreate");

        setupViewPager();
    }

    private AdpPager adpPager;
    private Button buttonEnd;

    private void setupViewPager() {
        Log.i(TAG, "setupViewPager");

        ViewPager viewPager = findViewById(R.id.actIntro_vp);

        buttonEnd = findViewById(R.id.actIntro_b);
        buttonEnd.setOnClickListener(view -> {
            Intent intent = new Intent(ActIntro.this, ActMain.class);
            startActivity(intent);
            finish();
        });

        buttonEnd.setAlpha(0);
        buttonEnd.setEnabled(false);

        adpPager = new AdpPager(getSupportFragmentManager());

        FrgIntro frgIntro;
        for (int i = 0; i < AnnIntro.count; i++) {
            frgIntro = new FrgIntro();
            frgIntro.setPage(i);
            adpPager.addItem(frgIntro);
        }

        viewPager.setAdapter(adpPager);
        viewPager.setOffscreenPageLimit(adpPager.getCount() - 1);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.i(TAG, "onPageScrolled: ps=" + position + " psOff=" + positionOffset);

        float targetAlpha = (float) Math.max(0.2, 1 - positionOffset);
        float targetScale = (float) Math.max(0.75, 1 - positionOffset);

        adpPager.getItem(position).setChange(targetAlpha, targetScale);

        if (position != adpPager.getCount() - 1) {
            targetAlpha = (float) Math.max(0.2, positionOffset);
            targetScale = (float) Math.max(0.75, positionOffset);

            adpPager.getItem(position + 1).setChange(targetAlpha, targetScale);
        }

        if (position == adpPager.getCount() - 1) {
            buttonEnd.setEnabled(true);
        } else buttonEnd.setEnabled(false);

        if (position == adpPager.getCount() - 2) {
            buttonEnd.setAlpha(positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
