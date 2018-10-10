package sgtmelon.handynotes.app.view.act;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import sgtmelon.handynotes.R;
import sgtmelon.handynotes.app.adapter.AdpPager;
import sgtmelon.handynotes.app.model.item.ItemIntro;
import sgtmelon.handynotes.app.view.frg.FrgIntro;

public class ActIntro extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private static final String TAG = "ActIntro";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_intro);

        setupViewPager();

//        Intent intent = new Intent(this, ActMain.class);
//        startActivity(intent);
//        finish();
    }

    public AdpPager adpPager;

    FrgIntro[] frg;

    private void setupViewPager() {
        Log.i(TAG, "setupViewPager");

        ViewPager viewPager = findViewById(R.id.actIntro_vp);
        adpPager = new AdpPager(getSupportFragmentManager());

        frg = new FrgIntro[]{new FrgIntro(), new FrgIntro(), new FrgIntro(), new FrgIntro()};

        ItemIntro[] intro = new ItemIntro[]{
                new ItemIntro(R.drawable.ic_add, R.string.info_notes_title, R.string.about_app_version),
                new ItemIntro(R.drawable.ic_bracket_right, R.string.dialog_title_add_note, R.string.about_app_used_libraries),
                new ItemIntro(R.drawable.ic_arrow_back, R.string.dialog_title_color, R.string.about_app_logo_designer),
                new ItemIntro(R.drawable.ic_check_done, R.string.pref_title_about, R.string.about_app_developer)
        };

        for (int i = 0; i < frg.length; i++) {
            frg[i].setItemIntro(intro[i]);
            adpPager.addItem(frg[i]);
        }

        viewPager.setAdapter(adpPager);
        viewPager.setOffscreenPageLimit(adpPager.getCount() - 1);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.d(TAG, "onPageScrolled: ps : " + position + " psOff : " + positionOffset + " psOffPx : " + positionOffsetPixels);

        float targetAlpha = (float) Math.max(0.2, 1 - positionOffset);
        float targetScale = (float) Math.max(0.75, 1 - positionOffset);

        frg[position].container.setAlpha(targetAlpha);
        frg[position].container.setScaleX(targetScale);
        frg[position].container.setScaleY(targetScale);

//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);

//        float width = size.x;
//        float height = size.y;
//
//        float radius = height / 2;
//
//        float angle = (1 - positionOffset) * 90;
//        float newX = width / 2 + (float) Math.cos(angle) * radius;
//        float newY = height / 2 + (float) Math.sin(angle) * radius;
//
//        frg[position].container.setX(newX);
//        frg[position].container.setY(newY);

        if (position != adpPager.getCount() - 1) {
            targetAlpha = (float) Math.max(0.2, positionOffset);
            targetScale = (float) Math.max(0.75, positionOffset);

            frg[position + 1].container.setAlpha(targetAlpha);
            frg[position + 1].container.setScaleX(targetScale);
            frg[position + 1].container.setScaleY(targetScale);

//            angle = positionOffset * 90;
//            newX = width / 2 + (float) Math.cos(angle) * radius;
//            newY = height / 2 + (float) Math.sin(angle) * radius;
//
//            frg[position + 1].container.setX(newX);
//            frg[position + 1].container.setY(newY);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
