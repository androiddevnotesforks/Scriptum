package sgtmelon.scriptum.presentation.screen.ui.impl.intro

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.extension.*
import sgtmelon.common.test.idling.AppIdlingResource
import sgtmelon.scriptum.domain.model.annotation.test.IdlingTag
import sgtmelon.scriptum.presentation.adapter.IntroPageAdapter
import sgtmelon.scriptum.presentation.screen.ui.ParentActivity
import sgtmelon.scriptum.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.presentation.screen.ui.callback.IIntroActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.main.MainActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.IIntroViewModel
import javax.inject.Inject

/**
 * Activity with start intro.
 */
class IntroActivity : ParentActivity(), IIntroActivity {

    @Inject internal lateinit var viewModel: IIntroViewModel

    private val pagerAdapter = IntroPageAdapter(supportFragmentManager, lifecycle)

    private val viewPager by lazy { findViewById<ViewPager2>(R.id.intro_view_pager) }
    private val pageContainer by lazy { findViewById<ViewGroup>(R.id.intro_page_container) }
    private val pageIndicator by lazy { findViewById<ScrollingPagerIndicator>(R.id.intro_page_indicator) }
    private val endButton by lazy { findViewById<View>(R.id.intro_end_button) }

    private val pageChangeListener = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            pagerAdapter.notifyItem(position, 1 - positionOffset)

            if (position != pagerAdapter.itemCount - 1) {
                pagerAdapter.notifyItem(position + 1, positionOffset)
            }

            endButton.isEnabled = position == pagerAdapter.itemCount - 1

            if (position == pagerAdapter.itemCount - 2) {
                pageIndicator.apply {
                    alpha = 1 - 2 * positionOffset
                    translationY = -positionOffset * height
                }

                endButton.apply {
                    alpha = positionOffset
                    translationY = height - positionOffset * height
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        ScriptumApplication.component.getIntroBuilder().set(activity = this).build()
            .inject(activity = this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        viewModel.onSetup(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSaveData(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    override fun setWindowBackground() {
        window.setBackgroundDrawable(ColorDrawable(getColorAttr(R.attr.colorPrimaryDark)))
    }

    override fun setStatusBarColor() {
        window.statusBarColor = Color.TRANSPARENT
    }

    override fun setNavigationColor(@Theme theme: Int) {
        window.navigationBarColor = Color.TRANSPARENT
    }

    override fun setNavigationDividerColor(@Theme theme: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.navigationBarDividerColor = Color.TRANSPARENT
        }
    }


    override fun setupViewPager(isLastPage: Boolean) {
        endButton.setOnClickListener {
            AppIdlingResource.getInstance().startWork(IdlingTag.Intro.FINISH)
            beforeFinish { viewModel.onClickEnd() }
        }

        viewPager.adapter = pagerAdapter
        viewPager.offscreenPageLimit = pagerAdapter.itemCount
        viewPager.registerOnPageChangeCallback(pageChangeListener)

        pageIndicator.attachToPager(viewPager)

        if (isLastPage) {
            pageIndicator.alpha = 0f
        } else {
            endButton.alpha = 0f
            endButton.isEnabled = false
        }
    }

    override fun setupInsets() {
        viewPager.doOnApplyWindowInsets { view, insets, _, padding, _ ->
            view.updatePadding(InsetsDir.LEFT, insets, padding)
            view.updatePadding(InsetsDir.TOP, insets, padding)
            view.updatePadding(InsetsDir.RIGHT, insets, padding)
            view.updatePadding(InsetsDir.BOTTOM, insets, padding)
            return@doOnApplyWindowInsets insets
        }

        pageContainer.doOnApplyWindowInsets { view, insets, _, _, margin ->
            view.updateMargin(InsetsDir.LEFT, insets, margin)
            view.updateMargin(InsetsDir.RIGHT, insets, margin)
            view.updateMargin(InsetsDir.BOTTOM, insets, margin)
            return@doOnApplyWindowInsets insets
        }

        endButton.doOnApplyWindowInsets { view, insets, _, _, margin ->
            view.updateMargin(InsetsDir.LEFT, insets, margin)
            view.updateMargin(InsetsDir.RIGHT, insets, margin)
            view.updateMargin(InsetsDir.BOTTOM, insets, margin)
            return@doOnApplyWindowInsets insets
        }
    }

    override fun getCurrentPosition(): Int = viewPager.currentItem

    override fun getItemCount(): Int = pagerAdapter.itemCount

    override fun openMainScreen() = startActivity(MainActivity[this])

    companion object {
        /**
         * After call [get] and [Intent] run application will be restarted with only this screen.
         */
        operator fun get(context: Context): Intent {
            return Intent(context, IntroActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
}