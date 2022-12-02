package sgtmelon.scriptum.infrastructure.screen.preference.disappear

import android.os.Bundle
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.databinding.ActivityHelpDisappearBinding
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeActivity
import sgtmelon.scriptum.infrastructure.utils.extensions.InsetsDir
import sgtmelon.scriptum.infrastructure.utils.extensions.getTintDrawable
import sgtmelon.scriptum.infrastructure.utils.extensions.setMarginInsets
import sgtmelon.scriptum.infrastructure.utils.extensions.startSettingsActivity
import sgtmelon.scriptum.infrastructure.utils.extensions.startUrlActivity
import sgtmelon.scriptum.infrastructure.utils.extensions.tintIcon

/**
 * Screen with help about disappearing notifications.
 */
class HelpDisappearActivity : ThemeActivity<ActivityHelpDisappearBinding>() {

    override val layoutId: Int = R.layout.activity_help_disappear

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
    }

    override fun inject(component: ScriptumComponent) {
        component.getHelpDescriptionBuilder()
            .set(owner = this)
            .build()
            .inject(activity = this)
    }

    override fun setupInsets() {
        super.setupInsets()

        binding?.parentContainer?.setMarginInsets(
            InsetsDir.LEFT, InsetsDir.TOP, InsetsDir.RIGHT, InsetsDir.BOTTOM
        )
    }

    override fun setupView() {
        super.setupView()

        binding?.toolbarInclude?.toolbar?.apply {
            title = getString(R.string.pref_title_help_disappear)
            navigationIcon = getTintDrawable(R.drawable.ic_cancel_exit)
            setNavigationOnClickListener { finish() }

            inflateMenu(R.menu.activity_help_disappear)
            setOnMenuItemClickListener {
                onVideoClick()
                return@setOnMenuItemClickListener true
            }

            menu?.findItem(R.id.item_video_lesson)?.tintIcon(context = this@HelpDisappearActivity)
        }

        binding?.settingsButton?.setOnClickListener {
            val toast = system?.toast ?: return@setOnClickListener
            startSettingsActivity(toast)
        }
    }

    private fun onVideoClick() {
        val toast = system?.toast ?: return

        val url = getString(R.string.help_notification_disappear_video_url)
        startUrlActivity(url, toast)
    }
}