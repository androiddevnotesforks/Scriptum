package sgtmelon.scriptum.presentation.screen.ui.impl.preference

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.extension.*
import sgtmelon.scriptum.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.presentation.screen.ui.impl.AppActivity

/**
 * Screen for display [PreferenceFragment].
 */
class PreferenceActivity : AppActivity() {

    private val parentContainer by lazy { findViewById<ViewGroup>(R.id.preference_parent_container) }
    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar_container) }

    override fun onCreate(savedInstanceState: Bundle?) {
        ScriptumApplication.component.getAppBuilder().set(activity = this).build()
                .inject(activity = this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference)

        setupView()
        setupInsets()

        fm.beginTransaction()
                .replace(R.id.preference_fragment_container, PreferenceFragment())
                .commit()
    }

    private fun setupView() {
        toolbar?.apply {
            title = getString(R.string.title_preference)
            navigationIcon = getTintDrawable(R.drawable.ic_cancel_exit)
            setNavigationOnClickListener { finish() }
        }
    }

    private fun setupInsets() {
        parentContainer?.doOnApplyWindowInsets { view, insets, _, margin ->
            view.updateMargin(InsetsDir.LEFT, insets, margin)
            view.updateMargin(InsetsDir.TOP, insets, margin)
            view.updateMargin(InsetsDir.RIGHT, insets, margin)
            return@doOnApplyWindowInsets insets
        }
    }

    override fun setNavigationColor(@Theme theme: Int) {
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            window.navigationBarColor = getColorAttr(R.attr.clNavigationBar)
        } else {
            super.setNavigationColor(theme)
        }
    }

    override fun setNavigationDividerColor(@Theme theme: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) return

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            window.navigationBarDividerColor = getColorAttr(R.attr.clNavigationBarDivider)
        } else {
            super.setNavigationDividerColor(theme)
        }
    }

    companion object {
        operator fun get(context: Context) = Intent(context, PreferenceActivity::class.java)
    }

}