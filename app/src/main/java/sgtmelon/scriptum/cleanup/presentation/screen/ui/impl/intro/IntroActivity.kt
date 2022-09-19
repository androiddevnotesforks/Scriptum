package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.intro

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import javax.inject.Inject
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.annotation.test.IdlingTag
import sgtmelon.scriptum.cleanup.extension.InsetsDir
import sgtmelon.scriptum.cleanup.extension.beforeFinish
import sgtmelon.scriptum.cleanup.extension.setMarginInsets
import sgtmelon.scriptum.cleanup.extension.setPaddingInsets
import sgtmelon.scriptum.cleanup.presentation.adapter.IntroPageAdapter
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ParentActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.IIntroActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.MainActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IIntroViewModel
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys
import sgtmelon.test.idling.getIdling

/**
 * Activity with start intro.
 */
class IntroActivity : ParentActivity(), IIntroActivity {

    override val background = WindowUiKeys.Background.Dark
    override val statusBar = WindowUiKeys.StatusBar.Transparent
    override val navigation = WindowUiKeys.Navigation.Transparent
    override val navDivider = WindowUiKeys.NavDivider.Transparent

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


    override fun setupViewPager(isLastPage: Boolean) {
        endButton.setOnClickListener {
            getIdling().start(IdlingTag.Intro.FINISH)
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
        viewPager.setPaddingInsets(InsetsDir.LEFT, InsetsDir.TOP, InsetsDir.RIGHT, InsetsDir.BOTTOM)
        pageContainer.setMarginInsets(InsetsDir.LEFT, InsetsDir.RIGHT, InsetsDir.BOTTOM)
        endButton.setMarginInsets(InsetsDir.LEFT, InsetsDir.RIGHT, InsetsDir.BOTTOM)
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