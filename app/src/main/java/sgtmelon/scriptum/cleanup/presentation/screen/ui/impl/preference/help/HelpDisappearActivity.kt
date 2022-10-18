package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.help

import android.os.Bundle
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.extension.getTintDrawable
import sgtmelon.scriptum.cleanup.extension.tintIcon
import sgtmelon.scriptum.databinding.ActivityHelpDisappearBinding
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeActivity
import sgtmelon.scriptum.infrastructure.utils.InsetsDir
import sgtmelon.scriptum.infrastructure.utils.doOnApplyWindowInsets
import sgtmelon.scriptum.infrastructure.utils.startSettingsActivity
import sgtmelon.scriptum.infrastructure.utils.startUrlActivity
import sgtmelon.scriptum.infrastructure.utils.updateMargin

/**
 * Screen with help about disappearing notifications
 */
class HelpDisappearActivity : ThemeActivity<ActivityHelpDisappearBinding>() {

    override val layoutId: Int = R.layout.activity_help_disappear

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
        setupInsets()
    }

    override fun inject(component: ScriptumComponent) {
        component.getHelpDescriptionBuilder()
            .set(activity = this)
            .set(owner = this)
            .build()
            .inject(activity = this)
    }

    override fun setupInsets() {
        binding?.parentContainer?.doOnApplyWindowInsets { view, insets, isFirstTime, _, margin ->
            view.updateMargin(InsetsDir.LEFT, insets, margin)
            view.updateMargin(InsetsDir.TOP, insets, margin)
            view.updateMargin(InsetsDir.RIGHT, insets, margin)
            view.updateMargin(InsetsDir.BOTTOM, insets, margin, !isFirstTime)
            return@doOnApplyWindowInsets insets
        }
    }

    private fun setupView() {
        binding?.toolbarInclude?.toolbar?.apply {
            title = getString(R.string.pref_title_help_notification_disappear)
            navigationIcon = getTintDrawable(R.drawable.ic_cancel_exit)
            setNavigationOnClickListener { finish() }

            inflateMenu(R.menu.activity_help_disappear)
            setOnMenuItemClickListener {
                onVideoClick()
                return@setOnMenuItemClickListener true
            }

            menu?.findItem(R.id.item_video_lesson)?.tintIcon(context = this@HelpDisappearActivity)
        }

        binding?.settingsButton?.setOnClickListener { startSettingsActivity(delegators.toast) }
    }

    private fun onVideoClick() {
        val url = getString(R.string.help_notification_disappear_video_url)
        startUrlActivity(url, delegators.toast)
    }

}