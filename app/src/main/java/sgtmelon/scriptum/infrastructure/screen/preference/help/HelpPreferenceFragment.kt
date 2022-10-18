package sgtmelon.scriptum.infrastructure.screen.preference.help

import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.infrastructure.factory.InstanceFactory
import sgtmelon.scriptum.infrastructure.screen.parent.ParentPreferenceFragment
import sgtmelon.scriptum.infrastructure.utils.setOnClickListener
import sgtmelon.scriptum.infrastructure.utils.startUrlActivity

/**
 * Fragment of help preferences.
 */
class HelpPreferenceFragment : ParentPreferenceFragment() {

    override val xmlId: Int = R.xml.preference_help

    private val binding = HelpPreferenceDataBinding(lifecycle, fragment = this)

    override fun inject(component: ScriptumComponent) = Unit

    override fun setup() {
        binding.disappearButton?.setOnClickListener {
            startActivity(InstanceFactory.Preference.HelpDisappear[it.context])
        }
        binding.policyButton?.setOnClickListener {
            it.context.startUrlActivity(BuildConfig.PRIVACY_POLICY_URL, delegators.toast)
        }
    }
}