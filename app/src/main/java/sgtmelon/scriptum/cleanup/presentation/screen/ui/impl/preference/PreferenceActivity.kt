package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference

import android.os.Bundle
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.extension.getTintDrawable
import sgtmelon.scriptum.databinding.ActivityPreferenceBinding
import sgtmelon.scriptum.infrastructure.factory.FragmentFactory
import sgtmelon.scriptum.infrastructure.model.key.PreferenceScreen
import sgtmelon.scriptum.infrastructure.screen.parent.ParentPreferenceFragment
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeActivity
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys
import sgtmelon.scriptum.infrastructure.utils.InsetsDir
import sgtmelon.scriptum.infrastructure.utils.setPaddingInsets

/**
 * Screen for display different [PreferenceScreen]'s.
 */
class PreferenceActivity : ThemeActivity<ActivityPreferenceBinding>() {

    override val layoutId: Int = R.layout.activity_preference

    override val navigation = WindowUiKeys.Navigation.RotationCatch
    override val navDivider = WindowUiKeys.NavDivider.RotationCatch

    private val bundleProvider = PreferenceBundleProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bundleProvider.getData(bundle = savedInstanceState ?: intent.extras)
        val screen = bundleProvider.screen ?: run {
            finish()
            return
        }

        setupView(screen)
        showFragment(screen)
    }

    override fun inject(component: ScriptumComponent) {
        component.getMainPreferenceBuilder()
            .set(owner = this)
            .build()
            .inject(activity = this)
    }

    /**
     * [InsetsDir.BOTTOM] will be set in [ParentPreferenceFragment] (list padding).
     */
    override fun setupInsets() {
        binding?.parentContainer?.setPaddingInsets(InsetsDir.LEFT, InsetsDir.TOP, InsetsDir.RIGHT)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        bundleProvider.saveData(outState)
    }

    private fun setupView(screen: PreferenceScreen) {
        val titleId = when (screen) {
            PreferenceScreen.PREFERENCE -> R.string.title_preference
            PreferenceScreen.BACKUP -> R.string.pref_title_backup
            PreferenceScreen.NOTE -> R.string.pref_title_note
            PreferenceScreen.ALARM -> R.string.pref_title_alarm
            PreferenceScreen.HELP -> R.string.pref_title_other_help
            PreferenceScreen.DEVELOP -> R.string.pref_title_developer
            PreferenceScreen.SERVICE -> R.string.pref_header_service
        }

        binding?.toolbarInclude?.toolbar?.apply {
            title = getString(titleId)
            navigationIcon = getTintDrawable(R.drawable.ic_cancel_exit)
            setNavigationOnClickListener { finish() }
        }
    }

    private fun showFragment(screen: PreferenceScreen) {
        val (fragment, tag) = FragmentFactory.Preference(fm).get(screen)

        fm.beginTransaction()
            .replace(R.id.fragment_container, fragment, tag)
            .commit()
    }
}