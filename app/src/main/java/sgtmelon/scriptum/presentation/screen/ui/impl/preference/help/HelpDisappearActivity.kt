package sgtmelon.scriptum.presentation.screen.ui.impl.preference.help

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.InsetsDir
import sgtmelon.scriptum.extension.doOnApplyWindowInsets
import sgtmelon.scriptum.extension.getTintDrawable
import sgtmelon.scriptum.extension.updateMargin
import sgtmelon.scriptum.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.presentation.screen.ui.impl.AppActivity

/**
 * Screen with help about disappearing notifications
 */
class HelpDisappearActivity : AppActivity() {

    private val parentContainer by lazy { findViewById<ViewGroup?>(R.id.disappear_parent_container) }
    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar_container) }

    override fun onCreate(savedInstanceState: Bundle?) {
        ScriptumApplication.component.getHelpDescriptionBuilder().set(activity = this).build()
            .inject(activity = this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_disappear)

        setupView()
        setupInsets()
    }

    private fun setupView() {
        toolbar?.apply {
            title = getString(R.string.pref_title_help_notification_disappear)
            navigationIcon = getTintDrawable(R.drawable.ic_cancel_exit)
            setNavigationOnClickListener { finish() }
        }
    }

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