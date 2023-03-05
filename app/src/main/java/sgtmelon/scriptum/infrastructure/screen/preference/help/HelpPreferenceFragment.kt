package sgtmelon.scriptum.infrastructure.screen.preference.help

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.infrastructure.screen.Screens
import sgtmelon.scriptum.infrastructure.screen.parent.ParentPreferenceFragment
import sgtmelon.scriptum.infrastructure.utils.extensions.setOnClickListener

/**
 * Fragment of help preferences.
 */
class HelpPreferenceFragment : ParentPreferenceFragment() {

    override val xmlId: Int = R.xml.preference_help

    private val binding = HelpPreferenceBinding(fragment = this)

    override fun inject(component: ScriptumComponent) = Unit

    override fun setup() {
        binding.disappearButton?.setOnClickListener {
            open.attempt {
                startActivity(Screens.toHelpDisappear(it.context))
            }
        }
    }

    override fun onResume() {
        super.onResume()

        /** Need clear [open], because may be case when open new screens. */
        open.clear()
    }
}