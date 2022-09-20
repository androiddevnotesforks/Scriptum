package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.help

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.extension.InsetsDir
import sgtmelon.scriptum.cleanup.extension.doOnApplyWindowInsets
import sgtmelon.scriptum.cleanup.extension.getSettingsIntent
import sgtmelon.scriptum.cleanup.extension.getSiteIntent
import sgtmelon.scriptum.cleanup.extension.getTintDrawable
import sgtmelon.scriptum.cleanup.extension.startActivitySafe
import sgtmelon.scriptum.cleanup.extension.tintIcon
import sgtmelon.scriptum.cleanup.extension.updateMargin
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ParentActivity

/**
 * Screen with help about disappearing notifications
 */
class HelpDisappearActivity : ParentActivity() {

    private val parentContainer by lazy { findViewById<ViewGroup?>(R.id.disappear_parent_container) }
    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar_container) }
    private val settingsButton by lazy { findViewById<View>(R.id.disappear_settings_button) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_disappear)

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

    private fun setupView() {
        toolbar.apply {
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

        settingsButton.setOnClickListener { onSettingsClick() }
    }

    private fun onVideoClick() {
        val intent = getSiteIntent(getString(R.string.help_notification_disappear_video_url))
        startActivitySafe(intent, toast)
    }

    private fun onSettingsClick() = startActivitySafe(getSettingsIntent(), toast)

    private fun setupInsets() {
        parentContainer?.doOnApplyWindowInsets { view, insets, isFirstTime, _, margin ->
            view.updateMargin(InsetsDir.LEFT, insets, margin)
            view.updateMargin(InsetsDir.TOP, insets, margin)
            view.updateMargin(InsetsDir.RIGHT, insets, margin)
            view.updateMargin(InsetsDir.BOTTOM, insets, margin, !isFirstTime)
            return@doOnApplyWindowInsets insets
        }
    }

    companion object {
        operator fun get(context: Context): Intent {
            return Intent(context, HelpDisappearActivity::class.java)
        }
    }
}