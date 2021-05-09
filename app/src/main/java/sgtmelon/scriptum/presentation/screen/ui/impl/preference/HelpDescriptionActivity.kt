package sgtmelon.scriptum.presentation.screen.ui.impl.preference

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.InsetsDir
import sgtmelon.scriptum.extension.doOnApplyWindowInsets
import sgtmelon.scriptum.extension.updateMargin
import sgtmelon.scriptum.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.presentation.screen.ui.impl.AppActivity

class HelpDescriptionActivity : AppActivity() {

    private val parentContainer by lazy { findViewById<ViewGroup?>(R.id.description_parent_container) }

    override fun onCreate(savedInstanceState: Bundle?) {
        ScriptumApplication.component.getHelpDescriptionBuilder().set(activity = this).build()
            .inject(activity = this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_description)

        setupInsets()
    }

    fun setupInsets() {
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
            return Intent(context, HelpDescriptionActivity::class.java)
        }
    }
}