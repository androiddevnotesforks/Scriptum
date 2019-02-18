package sgtmelon.scriptum.app.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.PagerAdapter
import sgtmelon.scriptum.app.view.fragment.IntroFragment
import sgtmelon.scriptum.office.data.IntroData
import sgtmelon.scriptum.office.state.PageState
import sgtmelon.scriptum.office.utils.AppUtils.beforeFinish

class IntroActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

    private val pagerAdapter = PagerAdapter(supportFragmentManager)

    private val pageIndicator: View by lazy {
        findViewById<View>(R.id.intro_page_indicator)
    }
    private val pageButtonEnd: Button by lazy {
        findViewById<Button>(R.id.intro_end_button)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        setupViewPager()
    }

    private fun setupViewPager() {
        pageButtonEnd.setOnClickListener {
            beforeFinish {
                startActivity(Intent(this@IntroActivity, MainActivity::class.java))
            }
        }

        pageButtonEnd.alpha = 0f
        pageButtonEnd.isEnabled = false

        for (i in 0 until IntroData.count) {
            pagerAdapter.addItem(IntroFragment.getInstance(PageState(i)))
        }

        val viewPager = findViewById<ViewPager>(R.id.intro_pager)
        viewPager.adapter = pagerAdapter
        viewPager.offscreenPageLimit = pagerAdapter.count - 1
        viewPager.addOnPageChangeListener(this)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        var targetAlpha = Math.max(0.2, (1 - positionOffset).toDouble()).toFloat()
        var targetScale = Math.max(0.75, (1 - positionOffset).toDouble()).toFloat()

        pagerAdapter.getItem(position).setChange(targetAlpha, targetScale)

        if (position != pagerAdapter.count - 1) {
            targetAlpha = Math.max(0.2, positionOffset.toDouble()).toFloat()
            targetScale = Math.max(0.75, positionOffset.toDouble()).toFloat()

            pagerAdapter.getItem(position + 1).setChange(targetAlpha, targetScale)
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