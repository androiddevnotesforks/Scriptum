package sgtmelon.scriptum.app.screen.view.intro

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.PagerAdapter
import sgtmelon.scriptum.app.model.data.IntroData
import sgtmelon.scriptum.app.screen.view.main.MainActivity
import sgtmelon.scriptum.office.utils.AppUtils.beforeFinish

/**
 * Экран со вступительным туториалом
 *
 * @author SerjantArbuz
 */
class IntroActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

    private val pagerAdapter = PagerAdapter(supportFragmentManager)

    private val pageIndicator: View by lazy { findViewById<View>(R.id.intro_page_indicator) }
    private val pageButtonEnd: Button by lazy { findViewById<Button>(R.id.intro_end_button) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        setupViewPager()
    }

    private fun setupViewPager() {
        pageButtonEnd.apply {
            alpha = 0f
            isEnabled = false
            setOnClickListener {
                beforeFinish { startActivity(MainActivity.getIntent(context = this@IntroActivity)) }
            }
        }

        for (i in 0 until IntroData.count) pagerAdapter.addItem(IntroFragment.getInstance(i))

        findViewById<ViewPager>(R.id.intro_pager).apply {
            adapter = pagerAdapter
            offscreenPageLimit = pagerAdapter.count - 1
            addOnPageChangeListener(this@IntroActivity)
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        pagerAdapter.notifyItem(position, 1 - positionOffset)

        if (position != pagerAdapter.count - 1) {
            pagerAdapter.notifyItem(position + 1, positionOffset)
        }

        pageButtonEnd.isEnabled = position == pagerAdapter.count - 1

        if (position == pagerAdapter.count - 2) {
            pageIndicator.translationY = positionOffset * pageButtonEnd.height
            pageIndicator.alpha = 1 - positionOffset

            pageButtonEnd.alpha = positionOffset
        }
    }

    override fun onPageSelected(position: Int) {}

    override fun onPageScrollStateChanged(state: Int) {}

}