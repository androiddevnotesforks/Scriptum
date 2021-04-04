package sgtmelon.scriptum.presentation.screen.ui.impl.preference

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.extension.*
import sgtmelon.scriptum.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.presentation.screen.ui.impl.AppActivity

/**
 * Screen for display [DevelopFragment].
 */
class DevelopActivity : AppActivity() {

    private val parentContainer by lazy { findViewById<ViewGroup>(R.id.develop_parent_container) }
    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar_container) }

    override fun onCreate(savedInstanceState: Bundle?) {
        ScriptumApplication.component.getAppBuilder().set(activity = this).build()
            .inject(activity = this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_develop)

        setupView()
        setupInsets()

        fm.beginTransaction()
            .replace(R.id.develop_fragment_container, DevelopFragment())
            .commit()
    }

    private fun setupView() {
        toolbar?.apply {
            title = getString(R.string.title_develop)
            navigationIcon = getTintDrawable(R.drawable.ic_cancel_exit)
            setNavigationOnClickListener { finish() }
        }
    }

    private fun setupInsets() {
        parentContainer?.doOnApplyWindowInsets { view, insets, _, _, margin ->
            view.updateMargin(InsetsDir.LEFT, insets, margin)
            view.updateMargin(InsetsDir.TOP, insets, margin)
            view.updateMargin(InsetsDir.RIGHT, insets, margin)
            return@doOnApplyWindowInsets insets
        }
    }

    /**
     * Make navigation translucent in portrait orientation.
     */
    override fun setNavigationColor(@Theme theme: Int) {
        if (isPortraitMode()) {
            window.navigationBarColor = getColorAttr(R.attr.clNavigationBar)
        } else {
            super.setNavigationColor(theme)
        }
    }

    /**
     * Make navigation translucent in portrait orientation.
     */
    @RequiresApi(Build.VERSION_CODES.P)
    override fun setNavigationDividerColor(@Theme theme: Int) {
        if (isPortraitMode()) {
            window.navigationBarDividerColor = getColorAttr(R.attr.clNavigationBarDivider)
        } else {
            super.setNavigationDividerColor(theme)
        }
    }

    companion object {
        operator fun get(context: Context) = Intent(context, DevelopActivity::class.java)
    }
}