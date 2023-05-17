package sgtmelon.scriptum.infrastructure.screen.preference

import android.os.Bundle
import sgtmelon.extensions.emptyString
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.databinding.ActivityPreferenceBinding
import sgtmelon.scriptum.infrastructure.bundle.BundleValue
import sgtmelon.scriptum.infrastructure.bundle.BundleValueImpl
import sgtmelon.scriptum.infrastructure.factory.FragmentFactory
import sgtmelon.scriptum.infrastructure.model.annotation.TestViewTag
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Preference.Key
import sgtmelon.scriptum.infrastructure.screen.parent.PreferenceFragment
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeActivity
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys
import sgtmelon.scriptum.infrastructure.utils.extensions.getTintDrawable
import sgtmelon.scriptum.infrastructure.utils.extensions.insets.InsetsDir
import sgtmelon.scriptum.infrastructure.utils.extensions.insets.setPaddingInsets

/**
 * Screen for display different [PreferenceScreen]'s.
 */
class PreferenceActivity : ThemeActivity<ActivityPreferenceBinding>() {

    override val layoutId: Int = R.layout.activity_preference

    override val navigation = WindowUiKeys.Navigation.RotationCatch
    override val navDivider = WindowUiKeys.NavDivider.RotationCatch

    private val screen = BundleValueImpl<PreferenceScreen>(Key.SCREEN)
    override val bundleValues: List<BundleValue> = listOf(screen)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showFragment()
    }

    override fun inject(component: ScriptumComponent) {
        component.getMainPreferenceBuilder()
            .set(owner = this)
            .build()
            .inject(activity = this)
    }

    /** [InsetsDir.BOTTOM] will be set in [PreferenceFragment] (list padding). */
    override fun setupInsets() {
        super.setupInsets()

        binding?.parentContainer?.setPaddingInsets(InsetsDir.LEFT, InsetsDir.TOP, InsetsDir.RIGHT)
    }

    override fun setupView() {
        val screen = screen.value

        binding?.parentContainer?.tag = when (screen) {
            PreferenceScreen.MENU -> TestViewTag.PREF_MENU
            PreferenceScreen.BACKUP -> TestViewTag.PREF_BACKUP
            PreferenceScreen.NOTES -> TestViewTag.PREF_NOTE
            PreferenceScreen.ALARM -> TestViewTag.PREF_ALARM
            else -> emptyString()
        }

        binding?.appBar?.toolbar?.apply {
            title = getString(screen.titleId)
            navigationIcon = getTintDrawable(R.drawable.ic_cancel_exit)
            setNavigationOnClickListener { finish() }
        }
    }

    private fun showFragment() {
        val (fragment, tag) = FragmentFactory.Preference(fm).get(screen.value)

        fm.beginTransaction()
            .replace(R.id.fragment_container, fragment, tag)
            .commit()
    }
}